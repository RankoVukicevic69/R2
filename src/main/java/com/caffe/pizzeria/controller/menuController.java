package com.caffe.pizzeria.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.caffe.pizzeria.data.FunkcionalnostRepo;
import com.caffe.pizzeria.model.Funkcionalnost;

@Named
@ViewScoped
public class menuController implements Serializable{
	private static final long serialVersionUID = -8057674610262587838L;
	
    @Inject
    private FunkcionalnostRepo funkcionalnostRepo;

    @Produces
    private Funkcionalnost izabrana;
    
    private MenuModel model;

    @Produces
	public MenuModel getModel() {
		return model;
	}

	public Funkcionalnost getIzabrana() {
		return izabrana;
	}

	public void setIzabrana(Funkcionalnost izabrana) {
		this.izabrana = izabrana;
	}

	private List<Funkcionalnost> funkcionalnosti;
	
	public List<Funkcionalnost> getFunkcionalnosti() {
		return funkcionalnosti;
	}
	public void setFunkcionalnosti(List<Funkcionalnost> funkcionalnosti) {
		this.funkcionalnosti = funkcionalnosti;
	}

	@PostConstruct
    private void init() {
        model = new DefaultMenuModel();
		funkcionalnosti= funkcionalnostRepo.findAll();
		if (funkcionalnosti != null) {
			for(Funkcionalnost f : funkcionalnosti) {
				if ( f == null) {
			    	System.out.println("f1 - null");											
				}
				else {
			    	System.out.println("f1 - " + f.getNaziv());						
					if (f.getPodredjene() != null ) {
						if ( f.getPodredjene().size() > 0) {
							for(Funkcionalnost ff : f.getPodredjene()) {
								System.out.println("f2 -- " + ff.getNaziv());
							}
						}
						else {
							if (f.getStranica() != null) {
						    	System.out.println("f3 - " + f.getStranica().getNaziv());						
							}
						}
					}
					else {
						if (f.getStranica() != null) {
					    	System.out.println("f3 - " + f.getStranica().getNaziv());						
						}
					}
				}
			}
		}
	}

	public void meniAdd(Funkcionalnost f) {
		izabrana = f;
		funkcionalnosti= funkcionalnostRepo.findByNadId(izabrana.getId());		
	}
	
	public void meniRemove() {
		if (izabrana == null) {
			izabrana = funkcionalnostRepo.findById(0L);
		}
		if (izabrana.getId() > 0) {
			izabrana = izabrana.getNadredjena(); 
		}
		funkcionalnosti= funkcionalnostRepo.findByNadId(izabrana.getId());		
	}
}