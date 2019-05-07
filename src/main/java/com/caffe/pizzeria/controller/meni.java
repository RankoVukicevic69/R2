package com.caffe.pizzeria.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.caffe.pizzeria.data.FunkcionalnostRepo;
import com.caffe.pizzeria.model.Funkcionalnost;

@Named
@ViewScoped
public class meni implements Serializable{
	private static final long serialVersionUID = -8057674610262587838L;
	
    @Inject
    private FunkcionalnostRepo funkcionalnostRepo;

	@Produces
    private Funkcionalnost izabrana;
    
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
		izabrana = funkcionalnostRepo.findById(0L);
		funkcionalnosti= funkcionalnostRepo.findByNadId(izabrana.getId());
    	System.out.println(izabrana.getStranica().getNaziv());		
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
