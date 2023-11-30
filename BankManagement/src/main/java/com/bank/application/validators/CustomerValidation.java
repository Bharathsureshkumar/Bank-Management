package com.bank.application.validators;

import com.bank.application.utility.ResourceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;


class CustomerObject{

    CustomerField panCard;

    CustomerField name;

    CustomerField email;

    CustomerField age;

    public CustomerField getPanCard() {
        return panCard;
    }

    public void setPanCard(CustomerField panCard) {
        this.panCard = panCard;
    }

    public CustomerField getName() {
        return name;
    }

    public void setName(CustomerField name) {
        this.name = name;
    }

    public CustomerField getEmail() {
        return email;
    }

    public void setEmail(CustomerField email) {
        this.email = email;
    }

    public CustomerField getAge() {
        return age;
    }

    public void setAge(CustomerField age) {
        this.age = age;
    }
}

class CustomerConfig{
    CustomerObject customer;

    public CustomerObject getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerObject customer) {
        this.customer = customer;
    }
}

@Component
public class CustomerValidation {

    static Logger log = LoggerFactory.getLogger(CustomerValidation.class);
    @Autowired
    ApplicationContext applicationContext;

    CustomerConfig customerConfig = new CustomerConfig();;


    @PostConstruct
    public void init(){


        ObjectMapper mapper = new ObjectMapper();

        Resource resource = applicationContext.getResource("classpath:CustomerValidationConfig.json");

        try {
            customerConfig = mapper.readValue(ResourceUtils.getResourceContent(resource), CustomerConfig.class);

            log.info("customer validation properties file loaded successfully");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //pending validate the file whether it maps to the object or not
    }


    public boolean validatePancard(String panCard){

        boolean validationFlag = false;

        String panPattern = customerConfig.getCustomer().getPanCard().getFormat();
        int panLength = Integer.parseInt(customerConfig.getCustomer().getPanCard().getLength());
        if(panCard.matches(panPattern) && panCard.length() == panLength){
            validationFlag = true;
        }

        return validationFlag;
    }

    public boolean validateName(String name){

        boolean validationFlag = false;

        String namePattern = customerConfig.getCustomer().getName().getFormat();
        if(name.matches(namePattern)){
            validationFlag = true;
        }

        return validationFlag;
    }

    public boolean validateEmail(String name){

        boolean validationFlag = false;

        String emailPattern = customerConfig.getCustomer().getEmail().getFormat();
        if(name.matches(emailPattern)){
            validationFlag = true;
        }

        return validationFlag;
    }

    public boolean validateAge(int age){

        boolean validationFlag = false;

        int min = customerConfig.getCustomer().getAge().getMin();
        int max = customerConfig.getCustomer().getAge().getMax();

        if(age > min && age <= max){
            validationFlag = true;
        }

        return validationFlag;
    }

}
