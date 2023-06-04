package com.ascending.training.service;

import com.ascending.training.init.AppInitializer;
import com.ascending.training.model.Book;
import com.ascending.training.model.Customer;
import com.ascending.training.model.IssueStatus;
import com.ascending.training.repository.*;
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
public class IssueStatusServiceTest {

    @Autowired
    private IssueStatusService issueStatusService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CustomerService customerService;
    private Customer fCustomer;
    private Customer sCustomer;
    private Book fNewBook;
    private Book sNewBook;
    private IssueStatus fIssueStatus;
    private IssueStatus sIssueStatus;
    private Logger logger = LoggerFactory.getLogger(IssueStatusService.class);

//    @BeforeClass
//    public void beforeClass() {
//        issueStatusService = new IssueStatusDaoImpl();
//        bookService = new BookDaoImpl();
//        customerService = new CustomerDaoImpl();
//    }
//
//    @AfterClass
//    public void afterClass() {
//        issueStatusService = null;
//    }

    @Before
    public void before() {
        fCustomer = new Customer("Timothy", "255 A St NE, Washington, DC");
        sCustomer = new Customer("Joe", "6787 Washington Blvd, Arlington, VA");

        customerService.save(fCustomer);
        customerService.save(sCustomer);

        fNewBook = new Book("Thinking in Java", "Technology", 47.27, false);
        sNewBook = new Book("Advanced Programming in the UNIX environment", "Technology", 43.95, true);

        bookService.save(fNewBook);
        bookService.save(sNewBook);

        fIssueStatus = new IssueStatus("01/22/2006", "03/22/2006", fCustomer, fNewBook);
        sIssueStatus = new IssueStatus("10/01/2018", "12/01/2018", sCustomer, sNewBook);

        issueStatusService.save(fIssueStatus);
        issueStatusService.save(sIssueStatus);
    }

    @After
    public void after() {
        issueStatusService.delete(fIssueStatus.getId());
        issueStatusService.delete(sIssueStatus.getId());

        bookService.delete(fNewBook.getTitle());
        bookService.delete(sNewBook.getTitle());
        customerService.delete(fCustomer.getName());
        customerService.delete(sCustomer.getName());

    }

    @Test
    public void save() {
        Customer newCustomer = new Customer("Graham", "450 North Washington Street, Falls Church, VA");
        customerService.save(newCustomer);

        Book newBook = new Book("Introduction to Algorithm", "Technology", 64.04, false);
        bookService.save(newBook);

        IssueStatus newIssueStatus = new IssueStatus("05/01/2019", "07/01/2019", newCustomer, newBook);

        long ret = issueStatusService.save(newIssueStatus);
        Assert.assertNotNull(ret);

        issueStatusService.delete(newIssueStatus.getId());

        bookService.delete(newBook.getTitle());
        customerService.delete(newCustomer.getName());

        logger.info("First Test!");
    }

    @Test
    public void getIssueStatuses() {
        List<IssueStatus> issueStatuses = issueStatusService.getIssueStatuses();

        for (IssueStatus issueStatus : issueStatuses) {
            System.out.println(issueStatus);
        }

        Assert.assertNotEquals(0, issueStatuses.size());

        logger.info("Second Test!");
    }

    @Test
    public void delete() {
        Boolean ret = issueStatusService.delete(fIssueStatus.getId());

        Assert.assertEquals(true, ret);

        logger.info("Third Test!");
    }

    @Test
    public void update() {
        Customer updatedCustomer = new Customer("Patrick", "2111 Richmond Hwy, Arlington, VA");
        customerService.save(updatedCustomer);

        Book updatedBook = new Book("Head First Java", "Technology", 34.08, true);
        bookService.save(updatedBook);

        IssueStatus updatedIssueStatus = new IssueStatus("09/01/2019", "11/01/2019", updatedCustomer, updatedBook);
        Boolean ret = issueStatusService.update(updatedIssueStatus);

        List<IssueStatus> issueStatuses = issueStatusService.getIssueStatuses();

        for (IssueStatus issueStatus : issueStatuses) {
            System.out.println(issueStatus);
        }

        Assert.assertEquals(true, ret);

        issueStatusService.delete(updatedIssueStatus.getId());

        bookService.delete(updatedBook.getTitle());
        customerService.delete(updatedCustomer.getName());

        logger.info("Fourth Test!");
    }

    @Test
    public void getIssueStatusById() {
        IssueStatus issueStatus = issueStatusService.getIssueStatusById(sIssueStatus.getId());

        Assert.assertNotNull(issueStatus);
    }
}
