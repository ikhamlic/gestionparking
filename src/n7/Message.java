package n7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	@Temporal(TemporalType.TIMESTAMP) 
	Date date;
	
	
	@ManyToOne
	@JoinColumn(name = "idReservation")
	Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name = "idExpediteur")
	Utilisateur expediteur;
		
	@ManyToOne
	@JoinColumn(name = "idDestinataire")
	Utilisateur destinataire;
	
	@Column(updatable = false, insertable = false)
	int idReservation;
		
	@Column(updatable = false, insertable = false)
	int idExpediteur;
	
	@Column(updatable = false, insertable = false)
	int idDestinataire;
	
	
	
	String contenu;

    public Message() {}
    
	public int getId() {
		return id;
	}




	public Reservation getReservation() {
		return reservation;
	}


	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}


	public Utilisateur getExpediteur() {
		return expediteur;
	}


	public void setExpediteur(Utilisateur expediteur) {
		this.expediteur = expediteur;
	}


	public Utilisateur getDestinataire() {
		return destinataire;
	}


	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}


	public String getContenu() {
		return contenu;
	}


	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	
	
	public int getIdReservation() {
		return idReservation;
	}



	public int getIdExpediteur() {
		return idExpediteur;
	}



	public int getIdDestinataire() {
		return idDestinataire;
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}