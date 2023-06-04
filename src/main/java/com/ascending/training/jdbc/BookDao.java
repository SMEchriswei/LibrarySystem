package com.ascending.training.jdbc;

import com.ascending.training.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookDao {
    static final String dbURL = System.getProperty("database.url");
    static final String username = System.getProperty("database.user");
    static final String password = System.getProperty("database.password");

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM books";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String category = rs.getString("category");
                double rental_price = rs.getDouble("rental_price");
                Boolean status = rs.getBoolean("status");

                Book book = new Book(id, title, category, rental_price, status);
                books.add(book);
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

        return books;
    }

    public int save(Book book) {
        int status = 0;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(dbURL, username, password);

            String sql = "INSERT INTO books (title, category, rental_price, status) " +
                    "VALUES (?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getCategory());
            ps.setDouble(3, book.getRentalPrice());
            ps.setBoolean(4, book.getStatus());

            status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return status;
    }

    public int update(String oldBookTitle, Book book) {
        int status = 0;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(dbURL, username, password);

            String sql = "UPDATE books SET title=?, category=?, rental_price=?, status=? WHERE title=?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getCategory());
            ps.setDouble(3, book.getRentalPrice());
            ps.setBoolean(4, book.getStatus());
            ps.setString(5, oldBookTitle);

            status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return status;
    }

    public int delete(String bookTitle) {
        int status = 0;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(dbURL, username, password);

            String sql = "DELETE FROM books WHERE title=?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, bookTitle);

            status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return status;
    }

    public static void main(String[] args) {
        BookDao bookDao = new BookDao();
        List<Book> books = bookDao.getBooks();

        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }
}
