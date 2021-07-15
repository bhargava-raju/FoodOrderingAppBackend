package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryEntity getCategoryByUuid(final String categoryUuid) {
        try {
            return entityManager.createNamedQuery("categoryByUuid", CategoryEntity.class)
                    .setParameter("uuid", categoryUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<CategoryEntity> getAllCategories() {

        return entityManager.createNamedQuery("getAllCategoriesOrderedByName", CategoryEntity.class)
                .getResultList();
    }
}
