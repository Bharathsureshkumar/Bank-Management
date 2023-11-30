package com.bank.application.service.Implementation;

import com.bank.application.persistance.Bank;
import com.bank.application.persistance.dto.BankDTO;
import com.bank.application.exception.NoDataPresentException;
import com.bank.application.repository.BankRepo;
import com.bank.application.service.IBankService;
import com.bank.application.utility.BankUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class BankService implements IBankService {

    static Logger log = LoggerFactory.getLogger(BankService.class);
    @Autowired
    BankRepo bankRepo;
    @Autowired
    BankUtility bankUtility;

    //Below function registers new bank into the DataBase.
    public BankDTO registerbank(BankDTO bankEntityWrapper){

        Bank bank = bankEntityWrapper.get();
        log.info("Recieved Bank Data : {}", bank);
        bankRepo.save(bank);
        return bankEntityWrapper;
    }

    //Below function retrieves all Banks from DB

    public List<BankDTO> getAllBanks(){

        List<Bank> banks = bankRepo.findAll();
        List<BankDTO> bankWrapperedList = new ArrayList<>();
        bankUtility.wrapBankToMapper(banks, bankWrapperedList);

        return bankWrapperedList;
    }

    //Below function retrieves the particular bank by ID.
    public BankDTO getBank(String bankId) throws NoDataPresentException {

        BankDTO bankEntityWrapper = null;
        bankEntityWrapper = new BankDTO().wrapEntity(bankRepo.findByBankId(bankId));
        log.info("Result for finding bank : {}", bankEntityWrapper);

        if(bankEntityWrapper == null) {
            throw new NoDataPresentException("Bank not found.. !");
        }
        else
            return bankEntityWrapper;
    }

    //Below function updates bankName and bankBranch code.
    public BankDTO updateBank(String bankId, String updateName, String updateBranch){

        Bank bank = bankRepo.findByBankId(bankId);

        log.info("Updating the bank with updated values : Bank Name {} : Branch Code {}", updateName, updateBranch);

        if(updateName != null) bank.setBankName(updateName);
        if(updateBranch != null) bank.setBranchCode(updateBranch);
        log.info("Saving the changes into DB : {}", bank);
        //Updating the changes in the DB
        bankRepo.save(bank);

        BankDTO bankEntityWrapper = new BankDTO().wrapEntity(bank);

        return bankEntityWrapper;
    }

    //Below function Deletes a particular bank account
    public BankDTO deleteBank(String bankId) throws NoDataPresentException{

        Bank bank = bankRepo.findByBankId(bankId);

        log.info("Result For finding bank to Delete : {}", bank);
        if(bank == null) {throw new NoDataPresentException("Bank not Found .. !");}
        else {
            BankDTO bankEntityWrapper = new BankDTO().wrapEntity(bank);
            bankRepo.delete(bank);
           return bankEntityWrapper;
        }
    }
}
