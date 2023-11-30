package com.bank.application.controller;

import com.bank.application.persistance.Transaction;
import com.bank.application.exception.*;
import com.bank.application.service.Implementation.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/bankService")
@Tag(name = "Transaction Service")
public class BankServiceController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("transfer")
    public ResponseEntity<List<Transaction>> transferAmount(@RequestHeader Map<String, String> headers)
            throws CommonException {

        List<Transaction> transactions = transactionService.transfer(headers);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
