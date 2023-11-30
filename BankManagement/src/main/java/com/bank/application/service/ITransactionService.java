package com.bank.application.service;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Transaction;
import com.bank.application.exception.*;

import java.util.List;
import java.util.Map;

public interface ITransactionService {

    List<Transaction> transfer(Map<String, String> transactionMapper) throws CommonException;
    Account getAllTransactions(Map<String, String> headers) throws CommonException;

}
