package com.ascending.training.service;

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
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    private Logger logger = LoggerFactory.getLogger(BookServiceTest.class);

//    @BeforeClass
//    public static void beforeClass() {
//        bookService = new BookDaoImpl();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        bookService = null;
//    }

    @Before
    public void before() {
        Book fNewBook = new Book("Thinking in Java", "Technology", 47.27, false);
        long fRet = bookService.save(fNewBook);
        Book sNewBook = new Book("Advanced Programming in the UNIX environment", "Technology", 43.95, true);
        long sRet = bookService.save(sNewBook);
    }

    @After
    public void after() {
        Boolean fret = bookService.delete("Thinking in Java");
        Boolean sret = bookService.delete("Advanced Programming in the UNIX environment");
    }

    @Test
    public void save() {
        Book newBook = new Book("Introduction to Algorithm", "Technology", 64.04, false);
        long ret = bookService.save(newBook);
        Assert.assertNotNull(ret);

        bookService.delete(newBook.getTitle());
        logger.info("First Test!");
    }

    @Test
    public void getBooks() {
        List<Book> books = bookService.getBooks();

        for (Book book : books) {
            System.out.println(book);
        }

        Assert.assertNotEquals(0, books.size());

        logger.info("Second Test!");
    }

    @Test
    public void delete() {
        Boolean ret = bookService.delete("Thinking in Java");

        Assert.assertEquals(true, ret);

        logger.info("Third Test!");
    }

    @Test
    public void update() {
        Book updatedBook = new Book("Head First Java", "Technology", 34.08, true);
        Boolean ret = bookService.update(updatedBook);

        Assert.assertEquals(true, ret);

        bookService.delete(updatedBook.getTitle());
        logger.info("Fourth Test!");
    }

    @Test
    public void getBookByTitle() {
        Book book = bookService.getBookByTitle("Thinking in Java");
        System.out.println(book);

        Assert.assertNotNull(book);
    }

    @Test
    public void getBookById() {
        Book oldBook = bookService.getBookByTitle("Thinking in Java");
        Book book = bookService.getBookById(oldBook.getId());
        System.out.println(book);

        Assert.assertNotNull(book);
    }
}
