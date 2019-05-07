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

import com.caffe.pizzeria.model.Stranica;

@ApplicationScoped
public class StranicaRepo {

    @Inject
    private EntityManager em;

    @Inject
    private Event<Stranica> stranicaEventSrc;

    @Inject
    private UserTransaction userTransaction;

    public Stranica findById(Long id) {
        return em.find(Stranica.class, id);
    }

    public Stranica findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Stranica> criteria = cb.createQuery(Stranica.class);
        Root<Stranica> stranica = criteria.from(Stranica.class);
        criteria.select(stranica).where(cb.equal(stranica.get("naziv"), naziv));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Stranica> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Stranica> criteria = cb.createQuery(Stranica.class);
        Root<Stranica> stranica = criteria.from(Stranica.class);
        criteria.select(stranica);
        return em.createQuery(criteria).getResultList();
    }

    public void Dodaj(Stranica stranica) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
     	try {
        	em.persist(stranica);
        	stranicaEventSrc.fire(stranica);
        	userTransaction.commit();
     	} catch (PersistenceException pe) {
     		userTransaction.rollback();
        	throw pe;
        } catch (Exception tr) {
        	userTransaction.rollback();
        	throw tr;
        }
    }
    
    public void Izmijeni(Stranica stranica) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
      	try {
         	em.merge(stranica);
         	stranicaEventSrc.fire(stranica);
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
