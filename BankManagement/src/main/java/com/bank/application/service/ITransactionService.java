package com.bank.application.service;

import com.bank.application.exception.*;
import com.bank.application.persistance.dto.TransactionDTO;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

public interface ITransactionService {

    List<TransactionDTO> transfer(Map<String, String> transactionMapper) throws CommonException;
    List<TransactionDTO> getAllTransactions() throws CommonException;

    void deposit(Map<String, String> headers)throws CommonException;

    void withdraw(Map<String, String> headers)throws CommonException;

    List<TransactionDTO> getTransactions(@RequestHeader Map<String, String> headers) throws CommonException;

}
