package fr.ensicaen.odfplot.sectionsVisualizer;

import fr.ensicaen.odfplot.graphicInterface.ECanvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.Coupe;
import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;
import fr.ensicaen.odfplot.engine.Parametre;
import fr.ensicaen.odfplot.engine.PointSurEcran;
import fr.ensicaen.odfplot.engine.PointSurEcranSelectionnable;
import fr.ensicaen.odfplot.engine.View;

public class SECanvas extends ECanvas implements MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private SectionsFrame fenetre = null;

	private SGraphe graph = null;

	private SAxe axe = null;

	private SEchelle scale = null;

	public SECanvas(SectionsFrame f, GenerateurPlageCouleur g, Parametre p) {
		fenetre = f;
		graph = new SGraphe(this, g, p);
		axe = graph.getAxe();//new SAxe(graph, p);
		// cible = new SCiblage(graph);
		scale = new SEchelle(this, fenetre.getFonction());

		addMouseListener(this);
		addMouseMotionListener(this);

		setBackground(Color.black);

	}
	
	public void changeCoupe(Coupe c ){
		axe.setCoupe(c);
	}

	public void changeView(View v) {
		graph.changeVue(v);
	}

	private void drawGraph(Graphics g) {
		graph.draw(g);
	}

	private void drawAxes(Graphics g) {
		axe.draw(g);
	}

	private void drawScale(Graphics g) {
		scale.draw(g);
	}
	


	public void paint(Graphics g) {
		super.paint(g);

		drawGraph(g);
		drawAxes(g);
		drawScale(g);
		// cible.draw(g);
	}

	public void setListePoints(ArrayList<PointSurEcranSelectionnable> p) {
		graph.setListePoints(p);
		repaint();

	}

	public void setInterpolator(boolean i) {

		graph.setInterpoler(i);

		repaint();

	}

	public void setTypeCouleur(Couleur t) {
		graph.setTypeCouleur(t);
		scale.setTypeCouleur(t);

		repaint();
	}

	public void setPolar(boolean polaire) {
		graph.setPolaire(polaire);
		// repaint();
	}

	public void mouseDragged(MouseEvent e) {
		// cible.setPosition(e.getX(), e.getY());
		graph.modifSelection(e.getX(), e.getY());
		repaint();

	}

	public void mouseMoved(MouseEvent e) {

		/*
		 * cible.setPosition(e.getX(), e.getY()); repaint();
		 */
		PointSurEcran point = null;
		for (PointSurEcranSelectionnable p : graph.getListePoints()) {
			PointSurEcran tmp = p.estAudessus(e.getX(), e.getY());
			if (tmp != null)
				point = tmp;
		}
		for (PointSurEcran p : graph.getListePointsInterpoles()) {
			PointSurEcran tmp = p.estAudessus(e.getX(), e.getY());
			if (tmp != null)
				point = tmp;
		}
		if (point != null) {
			
			scale.setPointValeur(point.getValeur());
			repaint();
		}

	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private void nouvelleSelection(int x, int y) {
		graph.nouvelleSelection(x, y);
	}

	private void finSelection(int x, int y) {
		graph.finSelection(x, y);
	}

	public void mousePressed(MouseEvent e) {
		nouvelleSelection(e.getX(), e.getY());

	}

	public void mouseReleased(MouseEvent e) {
		finSelection(e.getX(), e.getY());
		graph.setSelection(null);
		repaint();
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
