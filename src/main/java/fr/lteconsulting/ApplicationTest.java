package fr.lteconsulting;

import java.util.ArrayList;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

import fr.lteconsulting.dao.DisqueDAO;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.Saisie;

public class ApplicationTest
{
	public static void main( String[] args )
	{
		DisqueDAO dao = new DisqueDAO();

		//chercherEtAfficherDisque( dao, "pptt" );
		//chercherEtAfficherDisque( dao, "ppttdddd" );
		//chercherEtAfficherTousLesDisque(dao);
		ajouterDisque(dao);
	}

	private static void chercherEtAfficherDisque( DisqueDAO dao, String id )
	{
		Disque disque = dao.findById( id );
		if( disque != null )
		{
			System.out.println( "Le disque " + id + " a été trouvé :" );
			disque.afficher();
		}
		else
		{
			System.out.println( "Le disque " + id + "n'existe pas" );
		}
	}
	private static void chercherEtAfficherTousLesDisque(DisqueDAO dao)
	{
		List <Disque> tousLesDisques = dao.findAll();
		
		if( tousLesDisques != null )
		{
			System.out.println("Voici les disques : ");
			for(Disque current : tousLesDisques){
				
				current.afficher();
			}
			
		}
		else
		{
			System.out.println( "Il n'y a aucun disques" );
		}
	}
	private static void ajouterDisque(DisqueDAO dao){
		
		String id = Saisie.saisie("choisissez un identifiant");
		String nom = Saisie.saisie("choisissez un nom de disque");
		Disque disque1 = new Disque(nom);
		dao.addDisque(disque1);
		System.out.println("Vous avez bien ajouté :" + " "+ id + " "+ nom);
	}
}
