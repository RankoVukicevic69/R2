package com.caffe.pizzeria.util;


import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
//import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Stateless
public class Resources {


	@PersistenceContext(name="primary")
    private EntityManager em;
	
	@Produces
    public EntityManager getEm() {
		return em;
	}

    //@Produces
    //public Logger produceLog(InjectionPoint injectionPoint) {
    //	return LoggerFactory.getLogger(injectionPoint.getBean().getBeanClass());
    //}

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

}
