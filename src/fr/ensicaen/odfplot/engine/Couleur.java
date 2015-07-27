package fr.ensicaen.odfplot.engine;

public enum Couleur {
	NOIR_BLANC, COULEUR, BLANC_NOIR;

	private Couleur() {
	}

	public Couleur noirblanc() {

		return Couleur.NOIR_BLANC;
	}
	public Couleur blancNoir() {

		return Couleur.BLANC_NOIR;
	}

	public Couleur couleur() {

		return Couleur.COULEUR;
	}

	
}
