package com.bank.application.service;

import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.exception.CommonException;

import java.util.List;
import java.util.Map;

public interface IAccountService {
    AccountDTO register(AccountDTO account, Map<String, String> headers) throws CommonException;
    List<AccountDTO> fetchAccount(String accountNumber) throws CommonException;
    List<AccountDTO> findByAccountNumber(String accountNumber) throws CommonException;
    AccountDTO updateAccount(String accountId, String status) throws CommonException;

}
