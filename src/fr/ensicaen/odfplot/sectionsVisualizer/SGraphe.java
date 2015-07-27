package fr.ensicaen.odfplot.sectionsVisualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;
import fr.ensicaen.odfplot.engine.Interpolator;
import fr.ensicaen.odfplot.engine.Parametre;
import fr.ensicaen.odfplot.engine.PointSurEcran;
import fr.ensicaen.odfplot.engine.PointSurEcranSelectionnable;
import fr.ensicaen.odfplot.engine.View;
import it.unitn.ing.rista.util.Constants;

public class SGraphe extends JPanel {

	private static final long serialVersionUID = 1L;

	private SECanvas ecran = null;

	private BufferedImage imageTampon;

	private boolean nouvelleListe = false;

  private GenerateurPlageCouleur genCouleur = null;

	private boolean interpoler = true;

	private ArrayList<PointSurEcranSelectionnable> listePoints = null;

	private ArrayList<PointSurEcran> listePointsInterpoles = null;

	private Interpolator interpolator = null;

	private double zoom = 0.0;

	private int decalX = 30;

	private int decalY = 25;

	private double xMax = 0, xMin = 0, yMax = 0, yMin = 0;

	private SAxe axe = null;

	private Selection selection = null;

	private View vue = View.STANDARD;

	private Couleur typeCouleur = Couleur.COULEUR;

	public SGraphe(SECanvas e, GenerateurPlageCouleur g, Parametre p) {
		ecran = e;
		genCouleur = g;
		interpolator = new Interpolator();

		axe = new SAxe(this, p);

		listePoints = new ArrayList<PointSurEcranSelectionnable>();
		listePointsInterpoles = new ArrayList<PointSurEcran>();
	}

	public void changeVue(View v) {
		vue = v;
	}

