package fr.ensicaen.odfplot.sectionsVisualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class SCiblage {
	private Point position = null;

	private int xmin, xmax, ymin, ymax;

	private SGraphe graphe = null;

	public SCiblage(SGraphe p) {
		this.graphe = p;
		
		xmin = (int) Math.round(this.graphe.getXMin() * this.graphe.getZoom()
				+ this.graphe.getDecalX());
		
		ymin = (int) Math.round(this.graphe.getYMin() * this.graphe.getZoom()
				+ this.graphe.getDecalY());
		
		this.position = new Point(xmin, ymin);

	}

	public void dessine(Graphics g) {

		g.setColor(Color.green);

		xmin = (int) Math.round(this.graphe.getXMin() * this.graphe.getZoom()
				+ this.graphe.getDecalX());
		xmax = (int) Math.round(this.graphe.getXMax() * this.graphe.getZoom()
				+ this.graphe.getDecalX());
		ymin = (int) Math.round(this.graphe.getYMin() * this.graphe.getZoom()
				+ this.graphe.getDecalY());
		ymax = (int) Math.round(this.graphe.getYMax() * this.graphe.getZoom()
				+ this.graphe.getDecalY());
		ymax +=3;

		// dessin ligne verticale.
		g.drawLine(this.position.x, ymin, this.position.x, ymax);

		// dessin ligne horizontal.
		g.drawLine(xmin, this.position.y, xmax, this.position.y);
	}

	public void setPosition(int x, int y) {
		
		if (x >= xmin && x <= xmax && y >= ymin && y <= ymax){
			this.position.setLocation(x, y);
		}
	}
}
