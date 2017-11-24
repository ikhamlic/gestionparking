package n7;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

//TODO ajouter autres attributs éventuellements utiles (titre, type véhicule, etc...)

@Entity
public class Annonce {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	String description; 
	double prixHeure;
	String adresse;
	double adresseLong; // longitude
	double adresseLat; // latitude
	boolean estActive;
	
	@OneToMany(mappedBy="annonce", fetch = FetchType.LAZY)
	Collection<Reservation> reservations;	
	
	

	@ManyToOne
	Utilisateur proprietaire;
	@Transient
	int idProprio;//Pour des raisons de programmation, pas stocké dans la bd.
	
	public Annonce(){
		
	}
	public int getIdProprio() {
		return idProprio;
	}
	public void setIdProprio(int idProprio) {
		this.idProprio = idProprio;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

	public Utilisateur getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(Utilisateur proprietaire) {
		this.proprietaire = proprietaire;
	}	
	
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	
	public String getAdresse() {
		return adresse;
	}
	
	public double getPrixHeure() {
		return prixHeure;
	}
	public void setPrixHeure(double prixHeure) {
		this.prixHeure = prixHeure;
	}
	public double getAdresseLong() {
		return adresseLong;
	}
	public void setAdresseLong(double adresseLong) {
		this.adresseLong = adresseLong;
	}
	public double getAdresseLat() {
		return adresseLat;
	}
	public void setAdresseLat(double adresseLat) {
		this.adresseLat = adresseLat;
	}
	public boolean estActive() {
		return estActive;
	}

	public Collection<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public void desactiver() {
		estActive = false;
	}
	
	public void activer() {
		estActive = true;
	}
}