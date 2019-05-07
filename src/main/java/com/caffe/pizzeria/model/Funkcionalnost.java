package com.caffe.pizzeria.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "Funkcionalnost")
@Table(name = "funkcionalnost", uniqueConstraints = @UniqueConstraint(columnNames = "nz_fun"))
public class Funkcionalnost implements Serializable{

	private static final long serialVersionUID = -2080598182986504845L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_fun")
	private Long id;

	@NotNull
	@Size(min = 3, max = 45)
	@Column(name = "nz_fun")
	private String naziv;
	
	@NotNull
	@Column(name = "ak_fun")
	private boolean aktivna;
	
	@ManyToOne
    private Funkcionalnost nadredjena;
	
	@ManyToOne
    private Stranica stranica;
	
	public Stranica getStranica() {
		return stranica;
	}

	public void setStranica(Stranica stranica) {
		this.stranica = stranica;
	}

	@OneToMany(
	        mappedBy = "nadredjena",
	        cascade = CascadeType.ALL
	)
	private List<Funkcionalnost> podredjene = new ArrayList<Funkcionalnost>();

    @ManyToMany(cascade = { CascadeType.ALL })
	private List<Metoda> metode = new ArrayList<Metoda>();
    
	public List<Metoda> getMetode() {
		return metode;
	}

	public void setMetode(List<Metoda> metode) {
		this.metode = metode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aktivna ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nadredjena == null) ? 0 : nadredjena.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((podredjene == null) ? 0 : podredjene.hashCode());
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
		Funkcionalnost other = (Funkcionalnost) obj;
		if (aktivna != other.aktivna)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nadredjena == null) {
			if (other.nadredjena != null)
				return false;
		} else if (!nadredjena.equals(other.nadredjena))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (podredjene == null) {
			if (other.podredjene != null)
				return false;
		} else if (!podredjene.equals(other.podredjene))
			return false;
		return true;
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

	public Funkcionalnost getNadredjena() {
		return nadredjena;
	}

	public void setNadredjena(Funkcionalnost nadredjena) {
		this.nadredjena = nadredjena;
	}

	public List<Funkcionalnost> getPodredjene() {
		return podredjene;
	}

	public void setPodredjene(List<Funkcionalnost> podredjene) {
		this.podredjene = podredjene;
	}

	@Override
	public String toString() {
		return "Funkcionalnost [id=" + id + ", naziv=" + naziv + ", aktivna=" + aktivna + ", nadredjena=" + nadredjena
				+ ", podredjene=" + podredjene + "]";
	}



}
