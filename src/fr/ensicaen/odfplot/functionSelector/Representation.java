package fr.ensicaen.odfplot.functionSelector;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import fr.ensicaen.odfplot.engine.Coupe;
import fr.ensicaen.odfplot.engine.FunctionODF;
import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;
import fr.ensicaen.odfplot.engine.Parametre;
import fr.ensicaen.odfplot.engine.PointSurEcran;
import fr.ensicaen.odfplot.engine.PointSurEcranSelectionnable;
import fr.ensicaen.odfplot.engine.SelectionneurTranche;

public class Representation {

	private int x, y, largeur, hauteur, yMax;

	private static final long serialVersionUID = 1L;

	private FunctionODF function = null;

	public Representation(FunctionODF f) {
		this.function = f;
	}

	public void dessine(Graphics g) {

		g.setColor(Color.BLACK);
		g.fillRect(this.x, this.y, largeur, hauteur);
		this.dessinerFonction(g);
		g.setColor(Color.white);
		g.drawRect(this.x, y, this.largeur, this.hauteur);

	}

	public void setBounds(int x, int y, int l, int h) {
		this.x = x;
		this.y = y;
		this.hauteur = h;
		this.largeur = l;

		this.yMax = (this.y + this.hauteur);

	}

	public void dessinerFonction(Graphics g) {
		Parametre p = this.function.getParametre();
		double facteurX = this.largeur
				/ ((p.getBetaMax() - p.getBetaMin() + p.getBetaStep()));
		double facteurY = this.hauteur
				/ ((p.getGammaMax() - p.getGammaMin() + p.getGammaStep()));

		GenerateurPlageCouleur gpc = new GenerateurPlageCouleur(this.function);
		SelectionneurTranche st = new SelectionneurTranche(this.function);
		ArrayList<PointSurEcranSelectionnable> tmp = st.getTranche(Coupe.ALPHA, 0.0);

		for (PointSurEcran point : tmp) {

			Color c = gpc.rechercherNoirBlanc(point.getValeur());
			g.setColor(c);
			
			int x = (int) ((point.getX() * facteurX) + this.x);
			int y = (int) ((point.getY() * facteurY));
			int h = (int) (p.getGammaStep() * facteurY);
			int l = (int) (p.getBetaStep() * facteurX);

			g.fillRect(x, (this.yMax - y ), l + 1, h + 1);

		}

	}

	public void setSize(int l, int h) {
		this.hauteur = h;
		this.largeur = l;

	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;

	}

}
