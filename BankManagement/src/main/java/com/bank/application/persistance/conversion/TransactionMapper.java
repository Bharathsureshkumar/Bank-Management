package com.bank.application.persistance.conversion;

import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.dto.TransactionModel;

public class TransactionMapper {

    public static Transaction mapperToEntity(TransactionModel transactionModel){

        Transaction transaction = new Transaction();

        transaction.setSendTo(transactionModel.getSendTo());
        transaction.setReceivedFrom(transactionModel.getReceivedFrom());
        transaction.setDetails(transactionModel.getDetails());
        transaction.setPreviousBalance(transaction.getPreviousBalance());
        transaction.setCurrentBalance(transaction.getCurrentBalance());

        return transaction;

    }

    public static TransactionModel entityToMapper(Transaction transaction){

        TransactionModel transactionModel = new TransactionModel();

//        transactionModel.setAccount(transaction.getAccount());
        transactionModel.setAmount(transaction.getAmount());
        transactionModel.setAccount(transaction.getAccount());
        transactionModel.setTransactionId(transaction.getTransactionId());
        transactionModel.setType(transaction.getType());
        transactionModel.setDetails(transaction.getDetails());
        transactionModel.setPreviousBalance(transaction.getPreviousBalance());
        transactionModel.setCurrentBalance(transaction.getCurrentBalance());
        transactionModel.setAccountNumber(transaction.getAccount().getAccountNumber());
        transactionModel.setStatus(transaction.getStatus());
        transactionModel.setSendTo(transaction.getSendTo());
        transactionModel.setReceivedFrom(transaction.getReceivedFrom());

        return transactionModel;

    }


}
