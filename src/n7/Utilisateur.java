package n7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	String email;
	String motDePasseHash;
	String nom;
	
	String prenom;
	
	@OneToMany(mappedBy="proprietaire", fetch = FetchType.LAZY)
	Collection<Annonce> annonces;	

	@OneToMany(mappedBy="locataire", fetch = FetchType.LAZY)
	Collection<Reservation> reservations;	
	
	public Utilisateur() {}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMotDePasseHash() {
		return motDePasseHash;
	}
	public void setMotDePasseHash(String motDePasseHash) {
		this.motDePasseHash = motDePasseHash;
	}
	
	public void setMotDePasse(String motDePasse) {
		setMotDePasseHash(Fonctions.hash(motDePasse));
	}
	
	
	public Collection<Annonce> getAnnonces() {
		return annonces;
	}
	public void setAnnonces(Collection<Annonce> annonces) {
		this.annonces = annonces;
	}
	public Collection<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}

	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

}