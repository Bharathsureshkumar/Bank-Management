package com.bank.application.controller;

import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.persistance.dto.TransactionDTO;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestHeader Map<String, String> headers) throws CommonException {

//        Account account = transactionService.getAllTransactions(headers);
        List<TransactionDTO> trns = transactionService.getTransactions(headers);
        return new ResponseEntity<>(trns, HttpStatus.OK);
    }


    @GetMapping("getAllTransactions")
    public ResponseEntity<List<TransactionDTO>> getT(){

        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

}