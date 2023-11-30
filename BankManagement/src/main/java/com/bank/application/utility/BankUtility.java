package com.bank.application.utility;

import com.bank.application.persistance.Bank;
import com.bank.application.persistance.dto.BankDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankUtility {


    public void wrapBankToMapper(List<Bank> banks, List<BankDTO> bankEntityWrapper){

        for(Bank bank: banks){
            bankEntityWrapper.add(new BankDTO().wrapEntity(bank));
        }

    }



}
