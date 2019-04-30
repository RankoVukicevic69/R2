package com.caffe.pizzeria.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import com.caffe.pizzeria.model.Metoda;

@ApplicationScoped
public class MetodaRepo {

    @Inject
    private EntityManager em;

    @Inject
    private Event<Metoda> metodaEventSrc;

    @Inject
    private UserTransaction userTransaction;

    public Metoda findById(Long id) {
        return em.find(Metoda.class, id);
    }
   
    public Metoda findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Metoda> criteria = cb.createQuery(Metoda.class);
        Root<Metoda> metoda = criteria.from(Metoda.class);
        criteria.select(metoda).where(cb.equal(metoda.get("naziv"), naziv));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Metoda> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Metoda> criteria = cb.createQuery(Metoda.class);
        Root<Metoda> metoda = criteria.from(Metoda.class);
        criteria.select(metoda);
        return em.createQuery(criteria).getResultList();
    }
    
    public void Dodaj(Metoda metoda) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
     	try {
        	em.persist(metoda);
        	metodaEventSrc.fire(metoda);
        	userTransaction.commit();
     	} catch (PersistenceException pe) {
     		userTransaction.rollback();
        	throw pe;
        } catch (Exception tr) {
        	userTransaction.rollback();
        	throw tr;
        }
    }
    
    public void Izmijeni(Metoda metoda) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
      	try {
         	em.merge(metoda);
        	metodaEventSrc.fire(metoda);
        	userTransaction.commit();
     	} catch (PersistenceException pe) {
     		userTransaction.rollback();
        	throw pe;
        } catch (Exception tr) {
        	System.out.println("Greska");
        	userTransaction.rollback();
        	throw tr;
        }
    }
    
}
