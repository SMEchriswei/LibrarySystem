package com.ascending.training.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BookDaoTest.class,
        CustomerDaoTest.class,
        IssueStatusDaoTest.class
})
public class TestAll {
}