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

import com.caffe.pizzeria.data.UlogaRepo;
import com.caffe.pizzeria.model.Uloga;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class UlogaController implements Serializable{
	private static final long serialVersionUID = 1665769038290890675L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private UlogaRepo ulogaRepo;

    @Produces
    private Uloga novaUloga;

    public Uloga getNovaUloga() {
		return novaUloga;
	}

	public void setNovaUloga(Uloga novaUloga) {
		this.novaUloga = novaUloga;
	}

	@Produces
    private Uloga izabranaUloga;
    
    public Uloga getIzabranaUloga() {
		return izabranaUloga;
	}

	public void setIzabranaUloga(Uloga izabranaUloga) {
		this.izabranaUloga = izabranaUloga;
	}

	private List<Uloga> uloge;
    
	@PostConstruct
    private void init() {
    	if (novaUloga == null) setNovaUloga();
    	retrieveAllUloge();
    }
    
    private void setNovaUloga() {
        novaUloga = new Uloga();
    }
    
    private void retrieveAllUloge() {
        uloge = ulogaRepo.findAll();
    }
    
    public void onUlogaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Uloga uloga) {
        retrieveAllUloge();
    }
    
    @Produces
    public List<Uloga> getUloge() {
        return uloge;
    }
    
    public void Dodaj() throws Exception{
    	boolean uspjeh;
        try {
            ulogaRepo.Dodaj(novaUloga);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uloga ID = " + novaUloga.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            setNovaUloga();
            uspjeh = true;
        } catch (PersistenceException pe){
        	System.out.println("XXX");
        	String	errorMessage = "Već je registrovana uloga sa nazivom " + novaUloga.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje uloge ID = " + novaUloga.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
        	System.out.println("QQQ :"+ errorMessage);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje uloge ID = " + novaUloga.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        }
        PrimeFaces.current().ajax().addCallbackParam("uspjeh", uspjeh);
    }

    public void Izmijeni() {
    	boolean uspjeh;
        try {
            ulogaRepo.Izmijeni(izabranaUloga);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uloga ID = " + izabranaUloga.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            uspjeh = true;
        } catch (PersistenceException pe){
        	String errorMessage = "Već je registrovana uloga sa nazivom " + izabranaUloga.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje uloge ID = " + izabranaUloga.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje uloge ID = " + izabranaUloga.getId().toString()+ " nije uspješno!");
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
