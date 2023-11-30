package com.bank.application.advice;

import com.bank.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    //Exception Handler for invalid arguments.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception){

        Map<String, String> errorMsg = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    errorMsg.put(error.getField(), error.getDefaultMessage());
                });

        return errorMsg;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoDataPresentException.class)
    public Map<String, String> DataNotFountExceptionHandler(NoDataPresentException exception){

        Map<String, String> errMap = new HashMap<>();
        errMap.put("Error Message :", exception.getMessage());
        errMap.put("Reason : ", "Requested Data not present in the DB");

        return errMap;
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(DataMismatchedException.class)
    public Map<String, String> handleDataMismatchException(DataMismatchedException exception){

        Map<String, String> errMap = new HashMap<>();
        errMap.put("Exception : ", "Data mismatched exception");
        errMap.put("ErrorMessage", exception.getMessage());

        return errMap;
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(InvalidTransactionValueException.class)
    public Map<String, String> handleTransactionException(InvalidTransactionValueException exception){

        Map<String, String> errMap = new HashMap<>();
        errMap.put("Exception : ", "Invalid credentials to make transaction");
        errMap.put("ErrMessage", exception.getMessage());

        return errMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAccountFoundExcetion.class)
    public Map<String, String> handleNoAccountFound(NoAccountFoundExcetion exception){
        Map<String, String> errMap = new HashMap<>();
        errMap.put("Exception : ", "Account not present");
        errMap.put("ErrMessage", exception.getMessage());

        return errMap;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccountNotActiveException.class)
    public Map<String, String> handleAccountNotActive(AccountNotActiveException exception){
        Map<String, String> errMap = new HashMap<>();
        errMap.put("Exception : ", "Account is Not in active state");
        errMap.put("ErrMessage", exception.getMessage());

        return errMap;
    }


    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(MinimumBalanceException.class)
    public Map<String, String> MinimumBalanceException(MinimumBalanceException exception){
        Map<String, String> errMap = new HashMap<>();
        errMap.put("Exception : ", "Minimum balance required");
        errMap.put("ErrMessage", exception.getMessage());

        return errMap;
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(RequiredHeaderArgumentException.class)
    public Map<String, String> handleRequiredHeaderArgumentException(RequiredHeaderArgumentException exception){
        Map<String, String> errMap = new HashMap<>();
        errMap.put("Exception : ", "required argument missing");
        errMap.put("ErrMessage", exception.getMessage());

        return errMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPanCardNumberException.class)
    public Map<String, String> handleInvalidPanNumber(InvalidPanCardNumberException exception){

        Map<String, String> errMap = new HashMap<>();

        errMap.put("Error Message", exception.getMessage());

        return errMap;
    }
}