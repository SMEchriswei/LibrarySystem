package com.ascending.training.repository;

import com.ascending.training.model.IssueStatus;
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
public class IssueStatusDaoImpl implements IssueStatusDao {
    @Autowired
    private Logger logger;
    
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public long save(IssueStatus issueStatus) {
        Transaction transaction = null;
        long issueStatusId = 0;
        Boolean isSuccess = true;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            issueStatusId = (long) session.save(issueStatus);
            transaction.commit();
        } catch (Exception e) {
            isSuccess = false;
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }

        if (isSuccess) {
            logger.warn(String.format("The issue status %s has been inserted into the table.", issueStatus.toString()));
        }

        return issueStatusId;
    }

    @Override
    public boolean update(IssueStatus issueStatus) {
        Transaction transaction = null;
        boolean isSuccess = true;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(issueStatus);
            transaction.commit();
        } catch (Exception e) {
            isSuccess = false;
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }

        if (isSuccess) {
            logger.warn(String.format("The issue status %s has been updated.", issueStatus.toString()));
        }

        return isSuccess;
    }

    @Override
    public boolean delete(long issueStatusesId) {
        String hql = "delete IssueStatus as iss " +
                "where iss.id = :issueStatusesId";

        int deletedCount = 0;
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            Query<IssueStatus> query = session.createQuery(hql);
            query.setParameter("issueStatusesId", issueStatusesId);
            transaction = session.beginTransaction();
            deletedCount = query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }

        logger.warn(String.format("The issue status %s has been deleted.", issueStatusesId));

        return deletedCount >= 1 ? true : false;
    }

    @Override
    public List<IssueStatus> getIssueStatuses() {
        String hql = "from IssueStatus as iss " +
                "left join fetch iss.book " +
                "left join fetch iss.customer ";

        try (Session session = sessionFactory.openSession()) {
            Query<IssueStatus> query = session.createQuery(hql);
            return query.list();
        } catch (HibernateException e){
            logger.error(String.format("Unable to open session, %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public IssueStatus getIssueStatusById(long id) {

        String hql = "from IssueStatus as iss " +
                "left join fetch iss.book " +
                "left join fetch iss.customer " +
                "where iss.id= :issueStatusId";

        try (Session session = sessionFactory.openSession()) {
            Query<IssueStatus> query = session.createQuery(hql);
            query.setParameter("issueStatusId", id);

            IssueStatus issueStatus = query.uniqueResult();

            if (issueStatus != null) {
                logger.warn(issueStatus.toString());
            }

            return issueStatus;
        } catch (HibernateException e){
            logger.error(String.format("Unable to open session, %s", e.getMessage()));
            return null;
        }
    }
}