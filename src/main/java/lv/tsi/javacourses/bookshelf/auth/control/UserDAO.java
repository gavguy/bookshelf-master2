/*
 * Copyright (c) 2018 Dimitrijs Fedotovs
 * This code is licensed under MIT license
 * (see LICENSE.txt for details)
 */

package lv.tsi.javacourses.bookshelf.auth.control;

import lv.tsi.javacourses.bookshelf.auth.model.UserEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
public class UserDAO {
    @PersistenceContext
    private EntityManager em;

    public Optional<UserEntity> findUser(String name) {
        return em.createQuery("select u from User u where u.loginName = :loginName", UserEntity.class)
                .setParameter("loginName", name)
                .getResultStream()
                .findFirst();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createUser(UserEntity user) {
        em.persist(user);
    }

}
