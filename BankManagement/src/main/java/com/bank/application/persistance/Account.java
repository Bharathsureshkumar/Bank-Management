package com.bank.application.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Account {

//    @SequenceGenerator(name = "account_sequence", sequenceName = "account_id_sequence", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
//    private int accountId;

    @Id
//    @NotBlank
//    @Size(min = 11, max = 11, message = "Account number must be count of 11 digits")
//    @Pattern(regexp = "^[0-9]+$", message = "Account Number Should consist only Digits")
    private String accountNumber;

//    @NotBlank
//    @DecimalMin(value = "1000.00")
//    @Pattern(regexp = "^[0-9.]+$")
    private String accountBalance;
//    @NotBlank(message = "bank type cant be empty")
//    @Pattern(regexp = "^[a-z]+$", message = "Provide proper account type")
    private String accountType;
//    @NotBlank
//    @Pattern(regexp = "^[a-z]+$")
    private String status;
    @CreationTimestamp
    private Date creationTime;
    @UpdateTimestamp
    private Date lastUpdation;


    //Mapping to Transaction Table
    @JsonIgnore
    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @ManyToOne
    Customer customer;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdation() {
        return lastUpdation;
    }

    public void setLastUpdation(Date lastUpdation) {
        this.lastUpdation = lastUpdation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Account{" +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountBalance='" + accountBalance + '\'' +
                ", accountType='" + accountType + '\'' +
                ", status='" + status + '\'' +
                ", creationTime=" + creationTime +
                ", lastUpdation=" + lastUpdation +
                '}';
    }
}
