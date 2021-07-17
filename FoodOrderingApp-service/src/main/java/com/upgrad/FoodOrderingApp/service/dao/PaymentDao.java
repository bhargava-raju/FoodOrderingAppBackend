package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;

import javax.persistence.PersistenceContext;

import java.util.List;

@Repository
public class PaymentDao {


    @PersistenceContext private EntityManager entityManager;

    public PaymentEntity getPaymentByUUID(String paymentUUID) {
        try {
            PaymentEntity paymentEntity =
                entityManager
                    .createNamedQuery("getPaymentByUUID", PaymentEntity.class)
                    .setParameter("paymentUUID", paymentUUID)
                    .getSingleResult();
            return paymentEntity;
            }
        catch (NoResultException nre) {
            return null;
        }
    }

    public List<PaymentEntity> getPaymentMethods() {
        return entityManager.createNamedQuery("getAllPaymentMethods", PaymentEntity.class).getResultList();
    }

    public PaymentEntity getPaymentUuid(final String paymentUuid) {
        return entityManager.createNamedQuery("getPaymentUuid", PaymentEntity.class).setParameter("paymentUuid", paymentUuid).getSingleResult();
    }
}



