package com.bank.application.persistance.conversion;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.dto.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account convertToEntity(AccountDTO accountModel) {

        Account account = new Account();
        account.setAccountNumber(accountModel.getAccountNumber());
        account.setAccountBalance(accountModel.getAccountBalance());
        account.setAccountType(accountModel.getAccountType());
        account.setStatus(accountModel.getStatus());

        return account;
    }

    public AccountDTO convertToMapper(Account account){

        AccountDTO accountModel = new AccountDTO();
        accountModel.setAccountBalance(account.getAccountBalance());
        accountModel.setAccountNumber(account.getAccountNumber());
        accountModel.setAccountType(account.getAccountType());
        accountModel.setStatus(account.getStatus());

        System.out.println(accountModel);

        return accountModel;
    }

}
