package n7;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@JoinColumn(name = "idAnnonce")
	Annonce annonce;
	
	@Column(updatable = false, insertable = false)
	int idAnnonce;
	
	@ManyToOne
	@JoinColumn(name = "idLocataire")
	Utilisateur locataire;

	@Column(updatable = false, insertable = false)
	int idLocataire;
	

	@OneToMany(mappedBy="reservation", fetch = FetchType.EAGER)
	Set<Message> messages;	
    
	
	
	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public Reservation() {}

	public int getId() {
		return id;
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


	public Set<Message> getMessages() {
		return messages;
	}


	
	
	
}