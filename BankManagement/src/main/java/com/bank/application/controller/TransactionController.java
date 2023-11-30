package com.bank.application.controller;

import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.dto.TransactionModel;
import com.bank.application.exception.CommonException;
import com.bank.application.repository.AccountRepo;
import com.bank.application.repository.TransactionRepo;
import com.bank.application.service.Implementation.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/bank")
@Tag(name = "Transaction Management")
public class TransactionController {

    static Logger log = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountRepo accountRepo;

    @GetMapping("getTransactions")
    public ResponseEntity<List<TransactionModel>> getTransactions(@RequestHeader Map<String, String> headers) throws CommonException {

//        Account account = transactionService.getAllTransactions(headers);
        List<TransactionModel> trns = transactionService.fetchAccountWithTransaction(headers);
        return new ResponseEntity<>(trns, HttpStatus.OK);

    }


    @GetMapping("transactions")
    public ResponseEntity<List<Transaction>> getT(){

        return new ResponseEntity<>(transactionRepo.findByAccount(accountRepo.findByAccountNumber("20769789121").get(0)),HttpStatus.OK);

    }

}