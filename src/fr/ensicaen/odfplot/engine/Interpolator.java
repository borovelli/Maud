package fr.ensicaen.odfplot.engine;

import java.util.ArrayList;
import java.util.Collections;


public class Interpolator {

	private ArrayList<PointSurEcran> result = null;
// 	private ArrayList<Triangle> listeTriangles = null;

	private Tri tri = Tri.Y;

	public Interpolator() {
		result = new ArrayList<PointSurEcran>();
	}

	private void calculeMoyennePondereeAxeX(PointSurEcran p1, PointSurEcran p2) {

		int x = p1.getX();
		int x1 = p1.getX();
		int x2 = p2.getX();

		int distance = x2 - x1;

		while (x < x2) {

			// on genere les pixels manquant entre les points.

			double valeurResult = ((p1.getValeur() * (x2 - x)) + (p2.getValeur() * (x - x1)))
					/ (distance);

			PointSurEcran tmp = new PointSurEcran(x, p1.getY(), valeurResult);

			result.add(tmp);
			x += 3;
		}
	}

	private void calculeMoyennePondereeAxeY(PointSurEcran p1, PointSurEcran p2) {

		int y = p1.getY();
		int y1 = p1.getY();
		int y2 = p2.getY();

		int distance = y2 - y1;

		while (y < y2) {

			// on genere les pixels manquant entre les points.

			double valeurResult = ((p1.getValeur() * (y2 - y)) + (p2.getValeur() * (y - y1)))
					/ (distance);

			// int valeurResult =0;
			PointSurEcran tmp = new PointSurEcran(p1.getX(), y, valeurResult);

			result.add(tmp);
			y += 3;
		}
	}

	private void interpolerLignes() {

		tri = Tri.Y;
		for (PointSurEcran p : result) {
			p.setTri(tri);
		}

		Collections.sort(result);

		int taille = result.size();

		for (int i = 0; i < taille; i++) {
			PointSurEcran p = result.get(i);
			PointSurEcran pSuivant = result.get(i + 1);

			if (p.getY() == pSuivant.getY()) {
				calculeMoyennePondereeAxeX(p, pSuivant);
			}
		}

	}

	private void interpolerColones() {
		tri = Tri.X;
		for (PointSurEcran p : result) {
			p.setTri(tri);
		}

		Collections.sort(result);

		int taille = result.size();

		for (int i = 0; i < taille; i++) {
			PointSurEcran p = result.get(i);
			PointSurEcran pSuivant = result.get(i + 1);

			if (p.getX() == pSuivant.getX()) {
				
				calculeMoyennePondereeAxeY(p, pSuivant);
			}
		}

	}

	public ArrayList<PointSurEcran> interpoler(ArrayList<PointSurEcranSelectionnable> points) {
		result = new ArrayList<PointSurEcran>();

		for (PointSurEcran p : points) {
			result.add(p.copie());
		}

		interpolerLignes();
		interpolerColones();

		return result;

	}
	
	public ArrayList<PointSurEcran> interpolerPolaire(ArrayList<PointSurEcranSelectionnable> points) {
		
		result = new ArrayList<PointSurEcran>();

		for (PointSurEcran p : points) {
			result.add(p.copie());
		}
		
//    interpolerLignes();
//    interpolerColones();

		return result;
	}

}
