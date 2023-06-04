package com.ascending.training.controller;

import com.ascending.training.model.Book;
import com.ascending.training.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/books"})
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookService bookService;

//    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @GetMapping(value = "", produces = "application/json")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

//    @RequestMapping(value = "/{bookTitle}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Cacheable(value = "books")
    @GetMapping(value = "/{BookTitle}", produces = "application/json")
    public Book getBook(@PathVariable(name = "BookTitle") String bookTitle) {
        Book book = bookService.getBookByTitle(bookTitle);

        return book;
    }

//    @RequestMapping(value = "", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
//    @CachePut(value = "books", key = "#book.id", unless = "#book.title == null")
    @PostMapping(value = "", consumes = "application/json")
    public String createBook(@RequestBody Book book) {
        logger.debug(String.format("Book : %s", book.toString()));

        String msg = "The book has been created.";
        long isSuccess = bookService.save(book);

        if (isSuccess == 0) {
            msg = "The book has not been created.";
        }

        return msg;
    }

//    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PutMapping(value = "/{id}", consumes = "application/json")
    public String updateBook(@PathVariable(name = "id") long bookId, @RequestBody Book book) {
        logger.debug(String.format("Book ID : %d, Book : %s", bookId, book.toString()));

        Book newBook = new Book(bookId, book.getTitle(), book.getCategory(), book.getRentalPrice(), book.getStatus());
        String msg = "The book has been updated.";
        boolean isSuccess = bookService.update(newBook);

        if (!isSuccess) {
            msg = "The book has not been updated.";
        }

        return msg;
    }

//    @RequestMapping(value = "/{bookTitle}", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @DeleteMapping(value = "/{bookTitle}")
    public String deleteBook(@PathVariable String bookTitle) {
        logger.debug("Book title : " + bookTitle);

        String msg = "The book has been deleted.";
        boolean isSuccess = bookService.delete(bookTitle);

        if (!isSuccess) {
            msg = "The book has not been deleted.";
        }

        return msg;
    }
}
