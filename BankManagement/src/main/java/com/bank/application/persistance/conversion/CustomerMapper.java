package com.bank.application.persistance.conversion;

import com.bank.application.persistance.Customer;
import com.bank.application.persistance.dto.CustomerModel;

public class CustomerMapper {

    public static Customer convertToEntity(CustomerModel customerModel){

        Customer customer = new Customer();

        customer.setAge(customerModel.getAge());
        customer.setName(customerModel.getName());
        customer.setEmail(customerModel.getEmail());
        customer.setPanCard(customerModel.getPanCard());

        return customer;
    }

    public static CustomerModel convertToModel(Customer customer){

        CustomerModel customerModel = new CustomerModel();

        customerModel.setAge(customer.getAge());
        customerModel.setName(customer.getName());
        customerModel.setEmail(customer.getEmail());
        customerModel.setPanCard(customer.getPanCard());

        return customerModel;
    }

}
