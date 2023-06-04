package com.ascending.training.service;

import com.ascending.training.model.Book;
import com.ascending.training.repository.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;

    public long save(Book book) {
        return bookDao.save(book);
    }

    public boolean update(Book book) {
        return bookDao.update(book);
    }

    public boolean delete(String bookTitle) {
        return bookDao.delete(bookTitle);
    }

    public List<Book> getBooks() {
        return bookDao.getBooks();
    }

    public Book getBookById(long bookId) { return bookDao.getBookById(bookId);}

    public Book getBookByTitle(String bookTitle) {
        return bookDao.getBookByTitle(bookTitle);
    }
}
