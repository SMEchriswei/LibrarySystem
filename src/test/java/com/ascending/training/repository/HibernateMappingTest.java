package com.ascending.training.repository;

import com.ascending.training.model.Book;
import com.ascending.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HibernateMappingTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void mappingTest() {
        logger.info("mappingTest start...");
        String hql = "FROM Book";
        List<Book> books = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            if (session != null) {
                logger.info("session is not null");
            } else {
                logger.info("session is null");
            }

            Query<Book> query = session.createQuery(hql);
            books = query.list();
            if (books != null) {
                logger.info("books is not null");
            } else {
                logger.info("books is null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        Assert.assertNotNull(books);
        logger.info("mappingTest end.");
    }
}