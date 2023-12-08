package com.bank.application.service.Implementation;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.conversion.TransactionMapper;
import com.bank.application.persistance.dto.TransactionDTO;
import com.bank.application.exception.*;
import com.bank.application.repository.AccountRepo;
import com.bank.application.repository.TransactionRepo;
import com.bank.application.service.ITransactionService;
import com.bank.application.utility.ApplicationConstants;
import com.bank.application.utility.BankTransactionService;
import com.bank.application.utility.TransactionUtility;
import com.bank.application.validators.AccountNumberValidator;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService implements ITransactionService {

    TransactionUtility transactionUtility;

    BankTransactionService banktransactionService;

    AccountRepo accountRepo;

    TransactionRepo transactionRepo;

    AccountNumberValidator accountNumberValidator;
    ModelMapper modelMapper = new ModelMapper();
    static Logger log = LoggerFactory.getLogger(TransactionService.class);


    public TransactionService(TransactionUtility transactionUtility,
                              BankTransactionService banktransactionService,
                              AccountRepo accountRepo, TransactionRepo transactionRepo,
                              AccountNumberValidator accountNumberValidator) {
        this.transactionUtility = transactionUtility;
        this.banktransactionService = banktransactionService;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.accountNumberValidator = accountNumberValidator;
    }

    @Override
    public List<TransactionDTO> transfer(Map<String, String> headers)
            throws CommonException {
        List<Transaction> transactions = null;
        List<TransactionDTO> transactionsDTO = new ArrayList<>();
        //basic validation for transaction ..
        boolean basic_validation_flag = transactionUtility.validateData(headers);
        if (basic_validation_flag) log.info("Basic Validation Completed successfully..");
        else log.info("Basic Validation Failed .. {}", basic_validation_flag);

        //Business Validation for transaction ..
        boolean business_validation_flag = transactionUtility.checkCredentials(headers);

        if (business_validation_flag) log.info("Business Validation Completed successfully..");
        else log.info("Business Validation Failed .. : {}", business_validation_flag);



        if (basic_validation_flag && business_validation_flag) {
            transactions = banktransactionService.makeTransaction(headers);
        }

        //Mapping

        for(Transaction transaction: transactions){
            TransactionDTO trnsDTO = new TransactionDTO();
            modelMapper.map(transaction, trnsDTO);
            transactionsDTO.add(trnsDTO);

        }

        return transactionsDTO;
    }


    @Override
    public List<TransactionDTO> getAllTransactions() {

        List<Transaction> transactions = transactionRepo.findAll();

        List<TransactionDTO> transactionDTO = new ArrayList<>();

        for(Transaction transaction: transactions){

            TransactionDTO tempTransactionDTO = new TransactionDTO();
            modelMapper.map(transaction, tempTransactionDTO);
            transactionDTO.add(tempTransactionDTO);
        }

        return transactionDTO;
    }


    //finding particular account number account transaction details
    @Override
    public List<TransactionDTO> getTransactions(Map<String, String> headers) throws CommonException {

        if (!headers.containsKey(ApplicationConstants.ACCOUNT_NUMBER)) {
            throw new RequiredHeaderArgumentException("Required account number not present exception ..");
        }

        log.info("Account number :  {}", headers.get(ApplicationConstants.ACCOUNT_NUMBER));

        log.info("{} number is to be retrieve transaction", headers.get(ApplicationConstants.ACCOUNT_NUMBER));


        Account account = accountRepo.findByAccountNumber(headers.get(ApplicationConstants.ACCOUNT_NUMBER)).get(0);


        return fetchAccountTransaction(account);
    }


    @Transactional
    public List<TransactionDTO> fetchAccountTransaction(Account account) throws CommonException {




        List<Transaction> transactions = account.getTransactions();
        List<TransactionDTO> transactionModels = new ArrayList<>();

        for(Transaction t: transactions){

            transactionModels.add(TransactionMapper.entityToMapper(t));
        }

        return transactionModels;

    }


    @Override
    public void deposit(Map<String, String> headers)throws CommonException{

        if(!headers.containsKey(ApplicationConstants.ACCOUNT_NUMBER)) throw new RequiredHeaderArgumentException("Amount required .. !");


        if(!accountNumberValidator.validateAccountNumber(headers.get(ApplicationConstants.ACCOUNT_NUMBER))){
            throw new InvalidTransactionValueException("Invalid account Number");
        }

        if(Double.parseDouble(headers.get(ApplicationConstants.AMOUNT)) < 10 || Double.parseDouble(headers.get(ApplicationConstants.AMOUNT)) > 100000)
            throw new InvalidTransactionValueException("Amount should be between 10 - 100000");

        headers.put("details", "cash deposit");

        Account account = accountRepo.findByAccountNumber(headers.get(ApplicationConstants.ACCOUNT_NUMBER)).get(0);

        Double preBal = Double.parseDouble(account.getAccountBalance());

        Double currentBal = Double.parseDouble(headers.get(ApplicationConstants.AMOUNT)) + preBal;

        Transaction transaction = banktransactionService.registerTransfer(headers, "-", "-", preBal, currentBal, "Credit");
        account.getTransactions().add(
                transaction);
        transaction.setAccount(account);
        transactionRepo.save(transaction);
        account.setAccountBalance(currentBal+"");
        accountRepo.save(account);

    }


    @Override
    public void withdraw(Map<String, String> headers)throws CommonException{

        if(!headers.containsKey(ApplicationConstants.ACCOUNT_NUMBER)) throw new RequiredHeaderArgumentException("Account number required .. !");
        if(!headers.containsKey(ApplicationConstants.AMOUNT)) throw new RequiredHeaderArgumentException("Amount required .. !");


        if(!accountNumberValidator.validateAccountNumber(headers.get("account-number"))){
            throw new InvalidTransactionValueException("Invalid account Number");
        }

        if(Double.parseDouble(headers.get(ApplicationConstants.AMOUNT)) < 10 || Double.parseDouble(headers.get(ApplicationConstants.AMOUNT)) > 100000)
            throw new InvalidTransactionValueException("Amount should be between 10 - 100000");

        headers.put("details", "cash withdrawal");

        Account account = accountRepo.findByAccountNumber(headers.get("account-number")).get(0);
        log.info("Amount going to be debited {}", headers.get(ApplicationConstants.AMOUNT));
        Double preBal = Double.parseDouble(account.getAccountBalance());

        if(Double.parseDouble(account.getAccountBalance()) < Double.parseDouble(headers.get("amount"))){
            throw new MinimumBalanceException("Insufficient balance ..");
        }

        Double currentBal = preBal - Double.parseDouble(headers.get("amount")) ;

        Transaction transaction = banktransactionService.registerTransfer(headers, "-", "-", preBal, currentBal, "Debit");

        account.getTransactions().add(
                transaction);
        transaction.setAccount(account);
        transactionRepo.save(transaction);
        account.setAccountBalance(currentBal+"");
        accountRepo.save(account);
    }
}

