package com.ascending.training.repository;

import com.ascending.training.init.AppInitializer;
import com.ascending.training.model.Book;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInitializer.class)
public class BookDaoTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private Logger logger;

    @Before
    public void before() {
        Book fNewBook = new Book("Thinking in Java", "Technology", 47.27, false);
        long fRet = bookDao.save(fNewBook);
        Book sNewBook = new Book("Advanced Programming in the UNIX environment", "Technology", 43.95, true);
        long sRet = bookDao.save(sNewBook);
    }

    @After
    public void after() {
        Boolean fret = bookDao.delete("Thinking in Java");
        Boolean sret = bookDao.delete("Advanced Programming in the UNIX environment");
    }

    @Test
    public void save() {
        Book newBook = new Book("Introduction to Algorithm", "Technology", 64.04, false);
        long ret = bookDao.save(newBook);
        Assert.assertNotNull(ret);

        bookDao.delete(newBook.getTitle());
        logger.info("First Test!");
    }

    @Test
    public void getBooks() {
        List<Book> books = bookDao.getBooks();

        for (Book book : books) {
            System.out.println(book);
        }

        Assert.assertNotEquals(0, books);

        logger.info("Second Test!");
    }

    @Test
    public void delete() {
        Boolean ret = bookDao.delete("Advanced Programming in the UNIX environment");

        Assert.assertEquals(true, ret);

        logger.info("Third Test!");
    }

    @Test
    public void update() {
        Book updatedBook = new Book("Head First Java", "Technology", 34.08, true);
        Boolean ret = bookDao.update(updatedBook);

        Assert.assertEquals(true, ret);

        bookDao.delete(updatedBook.getTitle());
        logger.info("Fourth Test!");
    }

    @Test
    public void getBookByTitle() {
        Book book = bookDao.getBookByTitle("Thinking in Java");
        System.out.println(book);

        Assert.assertNotNull(book);
    }

}
