package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext private EntityManager entityManager;

    public AddressEntity getAddressByUUID(final String addressUUID) {
        try {
            return entityManager
                    .createNamedQuery("addressByUUID", AddressEntity.class)
                    .setParameter("addressUUID", addressUUID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
