package com.bank.application.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
public class Transaction {

    @Id
    @SequenceGenerator(name = "transaction_id_sequence", sequenceName = "transaction_id_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_sequence")
    private int transactionId;
    @CreationTimestamp
    private String transactionDateTime;
    private String type;
    private String details;
    @Column(updatable = false)
    private Double amount;
    @Column(updatable = false)
    private Double previousBalance;
    @Column(updatable = false)
    private Double currentBalance;
    @Column(updatable = false)
    private String status;
    @Column(updatable = false)
    private String sendTo;
    @Column(updatable = false)
    private String receivedFrom;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionDateTime='" + transactionDateTime + '\'' +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", amount=" + amount +
                ", previousBalance=" + previousBalance +
                ", currentBalance=" + currentBalance +
                ", status='" + status + '\'' +
                ", sendTo='" + sendTo + '\'' +
                ", receivedFrom='" + receivedFrom + '\'' +
                ", account=" + account +
                '}';
    }
}
