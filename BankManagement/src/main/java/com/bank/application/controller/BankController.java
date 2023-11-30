package com.bank.application.controller;

import com.bank.application.persistance.dto.BankDTO;
import com.bank.application.exception.NoDataPresentException;
import com.bank.application.service.Implementation.BankService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//This controller takes care of every bank operations

@RestController
@RequestMapping("api/v1/bank")
@Tag(name = "Bank Management")
@Hidden 
public class BankController {
    //Bank Service
    @Autowired
    private BankService bankService;

    //Endpoint for registering a bank
    @PostMapping("register")
    public ResponseEntity<BankDTO> registerBank(@Valid @RequestBody BankDTO newBank){
         return new ResponseEntity<>(bankService.registerbank(newBank), HttpStatus.CREATED);
    }

    @GetMapping("viewBanks")
    public ResponseEntity<List<BankDTO>> getAllBanks(){

        return new ResponseEntity<>(bankService.getAllBanks(), HttpStatus.OK);
    }

    @GetMapping("viewBanks/{bankId}")
    public ResponseEntity<BankDTO> getBank(@PathVariable String bankId) throws NoDataPresentException {

        BankDTO bankEntityWrapper = bankService.getBank(bankId);
        return new ResponseEntity<>(bankEntityWrapper, HttpStatus.OK);
    }

    @PutMapping("update/{bankId}")
    public ResponseEntity<BankDTO> updateBank(@PathVariable String bankId,
                                              @RequestParam(value = "updateName", required = false) String updateName,
                                              @RequestParam(value = "updateBranch", required = false) String updateBranch){

        BankDTO bankEntityWrapper = bankService.updateBank(bankId, updateName, updateBranch);
        return new ResponseEntity<>(bankEntityWrapper, HttpStatus.OK);
    }

    @DeleteMapping("delete/{bankId}")
    public ResponseEntity<String> deleteBank(@PathVariable String bankId) throws NoDataPresentException{
        BankDTO bankEntityWrapper = bankService.deleteBank(bankId);
        return ResponseEntity.ok().header("deleted Bank : " + bankEntityWrapper)
                .body("Deleted SuccessFully");
    }

}
