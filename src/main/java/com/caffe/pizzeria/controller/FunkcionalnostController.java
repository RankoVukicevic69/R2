package com.caffe.pizzeria.controller;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.caffe.pizzeria.data.FunkcionalnostRepo;
import com.caffe.pizzeria.data.MetodaRepo;
import com.caffe.pizzeria.model.Funkcionalnost;
import com.caffe.pizzeria.model.Metoda;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@Named
@ViewScoped
public class FunkcionalnostController implements Serializable{
	private static final long serialVersionUID = 3613882410894624665L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private FunkcionalnostRepo funkcionalnostRepo;
    
    @Inject
    private MetodaRepo metodaRepo;
	
    @Produces
    private Funkcionalnost novaFunkcionalnost;

	@Produces
    private Funkcionalnost izabranaFunkcionalnost;
    
	private List<Funkcionalnost> funkcionalnosti;
    
	private List<Metoda> metode;
	
	private List<Metoda> neIzabraneMetode;
	
	@Produces
	private Metoda metodaForAdd;
	
	@Produces
	private Metoda metodaForRemove;
    
    public Metoda getMetodaForAdd() {
		return metodaForAdd;
	}

	public void setMetodaForAdd(Metoda metodaForAdd) {
		this.metodaForAdd = metodaForAdd;
	}

	public Metoda getMetodaForRemove() {
		return metodaForRemove;
	}

	public void setMetodaForRemove(Metoda metodaForRemove) {
		this.metodaForRemove = metodaForRemove;
	}

	public Funkcionalnost getNovaFunkcionalnost() {
		return novaFunkcionalnost;
	}

	public void setNovaFunkcionalnost(Funkcionalnost novaFunkcionalnost) {
		this.novaFunkcionalnost = novaFunkcionalnost;
	}

    public Funkcionalnost getIzabranaFunkcionalnost() {
		return izabranaFunkcionalnost;
	}

	public void setIzabranaFunkcionalnost(Funkcionalnost izabranaFunkcionalnost) {
		this.izabranaFunkcionalnost = izabranaFunkcionalnost;
		neIzabraneMetode = new ArrayList<Metoda>();
		for (final Metoda metoda : metode) {
			Metoda m = metoda;
			for(final Metoda met : izabranaFunkcionalnost.getMetode()) {
				if (metoda.getId().equals(met.getId())) {
					m = null;
					break;
				}
			}
			if (m != null) {
				neIzabraneMetode.add(m);
			}
		}
	}

	@Produces
	public List<Metoda> getNeIzabraneMetode() {
		return neIzabraneMetode;
	}

	public void setNeIzabraneMetode(List<Metoda> neIzabraneMetode) {
		this.neIzabraneMetode = neIzabraneMetode;
	}

	@Produces
	public List<Metoda> getMetode() {
		return metode;
	}

	public void OnAddMetoda(final SelectEvent event) {
		Metoda metoda = (Metoda) event.getObject();
		neIzabraneMetode.remove(metoda);
		novaFunkcionalnost.getMetode().add(metoda);
	}
	
	public void OnRemoveMetoda(final SelectEvent event) {
		Metoda metoda = (Metoda) event.getObject();
		neIzabraneMetode.add(metoda);
		novaFunkcionalnost.getMetode().remove(metoda);
	}	
	
	@PostConstruct
    private void init() {
    	retrieveAllMetode();
    	retrieveAllFunkcionalnosti();
    	if (novaFunkcionalnost == null) setNovaFunkcionalnost();
    }
    
    private void setNovaFunkcionalnost() {
        novaFunkcionalnost = new Funkcionalnost();
		neIzabraneMetode = new ArrayList<Metoda>();
		for (final Metoda metoda : metode) {
			Metoda m = metoda;
			for(final Metoda met : novaFunkcionalnost.getMetode()) {
				if (metoda.getId().equals(met.getId())) {
					m = null;
					break;
				}
			}
			if (m != null) {
				neIzabraneMetode.add(m);
			}
		}
    }
    
    private void retrieveAllFunkcionalnosti() {
    	funkcionalnosti = funkcionalnostRepo.findAll();
    }
    
    private void retrieveAllMetode() {
    	metode = metodaRepo.findAll();
    }

    public void onFunkcionalnostListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Funkcionalnost funkcionalnost) {
        retrieveAllFunkcionalnosti();
    }
    
    @Produces
    public List<Funkcionalnost> getFunkcionalnosti() {
        return funkcionalnosti;
    }
    
    public void Dodaj() {
    	boolean uspjeh;
        try {
            funkcionalnostRepo.Dodaj(novaFunkcionalnost);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Funkcionalnost ID = " + novaFunkcionalnost.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            setNovaFunkcionalnost();
            uspjeh = true;
        } catch (PersistenceException pe){
        	String	errorMessage = "Već je registrovana funkcionalnost sa nazivom " + novaFunkcionalnost.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje funkcionalnosti ID = " + novaFunkcionalnost.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje funkcionalnosti ID = " + novaFunkcionalnost.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        }
        PrimeFaces.current().ajax().addCallbackParam("uspjeh", uspjeh);
    }

    public void Izmijeni() {
    	boolean uspjeh;
        try {
            funkcionalnostRepo.Izmijeni(izabranaFunkcionalnost);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uloga ID = " + izabranaFunkcionalnost.getId().toString(), "Uspješno snimljena!");
            facesContext.addMessage(null, m);
            uspjeh = true;
        } catch (PersistenceException pe){
        	String errorMessage = "Već je registrovana funkcionalnost sa nazivom " + izabranaFunkcionalnost.getNaziv();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje funkcionalnosti ID = " + izabranaFunkcionalnost.getId().toString()+ " nije uspješno!");
            facesContext.addMessage(null, m);
            uspjeh = false;
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Snimanje funkcionalnosti ID = " + izabranaFunkcionalnost.getId().toString()+ " nije uspješno!");
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
