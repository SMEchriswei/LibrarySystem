package com.ascending.training.controller;

import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.ascending.training.model.Customer;
import com.ascending.training.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity getCustomerbyID(@PathVariable long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        String msg = "Customer with ID: " + customerId + " doesn't exist.";
        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(msg);

        if (customer != null) {
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_OK).body(customer);
            logger.info("Successfully get customer with ID: " + customer.getId()
                    + " Name: " + customer.getName());
        } else {
            logger.debug(msg);
        }

        return responseEntity;
    }

    @GetMapping(value = "/name/{customerName}", produces = "application/json")
    public ResponseEntity getCustomerbyName(@PathVariable String customerName) {
        List<Customer> customers = customerService.getCustomerByName(customerName);
        String msg = "Customer with Name: " + customerName + " doesn't exist.";
        ResponseEntity responseEntity = responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(msg);

        if (customers != null) {
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_OK).body(customers);

            for (Customer customer : customers) {
                logger.info("Successfully get customer with ID: " + customer.getId()
                        + " Name: " + customer.getName());
            }
        } else {
            logger.debug(msg);
        }

        return responseEntity;
    }

    @PostMapping(value = "", consumes = "application/json")
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        String msg = msg = "Cannot create customer with ID: " + customer.getId()
                + " Name: " + customer.getName();
        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_ACCEPTABLE).body(msg);
        boolean isSuccess = customerService.save(customer);

        if (isSuccess == true) {
            List<Customer> customers = customerService.getCustomerByName(customer.getName());
            Collections.sort(customers, (Customer fCus, Customer sCus) -> (int)(sCus.getId() - fCus.getId()));

            for (Customer currentCustomer : customers) {
                if (currentCustomer.equals(customer)) {
                    msg = "Customer has been created with ID: " + currentCustomer.getId()
                            + " Name: " + currentCustomer.getName();
                    responseEntity = ResponseEntity.status(HttpServletResponse.SC_CREATED).body(msg);
                    logger.info(msg);
                    break;
                }
            }
        } else {
            logger.info(msg + " " + customer.toString());
        }

        return responseEntity;
    }

    @PutMapping(value = "/{customerId}", consumes = "application/json")
    public ResponseEntity updateCustomer(@PathVariable long customerId, @RequestBody Customer customer) {
        Customer newCustomer = new Customer(customerId, customer.getName(), customer.getAddress());
        String msg = "Customer with ID: " + customerId
                + " Name: " + customer.getName() + " has been updated.";
        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_OK).body(msg);
        boolean isSuccess = customerService.update(newCustomer);

        if (!isSuccess) {
            msg = "Cannot update. Customer with ID: " + customerId
                    + " Name: " + customer.getName() + " does not exist.";
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(msg);
            logger.debug(msg);
        } else {
            logger.info(msg);
        }

        return responseEntity;
    }

    @DeleteMapping(value = "/{customerId}", produces = "application/json")
    public ResponseEntity deleteCustomer(@PathVariable long customerId) {
        String msg = "Customer with ID: " + customerId + " was deleted.";
        HashMap<String, String> jsonMap = new HashMap<>();
        jsonMap.put("msg", msg);

        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_NO_CONTENT).body(jsonMap);
        Customer customer = customerService.getCustomerById(customerId);
        boolean isSuccess = false;

        if (customer != null) {
            isSuccess = customerService.delete(customer.getName());
        }

        if (!isSuccess) {
            msg = "Cannot delete. Customer with ID: " + customerId + " does not exist.";
            jsonMap.put("msg", msg);
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(jsonMap);
            logger.debug(msg);
        } else {
            logger.info(msg);
        }

        return responseEntity;
    }
}
