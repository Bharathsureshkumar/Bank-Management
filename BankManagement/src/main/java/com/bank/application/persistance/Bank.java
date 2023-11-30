package com.bank.application.persistance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class  Bank {
    @Id
//    @NotBlank
//    @Size(min = 8, max = 11, message= "bank code should be  8 - 11 alphanumeric characters")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "bank code should contain only alpha numeric characters only.")
    private String bankId;
//    @NotNull(message = "Bank Name cant be empty")
//    @Pattern(regexp = "^[a-zA-Z]+$", message = "Bank Name Should contain Alphabets only")
    private String bankName;
    @CreationTimestamp
    private Date createdDate;
//    @Size(min=3, max=3, message = "branch code should be exactly 3 digit")
//    @Pattern(regexp = "^[0-9]+$", message = "provide a valid 3 digit numeric branch code")
    private String branchCode;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }


}
