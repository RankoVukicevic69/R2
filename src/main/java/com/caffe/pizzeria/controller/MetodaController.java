package com.caffe.pizzeria.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

import com.caffe.pizzeria.data.MetodaRepo;
import com.caffe.pizzeria.model.Metoda;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class MetodaController implements Serializable{
	private static final long serialVersionUID = 1296206974027529180L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private MetodaRepo metodaRepo;
	
    @Produces
    private Metoda novaMetoda;

    public Metoda getNovaMetoda() {
		return novaMetoda;
	}

	public void setNovaMetoda(Metoda novaMetoda) {
		this.novaMetoda = novaMetoda;
	}

	@Produces
    private Metoda izabranaMetoda;
    
    public Metoda getIzabranaMetoda() {
		return izabranaMetoda;
	}

	public void setIzabranaMetoda(Metoda izabranaMetoda) {
		this.izabranaMetoda = izabranaMetoda;
	}

	private List<Metoda> metode;
    
	@PostConstruct
    private void init() {
    	if (novaMetoda == null) setNovaMetoda();
    	retrieveAllMetode();
    }
    
    private void setNovaMetoda() {
        novaMetoda = new Metoda();
    }
    
    private void retrieveAllMetode() {
        metode = metodaRepo.findAll();
    }

    public void onMetodaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Metoda metoda) {
        retrieveAllMetode();
    }
    
    @Produces
    public List<Metoda> getMetode() {
        return metode;
    }
    
    public void Dodaj() {
    	boolean uspjeh;
        try {
            metodaRepo.Dodaj(novaMetoda);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Metoda ID = " + novaMetoda.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            setNovaMetoda();
            uspjeh = true;
        } catch (PersistenceException pe){
        	String	errorMessage = "Već je registrovana metoda sa nazivom " + novaMetoda.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje metode ID = " + novaMetoda.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje metode ID = " + novaMetoda.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        }
        PrimeFaces.current().ajax().addCallbackParam("uspjeh", uspjeh);
    }

    public void Izmijeni() {
    	boolean uspjeh;
        try {
            metodaRepo.Izmijeni(izabranaMetoda);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uloga ID = " + izabranaMetoda.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            uspjeh = true;
        } catch (PersistenceException pe){
        	String errorMessage = "Već je registrovana metoda sa nazivom " + izabranaMetoda.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje metode ID = " + izabranaMetoda.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje metode ID = " + izabranaMetoda.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        }
        PrimeFaces.current().ajax().addCallbackParam("uspjeh", uspjeh);
    }
    
	private String getRootErrorMessage(Exception e) {
        String errorMessage = "Neuspješna registracija nove Metode!";
        if (e == null) {
            return errorMessage;
        }
        Throwable t = e;
        while (t != null) {
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        return errorMessage;
    }

}
