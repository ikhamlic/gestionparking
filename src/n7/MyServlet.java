package n7;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.jndi.cosnaming.IiopUrl.Address;


//TODO A REFACTORISER
//TODO Documenter les codes statuts
//TODO ajouter des apis pour les autres méthodes de facade (supprimer une annonce, etc..)
/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@EJB
	Facade f;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("op").equals("ajouterUtilisateur")) {
			if (f.idUtilisateur(request.getParameter("email")) == -1)
				response.setStatus(500);
			else {
				f.ajouterUtilisateur(request.getParameter("email"), request.getParameter("mdp"), request.getParameter("nom"), request.getParameter("prenom"));
				HttpSession sess = request.getSession(true);
				sess.setAttribute("idUtilisateur", f.idUtilisateur(request.getParameter("email")));
				response.setStatus(200);
			}
		}
		
		else if (request.getParameter("op").equals("connexion")) {
			
			if (f.authentifier(request.getParameter("email"), request.getParameter("mdp")))
				response.setStatus(400);
			
			else {
				HttpSession sess = request.getSession(true);
				sess.setAttribute("idUtilisateur", f.idUtilisateur(request.getParameter("email")));
				response.setStatus(200);
			}
		}
		
		else if (request.getParameter("op").equals("idUtilisateurConnecte")) {
			HttpSession sess = request.getSession(true);
			if (sess.getAttribute("idUtilisateur") == null) {
				response.getWriter().write("");	
			}
			
			else
				response.getWriter().write((String) sess.getAttribute("idUtilisateur"));
			
		}
		
		else if (request.getParameter("op").equals("deconnexion")){
			request.getSession().invalidate();
			response.getWriter().write("Utilisateur deconnecté avec succès");
		}
		
	   else if (request.getParameter("op").equals("ajouterAnnonce")) {
			HttpSession sess = request.getSession(true);
			if (sess.getAttribute("idUtilisateur") == null)
				response.setStatus(500);
			else {
				f.ajouterAnnonce(Integer.parseInt((String) sess.getAttribute("idUtilisateur")), Double.parseDouble(request.getParameter("latitude")), Double.parseDouble(request.getParameter("longitude")), Double.parseDouble(request.getParameter("prixHeure")), (String) request.getParameter("adresse"));
				response.getWriter().write("Annonce ajoutée avec succès.");		
			}
		}
	   
	   else if (request.getParameter("op").equals("ajouterReservation")) {
			HttpSession sess = request.getSession(true);
			if (sess.getAttribute("idUtilisateur") == null)
				response.setStatus(500);
			else {
				try {
					f.ajouterReservation(Integer.parseInt((String) sess.getAttribute("idUtilisateur")), Integer.parseInt(request.getParameter("idAnnonce")),  Fonctions.stringToDate(request.getParameter("dateDebut")),  Fonctions.stringToDate(request.getParameter("dateFin")));
					response.getWriter().write("Adresse ajoutée avec succès.");
				} catch (NumberFormatException e) {
					response.setStatus(400);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					response.setStatus(500);
				}
			}

		}
	   
	   else if (request.getParameter("op").equals("listeUtilisateurs")) {

			Gson gson = new Gson();			
			Collection<Utilisateur> utilisateurs= f.listeUtilisateurs();
			
			for (Utilisateur u: utilisateurs) {
				u.setAnnonces(null);
				u.setReservations(null);
			}
			
			response.getWriter().write(gson.toJson(utilisateurs));
			
			
		}
	   
	   else if (request.getParameter("op").equals("listeAnnonces")) {

			Gson gson = new Gson();
			Collection<Annonce> annonces= f.listeAnnonces();
			
			for (Annonce a: annonces){
				a.setProprietaire(null);
				a.setReservations(null);
			}
			
			response.getWriter().write(gson.toJson(annonces));
			
		}
	   
	   else if (request.getParameter("op").equals("listeAnnoncesLibres")) {

			Gson gson = new Gson();
			Collection<Annonce> annonces= f.listeAnnoncesLibres(Fonctions.stringToDate(request.getParameter("dateDebut")),  Fonctions.stringToDate(request.getParameter("dateFin")));
			
			for (Annonce a: annonces){
				a.setProprietaire(null);
				a.setReservations(null);
			}
			
			response.getWriter().write(gson.toJson(annonces));
			
			
		}
	   
	   else if (request.getParameter("op").equals("mesAnnonces")) {
		   HttpSession sess = request.getSession(true);
		   
		   if (sess.getAttribute("idUtilisateur") == null)
			   response.setStatus(401); // Utilisateur non connecté
		   
		   else {
			   
			Gson gson = new Gson();			
			Collection<Annonce> annonces = f.listeAnnoncesUtilisateur((int) sess.getAttribute("idUtilisateur"));
					
			for (Annonce a: annonces){
				a.setProprietaire(null);
				a.setReservations(null);
			}
			response.getWriter().write(gson.toJson(annonces));

		   }
		}
		
		
	   else if (request.getParameter("op").equals("mesReservations")) {
		   HttpSession sess = request.getSession(true);
		   
		   if (sess.getAttribute("idUtilisateur") == null)
			   response.setStatus(401); // Utilisateur non connecté
		   
		   else {
			   
			Gson gson = new Gson();			
			Collection<Reservation> reservations = f.listeReservationsUtilisateur((int) sess.getAttribute("idUtilisateur"));
					
			for (Reservation r: reservations) {
				r.setAnnonce(null);
				r.setLocataire(null);
			}
			response.getWriter().write(gson.toJson(reservations));

		   }
		}
		
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
