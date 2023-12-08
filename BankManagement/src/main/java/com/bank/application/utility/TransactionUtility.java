package com.bank.application.utility;

import com.bank.application.persistance.Transaction;
import com.bank.application.persistance.dto.TransactionDTO;
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

    public List<TransactionDTO> getMapperFromRepo(){
        List<TransactionDTO> transactionMapper = null;

        List<Transaction> transactions = transactionRepo.findAll();

        //Conerting Transaction to Mapper

        for(Transaction transaction: transactions){
            transactionMapper.add(new TransactionDTO().mapFromTransaction(transaction));
        }

        return transactionMapper;

    }

    //Checking all the required details present to make transaction
    public boolean validateData(Map<String, String> headers) throws RequiredHeaderArgumentException, InvalidTransactionValueException {

        boolean validationFlag = true;

        if(headers.containsKey(ApplicationConstants.PAYEE_ACCOUNT_NUMBER) && headers.containsKey(ApplicationConstants.PAYER_ACCOUNT_NUMBER) && headers.containsKey("details") && headers.containsKey("amount")) {

            List<String> accountNumbers = Arrays.asList(headers.get(ApplicationConstants.PAYEE_ACCOUNT_NUMBER), headers.get(ApplicationConstants.PAYER_ACCOUNT_NUMBER));
            String amount = headers.get(ApplicationConstants.AMOUNT);
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
                        log.info("Amount validation for transaction passed ..");
                    }
                    else{
                        validationFlag = false;
                        throw new InvalidTransactionValueException("Invalid amount or transaction amount should be between 10 - 100000");
                    }

                //details Validation
                if(detailsField.getRequired())
                    if(details.matches(detailsField.getFormat())
                            && details.length() <= detailsField.getLength()){

                        System.out.println(details.matches(detailsField.getFormat())+":\n:" + (details.length() <= detailsField.getLength()) +":\n:");
                        log.info("Amount validation for transaction passed ..");
                    }
                    else{

                        validationFlag = false;
                        if(!details.matches(detailsField.getFormat()) || (details.length() <= detailsField.getLength())) {
                            throw new InvalidTransactionValueException("Invalid Message");
                        }
                        throw new InvalidTransactionValueException("Invalid details should contain alphabets and length less than 100..");
                    }
            } catch (IOException e) {

                log.error(e.getMessage());
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

        if(headers.containsKey(ApplicationConstants.PAYEE_ACCOUNT_NUMBER)
                && headers.containsKey(ApplicationConstants.PAYER_ACCOUNT_NUMBER)
                && headers.containsKey("amount")) {

            boolean payee = transactionBusinessValidation.checkAccount(headers.get(ApplicationConstants.PAYEE_ACCOUNT_NUMBER));
            boolean payer = transactionBusinessValidation.checkAccount(headers.get(ApplicationConstants.PAYER_ACCOUNT_NUMBER));

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
                log.info("Payee Account status is : {}" , payee_status);
                throw new AccountNotActiveException("The payee account is not active ..");
            }

            boolean payer_status = transactionBusinessValidation.checkStatus(headers.get("payer-account-number"));

            if(!payer_status){
                log.info("Payer Account is status is : {}" , payer_status);
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



