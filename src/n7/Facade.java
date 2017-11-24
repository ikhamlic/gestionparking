package n7;


import java.util.Collection;
import java.util.Date;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
public class Facade {
	
	@PersistenceContext
	EntityManager em;
	
	public  void ajouterUtilisateur(String email, String mdp, String nom, String prenom) {
		Utilisateur u = new Utilisateur();
		u.setNom(nom);
		u.setPrenom(prenom);
		u.setMotDePasse(mdp);
		u.setEmail(email);
		em.persist(u);
	}
	
	public Collection<Utilisateur> listeUtilisateurs(){
		return em.createQuery("from Utilisateur", Utilisateur.class).getResultList();
	}
	
	public Utilisateur utilisateur(int idUtilisateur) {
		Utilisateur u = null;
		
		try {

		u = em.find(Utilisateur.class, idUtilisateur);

		}


		catch (Exception e) {
			e.printStack();
		}

		return u;
	}
	
	public Collection<Annonce> listeAnnonces() {
		return em.createQuery("from Annonce", Annonce.class).getResultList();	
	}
	
	public Annonce annonce(int idAnnonce) {
		Annonce a = em.find(Annonce.class, idAnnonce);
		return a;
	}
	
	public Collection<Annonce> listeAnnoncesUtilisateur(int idUtilisateur) {
		return utilisateur(idUtilisateur).getAnnonces();	
	}
	
	public Collection<Annonce> listeAnnoncesLibresUtilisateur(int idUtilisateur, Date debut, Date fin) {
		return filtrerAnnoncesLibres(utilisateur(idUtilisateur).getAnnonces(), debut, fin);	
	}
	
	public Collection<Reservation> listeReservationsUtilisateur(int idUtilisateur) {
		return utilisateur(idUtilisateur).getReservations();	
	}
	

	
	public Collection<Annonce> filtrerAnnoncesLibres(Collection<Annonce> listeAnnonces, Date debut, Date fin) {
		Collection<Annonce> listeAnnoncesLibres = listeAnnonces();
		for (Annonce a: listeAnnonces) {
			if (estLibre(a, debut, fin)) 
				listeAnnoncesLibres.add(a);
		}
		
		return listeAnnoncesLibres;
	}
	
	
	public Collection<Annonce> filtrerAnnoncesDistance(Collection<Annonce> listeAnnonces, double latitude, double longitude, double rayon) {
		Collection<Annonce> listeAnnoncesDistance = listeAnnonces();
		for (Annonce a: listeAnnonces) {
			if (AdresseLib.distance(a.getAdresseLat(), a.getAdresseLong(), latitude, longitude) < rayon) 
				listeAnnoncesDistance.add(a);
		}
		
		return listeAnnoncesDistance;
	}

	public Collection<Annonce> listeAnnoncesLibres(Date debut, Date fin) {
		return filtrerAnnoncesLibres(listeAnnonces(), debut, fin);
	}
	public void ajouterAnnonce(int idProprietaire, double latitude, double longitude, double prixHeure, String adresse) {
		Annonce a = new Annonce();
		a.setAdresse(adresse);
		a.setAdresseLat(latitude);
		a.setAdresseLong(longitude);
		a.setPrixHeure(prixHeure);
		a.setProprietaire(utilisateur(idProprietaire));
		a.activer();
		em.persist(a);
	}	
	
	public void ajouterReservation(int idFuturLocataire, int idAnnonce, Date debut, Date fin) throws Exception {
		if (estOccupee(idAnnonce, debut, fin))
			throw new Exception("La place n'est pas libre durant la période sélectionnée");
			
		Reservation r = new Reservation();
		r.setIdLocataire(idFuturLocataire);
		r.setIdAnnonce(idAnnonce);
		
		r.setLocataire(utilisateur(idFuturLocataire));
		em.persist(r);
		
	}
	
	//TODO ajouter list annonces distance
	public boolean estLibre(int idAnnonce, Date debut, Date fin) {
		return !estOccupee(idAnnonce, debut, fin);
		
	}
	
	public boolean estLibre(Annonce a, Date debut, Date fin) {
		return !estOccupee(a, debut, fin);
		
	}
	
	public boolean estOccupee(int idAnnonce, Date debut, Date fin) { 
		return estOccupee(annonce(idAnnonce), debut, fin);
	}
	
	
	
	public boolean estOccupee(Annonce a, Date debut, Date fin) { 
		
		for (Reservation r: a.getReservations()) {
			if (!(debut.after(r.getDateSortie()) || fin.before(r.getDateEntree()))) // équivaut à dire que la place n'est pas libre (non(libre) <=> occupé)
					return true; // la place est occupée durant au moins une partie de la période désirée
			
		}
		
		return false; // la place est libre du début à la fin de la période désirée
		
		
	}
	
	public boolean estLibre(int idAnnonce, Date date) { //TODO à optimiser à l'aide d'un cache (hashset des dates occupées)
		return estLibre(idAnnonce, date, date);
	}
	
	public boolean estOccupee(int idAnnonce, Date date) {
		return estOccupee(idAnnonce, date, date);
	}
	
	public void desactiverAnnonce(int idAnnonce) {
		annonce(idAnnonce).desactiver();
	}
	
	public boolean authentifier(String nomUtilisateur, String mdp) {
		Utilisateur u =  (Utilisateur) em.createQuery("from Utilisateur where nom = :nomUtilisateur")
				.setParameter("nom", nomUtilisateur)
				.getSingleResult();
		
		if (u == null || !u.getMotDePasseHash().equals(Fonctions.hash(mdp)))
			return false;
		
		
		return true;
	}
	
	public int idUtilisateur(String emailUtilisateur) {
		Utilisateur u =  (Utilisateur) em.createQuery("from Utilisateur where email = :emailUtilisateur")
				.setParameter("emailUtilisateur", emailUtilisateur)
				.getSingleResult();
		
		if (u == null)
			return -1;
		
		
		return u.getId();
	}
	

}