package com.bank.application.persistance.dto;

import com.bank.application.persistance.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccountDTO {

    @NotBlank
    @Size(min = 11, max = 11, message = "Account number must be count of 11 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Account Number Should consist only Digits")
    private String accountNumber;

//    private String panCard;
    @NotBlank
    @DecimalMin(value = "1000.00")
    @Pattern(regexp = "^[0-9.]+$")
    private String accountBalance;
    @NotBlank(message = "bank type cant be empty")
    @Pattern(regexp = "^[a-z]+$", message = "Provide proper account type")
    private String accountType;
    @NotBlank
    @Pattern(regexp = "^[a-z]+$")
    private String status;

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

    public AccountDTO wrapBankDetails(Account account){

        this.accountNumber = account.getAccountNumber();
        this.accountBalance = account.getAccountBalance();
        this.accountType = account.getAccountType();
        this.status = account.getStatus();
        return this;
    }

}
