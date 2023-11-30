package com.bank.application.controller;


import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.persistance.dto.CustomerModel;
import com.bank.application.exception.CommonException;
import com.bank.application.service.Implementation.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/bank/customer")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Customer Management")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("register")
    public ResponseEntity<CustomerModel> registerCustomer(@Valid @RequestBody CustomerModel customer)throws CommonException {

        customerService.register(customer);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("viewAllAccounts")
    public ResponseEntity<List<CustomerModel>> getAccount() throws CommonException{

        return new ResponseEntity<>(customerService.viewAccounts(), HttpStatus.OK);
    }

    @GetMapping("viewAccount")
    public ResponseEntity<CustomerModel> getAccount(@RequestHeader Map<String, String> headers) throws CommonException{

        return new ResponseEntity<>(customerService.viewAccount(headers), HttpStatus.OK);
    }

    @PostMapping("updateAccount")
    public ResponseEntity<String> updateAccount(@RequestHeader Map<String, String> headers) throws CommonException{

        return new ResponseEntity<>(customerService.updateDetails(headers), HttpStatus.OK);
    }


    @GetMapping("getBankAccounts")
    public ResponseEntity<List<AccountDTO>> getAllBankAccounts(@RequestHeader Map<String, String> headers) throws CommonException{

        return new ResponseEntity<>(customerService.findBankAccounts(headers), HttpStatus.OK);
    }


    //Get mapping for retrieving accounts of a customer

}
