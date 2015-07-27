package fr.ensicaen.odfplot.functionSelector;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import fr.ensicaen.odfplot.engine.FunctionODF;

public class FSPanel extends JPanel implements MouseListener {

	/**
	 * 
	 */

	private FunctionSelector parent = null;

	private static final long serialVersionUID = 1L;

	private LinkedList<Label> listeLabels = null;

	private Label labelSelectionne = null;

	public FSPanel(FunctionSelector p) {
		this.listeLabels = new LinkedList<Label>();
		this.parent = p;
		this.addMouseListener(this);
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (Label v : this.listeLabels) {
			v.dessine(g);

		}
	}

	public void mettreAjourFonction(LinkedList<FunctionODF> listes) {

		this.listeLabels = new LinkedList<Label>();

		int ity = 0;
		for (FunctionODF f : listes) {
			Label v = new Label(f);
			v.setPosition((this.parent.getWidth() - v.getLargeur()) / 2, ity
					* (v.getHauteur() + 3));

			this.listeLabels.add(v);
			ity++;
		}

		this.repaint();
	}

	public void ajouterFonction(FunctionODF f) {
		this.listeLabels.add(new Label(f));
		this.repaint();
	}

	public void rechercherSelectionnee(int x, int y) {
		this.labelSelectionne = null;
		for (Label v : this.listeLabels) {
			Label tmp = v.etreSelectionner(x, y);
			if (tmp != null)
				this.labelSelectionne = tmp;
		}

		if (this.labelSelectionne != null) {
			this.parent.selectionnerFonction(this.labelSelectionne
          .getFonction());
		}
		this.repaint();
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		this.rechercherSelectionnee(x, y);

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
