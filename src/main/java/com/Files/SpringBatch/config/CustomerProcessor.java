package com.Files.SpringBatch.config;

import org.springframework.batch.item.ItemProcessor;

import com.Files.SpringBatch.Entity.Customer;

 
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

	public Customer process(Customer customer) throws Exception{
		return customer;
	}
}
