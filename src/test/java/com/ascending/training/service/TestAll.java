package com.ascending.training.service;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BookServiceTest.class,
        CustomerServiceTest.class,
        FileServiceMockAWSTest.class,
        IssueStatusServiceTest.class,
        MessageServiceMockAWSTest.class
})
public class TestAll {
}
