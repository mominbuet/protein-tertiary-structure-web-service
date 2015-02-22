/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sample.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author shad
 */
public class insertDB {
    public PDetails insertDetails(PDetails p) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            EntityTransaction entr = em.getTransaction();
            entr.begin();
            em.persist(p);
            entr.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }
    public void incrStat(SiteStats s) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            EntityTransaction entr = em.getTransaction();
            entr.begin();
            //em.find(SiteStats.class, 1);
            em.createQuery("update SiteStats p set p.queryNo=1+p.queryNo, p.visitorNo=1+p.visitorNo where p.id="+1).executeUpdate();
            //System.out.println("here "+s.getQueryNo());
            //em.persist(s);
            entr.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return;
    }
}
