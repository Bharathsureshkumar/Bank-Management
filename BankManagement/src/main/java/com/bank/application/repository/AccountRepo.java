package com.bank.application.repository;

import com.bank.application.persistance.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    List<Account> findByAccountNumber(String accountNumber);

//    @Query("UPDATE Account SET Account.status='active' WHERE Account.account_number='207697891'")
//    void updateAccount(String accountNumber,String updated_status);

    @Query("select a.status from Account a where a.accountNumber=:accountNumber")
    String getStatus(String accountNumber);

    @Query("select a.accountBalance from Account a where a.accountNumber=:accountNumber")
    String getBalance(String accountNumber);

}
