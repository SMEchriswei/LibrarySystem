
package com.ascending.training.repository;

import com.ascending.training.model.Role;
import com.ascending.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {
    @Override
    public Role getRoleByName(String name) {
        String hql = "FROM Role as r where lower(r.name) = :name";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Role> query = session.createQuery(hql);
            query.setParameter("name", name.toLowerCase());

            return query.uniqueResult();
        }
    }
}