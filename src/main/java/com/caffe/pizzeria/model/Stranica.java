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

@Entity(name = "Stranica")
@Table(name = "stranica", uniqueConstraints = @UniqueConstraint(columnNames = "nz_str"))
public class Stranica implements Serializable {
	private static final long serialVersionUID = 4379406345443608343L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_str")
	private Long id;

	@NotNull
	@Size(min = 3, max = 45)
	@Column(name = "nz_str")
	private String naziv;
	
	@NotNull
	@Column(name = "ak_str")
	private boolean aktivna;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aktivna ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stranica other = (Stranica) obj;
		if (aktivna != other.aktivna)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Stranica [id=" + id + ", naziv=" + naziv + ", aktivna=" + aktivna + "]";
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
