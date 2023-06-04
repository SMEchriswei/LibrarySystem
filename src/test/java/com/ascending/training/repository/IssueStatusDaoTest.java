package com.ascending.training.repository;

import com.ascending.training.init.AppInitializer;
import com.ascending.training.model.Book;
import com.ascending.training.model.Customer;
import com.ascending.training.model.IssueStatus;
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
public class IssueStatusDaoTest {
    @Autowired
    private IssueStatusDao issueStatusDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private Logger logger;

    private Customer fCustomer;
    private Customer sCustomer;
    private Book fNewBook;
    private Book sNewBook;
    private IssueStatus fIssueStatus;
    private IssueStatus sIssueStatus;

    @Before
    public void before() {
        fCustomer = new Customer("Timothy", "255 A St NE, Washington, DC");
        sCustomer = new Customer("Joe", "6787 Washington Blvd, Arlington, VA");

        customerDao.save(fCustomer);
        customerDao.save(sCustomer);

        fNewBook = new Book("Thinking in Java", "Technology", 47.27, false);
        sNewBook = new Book("Advanced Programming in the UNIX environment", "Technology", 43.95, true);

        bookDao.save(fNewBook);
        bookDao.save(sNewBook);

        fIssueStatus = new IssueStatus("01/22/2006", "03/22/2006", fCustomer, fNewBook);
        sIssueStatus = new IssueStatus("10/01/2018", "12/01/2018", sCustomer, sNewBook);

        long fRet = issueStatusDao.save(fIssueStatus);
        long sRet = issueStatusDao.save(sIssueStatus);
    }

    @After
    public void after() {
        issueStatusDao.delete(fIssueStatus.getId());
        issueStatusDao.delete(sIssueStatus.getId());
        bookDao.delete(fNewBook.getTitle());
        bookDao.delete(sNewBook.getTitle());
        customerDao.delete(fCustomer.getName());
        customerDao.delete(sCustomer.getName());
    }

    @Test
    public void save() {
        Customer newCustomer = new Customer("Graham", "450 North Washington Street, Falls Church, VA");
        customerDao.save(newCustomer);

        Book newBook = new Book("Introduction to Algorithm", "Technology", 64.04, false);
        bookDao.save(newBook);

        IssueStatus newIssueStatus = new IssueStatus("05/01/2019", "07/01/2019", newCustomer, newBook);

        long ret = issueStatusDao.save(newIssueStatus);
        Assert.assertNotNull(ret);

        issueStatusDao.delete(newIssueStatus.getId());

        logger.info("First Test!");
    }

    @Test
    public void getIssueStatuses() {
        List<IssueStatus> issueStatuses = issueStatusDao.getIssueStatuses();

        for (IssueStatus issueStatus : issueStatuses) {
            System.out.println(issueStatus);
        }

        Assert.assertNotEquals(0, issueStatuses.size());

        logger.info("Second Test!");
    }

    @Test
    public void delete() {
        Boolean ret = issueStatusDao.delete(fIssueStatus.getId());

        Assert.assertEquals(true, ret);

        logger.info("Third Test!");
    }

    @Test
    public void update() {
        Customer updatedCustomer = new Customer("Patrick", "2111 Richmond Hwy, Arlington, VA");
        customerDao.save(updatedCustomer);

        Book updatedBook = new Book("Head First Java", "Technology", 34.08, true);
        bookDao.save(updatedBook);

        IssueStatus updatedIssueStatus = new IssueStatus("09/01/2019", "11/01/2019", updatedCustomer,updatedBook);
        Boolean ret = issueStatusDao.update(updatedIssueStatus);

        List<IssueStatus> issueStatuses = issueStatusDao.getIssueStatuses();

        for (IssueStatus issueStatus : issueStatuses) {
            System.out.println(issueStatus);
        }

        Assert.assertEquals(true, ret);

        issueStatusDao.delete(updatedIssueStatus.getId());
        bookDao.delete(updatedBook.getTitle());
        customerDao.delete(updatedCustomer.getName());
        logger.info("Fourth Test!");
    }

    @Test
    public void getIssueStatusById() {
        IssueStatus issueStatus = issueStatusDao.getIssueStatusById(fIssueStatus.getId());

        Assert.assertNotNull(issueStatus);
    }
}