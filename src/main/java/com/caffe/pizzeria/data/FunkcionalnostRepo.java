package com.caffe.pizzeria.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
//import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import com.caffe.pizzeria.model.Funkcionalnost;
//import com.caffe.pizzeria.model.Metoda;

@ApplicationScoped
public class FunkcionalnostRepo {

    @Inject
    private EntityManager em;

    @Inject
    private Event<Funkcionalnost> funkcionalnostEventSrc;

    @Inject
    private UserTransaction userTransaction;

    public Funkcionalnost findById(Long id) {
        return em.find(Funkcionalnost.class, id);
    }
   
    public Funkcionalnost findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funkcionalnost> criteria = cb.createQuery(Funkcionalnost.class);
        Root<Funkcionalnost> funkcionalnost = criteria.from(Funkcionalnost.class);
        criteria.select(funkcionalnost).where(cb.equal(funkcionalnost.get("naziv"), naziv));
        return em.createQuery(criteria).getSingleResult();
    	//Query query = em.createQuery("SELECT f FROM Funkcionalnost f LEFT OUTHER JOIN f.metode m WHERE f.naziv = '" + naziv + "'");
		//@SuppressWarnings("unchecked")
		//List<Funkcionalnost> funkcionalnosti = query.getResultList();
        //if (funkcionalnosti != null && funkcionalnosti.size() > 0) return funkcionalnosti.get(0);
        //return null;
    }

    public List<Funkcionalnost> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funkcionalnost> criteria = cb.createQuery(Funkcionalnost.class);
        Root<Funkcionalnost> funkcionalnost = criteria.from(Funkcionalnost.class);
        criteria.select(funkcionalnost);
        return em.createQuery(criteria).getResultList();

        //Query query = em.createQuery("SELECT f FROM Funkcionalnost f LEFT OUTHER JOIN f.metode m ORDER BY f.naziv ASC");
		//@SuppressWarnings("unchecked")
		//List<Funkcionalnost> funkcionalnosti = query.getResultList();
        //return funkcionalnosti;
    }

    public void Dodaj(Funkcionalnost funkcionalnost) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
     	try {
        	em.persist(funkcionalnost);
        	funkcionalnostEventSrc.fire(funkcionalnost);
        	userTransaction.commit();
     	} catch (PersistenceException pe) {
     		userTransaction.rollback();
        	throw pe;
        } catch (Exception tr) {
        	userTransaction.rollback();
        	throw tr;
        }
    }
    public void Izmijeni(Funkcionalnost funkcionalnost) throws Exception {
        userTransaction.begin();
        em.joinTransaction();
      	try {
         	em.merge(funkcionalnost);
        	funkcionalnostEventSrc.fire(funkcionalnost);
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
