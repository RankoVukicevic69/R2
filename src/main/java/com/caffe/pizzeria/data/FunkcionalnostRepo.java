package com.caffe.pizzeria.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
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
   
	public List<Funkcionalnost> findByNadId(Long nadId) {
		System.out.println("-- executing query 01 --");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		System.out.println("-- executing query 02 --");
		CriteriaQuery<Funkcionalnost> criteria = builder.createQuery( Funkcionalnost.class );
		System.out.println("-- executing query 03 --");
		Root<Funkcionalnost> f1 = criteria.from(Funkcionalnost.class);
		System.out.println("-- executing query 04 --");
		f1.fetch("nadredjena", JoinType.LEFT);
		System.out.println("-- executing query 05 --");
		criteria.where(builder.equal(f1.get("nadredjena").get("aktivna"), true));
		System.out.println("-- executing query 06 --");
		builder.and(builder.equal(f1.get("nadredjena").get("id"), nadId));	
        return em.createQuery(criteria).getResultList();
    }

    public Funkcionalnost findByNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funkcionalnost> criteria = cb.createQuery(Funkcionalnost.class);
        Root<Funkcionalnost> funkcionalnost = criteria.from(Funkcionalnost.class);
        criteria.select(funkcionalnost).where(cb.equal(funkcionalnost.get("naziv"), naziv));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Funkcionalnost> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Funkcionalnost> criteria = cb.createQuery(Funkcionalnost.class);
        Root<Funkcionalnost> funkcionalnost = criteria.from(Funkcionalnost.class);
        criteria.select(funkcionalnost);
        return em.createQuery(criteria).getResultList();
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
