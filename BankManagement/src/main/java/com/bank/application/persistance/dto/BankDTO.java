package com.bank.application.persistance.dto;

import com.bank.application.persistance.Bank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class BankDTO {

    @NotBlank
    @Size(min = 8, max = 11, message = "bank code should be  8 - 11 alphanumeric characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "bank code should contain only alpha numeric characters only.")
    private String bankId;
    @NotNull(message = "Bank Name cant be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Bank Name Should contain Alphabets only")
    private String bankName;
    @Size(min=3, max=3, message = "branch code should be exactly 3 digit")
    @Pattern(regexp = "^[0-9]+$", message = "provide a valid 3 digit numeric branch code")
    private String branchCode;

    public Bank get(){

        Bank bank = new Bank();
        bank.setBankId(this.bankId);
        bank.setBankName(this.bankName);
        bank.setBranchCode(this.branchCode);

        return bank;
    }

    public BankDTO wrapEntity(Bank bank){

        this.bankId = bank.getBankId();
        this.bankName = bank.getBankName();
        this.branchCode = bank.getBranchCode();
        return this;
    }

}
