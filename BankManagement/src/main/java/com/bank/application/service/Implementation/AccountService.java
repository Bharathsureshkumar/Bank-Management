package com.bank.application.service.Implementation;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Customer;
import com.bank.application.persistance.conversion.AccountMapper;
import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.exception.*;
import com.bank.application.repository.AccountRepo;
import com.bank.application.repository.CustomerRepo;
import com.bank.application.repository.TransactionRepo;
import com.bank.application.service.IAccountService;
import com.bank.application.validators.CustomerValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AccountService implements IAccountService {

    static Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountMapper accountMapperToEntity;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    CustomerValidation customerValidation;

    @Autowired
    CustomerRepo customerRepo;

    @Override
    public AccountDTO register(AccountDTO accountEntityMapper, Map<String, String> headers) throws CommonException {

        if(headers.containsKey("pan-card")) {
            //Validating valid pan card or not
            if(!customerValidation.validatePancard(headers.get("pan-card"))){
                throw new InvalidPanCardNumberException("Invalid pan card number ..");
            }
            //Validating customer present or not
            if(!customerRepo.findById(headers.get("pan-card")).isPresent()){
                throw new NoDataPresentException("Entered pan card customer not found in the DB");
            }
            //retrieving customer details ..
            Customer customer = customerRepo.findById(headers.get("pan-card")).get();
            log.info("Received Account Details : {}", accountEntityMapper);
            log.info("Received Customer Details : {}", customer);
            Account account = accountMapperToEntity.convertToEntity(accountEntityMapper);
            accountRepo.save(account);
            accountEntityMapper = accountMapperToEntity.convertToMapper(account);
            account.setCustomer(customer);
            accountRepo.save(account);
            customer.getAccounts().add(account);
            customerRepo.save(customer);
            return accountEntityMapper;
        }
        else{
            throw new RequiredHeaderArgumentException("To create account registered customer pan card number needed ..");
        }

    }

    //Below function retrieves all the accounts from the DB.

    @Override
    public List<AccountDTO> fetchAccount(String accountNumber) throws CommonException{



        if(accountNumber != null){
                return this.findByAccountNumber(accountNumber);
        }
        else{
                return this.viewAllAccount();
        }
    }

    public List<AccountDTO> viewAllAccount() {
        List<AccountDTO> accountEntityMapper = new ArrayList<>();

        for(Account account : accountRepo.findAll()){

            accountEntityMapper.add(accountMapperToEntity.convertToMapper(account));
        }
        return accountEntityMapper;
    }

    public List<AccountDTO> findByAccountNumber(String accountNumber) throws CommonException {



         List<AccountDTO> accountWrapper = new ArrayList<>();
         List<Account> f_account = accountRepo.findByAccountNumber(accountNumber);

         if(f_account.size() > 0) accountWrapper.add(new AccountDTO().wrapBankDetails(f_account.get(0)));
         else throw new NoDataPresentException("Requested account not present in the DB ..");

         return accountWrapper;
    }

    //Below function updates Particular account status.
    @Override
    public AccountDTO updateAccount(String accountNumber, String update_status) throws CommonException {

        Account account = null;
        AccountDTO accountEntityMapper = null;

//      checking the account input status valid and the account number present in the DB.
        System.out.println(findByAccountNumber(accountNumber).isEmpty());
        if((update_status.equals("activate") || update_status.equals("deactivate")) && !findByAccountNumber(accountNumber).isEmpty()){

            account = accountRepo.findByAccountNumber(accountNumber).get(0);
           String current_status = account.getStatus();
           //checking the current status is equals to update status.
            if(update_status.equals(current_status)){
                log.warn("Already current Account Status is : " + update_status);
            }
            if(update_status.equals("activate")) update_status = "active";
            else update_status = "not active";

            account.setStatus(update_status);
            accountRepo.save(account);
            log.info("Account Updated Successfully");
            accountEntityMapper = new AccountDTO().wrapBankDetails(account);


        }
        else{
                throw new DataMismatchedException("status parameter value mismatched either [active or deactivate]");
        }
        return accountEntityMapper;
    }

    public String getStatus(String accountNumber){

        String status = accountRepo.getStatus(accountNumber);

        return status;
    }

    public Float checkBalance(String accountNumber){

        return Float.parseFloat(accountRepo.getBalance(accountNumber));
    }

}