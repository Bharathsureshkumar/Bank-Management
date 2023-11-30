package com.bank.application.repository;

import com.bank.application.persistance.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepo extends JpaRepository<Bank, String> {
    Bank findByBankId(String bankId);
}
