package com.bank.application.persistance.conversion;

import com.bank.application.persistance.Customer;
import com.bank.application.persistance.dto.CustomerDTO;

public class CustomerMapper {

    public static Customer convertToEntity(CustomerDTO customerModel){

        Customer customer = new Customer();

        customer.setAge(customerModel.getAge());
        customer.setName(customerModel.getName());
        customer.setEmail(customerModel.getEmail());
        customer.setPanCard(customerModel.getPanCard());

        return customer;
    }

    public static CustomerDTO convertToModel(Customer customer){

        CustomerDTO customerModel = new CustomerDTO();

        customerModel.setAge(customer.getAge());
        customerModel.setName(customer.getName());
        customerModel.setEmail(customer.getEmail());
        customerModel.setPanCard(customer.getPanCard());

        return customerModel;
    }

}
