package fr.ensicaen.odfplot.functionSelector;

import java.awt.Color;
import java.awt.Graphics;

import fr.ensicaen.odfplot.engine.FunctionODF;

public class Label {

	/**
	 * 
	 */
	private boolean estSelectionnee = false;

	private int x, y, largeur = 170, hauteur = 150;

	private static final long serialVersionUID = 1L;

	private FunctionODF function = null;

	private Representation icone = null;

	public Label(FunctionODF f) {

		this.function = f;
		this.icone = new Representation(f);
		this.mettreAJourIcone();

	}

	public void mettreAJourIcone() {
		this.icone.setBounds(this.x + 20, this.y + 20, largeur - 40,
				hauteur - 60);
	}

	public void selectionner() {
		this.estSelectionnee = true;
	}

	public void deselectionner() {
		this.estSelectionnee = false;
	}

	public Label etreSelectionner(int x, int y) {
		if (x > this.x && x < this.x + this.largeur && y > this.y
				&& y < this.hauteur + this.y) {
			this.selectionner();
			
			return this;
		} else {
			
			this.deselectionner();
			return null;
		}
	}

	public void dessine(Graphics g) {
		
		if (this.estSelectionnee)
			g.setColor(Color.RED);
		else
			g.setColor(Color.WHITE);

		g.drawRect(this.x, this.y, largeur, hauteur);

		g.setColor(Color.BLACK);
		g.drawString(this.function.getNom(),this.x + 5, this.y+this.hauteur-6);
		this.icone.dessine(g);
	}

	public void setBounds(int x, int y, int l, int h) {

		this.x = x;
		this.y = y;
		this.hauteur = h;
		this.largeur = l;
		this.mettreAJourIcone();
	}

	public void setSize(int l, int h) {
		this.hauteur = h;
		this.largeur = l;
		this.mettreAJourIcone();

	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.mettreAJourIcone();

	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public FunctionODF getFonction() {
		return function;
	}

}
