package com.ascending.training.controller;

import com.ascending.training.model.Book;
import com.ascending.training.model.Customer;
import com.ascending.training.model.IssueStatus;
import com.ascending.training.service.BookService;
import com.ascending.training.service.CustomerService;
import com.ascending.training.service.IssueStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/issuestatuses"})
public class IssueStatusController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueStatusService issueStatusService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "", produces = "application/json")
    public List<IssueStatus> getIssueStatuses() {
        return issueStatusService.getIssueStatuses();
    }

    @GetMapping(value = "/{IssueStatusId}", produces = "application/json")
    public IssueStatus getIssueStatus(@PathVariable(name = "IssueStatusId") long issueStatusId) {
        IssueStatus issueStatus = issueStatusService.getIssueStatusById(issueStatusId);

        return issueStatus;
    }

    @PostMapping(value = "")
    public String createIssueStatus(@RequestParam Map<String, String> allParams) {
        logger.info(String.format(">>>>>>> Param : %s", allParams.entrySet()));

        Book book = bookService.getBookById(Integer.parseInt(allParams.get("bookId")));
        Customer cutomer = customerService.getCustomerById(Integer.parseInt(allParams.get("customerId")));
        IssueStatus issueStatus = new IssueStatus(cutomer, book);

        String msg = "The issue status has been created.";

        if (!allParams.containsKey("issueDate")) {
            issueStatus.setIssueDate();
        } else {
            issueStatus.setIssueDate(allParams.get("issueDate"));
        }

        logger.debug(String.format("Issue Status : %s", issueStatus.toString()));

        long isSuccess = issueStatusService.save(issueStatus);

        if (isSuccess == 0) {
            msg = "The issue status has not been created.";
        }

        return msg;
    }

//    @RequestMapping(value = "header", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PutMapping(value = "", consumes = "application/json")
    public String updateIssueStatus(@RequestParam Map<String, String> allParams) {
        //IssueStatus issueStatus = issueStatusService.getIssueStatusById(issueStatusId);
        //issueStatus.setReturnDate(new Date());

        logger.info(String.format(">>>>>>> Param : %s", allParams.entrySet()));

        IssueStatus issueStatus = issueStatusService.getIssueStatusById(Integer.parseInt(allParams.get("issueStatusId")));

        if (!allParams.containsKey("returnDate")) {
            issueStatus.setReturnDate();
        } else {
            issueStatus.setReturnDate(allParams.get("returnDate"));
        }

        logger.debug(String.format("Issue Status : %s", issueStatus.toString()));

        String msg = "The issue status has been updated.";

        boolean isSuccess = issueStatusService.update(issueStatus);

        if (isSuccess == false) {
            msg = "The issue status has not been updated.";
        }

        return msg;
    }


    @DeleteMapping(value = "")
    public String deleteIssueStatus(@RequestParam Map<String, String> allParams) {
        logger.info(String.format(">>>>>>> Param : %s", allParams.entrySet()));

        boolean isSuccess = issueStatusService.delete(Integer.parseInt(allParams.get("issueStatusId")));
        String msg = "The issue status has been deleted.";

        if (isSuccess == false) {
            msg = "The issue status has not been deleted.";
        }

        return msg;
    }
}
