package n7;


import java.util.Collection;
import java.util.Date;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
	
	public Collection<Utilisateur> listeUtilisateurs() {
		return em.createQuery("from Utilisateur", Utilisateur.class).getResultList();
	}
	
	public Utilisateur utilisateur(int idUtilisateur) {

		return em.find(Utilisateur.class, idUtilisateur);

	}
	public Reservation reservation(int idReservation) {

		return em.find(Reservation.class, idReservation);

	}
	public Collection<Annonce> listeAnnonces() {
		return em.createQuery("from Annonce", Annonce.class).getResultList();	
	}
	
	public Annonce annonce(int idAnnonce) {
		return em.find(Annonce.class, idAnnonce);


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
	
	public boolean ajouterReservation(int idFuturLocataire, int idAnnonce, Date debut, Date fin) {

		if (annonce(idAnnonce) == null || estOccupee(idAnnonce, debut, fin))
			return false;
			
		Reservation r = new Reservation();
		r.setDateEntree(debut);
		r.setDateSortie(fin);

		
		r.setLocataire(utilisateur(idFuturLocataire));
		r.setAnnonce(annonce(idAnnonce));
		em.persist(r);
		
		return true;
		
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
			if (!(debut.after(r.getDateSortie()) || fin.before(r.getDateEntree()))) // ?quivaut ? dire que la place n'est pas libre (non(libre) <=> occup?)
					return true; // la place est occup?e durant au moins une partie de la p?riode d?sir?e
			
		}
		
		return false; // la place est libre du d?but ? la fin de la p?riode d?sir?e
		
		
	}
	
	public boolean estLibre(int idAnnonce, Date date) { //TODO ? optimiser ? l'aide d'un cache (hashset des dates occup?es)
		return estLibre(idAnnonce, date, date);
	}
	
	public boolean estOccupee(int idAnnonce, Date date) {
		return estOccupee(idAnnonce, date, date);
	}
	
	public void desactiverAnnonce(int idAnnonce) {
		annonce(idAnnonce).desactiver();
	}
	
	public boolean authentifier(String email, String mdp) {

		Query q = em.createQuery("from Utilisateur where email = :email")
					.setParameter("email", email);
		
		if (q.getResultList().isEmpty())
			return false;
		

		Utilisateur u = (Utilisateur) q.getSingleResult();
		
		if (u.getMotDePasseHash().equals(Fonctions.hash(mdp)))
			return true;
		else
			return false;
		
	}
	
	public int idUtilisateur(String emailUtilisateur) {

		Query q = em.createQuery("from Utilisateur where email = :emailUtilisateur")
					.setParameter("emailUtilisateur", emailUtilisateur);
			
		if (q.getResultList().isEmpty())
				return -1;
		else {
			Utilisateur u = (Utilisateur) q.getSingleResult();
			return u.getId();
		}
		

	}
	
    public void ajouterMessage(Reservation reservation, Utilisateur expediteur, Utilisateur destinataire, Date date, String contenu) {
    	
    	Message m = new Message();
    	m.setDestinataire(destinataire);
    	m.setExpediteur(expediteur);
    	m.setReservation(reservation);
    	m.setDate(date);
    	m.setContenu(contenu);
    	em.persist(m);
    	    	
    }
    
    

    
    public boolean ajouterMessage(int idUtilisateur,int idReservation, String contenu) {
    	
    	Reservation r = reservation(idReservation);
    	Utilisateur locataire = r.getLocataire();
    	Utilisateur proprietaire = r.getAnnonce().getProprietaire();
    	
    	Utilisateur expediteur = null;
    	Utilisateur destinataire = null;
    	
    	if (idUtilisateur == locataire.getId()) {
    		expediteur = locataire;
    		destinataire = proprietaire;
    	}
    	if (idUtilisateur == proprietaire.getId()) {
    		expediteur = proprietaire;
    		destinataire = locataire;
    	}
    	if(idUtilisateur != locataire.getId() && idUtilisateur != proprietaire.getId())
    		return false;
    	
    	
    	Date date = Fonctions.today();
    	
    	if (expediteur == null || destinataire == null) return false;
    	
    	ajouterMessage(r, expediteur,destinataire, date, contenu);
    	
    	return true;
    	
    	
    }

    
    public Collection<Reservation> listerReservationsAnnonce(int idAnnonce) {
		return annonce(idAnnonce).getReservations();	
	}
	
    public Collection<Message> listerMessageReservation(int idUtilisateur, int idReservation) {
    	Reservation r = reservation(idReservation);
    	Utilisateur locataire = r.getLocataire();
    	Utilisateur proprietaire = r.getAnnonce().getProprietaire();
    	if(idUtilisateur != locataire.getId() && idUtilisateur != proprietaire.getId())
    		return null;
    	
    	else
		return reservation(idReservation).getMessages();	
	}
    public Collection<Message> listerMessageRecus(int idUtilisateur) {
		return utilisateur(idUtilisateur).getMessagesRecus();	
	}
    public Collection<Message> listerMessageExpedies(int idUtilisateur) {
		return utilisateur(idUtilisateur).getMessagesExpedies();	
	}
}