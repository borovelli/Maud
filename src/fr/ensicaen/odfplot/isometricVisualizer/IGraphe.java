package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.FunctionODF;
import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;
import fr.ensicaen.odfplot.engine.ODFPoint;
import fr.ensicaen.odfplot.engine.View;

public class IGraphe extends JPanel {

	private static final long serialVersionUID = 1L;

	private IECanvas ecran = null;

	private BufferedImage imageTampon;

	private Graphics2D graphique;

	private GenerateurPlageCouleur genCouleur = null;

	private double zoom = 0.0;

	private int decalX = 30;

	private int decalY = 25;

	private double xMax = 0, xMin = 0, yMax = 0, yMin = 0;

	private FunctionODF function = null;

	private IAxe axe = null;

	private Selection selection = null;

	private View vue = View.STANDARD;

	private Couleur typeCouleur = Couleur.COULEUR;

	public IGraphe(IECanvas e, GenerateurPlageCouleur g, FunctionODF f) {
		this.ecran = e;
		this.genCouleur = g;
		this.function = f;

		this.axe = new IAxe(this, f.getParametre());
	}

	public void changeVue(View v) {
		this.vue = v;
	}

	private void dessinePoint(Graphics g) {

		
		
		// initialisation des variables.
		xMax = 0;
		xMin = 0;
		yMax = 0;
		yMin = 0;
		zoom = 0.0;

		// determination de minimum et des maximum des coordonnees des points.
		for (ODFPoint p : function.getListePoints()) {

			int x = (int) Math.round(p.getAlpha());
			int y = (int) Math.round(p.getBeta());
			int z = (int) Math.round(p.getGamma());

			x += (z / 2);
			y += (z / 2);

			if (x > xMax)
				xMax = x;
			if (x < xMin)
				xMin = x;

			if (y > yMax)
				yMax = y;
			if (y < yMin)
				yMin = y;
			
			
		}
		
		// cadrage des points dans la fenetre.
		// on cadrage les points sur l'axe des x.
		zoom = (this.ecran.getWidth() - 200) / (xMax - xMin);
		// puis sur l'axe des y.
		double tmp = (this.ecran.getHeight() - 100) / (yMax - yMin);

		/*
		 * on ne garde que le zoom le plus petit pour afficher les points et
		 * garder les proportions d'afichage.
		 */
		if (zoom > tmp)
			zoom = tmp;

		/*
		 * decalage temporaire du graphe. Si les cohordonnees min sont
		 * inferieures a Zero.
		 */
		double decalXTmp = 0;
		double decalYTmp = 0;

		if (xMin < 0)
			decalXTmp = Math.abs(xMin);
		if (yMin < 0) {
			decalYTmp = Math.abs(xMin);
			yMax -= yMin;
		}
		
		imageTampon = new BufferedImage(this.ecran.getWidth(), this.ecran
				.getHeight(), BufferedImage.TYPE_INT_RGB);

		graphique = imageTampon.createGraphics();

		// on redefini le placement des point sur l'ecran.
		for (ODFPoint p : function.getListePoints()) {

			int x = (int) Math.round(p.getAlpha());
			int y = (int) Math.round(p.getBeta());
			int z = (int) Math.round(p.getGamma());

			x += (z / 2);
			y += (z / 2);
			
			graphique.setColor(this.getColor(p.getValeur()));
					
			x = (int) Math.round((x + decalXTmp) * zoom + decalX);
			y = (int) Math.round((yMax - (y + decalYTmp)) * zoom
					+ decalY);
			
			graphique.fillRect(x, y, 3, 3);
			
		}
		graphique.setColor(Color.green);
		
		
		g.drawImage(imageTampon, 0, 0, this.ecran.getWidth(), this.ecran
				.getHeight(), null);
	}

	public void dessine(Graphics g) {

		g.setColor(this.getColorFond());
		g.fillRect(0, 0, this.ecran.getWidth(), this.ecran.getHeight());

		this.dessinePoint(g);

		//this.axe.draw(g);
	}

	public Color getColorFond() {
		Color fond = Color.BLACK;
		switch (typeCouleur) {
		case BLANC_NOIR:
			fond = Color.white;
			break;
		}
		return fond;
	}

	public Color getColor(Double v) {
		Color c = null;
		switch (typeCouleur) {
		case NOIR_BLANC:
			c = this.genCouleur.rechercherNoirBlanc(v);
			break;

		case BLANC_NOIR:
			c = this.genCouleur.rechercherBlancNoir(v);

			break;
		case COULEUR:
			c = this.genCouleur.doubleVersCouleur(v);
			break;
		}
		return c;
	}

	

	
	

	public void modifSelection(int x, int y) {
		this.selection.setPoint2(new Point(x, y));

	}

	public int getDecalX() {
		return decalX;
	}

	public void setDecalX(int decalX) {
		this.decalX = decalX;
	}

	public int getDecalY() {
		return decalY;
	}

	public void setDecalY(int decalY) {
		this.decalY = decalY;
	}

	public int getLargeur() {
		return (int) Math.round(this.xMax * this.zoom);
	}

	public int getHauteur() {
		return (int) Math.round(this.yMax * this.zoom);
	}

	public double getZoom() {
		return zoom;
	}

	public double getXMax() {
		return xMax;
	}

	public double getXMin() {
		return xMin;
	}

	public double getYMax() {
		return yMax;
	}

	public double getYMin() {
		return yMin;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}

	public void setTypeCouleur(Couleur typeCouleur) {
		this.typeCouleur = typeCouleur;
		this.repaint();
	}

	public IAxe getAxe() {
		return axe;
	}
}
