package com.ascending.training.repository;

import com.ascending.training.model.Book;

import java.util.List;

public interface BookDao {
    long save(Book book);

    boolean update(Book book);

    boolean delete(String bookName);

    List<Book> getBooks();

    Book getBookById(long bookId);

    Book getBookByTitle(String bookTitle);
}
