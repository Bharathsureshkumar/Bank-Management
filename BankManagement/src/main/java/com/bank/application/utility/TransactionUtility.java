package com.bank.application.utility;

import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.dto.TransactionModel;
import com.bank.application.exception.*;
import com.bank.application.repository.TransactionRepo;

import com.bank.application.validators.AccountValidationConfig;
import com.bank.application.validators.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class TransactionUtility {

    static Logger log= LoggerFactory.getLogger(TransactionUtility.class);
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    TransactionBusinessValidation transactionBusinessValidation;




    public List<TransactionModel> getMapperFromRepo(){
        List<TransactionModel> transactionMapper = null;

        List<Transaction> transactions = transactionRepo.findAll();

        //Conerting Transaction to Mapper

        for(Transaction transaction: transactions){
            transactionMapper.add(new TransactionModel().mapFromTransaction(transaction));
        }

        return transactionMapper;

    }

    //Checking all the required details present to make transaction
    public boolean validateData(Map<String, String> headers) throws RequiredHeaderArgumentException, InvalidTransactionValueException {

        boolean validationFlag = true;

        if(headers.containsKey("payee-account-number") && headers.containsKey("payer-account-number") && headers.containsKey("details") && headers.containsKey("amount")) {

            List<String> accountNumbers = Arrays.asList(headers.get("payee-account-number"), headers.get("payer-account-number"));
            String amount = headers.get("amount");
            String details = headers.get("details");

            Resource resource = applicationContext.getResource("classpath:TransactionValidationConfig.json");
            ObjectMapper mapper = new ObjectMapper();

            try {
                AccountValidationConfig tnsConfig = mapper.readValue(ResourceUtils.getResourceContent(resource), AccountValidationConfig.class);
                System.out.println(tnsConfig.toString());

                Field accountField = tnsConfig.getRequiredTransactionFields().getHeader().getAccount();
                Field amountField = tnsConfig.getRequiredTransactionFields().getHeader().getAmount();
                Field detailsField = tnsConfig.getRequiredTransactionFields().getHeader().getDetails();


                //Account Number Validation ..
                if(accountField.getRequired())
                for(String accountNumber: accountNumbers) {

                    if (accountNumber.matches(accountField.getFormat())
                            && accountNumber.length() == accountField.getLength()) {
                        System.out.println("Account Number validation for transaction passed ..");
                    } else {
                        validationFlag = false;
                        throw new InvalidTransactionValueException("Invalid account Number Parameter in header Exception ..");
                    }
                }

                //Amount Validation
                if(amountField.getRequired())
                    if(amount.matches(amountField.getFormat())
                            && Double.parseDouble(amount) <= amountField.getMax()
                            && Double.parseDouble(amount) >= amountField.getMin()){
                        System.out.println("Amount validation for transaction passed ..");
                    }
                    else{
                        validationFlag = false;
                        throw new InvalidTransactionValueException("Invalid amount Parameter in header Exception ..");
                    }

                //details Validation
                if(detailsField.getRequired())
                    if(details.matches(detailsField.getFormat())
                            && details.length() <= detailsField.getLength()){
                        System.out.println(details.matches(detailsField.getFormat())+":\n:" + (details.length() <= detailsField.getLength()) +":\n:");
                        System.out.println("Amount validation for transaction passed ..");
                    }
                    else{
                        validationFlag = false;
                        System.out.println(details.matches(detailsField.getFormat())+":\n:" + (details.length() <= detailsField.getLength()) +":\n:");
                        throw new InvalidTransactionValueException("Invalid details should contain alphabets and length less than 100..");
                    }


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            throw new RequiredHeaderArgumentException("The Required details to make transaction is not fulfilled in basic validation..");
        }


        return validationFlag;
    }

    public boolean checkCredentials(Map<String, String> headers)
            throws CommonException{

        //In this method that verifies the payee and payer account and amount to be transferred

        boolean validationFlag = true;

        if(headers.containsKey("payee-account-number")
                && headers.containsKey("payer-account-number")
                && headers.containsKey("amount")) {

            boolean payee = transactionBusinessValidation.checkAccount(headers.get("payee-account-number"));
            boolean payer = transactionBusinessValidation.checkAccount(headers.get("payer-account-number"));

            //checking payee account present in the DB
            if(!payee){

                throw new NoAccountFoundExcetion("Payee account Not Found Exception ..");
            }
            else{
                log.info("Payee account found ..");
            }

            //checking payer account present in the DB
            if(!payer){

                throw new NoAccountFoundExcetion("Payer account Not Found Exception ..");
            }
            else{
                log.info("Payer account found ..");
            }


            //checking both of the accounts are active
            boolean payee_status = transactionBusinessValidation.checkStatus(headers.get("payee-account-number"));

            if(!payee_status){
                log.info("Payee Account is status active : {}" , payee_status);
                throw new AccountNotActiveException("The payee account is not active ..");
            }

            boolean payer_status = transactionBusinessValidation.checkStatus(headers.get("payer-account-number"));

            if(!payer_status){
                log.info("Payer Account is status active : {}" , payer_status);
                throw new AccountNotActiveException("The payer account is not active ..");
            }

            //Checking the transaction amount can be made
            boolean balance_flag = transactionBusinessValidation.compareBalance(headers.get("payee-account-number"), headers.get("amount"));

            if(!balance_flag){
                throw new MinimumBalanceException("Insufficient balance to make transaction .. ");
            }
        }
        else{
            throw new RequiredHeaderArgumentException("The Required details to make transaction is not fulfilled in Business validation..");
        }

        return validationFlag;
    }



}



