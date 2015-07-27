package fr.ensicaen.odfplot.engine;

import java.util.LinkedList;

public class FunctionODF {

	/*
	 * Variables d'instance.
	 */
	

	private Parametre parametre;

	private String nom ;
	private LinkedList<ODFPoint> listePoints = new LinkedList<ODFPoint>();



	/*
	 * Constructeurs
	 */

	public FunctionODF(Parametre p, LinkedList<ODFPoint> l) {
		parametre = p;
		listePoints = l;

	}

	public FunctionODF() {
		parametre = new Parametre();
		this.listePoints = new  LinkedList<ODFPoint>();
		
	}

	/*
	 * Methodes
	 */

	
	public LinkedList<ODFPoint> getListePoints() {
		return listePoints;
	}

	public void ajouterPoint(ODFPoint p) {
		this.listePoints.add(p);
	}

	public Parametre getParametre() {
		return parametre;
	}

	public void setParametre(Parametre parametre) {
		this.parametre = parametre;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	

}
