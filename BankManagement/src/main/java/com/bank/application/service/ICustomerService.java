package com.bank.application.service;

import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.persistance.dto.CustomerDTO;
import com.bank.application.exception.CommonException;

import java.util.List;
import java.util.Map;

public interface ICustomerService {

    CustomerDTO register(CustomerDTO customerModel);

    List<CustomerDTO> viewAccounts() throws CommonException;

    CustomerDTO viewAccount(Map<String,String> panCard) throws CommonException;

    String updateDetails(Map<String, String> headers)throws CommonException;

    List<AccountDTO> findBankAccounts(Map<String, String> headers) throws CommonException;

}
