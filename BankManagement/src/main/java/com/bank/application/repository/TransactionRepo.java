package com.bank.application.repository;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

    @Query("select t from Transaction t where account=:account")
    List<Transaction> findByAccount(Account account);
}
