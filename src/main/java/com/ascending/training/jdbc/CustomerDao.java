package com.ascending.training.jdbc;

import com.ascending.training.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class CustomerDao {
    static final String dbURL = System.getProperty("database.url");
    static final String username = System.getProperty("database.user");
    static final String password = System.getProperty("database.password");

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM customers";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");

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

        return customers;
    }
}