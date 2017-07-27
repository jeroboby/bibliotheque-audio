package fr.lteconsulting.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.lteconsulting.dao.DisqueDAO;

public class Bibliotheque
{
	private Map<String, Disque> disques = new HashMap<>();
	 DisqueDAO disqueDAO = new DisqueDAO();

	public void ajouterDisque( Disque disque )
	{
		//disques.put( disque.getCodeBarre(), disque );
		
		disqueDAO.addDisque(disque);
	}

	public List<Disque> getDisques()
	{
		return new ArrayList<>( disques.values() );
		
	}

	public Disque rechercherDisqueParCodeBarre( String codeBarre )
	{
		return disques.get( codeBarre );
	}

	public List<Disque> rechercherDisqueParNom( String recherche )
	{
		recherche = recherche.toLowerCase();

		List<Disque> resultat = new ArrayList<>();

		for( Disque disque : disques.values() )
		{
			if( disque.getNom().toLowerCase().contains( recherche ) )
				resultat.add( disque );
		}

		return resultat;
	}

	public List<Disque> rechercherDisqueParNom( List<String> termes )
	{
		List<Disque> resultat = new ArrayList<>();

		for( Disque disque : disques.values() )
		{
			boolean estValide = true;
			for( String terme : termes )
			{
				if( !disque.getNom().toLowerCase().contains( terme.toLowerCase() ) )
				{
					estValide = false;
					break;
				}
			}

			if( estValide )
				resultat.add( disque );
		}

		return resultat;
	}

	public void afficher()
	{
		System.out.println( "BIBLIOTHEQUE avec " + disques.size() + " disques" );
		for( Disque disque : disques.values() )
			disque.afficher();
	}
	public void delete(String id){
		
	}
}
