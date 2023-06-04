package com.ascending.training.jdbc;

import com.ascending.training.model.Book;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BookDaoTest {
    private BookDao bookDao = new BookDao();
    private static Logger logger = LoggerFactory.getLogger(BookDaoTest.class);
    private Book oldBook = new Book("Advanced Programming in the UNIX environment", "Technology", 43.95, true);

    @Before
    public void before() {
        bookDao.save(oldBook);
    }

    @After
    public void after() {
        bookDao.delete(oldBook.getTitle());
    }

    // Currently not giving id can cause duplicate key value error
    @Test
    public void save() {
        Book fNewBook = new Book("Thinking in Java", "Technology", 47.27, false);
        int fRet = bookDao.save(fNewBook);
        Book sNewBook = new Book("Introduction to Algorithm", "Technology", 64.04, false);
        int sRet = bookDao.save(sNewBook);

        Assert.assertEquals(1, fRet);
        Assert.assertEquals(1, sRet);

        bookDao.delete(fNewBook.getTitle());
        bookDao.delete(sNewBook.getTitle());
        logger.info("First Test!");
    }

    @Test
    public void getBooks() {
        List<Book> books = bookDao.getBooks();

        for (Book book : books) {
            System.out.println(book);
        }

//        books.forEach(bk -> logger.info(bk));

        Assert.assertNotEquals(0, books);

        logger.info("Second Test!");
    }

    @Test
    public void delete() {
        int ret = bookDao.delete(oldBook.getTitle());

        Assert.assertEquals(1, ret);

        logger.info("Third Test!");
    }

    @Test
    public void update() {
        Book updatedBook = new Book("Head First Java", "Technology", 34.08, true);
        int ret = bookDao.update(oldBook.getTitle(), updatedBook);

        Assert.assertEquals(1, ret);

        bookDao.delete(updatedBook.getTitle());
        logger.info("Fourth Test!");
    }
}
