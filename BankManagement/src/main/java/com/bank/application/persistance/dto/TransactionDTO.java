package com.bank.application.persistance.dto;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TransactionDTO {

    private int transactionId;
    //Mapping
    private String accountNumber;

    @NotBlank(message = "account type can not be null")
    @Pattern(regexp = "^[a-z]+$", message="Type of transaction is not valid")
    private String type;

    @NotBlank(message = "Transaction Details can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Transaction details should contain only alphaNumeric characters")
    private String details;
    @NotBlank(message = "Amount field should be filled")
    @Size(min = 10, max = 100000, message = "Enter the amount between 10 to 100000")
    @Pattern(regexp = "^[0-9]+$")
    private Double amount;
    @NotBlank(message = "Previous balance is empty")
    @Pattern(regexp = "^[0-9]", message = "Message should contain only numeric values")
    private Double previousBalance;
    @NotBlank(message = "Previous balance is empty")
    @Size(min = 1, max = 100000, message = "Current balance should be between 1 - 100000")
    @Pattern(regexp = "^[0-9]", message = "Message should contain only numeric values")
    private Double currentBalance;
    @NotBlank(message = "status of the transaction is Empty")
    @Pattern(regexp = "^[a-z]+$", message = "Status of the transaction is invalid")
    private String status;

    private String sendTo;
    private String receivedFrom;

    @JsonIgnore
    private Account account;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(Double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public TransactionDTO mapFromTransaction(Transaction transaction){

        this.amount = transaction.getAmount();
        this.status = transaction.getStatus();
        this.currentBalance = transaction.getCurrentBalance();
        this.previousBalance = transaction.getPreviousBalance();
        this.details = transaction.getDetails();

        return this;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
