package com.bank.application.validators;

import com.bank.application.utility.ResourceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberValidator {


    AccountValidationConfig accountvalidationConfig;

    @Autowired
    ApplicationContext applicationContext;

    public boolean validateAccountNumber(String accountNumber){

        accountvalidationConfig = new AccountValidationConfig();

        boolean validationFlag = false;

        Resource resource = applicationContext.getResource("classpath:TransactionValidationConfig.json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            AccountValidationConfig tnsConfig = mapper.readValue(ResourceUtils.getResourceContent(resource), AccountValidationConfig.class);
            System.out.println(tnsConfig.toString());


            Field accountField = tnsConfig.getRequiredTransactionFields().getHeader().getAccount();

            if(accountNumber.matches(accountField.getFormat())){
                validationFlag = true;
            }

        }
        catch (Exception e){

        }
        return validationFlag;
    }
}
