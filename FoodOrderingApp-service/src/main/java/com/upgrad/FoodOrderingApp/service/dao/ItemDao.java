package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext private EntityManager entityManager;

    public ItemEntity getItemByUUID(String itemUUID) {
        try {
            ItemEntity item =
                entityManager
                    .createNamedQuery("itemByUUID", ItemEntity.class)
                    .setParameter("itemUUID", itemUUID)
                    .getSingleResult();
            return item;
        }
        catch (NoResultException nre) {
            return null;
        }
    }
}
