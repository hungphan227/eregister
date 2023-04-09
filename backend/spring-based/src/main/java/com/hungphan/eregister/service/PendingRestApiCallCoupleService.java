package com.hungphan.eregister.service;

import com.hungphan.eregister.model.PendingRestApiCallCouple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Component
public class PendingRestApiCallCoupleService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory emf;

    public void createPendingRestApiCallCoupleThenCommit(PendingRestApiCallCouple pendingRestApiCallCouple) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(pendingRestApiCallCouple);
        tx.commit();
        em.close();
    }

    public void updateSecondMethodNameOfPendingRestApiCallCoupleThenCommit(String requestId, String secondNameMethod) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        PendingRestApiCallCouple pendingRestApiCallCouple = em.createQuery("from PendingRestApiCallCouple where requestId=:requestId", PendingRestApiCallCouple.class)
                .setParameter("requestId", requestId).getSingleResult();
        pendingRestApiCallCouple.setSecondMethodName("useCredit");
        em.persist(pendingRestApiCallCouple);
        tx.commit();
        em.close();
    }

    public void deletePendingRestApiCallCoupleThenCommit(String requestId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        PendingRestApiCallCouple pendingRestApiCallCouple = em.createQuery("from PendingRestApiCallCouple where requestId=:requestId", PendingRestApiCallCouple.class)
                .setParameter("requestId", requestId).getSingleResult();
        em.remove(pendingRestApiCallCouple);
        tx.commit();
        em.close();
    }

}