	public void placeFigure() {

		placePoint();

		// initialisation des variables.
		xMax = 0;
		xMin = 0;
		yMax = 0;
		yMin = 0;
		zoom = 0.0;

		// determination de minimum et des maximum des coordonnees des points.
		for (PointSurEcran p : listePoints) {

			int x = p.getX();
			int y = p.getY();

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
		zoom = (ecran.getWidth() - 200) / (xMax - xMin);
		// puis sur l'axe des y.
		double tmp = (ecran.getHeight() - 100) / (yMax - yMin);

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

		// on redefini le placement des point sur l'ecran.
		for (PointSurEcran p : listePoints) {

			p.setX((int) Math.round((p.getX() + decalXTmp) * zoom + decalX));
			p.setY((int) Math.round((yMax - (p.getY() + decalYTmp)) * zoom
					+ decalY));

		}

		interpoler();

	}

	private void interpoler() {
		switch (vue) {

    case STEREOGRAPHIC_WULFF:
    case EQUAL_AREA_SCHMIDT:

			if (interpoler)
				listePointsInterpoles = interpolator.interpolerPolaire(listePoints);
			break;

		case STANDARD:
			if (interpoler)
				listePointsInterpoles = interpolator.interpoler(listePoints);

			break;
		default:
		}
	}

	public void draw(Graphics g) {

		g.setColor(getColorFond());
		g.fillRect(0, 0, ecran.getWidth(), ecran.getHeight());

		if (interpoler) {
			dessinePointInterpoles(g);
		}

		dessinePoint(g);

		axe.draw(g);

		if (selection != null) {
			selection.dessiner(g);

		}

	}

	private void placePoint() {

		switch (vue) {

		case STEREOGRAPHIC_WULFF:

			for (PointSurEcranSelectionnable p : listePoints) {
        double projection = p.getX();
//            System.out.println(angles[0] + " " + angles[1]);
        if (p.getY() > 90.) {
          projection = 180. - p.getX();
        }
				double xp = projection * Math.cos(Math.toRadians(p.getY()));
				double yp = projection * Math.sin(Math.toRadians(p.getY()));

 				p.setX((int) Math.round(xp));
				p.setY((int) Math.round(yp));
			}
			break;

		case EQUAL_AREA_SCHMIDT:

			for (PointSurEcranSelectionnable p : listePoints) {

        double projection = Constants.sqrt2 * Math.sin(Math.toRadians(p.getX()) / 2.0);
//            System.out.println(angles[0] + " " + angles[1]);
        if (p.getY() > 90.) {
          projection = Constants.sqrt2 * Math.sin(Math.toRadians((180. - p.getX())) / 2.0);
        }
				double xp = projection * Math.cos(Math.toRadians(p.getY()));
				double yp = projection * Math.sin(Math.toRadians(p.getY()));

				xp *= 90;
				yp *= 90;

				p.setX((int) Math.round(xp));
				p.setY((int) Math.round(yp));
			}
			break;
		case STANDARD:

			break;
		default:
		}

	}

	private void dessinePointInterpoles(Graphics g) {

		if (nouvelleListe) {

			imageTampon = new BufferedImage(ecran.getWidth(), ecran
					.getHeight(), BufferedImage.TYPE_INT_RGB);

      Graphics2D graphique = imageTampon.createGraphics();

      graphique.setColor(getColorFond());
			graphique.fillRect(0, 0, ecran.getWidth(), ecran
					.getHeight());

			for (PointSurEcran p : listePointsInterpoles) {
				graphique.setColor(getColor(p));
				graphique.fillRect(p.getX(), p.getY(), 3, 3);
			}
		}

		g.drawImage(imageTampon, 0, 0, ecran.getWidth(), ecran
				.getHeight(), null);

		nouvelleListe = false;

	}

	private void dessinePoint(Graphics g) {
		// desin des points (en vert) selectionnees ou non .

		for (PointSurEcranSelectionnable p : listePoints) {

			if (p.isSelectionne()) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(getColor(p));
			}
			g.fillRect(p.getX(), p.getY(), 3, 3);
		}

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

	public Color getColor(PointSurEcran p) {
		Color c = null;
		switch (typeCouleur) {
		case NOIR_BLANC:
			c = genCouleur.rechercherNoirBlanc(p.getValeur());
			break;

		case BLANC_NOIR:
			c = genCouleur.rechercherBlancNoir(p.getValeur());

			break;
		case COULEUR:
			c = genCouleur.doubleVersCouleur(p.getValeur());
			break;
		}
		return c;
	}

	public void setListePoints(ArrayList<PointSurEcranSelectionnable> p) {

		listePoints = p;
		placeFigure();
		nouvelleListe = true;

	}

	private void selectionner() {

		for (PointSurEcranSelectionnable p : listePoints) {
			if (p.getX() > selection.getPoint1().x
					&& p.getX() < selection.getPoint2().x
					&& p.getY() > selection.getPoint1().y
					&& p.getY() < selection.getPoint2().y) {
				p.setSelectionne(true);
			} else {
				p.setSelectionne(false);
			}
		}
		nouvelleListe = true;

	}

	public void nouvelleSelection(int x, int y) {
		selection = new Selection();
		selection.setPoint1(new Point(x, y));
	}

	public void finSelection(int x, int y) {
		selection.setPoint2Fin(new Point(x, y));
		selectionner();
	}

	public void modifSelection(int x, int y) {
		selection.setPoint2(new Point(x, y));

	}

	public boolean isInterpoler() {
		return interpoler;
	}

	public void setInterpoler(boolean i) {
		interpoler = i;

		nouvelleListe = true;
		if (i)
			interpoler();

	}

	public void setPolaire(boolean polaire) {
		if (polaire) {

			vue = View.EQUAL_AREA_SCHMIDT;

		} else
			vue = View.STANDARD;
		nouvelleListe = true;

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
		return (int) Math.round(xMax * zoom);
	}

	public int getHauteur() {
		return (int) Math.round(yMax * zoom);
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
		nouvelleListe = true;
	}

	public ArrayList<PointSurEcranSelectionnable> getListePoints() {
		return listePoints;
	}

	public ArrayList<PointSurEcran> getListePointsInterpoles() {
		return listePointsInterpoles;
	}

	public SAxe getAxe() {
		return axe;
	}
}
