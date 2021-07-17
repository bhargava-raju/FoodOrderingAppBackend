package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {


    @PersistenceContext
    private EntityManager entityManager;


    public CustomerAuthTokenEntity getCustomerAuthByToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByToken", CustomerAuthTokenEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }



    public void createCustomerAuthToken(CustomerAuthTokenEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
    }

}
