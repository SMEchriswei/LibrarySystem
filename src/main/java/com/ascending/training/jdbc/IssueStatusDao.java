package com.ascending.training.jdbc;

import com.ascending.training.model.IssueStatus;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class IssueStatusDao {
    static final String dbURL = System.getProperty("database.url");
    static final String username = System.getProperty("database.user");
    static final String password = System.getProperty("database.password");

    public List<IssueStatus> getIssueStatus() {
        List<IssueStatus> issueStatuses = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("Connecting to database ...");
            conn = DriverManager.getConnection(dbURL, username, password);

            System.out.println("Creating statement ...");
            stmt = conn.createStatement();
            String sql;
            sql = "select * from issuestatus";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                long id = rs.getLong("id");
                Date issueDate = rs.getDate("issue_date");
                Date returnDate = rs.getDate("return_date");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return issueStatuses;
    }
}
