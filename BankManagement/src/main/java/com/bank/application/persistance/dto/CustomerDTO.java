package com.bank.application.persistance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTO {


    @Size(min = 10, max = 10, message = "pan card number size should be exactly 10")
    @Pattern(regexp = "^[0-9A-Z]+$", message = "pan card should contain only alpha numeric values ..")
    String panCard;

    @NotBlank(message = "customer name can not be empty ..")
    @Pattern(regexp = "^[a-zA-z]+$", message = "provide a valid customer name")
    @JsonProperty(namespace = "Customer Name")
    String name;

    @NotNull
    @Positive(message = "Enter the valid age ..")
    @Min(value = 11 , message = "Age should be more than 10")
    @Max(value = 100 , message = "Age should less than 100")
    @JsonProperty(namespace = "Customer Age")
    int age;

    @NotBlank(message = "Email cannot be empty ..")
    @Pattern(regexp = "^(.+)@(.+)$", message = "Provide a valid email address ..")
    @JsonProperty(namespace = "Customer Email ID")
    String email;

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
