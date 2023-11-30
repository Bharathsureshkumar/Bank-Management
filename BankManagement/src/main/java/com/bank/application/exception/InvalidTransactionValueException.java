package com.bank.application.exception;

public class InvalidTransactionValueException extends CommonException{

    public InvalidTransactionValueException(String msg){
        super(msg);
    }
}
