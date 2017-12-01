package n7;

import java.util.Date;

import javax.persistence.*;

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
	@JoinColumn(name = "idAnnonce")
	Annonce annonce;

	@Column(updatable = false, insertable = false)
	int idAnnonce;
	
	@ManyToOne
	@JoinColumn(name = "idLocataire")
	Utilisateur locataire;

	@Column(updatable = false, insertable = false)
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

	public int getIdAnnonce() {
		return idLocataire;
	}

	
}

