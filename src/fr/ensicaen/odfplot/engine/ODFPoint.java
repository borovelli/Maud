package fr.ensicaen.odfplot.engine;

import it.unitn.ing.rista.util.Misc;

public class ODFPoint implements Comparable{

	/*
	 * Variable d'intance: les variables d'instance sont de simples variables
	 * que possederons tous les objets de la meme classe.
	 */

	private double valeur;

	private double alpha, beta, gamma;

	/*
	 * Une classe est un moule pour cree des objets. Ainsi tous les objets de la
	 * meme classe ont la meme structure. Ils ont donc les memes variables et
	 * les memes methodes.
	 */

	/**
	 * cree un point avec ses coordonnees alpha beta gamma entrees en parametre
	 * ainsi que la densite associee e ce point.
	 * 
	 * @param alpha
	 * @param beta
	 * @param gamma
	 * @param valeur
	 */

	public ODFPoint(double alpha, double beta, double gamma, double valeur) {
		this.setAlpha(alpha);
		this.setBeta(beta);
		this.setGamma(gamma);
		this.setValeur(valeur);
	}

	public String toString() {
		String retour = "[" + this.alpha + "," + this.beta + "," + this.gamma
				+ "," + this.valeur + "]";
		return retour;
	}

	/**
	 * @renvoie la coordonee Alpha
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * @renvoie la coordonee beta
	 */
	public double getBeta() {
		return beta;
	}

	/**
	 * @renvoie la coordonee gamma
	 */
	public double getGamma() {
		return gamma;
	}

	/**
	 * @renvoie la densite du point.
	 */
	public double getValeur() {
		return valeur;
	}

	/**
	 * @param i
	 */
	public void setAlpha(double i) {
		alpha = i;
	}

	/**
	 * @param i
	 */
	public void setBeta(double i) {
		beta = i;
	}

	public int compareTo(Object autre) {
		double nombre1 = 0;
		double nombre2 = 0;
		
		nombre1 = ((ODFPoint) autre).getValeur();
		nombre2 = this.getValeur();
		if (nombre1 > nombre2)
			return -1;
		else if (nombre1 == nombre2)
			return 0;
		else
			return 1;

	}

	/**
	 * @param i
	 */
	public void setGamma(double i) {
		gamma = i;
	}

	/**
	 * @param i
	 */
	public void setValeur(double i) {
		valeur = i;
	}

	public void print() {
		System.out.println("[" + alpha + "," + beta + "," + gamma + ","
				+ valeur + "]");
	}
}
