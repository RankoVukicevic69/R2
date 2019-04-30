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

import com.caffe.pizzeria.model.Uloga;

@ApplicationScoped
public class UlogaRepo {

    @Inject
    private EntityManager em;

    @Inject
    private Event<Uloga> ulogaEventSrc;

    @Inject
    private UserTransaction userTransaction;

    public Uloga findById(Long id) {
        return em.find(Uloga.class, id);
    }

    public Uloga findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Uloga> criteria = cb.createQuery(Uloga.class);
        Root<Uloga> uloga = criteria.from(Uloga.class);
        criteria.select(uloga).where(cb.equal(uloga.get("naziv"), naziv));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Uloga> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Uloga> criteria = cb.createQuery(Uloga.class);
        Root<Uloga> uloga = criteria.from(Uloga.class);
        criteria.select(uloga);
        return em.createQuery(criteria).getResultList();
    }

    public void Dodaj(Uloga uloga) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
     	try {
        	em.persist(uloga);
        	ulogaEventSrc.fire(uloga);
        	userTransaction.commit();
     	} catch (PersistenceException pe) {
     		userTransaction.rollback();
        	throw pe;
        } catch (Exception tr) {
        	userTransaction.rollback();
        	throw tr;
        }
    }
    
    public void Izmijeni(Uloga uloga) throws Exception{
        userTransaction.begin();
        em.joinTransaction();
      	try {
         	em.merge(uloga);
        	ulogaEventSrc.fire(uloga);
        	userTransaction.commit();
     	} catch (PersistenceException pe) {
     		userTransaction.rollback();
        	throw pe;
        } catch (Exception tr) {
        	userTransaction.rollback();
        	throw tr;
        }
    }
}
