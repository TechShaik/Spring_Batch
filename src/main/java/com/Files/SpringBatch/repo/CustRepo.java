package com.Files.SpringBatch.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Files.SpringBatch.Entity.Customer;

 
@Repository
public interface CustRepo extends JpaRepository<Customer, Integer> {

}
