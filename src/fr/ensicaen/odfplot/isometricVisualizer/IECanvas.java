package fr.ensicaen.odfplot.isometricVisualizer;

import fr.ensicaen.odfplot.graphicInterface.ECanvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.Coupe;
import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;
import fr.ensicaen.odfplot.engine.Parametre;
import fr.ensicaen.odfplot.engine.View;

public class IECanvas extends ECanvas implements MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private IsometricFrame fenetre = null;

	private IGraphe graphe = null;

	private IAxe axe = null;

	private IEchelle echelle = null;

	public IECanvas(IsometricFrame f, GenerateurPlageCouleur g, Parametre p) {
		this.fenetre = f;
		this.graphe = new IGraphe(this, g, fenetre.getFonction());
		this.axe = this.graphe.getAxe();//new SAxe(graphe, p);
		// this.cible = new SCiblage(graphe);
		this.echelle = new IEchelle(this, this.fenetre.getFonction());

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.setBackground(Color.black);

	}
	
	public void changeCoupe(Coupe c ){
		this.axe.setCoupe(c);
	}

	public void changeVue(View v) {
		this.graphe.changeVue(v);
	}

	private void dessineGraphe(Graphics g) {
		this.graphe.dessine(g);
	}

	private void dessineAxes(Graphics g) {
		this.axe.dessine(g);
	}

	private void dessineEchelle(Graphics g) {
		this.echelle.dessine(g);
	}
	


	public void paint(Graphics g) {
		super.paint(g);

		this.dessineGraphe(g);
		this.dessineAxes(g);
		this.dessineEchelle(g);
		// this.cible.draw(g);
	}

	

	public void setTypeCouleur(Couleur t) {
		this.graphe.setTypeCouleur(t);
		this.echelle.setTypeCouleur(t);

		repaint();
	}

	

	public void mouseDragged(MouseEvent e) {
		// this.cible.setPosition(e.getX(), e.getY());
		this.graphe.modifSelection(e.getX(), e.getY());
		repaint();

	}

	public void mouseMoved(MouseEvent e) {

		/*
		 * this.cible.setPosition(e.getX(), e.getY()); repaint();
		 */
		/*PointSurEcran point = null;
		for (PointSurEcranSelectionnable p : this.graphe.getListePoints()) {
			PointSurEcran tmp = p.estAudessus(e.getX(), e.getY());
			if (tmp != null)
				point = tmp;
		}
		for (PointSurEcran p : this.graphe.getListePointsInterpoles()) {
			PointSurEcran tmp = p.estAudessus(e.getX(), e.getY());
			if (tmp != null)
				point = tmp;
		}
		if (point != null) {
			
			this.echelle.setPointValeur(point.getValeur());
			this.repaint();
		}*/

	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	

	

	public void mousePressed(MouseEvent e) {
		

	}

	public void mouseReleased(MouseEvent e) {
		
		this.graphe.setSelection(null);
		this.repaint();
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
