package com.ascending.training.service;

import com.ascending.training.init.AppInitializer;
import com.ascending.training.model.Customer;
import com.ascending.training.repository.CustomerDao;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInitializer.class)
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    private Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

//    @BeforeClass
//    public static void beforeClass() {
//        customerService = new CustomerDaoImpl();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        customerService = null;
//    }

    @Before
    public void before() {
        Customer fCustomer = new Customer("Timothy", "255 A St NE, Washington, DC");
        customerService.save(fCustomer);
        Customer sCustomer = new Customer("Joe", "6787 Washington Blvd, Arlington, VA");
        customerService.save(sCustomer);
    }

    @After
    public void after() {
        Boolean fret = customerService.delete("Joe");
        Boolean sret = customerService.delete("Timothy");
    }

    @Test
    public void save() {
        Customer newCustomer = new Customer("Kenton", "2111 Jefferson Davis Hwy APT 703N, Arlington, VA");
        boolean ret = customerService.save(newCustomer);
        Assert.assertTrue(ret);

        customerService.delete(newCustomer.getName());
        logger.info("First Test!");
    }

    @Test
    public void getCustomers() {
        List<Customer> customers = customerService.getCustomers();

        for (Customer customer : customers) {
            System.out.println(customer);
        }

        Assert.assertNotEquals(0, customers.size());

        logger.info("Second Test!");
    }

    @Test
    public void delete() {
        Boolean ret = customerService.delete("Joe");

        Assert.assertEquals(true, ret);

        logger.info("Third Test!");
    }

    @Test
    public void update() {
        Customer updatedCustomer = new Customer("Kenton", "3444 Fairfax Dr, Arlington, VA");
        Boolean ret = customerService.update(updatedCustomer);

        Assert.assertEquals(true, ret);

        logger.info("Fourth Test!");
    }

    @Test
    public void getCustomerByName() {
        List<Customer> customer = customerService.getCustomerByName("Joe");

        Assert.assertNotNull(customer);
    }

    @Test
    public void getCustomerById() {
        List<Customer> customers = customerService.getCustomerByName("Joe");
        Customer customer = customerService.getCustomerById(customers.get(0).getId());

        Assert.assertNotNull(customer);
    }
}
