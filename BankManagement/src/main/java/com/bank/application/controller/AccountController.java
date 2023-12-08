package com.bank.application.controller;

import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.exception.CommonException;
import com.bank.application.exception.RequiredHeaderArgumentException;
import com.bank.application.service.IAccountService;
import com.bank.application.utility.ApplicationConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/account")
@Tag( name = "Account Management")
public class AccountController {

    static Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    IAccountService accountService;

    @PostMapping("register")
    public ResponseEntity<AccountDTO> register(@RequestBody @Valid AccountDTO account, @RequestHeader Map<String, String> headers) throws CommonException{

        return new ResponseEntity<>(accountService.register(account, headers), HttpStatus.CREATED);
    }

    @GetMapping("viewAccounts")
    public ResponseEntity<List<AccountDTO>> viewAllAccounts(@RequestHeader Map<String, String> headers)
            throws CommonException {

        String accountNumber = null;
        if(headers.containsKey(ApplicationConstants.ACCOUNT_NUMBER))
        {
            accountNumber = headers.get(ApplicationConstants.ACCOUNT_NUMBER);
        }

        log.info("Fetching accounts endpoint has been called");
        List<AccountDTO> accounts = accountService.fetchAccount(accountNumber);

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<AccountDTO> updateStatus(@RequestHeader Map<String, String> headers)
            throws CommonException {

        log.info( "Account updation endpoint has been called with : {} ", headers.get(ApplicationConstants.STATUS));
        if(headers.containsKey(ApplicationConstants.STATUS) && headers.containsKey("account-number")){

            return new ResponseEntity<>(accountService.updateAccount(headers.get("account-number"),
                    headers.get(ApplicationConstants.STATUS)), HttpStatus.OK);
        }
        else{
            throw new RequiredHeaderArgumentException("Missing required status argument in the header");
        }
    }
}