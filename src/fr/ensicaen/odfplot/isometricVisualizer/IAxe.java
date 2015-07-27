package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.Color;
import java.awt.Graphics;

import fr.ensicaen.odfplot.engine.Coupe;
import fr.ensicaen.odfplot.engine.Parametre;
import it.unitn.ing.rista.util.Misc;

public class IAxe {

	private IGraphe graphe = null;

	private Parametre parametre;

	private Coupe coupe = Coupe.ALPHA;

	public IAxe(IGraphe g, Parametre p) {
		this.graphe = g;
		this.parametre = p;

	}

	public void dessine(Graphics g) {
		g.setColor(Color.green);
		int xmin = (int) Math.round(this.graphe.getXMin()
				* this.graphe.getZoom() + this.graphe.getDecalX());
		int xmax = (int) Math.round(this.graphe.getXMax()
				* this.graphe.getZoom() + this.graphe.getDecalX());
		int ymin = (int) Math.round(this.graphe.getYMin()
				* this.graphe.getZoom() + this.graphe.getDecalY());
		int ymax = (int) Math.round(this.graphe.getYMax()
				* this.graphe.getZoom() + this.graphe.getDecalY());

		g.drawLine(xmin, ymax + 3, xmax, ymax + 3);
		g.drawLine(xmin, ymax + 3, xmin, ymin);

		// axe des x
		// une ligne qui separe l'axe en deux.
		g.drawLine(((xmax + xmin) / 2), ymax + 3, (xmax + xmin) / 2, ymax + 8);
		g
				.drawLine(xmin, (ymin + ymax + 3) / 2, xmin - 5,
						(ymin + ymax + 3) / 2);

		g.drawLine((int)Math.round((xmax + xmin) * 0.25) , ymax + 3,
				(int)Math.round((xmax + xmin)*0.25), ymax + 8);
		g.drawLine(xmin, (int)Math.round((ymin + ymax + 3)*0.25 ), xmin - 5,
				(int)Math.round((ymin + ymax + 3)*0.25));

		g.drawLine((int)Math.round((xmax + xmin) * 0.75) , ymax + 3,
				(int)Math.round((xmax + xmin)*0.75), ymax + 8);
		g.drawLine(xmin, (int)Math.round((ymin + ymax + 3)*0.75 ), xmin - 5,
				(int)Math.round((ymin + ymax + 3)*0.75));

		Character x = ' ', y = ' ';
		switch (coupe) {

		case ALPHA:
			x = '\u03B2'; //beta
			y = '\u03B3'; //gamma
			break;
		case BETA:
			x = '\u03B1'; //alpha
			y = '\u03B3'; //gamma
			break;
		case GAMMA:
			x = '\u03B1'; //alpha
			y = '\u03B2'; //beta
			break;
		default:

		}
		g.drawString(Character.toString(x), xmax + 3, ymax + 8);
		g.drawString(Character.toString(y), xmin - 8, ymin - 8);

	}

	public void setCoupe(Coupe coupe) {
		this.coupe = coupe;
		System.out.println(this.coupe);
	}

}
