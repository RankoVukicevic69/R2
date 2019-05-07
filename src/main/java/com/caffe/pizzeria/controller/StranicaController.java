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

import org.primefaces.PrimeFaces;

import com.caffe.pizzeria.data.StranicaRepo;
import com.caffe.pizzeria.model.Stranica;

@Named
@ViewScoped
public class StranicaController implements Serializable{
	private static final long serialVersionUID = -4043598266303102043L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private StranicaRepo stranicaRepo;
    
    @Produces
    private Stranica novaStranica;


	@Produces
    private Stranica izabranaStranica;
    
	private List<Stranica> stranice;
	
	public Stranica getNovaStranica() {
		return novaStranica;
	}

	public void setNovaStranica() {
		this.novaStranica = new Stranica();
	}

	public Stranica getIzabranaStranica() {
		return izabranaStranica;
	}

	public void setIzabranaStranica(Stranica izabranaStranica) {
		this.izabranaStranica = izabranaStranica;
	}

	@Produces
	public List<Stranica> getStranice() {
		return stranice;
	}

	@PostConstruct
    private void init() {
		stranice = stranicaRepo.findAll();
	}

    public void Dodaj() {
    	boolean uspjeh;
        try {
            stranicaRepo.Dodaj(novaStranica);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Stranica ID = " + novaStranica.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            setNovaStranica();
            uspjeh = true;
        } catch (PersistenceException pe){
        	String	errorMessage = "Već je registrovana stranica sa nazivom " + novaStranica.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje stranice ID = " + novaStranica.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje stranice ID = " + novaStranica.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        }
        PrimeFaces.current().ajax().addCallbackParam("uspjeh", uspjeh);
    }

    public void Izmijeni() {
    	boolean uspjeh;
        try {
            stranicaRepo.Izmijeni(izabranaStranica);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Stranica ID = " + izabranaStranica.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            uspjeh = true;
        } catch (PersistenceException pe){
        	String errorMessage = "Već je registrovana stranica sa nazivom " + izabranaStranica.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje stranice ID = " + izabranaStranica.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje stranice ID = " + izabranaStranica.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        }
        PrimeFaces.current().ajax().addCallbackParam("uspjeh", uspjeh);
    }
    
	private String getRootErrorMessage(Exception e) {
        String errorMessage = "Neuspješna registracija Stranice!";
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
	
    public void onStranicaListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Stranica stranica) {
		stranice = stranicaRepo.findAll();
    }

}
