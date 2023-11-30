package com.bank.application.service.Implementation;

import com.bank.application.persistance.Account;
import com.bank.application.persistance.Customer;
import com.bank.application.persistance.conversion.AccountMapper;
import com.bank.application.persistance.conversion.CustomerMapper;
import com.bank.application.persistance.dto.AccountDTO;
import com.bank.application.persistance.dto.CustomerModel;
import com.bank.application.exception.*;
import com.bank.application.repository.CustomerRepo;
import com.bank.application.service.ICustomerService;
import com.bank.application.validators.CustomerValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    static Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerValidation customerValidation;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AccountMapper accountMapper;

    @Override
    public CustomerModel register(CustomerModel customerModel) {

        Customer customer = CustomerMapper.convertToEntity(customerModel);


        log.info("customer details being saved :  {}", customer);
        customerRepo.save(customer);
        customerModel = CustomerMapper.convertToModel(customer);

        return customerModel;
    }

    @Override
    public List<CustomerModel> viewAccounts() throws CommonException{

        List<Customer> customers = customerRepo.findAll();

        List<CustomerModel> customerModels = new ArrayList<>();

        if(customers.isEmpty()) throw new NoDataPresentException("No customer records fount ..");
        for(Customer customer: customers){
            customerModels.add(CustomerMapper.convertToModel(customer));
        }

        return customerModels;
     }

    @Override
    public CustomerModel viewAccount(Map<String, String> headers) throws CommonException{

        CustomerModel customerModel = null;
        String panCard = null;

        //Validating panCard field present or not;
        if(!headers.containsKey("pan-card")){
            log.info("pan-card field not found in the headers");
            throw new RequiredHeaderArgumentException("pan-card number field not found Exception ..");
        }
        else {

            panCard = headers.get("pan-card");
            log.info("Retrieving pan-card : {}", panCard);
            //Validation valid panCard number or not
            if (customerValidation.validatePancard(panCard)) {

                Optional<Customer> customer = customerRepo.findById(panCard);
                //Validating customer present in the DB or not
                if (customer.isEmpty()) {

                    log.info("Customer details not found for the pan number : {}", panCard);
                    throw new NoCustomerExistException("No bank customer found with the pan number..");
                } else {

                    log.info("Customer details for the pan number {} : FOUND", panCard);
                    customerModel = CustomerMapper.convertToModel(customerRepo.findById(panCard).get());
                }
            } else {

                log.info("Invalid pan card number exception raised ..");
                throw new InvalidPanCardNumberException("Invalid pan card number Exception ..");
            }
        }

        return customerModel;
    }

    @Override
    public String updateDetails(Map<String, String> headers) throws CommonException{

        CustomerModel customerModel = null;

        //Checking panCard number present or not
        if(headers.containsKey("pan-card")){

            String panCard = headers.get("pan-card");

            //Validating the pan card value
            if(customerValidation.validatePancard(panCard)){

                //Validating the account present in the DB or not
                if(customerRepo.findById(panCard).isPresent()){

                    Customer customer = customerRepo.findById(panCard).get();
                    //validating the fields to be updated
                    //name, age, email
                    boolean updation_flag = false;
                    if(headers.containsKey("name")){

                        if(customerValidation.validateName(headers.get("name"))){
                            customer.setName(headers.get("name"));
                        }
                        else{
                            throw new DataMismatchedException("Updation name is not valid ..");
                        }
                        updation_flag = true;
                    }
                    if(headers.containsKey("age")){

                        if(customerValidation.validateAge(Integer.parseInt(headers.get("age")))){
                            customer.setAge(Integer.parseInt(headers.get("age")));
                        }
                        else{
                            throw new DataMismatchedException("Updation age is not valid ..");
                        }
                        updation_flag = true;
                    }
                    if(headers.containsKey("email")){

                        if(customerValidation.validateEmail(headers.get("email"))){
                            customer.setEmail(headers.get("email"));
                        }
                        else{
                            throw new DataMismatchedException("Updation email is not valid ..");
                        }

                        updation_flag = true;
                    }
                    //End of header fields validation ..
                    if(updation_flag) { customerRepo.save(customer);}
                    else{throw new RequiredHeaderArgumentException("No updation fields found Exception .."); }

                }else{
                    log.info("Customer details not found for the pan number : {}", panCard);
                    throw new NoCustomerExistException("No bank customer found with the pan number..");
                }

            }
            else{
                log.info("Invalid pan card number exception raised ..");
                throw new InvalidPanCardNumberException("Invalid pan card number Exception ..");
            }
        }else{

            log.info("pan card field not found in the requested header ..");
            throw new RequiredHeaderArgumentException("pan card field not found in the requested header ..");
        }

        return "Updated successfully .. !";
    }
    @Override
    public List<AccountDTO> findBankAccounts(Map<String, String> headers) throws CommonException{

        List<AccountDTO> accountsModel = new ArrayList<>();

        if(headers.containsKey("pan-card")) {

            //Validating valid pan card or not
            if (!customerValidation.validatePancard(headers.get("pan-card"))) {

                throw new InvalidPanCardNumberException("Invalid pan card number ..");
            }
            //Validating customer present or not
            if (!customerRepo.findById(headers.get("pan-card")).isPresent()) {

                throw new NoDataPresentException("Entered pan card customer not found in the DB");
            }
            Customer customer = customerRepo.findById(headers.get("pan-card")).get();
             List<Account> accounts = customer.getAccounts();
            System.out.println(accounts.size());
            //converting into models
            for(Account account: accounts){

                System.out.println(account);
                accountsModel.add(accountMapper.convertToMapper(account));
            }
        }
        else{

            throw new RequiredHeaderArgumentException("Required header pan-card not found Exception ..");
        }


        return accountsModel;
    }

}
