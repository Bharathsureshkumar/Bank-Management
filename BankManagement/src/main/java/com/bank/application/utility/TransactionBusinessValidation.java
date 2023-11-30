package com.bank.application.utility;

import com.bank.application.exception.CommonException;
import com.bank.application.exception.MinimumBalanceException;
import com.bank.application.exception.NoDataPresentException;
import com.bank.application.service.Implementation.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionBusinessValidation {
    @Autowired
    AccountService accountService;

    @Value(value = "${account.min.balance}")
    float minBalance;

    static Logger log = LoggerFactory.getLogger(TransactionBusinessValidation.class);

    public boolean checkAccount(String accountNumber) throws CommonException {

        boolean accountFlag = true;

        try {
            if(accountService.fetchAccount(accountNumber).size() > 0){
                accountFlag = true;
            }
        } catch (NoDataPresentException e) {
            throw new NoDataPresentException(e.getMessage());
        }
        return accountFlag;
    }

    public boolean checkStatus(String accountNumber){

        boolean status_flag = false;
        String status = accountService.getStatus(accountNumber);
        log.info("Account Number : {} Status : {}", accountNumber, status);
        if(status.trim().equals("active")){
            status_flag = true;
        }

        return status_flag;

    }

    public boolean compareBalance(String accountNumber,String amount) {

        boolean account_flag = true;

        Float current_balance = accountService.checkBalance(accountNumber);

        Float new_balance = current_balance - Float.parseFloat(amount);

        System.out.println("\n\n\n\n\n\n\nMinimum balance : " + minBalance);

        if(new_balance < minBalance){
            log.warn("Minimum balance required exception .. !");
            account_flag = false;
        }
        return account_flag;
    }
}
