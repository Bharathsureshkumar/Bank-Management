package com.bank.application.service.Implementation;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.conversion.TransactionMapper;
import com.bank.application.persistance.dto.TransactionModel;
import com.bank.application.exception.*;
import com.bank.application.repository.AccountRepo;
import com.bank.application.service.ITransactionService;
import com.bank.application.utility.BankTransactionService;
import com.bank.application.utility.TransactionUtility;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    TransactionUtility transactionUtility;

    @Autowired
    BankTransactionService banktransactionService;

    @Autowired
    AccountRepo accountRepo;


    static Logger log = LoggerFactory.getLogger(TransactionService.class);


    @Override
    public List<Transaction> transfer(Map<String, String> headers)
            throws CommonException {
        List<Transaction> transactions = null;
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

        return transactions;
    }


    @Override
    public Account getAllTransactions(Map<String, String> headers) {

        Account account = null;

        System.out.println(headers.get("account-number"));

        try {
            if (!headers.containsKey("account-number"))
                throw new RequiredHeaderArgumentException("provide a valid AccountNumber");
//            account = accountService.fetchAccountWithTransaction(headers.get("account-number"));
        } catch (RequiredHeaderArgumentException e) {
            System.out.println(e.getMessage());
        }

        for (Transaction t : account.getTransactions()) {
            System.out.println("\n\n\n\n" + t + "\n\n");
        }

        return account;
    }

    @Transactional
    public List<TransactionModel> fetchAccountWithTransaction(Map<String, String> headers) throws CommonException {

        log.info("Account number :  {}", headers.get("account-number"));

        log.info("{} number is to be retrieve transaction", headers.get("account-number"));

        if (!headers.containsKey("account-number")) {
            throw new RequiredHeaderArgumentException("Required account number not present exception ..");
        }

        Account account = accountRepo.findByAccountNumber(headers.get("account-number")).get(0);

//        ObjectMapper mapper = new ObjectMapper();
//
//        StringWriter w = new StringWriter();
////         writter = new StringBuffer();
//
//
//        try {
//            mapper.writeValue(w, account);
//        } catch (IOException e) {
//            System.out.println("\n\n\n\n" + e.getMessage());
//            throw new RuntimeException(e);
//        }

//        account.setTransactions(transactionRepo.findByAccount(accountRepo.findByAccountNumber(accountNumber).get(0)));
//
        List<Transaction> transactions = account.getTransactions();
//
        List<TransactionModel> transactionModels = new ArrayList<>();

        for(Transaction t: transactions){

            transactionModels.add(TransactionMapper.entityToMapper(t));
        }

//        for(Transaction t: ts){
//            account.getTransactions().add(t);
//        }
//
        return transactionModels;

    }

}
//So far completed up to minimum balance require exception
//pending make transaction update on both account and transaction table.
