package n7;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	@Temporal(TemporalType.TIMESTAMP) 
	Date dateEntree;
	
	@Temporal(TemporalType.TIMESTAMP) 
	Date dateSortie;
	
	
	@ManyToOne
	Annonce annonce;
	
	@Transient
	int idAnnonce;
	
	@ManyToOne
	Utilisateur locataire;

	@Transient
	int idLocataire;
	
	public Reservation() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateEntree() {
		return dateEntree;
	}

	public void setDateEntree(Date dateEntree) {
		this.dateEntree = dateEntree;
	}

	public Date getDateSortie() {
		return dateSortie;
	}

	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
	}

	public Annonce getAnnonce() {
		return annonce;
	}

	public void setAnnonce(Annonce annonce) {
		this.annonce = annonce;
	}

	public Utilisateur getLocataire() {
		return locataire;
	}

	public void setLocataire(Utilisateur locataire) {
		this.locataire = locataire;
	}

	public int getIdLocataire() {
		return idLocataire;
	}

	public void setIdLocataire(int idLocataire) {
		this.idLocataire = idLocataire;
	}

	public int getIdAnnonce() {
		return idLocataire;
	}

	public void setIdAnnonce(int idAnnonce) {
		this.idAnnonce = idAnnonce;
	}
	
}

