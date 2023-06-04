package com.ascending.training.repository;

import com.ascending.training.model.Customer;
import com.ascending.training.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDaoImpl implements CustomerDao {
    @Autowired
    private Logger logger;
    
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean save(Customer customer) {
        Transaction transaction = null;
        long customerId = 0;
        boolean isSuccess = true;

        logger.warn("Before getSessionFactory.openSession().");
        try (Session session = sessionFactory.openSession()) {
            logger.warn("Enter getSessionFactory.openSession().");
            transaction = session.beginTransaction();

            logger.warn("Enter beginTransaction().");
            session.save(customer);

            logger.warn("session.save() completes.");
            transaction.commit();
        } catch (Exception e) {
            isSuccess = false;
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }

        if (isSuccess) {
            logger.warn(String.format("Successfully saved. Customer %s has been added to the table.", customer.toString()));
        } else {
            logger.warn(String.format("Failed to save. Customer %s has not been added to the table.", customer.toString()));
        }

        return isSuccess;
    }

    @Override
    public boolean update(Customer customer) {
        Transaction transaction = null;
        Boolean isSuccess = true;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(customer);
            transaction.commit();
        } catch (Exception e) {
            isSuccess = false;
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }

        if (isSuccess) {
            logger.warn(String.format("The customer %s has been updated.", customer.toString()));
        }

        return isSuccess;
    }

    @Override
    public boolean delete(String customerName) {
        String hql = "delete Customer where name = :customerName";         //  Placeholder with a name, not anonymous
        int deletedCount = 0;
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(hql);
            query.setParameter("customerName", customerName);
            transaction = session.beginTransaction();
            deletedCount = query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }

        logger.warn(String.format("The customer %s has been deleted.", customerName));

        return deletedCount >= 1 ? true : false;
    }

    @Override
    public List<Customer> getCustomers() {
        String hql = "from Customer as ct " +
                "left join fetch ct.issueStatuses";
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(hql);

            for (Customer customer : query.list()) {
                logger.warn(customer.toString());
            }

            return query.list();
        } catch (HibernateException e){
            logger.error(String.format("Unable to open session, %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Customer> getCustomerByName(String customerName) {
        if (customerName == null) {
            return null;
        }

        String hql = "from Customer as ct " +
                "left join fetch ct.issueStatuses as is " +
                "where lower(ct.name) = :customerName";

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(hql);
            query.setParameter("customerName", customerName.toLowerCase());

            for (Customer customer : query.list()) {
                logger.warn(customer.toString());
            }

            return query.list();
        } catch (Exception e){
            logger.error(String.format("Unable to open session, %s", e.getMessage()));
        }

        return null;
    }

    @Override
    public Customer getCustomerById(long customerId) {
        if (customerId < 1) {
            logger.debug(String.format("Illegal customerId!", customerId));

            return null;
        }

        String hql = "from Customer as ct " +
                "left join fetch ct.issueStatuses as is " +
                "where ct.id = :customerId";

        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery(hql);
            query.setParameter("customerId", customerId);

            Customer customer = query.uniqueResult();

            if (customer != null) {
                logger.debug(customer.toString());
            }

            return customer;
        } catch (HibernateException e){
            logger.error(String.format("Unable to open session, %s", e.getMessage()));
            return null;
        }
    }
}
