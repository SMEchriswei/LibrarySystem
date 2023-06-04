package com.ascending.training.repository;

import com.ascending.training.model.Book;
import com.ascending.training.model.IssueStatus;

import java.util.List;

public interface IssueStatusDao {
    long save(IssueStatus issueStatus);

    boolean update(IssueStatus issueStatus);

    boolean delete(long issueStatusesId);

    List<IssueStatus> getIssueStatuses();

    IssueStatus getIssueStatusById(long id);

}
