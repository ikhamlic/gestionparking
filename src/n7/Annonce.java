package n7;

import java.util.Collection;
import java.util.Set;

import javax.persistence.*;



//TODO ajouter autres attributs ?ventuellement utiles (titre, type v?hicule, etc...)

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
	String indicationsPrivees;

	

	@Column(updatable = false, insertable = false)
	int idProprietaire;

    @OneToMany(mappedBy="annonce", fetch = FetchType.EAGER)
	Set<Reservation> reservations;


	@ManyToOne
	@JoinColumn(name = "idProprietaire")
	Utilisateur proprietaire;




	public Annonce() {}

	public int getIdProprietaire() {
		return idProprietaire;
	}



	public int getId() {
		return id;
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

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}
	public String getIndicationsPrivees() {
		return indicationsPrivees;
	}

	public void setIndicationsPrivees(String indicationsPrivees) {
		this.indicationsPrivees = indicationsPrivees;
	}
	public void desactiver() {
		estActive = false;
	}

	public void activer() {
		estActive = true;
	}

}