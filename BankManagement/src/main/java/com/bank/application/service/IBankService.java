package com.bank.application.service;

import com.bank.application.persistance.dto.BankDTO;
import com.bank.application.exception.NoDataPresentException;

public interface IBankService {

    BankDTO registerbank(BankDTO bank);
    BankDTO updateBank(java.lang.String bankId, java.lang.String updateName, java.lang.String updateBranch);
    BankDTO deleteBank(java.lang.String bankId) throws NoDataPresentException;
    BankDTO getBank(java.lang.String bankId) throws NoDataPresentException;
}
