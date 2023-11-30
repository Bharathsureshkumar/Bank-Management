package com.bank.application.service;

import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.persistance.dto.CustomerModel;
import com.bank.application.exception.CommonException;

import java.util.List;
import java.util.Map;

public interface ICustomerService {

    CustomerModel register(CustomerModel customerModel);

    List<CustomerModel> viewAccounts() throws CommonException;

    CustomerModel viewAccount(Map<String,String> panCard) throws CommonException;

    String updateDetails(Map<String, String> headers)throws CommonException;

    List<AccountDTO> findBankAccounts(Map<String, String> headers) throws CommonException;

}
