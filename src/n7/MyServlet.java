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


//TODO A REFACTORISER (bcp de redondances)
//TODO Documenter les codes statuts
//TODO ajouter des apis pour les autres m?thodes de facade (supprimer une annonce, etc..)
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
			if (f.idUtilisateur(request.getParameter("email")) != -1) // <=> utilisateur d?j? existant
				response.getWriter().write("erreur");
			else {
				f.ajouterUtilisateur(request.getParameter("email"), request.getParameter("mdp"), request.getParameter("nom"), request.getParameter("prenom"));
				HttpSession sess = request.getSession(true);
				sess.setAttribute("idUtilisateur", f.idUtilisateur(request.getParameter("email")));
				response.getWriter().write("succes");
			}
		}
		
		else if (request.getParameter("op").equals("connexion")) {
			
			if (!f.authentifier(request.getParameter("email"), request.getParameter("mdp")))
				response.getWriter().write("erreur");
			
			else {
				HttpSession sess = request.getSession(true);
				sess.setAttribute("idUtilisateur", f.idUtilisateur(request.getParameter("email")));
				response.getWriter().write("succes");
			}
		}
		
		else if (request.getParameter("op").equals("idUtilisateurConnecte")) {
			HttpSession sess = request.getSession(true);
			if (sess.getAttribute("idUtilisateur") == null) {
				response.getWriter().write("");	
			}
			
			else
				response.getWriter().write(Integer.toString((Integer) sess.getAttribute("idUtilisateur")));
			
		}
		
		
		else if (request.getParameter("op").equals("deconnexion")){
			request.getSession().invalidate();
			response.getWriter().write("Utilisateur deconnect? avec succ?s");
		}
		
	   else if (request.getParameter("op").equals("ajouterAnnonce")) {
			HttpSession sess = request.getSession(true);
			if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("Veuillez vous connecter");
			else {
				f.ajouterAnnonce((Integer) sess.getAttribute("idUtilisateur"), Double.parseDouble(request.getParameter("latitude")), Double.parseDouble(request.getParameter("longitude")), Double.parseDouble(request.getParameter("prixHeure")), (String) request.getParameter("adresse"));
				response.getWriter().write("succes");		
			}
		}
	   
	   else if (request.getParameter("op").equals("ajouterReservation")) {
			HttpSession sess = request.getSession(true);
			if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("Veuillez vous connecter");
			
			else if (f.utilisateur((Integer) sess.getAttribute("idUtilisateur")).getAnnonces().contains(f.annonce(Integer.parseInt(request.getParameter("idAnnonce")))))
				response.getWriter().write("erreur"); // l'annonce lui appartient
			else {
				try {
					if (f.ajouterReservation((Integer) sess.getAttribute("idUtilisateur"), Integer.parseInt(request.getParameter("idAnnonce")),  Fonctions.stringToDate(request.getParameter("dateDebut")),  Fonctions.stringToDate(request.getParameter("dateFin"))))
						response.getWriter().write("succes");
					else
						response.getWriter().write("occupe");
				} catch (NumberFormatException e) {
					response.getWriter().write("erreur");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					response.getWriter().write("erreur");
				}
			}

		}
	   
	   else if (request.getParameter("op").equals("listeUtilisateurs")) {

			Gson gson = new Gson();			
			Collection<Utilisateur> utilisateurs= f.listeUtilisateurs();
			
			for (Utilisateur u: utilisateurs) {
				u.setAnnonces(null);
				u.setReservations(null);
				u.setMessagesExpedies(null);
				u.setMessagesRecus(null);
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
				response.getWriter().write("erreur"); // Utilisateur non connect?
		   
		   else {
			   
			Gson gson = new Gson();			
			Collection<Annonce> annonces = f.listeAnnoncesUtilisateur((Integer)sess.getAttribute("idUtilisateur"));
					
			for (Annonce a: annonces){
				a.setProprietaire(null);
				a.setReservations(null);
			}
			response.getWriter().write(gson.toJson(annonces));

		   }
		}
	   else if (request.getParameter("op").equals("ajouterMessage")) {
		   HttpSession sess = request.getSession(true);
		   f.ajouterMessage((Integer) sess.getAttribute("idUtilisateur"),Integer.parseInt(request.getParameter("idReservation")), request.getParameter("contenu")) ;
			
		
			response.getWriter().write("succes");
		}
		
	   /*else if (request.getParameter("op").equals("listeMessages")) {
		    HttpSession sess = request.getSession(true);
			Gson gson = new Gson();
			Collection<Message> messages = f.listeMessageReservation(Integer.parseInt(request.getParameter("idReservation")));
			for (Message m: messages) {
				m.setReservation(null);
				m.setDestinataire(null);
				m.setExpediteur(null);
			}
			
			response.getWriter().write(gson.toJson(messages));
			
		}*/
		
	   else if (request.getParameter("op").equals("mesReservationsAnnonce")) {
		   HttpSession sess = request.getSession(true);
		   
		   if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("erreur"); // Utilisateur non connect?
		   
		   else {
			   
			Gson gson = new Gson();			
			Collection<Reservation> reservations = f.listerReservationsAnnonce(Integer.parseInt(request.getParameter("idAnnonce")));
					
			for (Reservation r: reservations){
				r.setAnnonce(null);
				r.setLocataire(null);
				r.setMessages(null);
			}
			response.getWriter().write(gson.toJson(reservations));

		   }
		}
		
	   else if (request.getParameter("op").equals("ajouterMessage")) {
		   HttpSession sess = request.getSession(true);
		   
		   if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("erreur"); // Utilisateur non connect?
		   
		   else {
			   if (f.ajouterMessage((Integer)(sess.getAttribute("idUtilisateur")),Integer.parseInt(request.getParameter("idReservation")), request.getParameter("contenu")))
				   response.getWriter().write("succes");
			   else
				   response.getWriter().write("erreur");
		   }
			
		

		}
		
	   else if (request.getParameter("op").equals("listerMessages")) {
		    HttpSession sess = request.getSession(true);
			   
			if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("erreur"); // Utilisateur non connect?
			else {
				Gson gson = new Gson();
				Collection<Message> messages = f.listerMessageReservation((Integer)sess.getAttribute("idUtilisateur"), Integer.parseInt(request.getParameter("idReservation")));
				
				for (Message m: messages) {
					m.setReservation(null);
					m.setDestinataire(null);
					m.setExpediteur(null);
				}
				
				response.getWriter().write(gson.toJson(messages));
			}
			
		}
		

	   else if (request.getParameter("op").equals("listerMessagesRecus")) {
		    HttpSession sess = request.getSession(true);
			   
			if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("erreur"); // Utilisateur non connect?
			else {
				Gson gson = new Gson();
				Collection<Message> messages = f.listerMessageRecus((Integer) sess.getAttribute("idUtilisateur"));
				
				for (Message m: messages) {
					m.setReservation(null);
					m.setDestinataire(null);
					m.setExpediteur(null);
				}
				
				response.getWriter().write(gson.toJson(messages));
			}
			
		}

	   else if (request.getParameter("op").equals("listerMessagesExpedies")) {
		    HttpSession sess = request.getSession(true);
			   
			if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("erreur"); // Utilisateur non connect?
			else {
				Gson gson = new Gson();
				Collection<Message> messages = f.listerMessageExpedies((Integer) sess.getAttribute("idUtilisateur"));
				
				for (Message m: messages) {
					m.setReservation(null);
					m.setDestinataire(null);
					m.setExpediteur(null);
				}
				
				response.getWriter().write(gson.toJson(messages));
			}
			
		}
		
	   else if (request.getParameter("op").equals("mesReservations")) {
		   HttpSession sess = request.getSession(true);
		   
		   if (sess.getAttribute("idUtilisateur") == null)
				response.getWriter().write("erreur"); // Utilisateur non connect?
		   
		   else {
			   
			Gson gson = new Gson();			
			Collection<Reservation> reservations = f.listeReservationsUtilisateur((Integer) sess.getAttribute("idUtilisateur"));
					
			for (Reservation r: reservations) {
				r.setAnnonce(null);
				r.setLocataire(null);
				r.setMessages(null);
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