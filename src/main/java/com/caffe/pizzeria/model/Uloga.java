package com.caffe.pizzeria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "uloga", uniqueConstraints = @UniqueConstraint(columnNames = "nz_ulg"))
public class Uloga implements Serializable{
	private static final long serialVersionUID = -5487083796020152000L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_ulg")
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 25)
	@Column(name = "nz_ulg")
	private String naziv;
	
	@NotNull
	@Column(name = "ak_ulg")
	private boolean aktivna;
	
	public  Uloga() {
		this.setNaziv("");
		this.setAktivna(true);
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public boolean isAktivna() {
		return aktivna;
	}

	public void setAktivna(boolean aktivna) {
		this.aktivna = aktivna;
	}

}