package fr.ensicaen.odfplot.engine;

import java.util.ArrayList;

public class SelectionneurTranche {

	private FunctionODF function = null;

	private double min, max;

	public SelectionneurTranche(FunctionODF f) {
		this.function = f;
		min = Integer.MIN_VALUE;
		max = Integer.MAX_VALUE;
	}

	public void setMinMax(double mi, double ma) {
		this.min = mi;
		this.max = ma;
	}

	public void resetMinMax() {
		min = Integer.MIN_VALUE;
		max = Integer.MAX_VALUE;
	}

	public ArrayList<PointSurEcranSelectionnable> getTranche(Coupe coupe,
			double tranche) {

		ArrayList<PointSurEcranSelectionnable> tmp = new ArrayList<PointSurEcranSelectionnable>();

		switch (coupe) {

		case ALPHA:

			for (ODFPoint point : function.getListePoints()) {
				double valeur = point.getValeur();
				if (point.getAlpha() == tranche) {
					if (valeur > this.min && valeur < this.max) {

						tmp.add(new PointSurEcranSelectionnable((int) (point
								.getBeta()), (int) (point.getGamma()),
								(int) point.getValeur(), point));
					} else {

						tmp.add(new PointSurEcranSelectionnable((int) (point
								.getBeta()), (int) (point.getGamma()),
								(int) function.getParametre().getMinValue(),
								point));

					}
				}
			}
			break;

		case BETA:

			for (ODFPoint point : function.getListePoints()) {
				double valeur = point.getValeur();
				if (point.getBeta() == tranche) {
					if (valeur > this.min && valeur < this.max) {
						tmp.add(new PointSurEcranSelectionnable((int) (point
								.getAlpha()), (int) (point.getGamma()),
								(int) point.getValeur(), point));
					} else {

						tmp.add(new PointSurEcranSelectionnable((int) (point
								.getAlpha()), (int) (point.getGamma()),
								(int) function.getParametre().getMinValue(),
								point));

					}
				}
			}
			break;

		case GAMMA:
			for (ODFPoint point : function.getListePoints()) {
				double valeur = point.getValeur();
				if (point.getGamma() == tranche) {
					if (valeur > this.min && valeur < this.max) {
						tmp.add(new PointSurEcranSelectionnable((int) (point
								.getAlpha()), (int) (point.getBeta()),
								(int) point.getValeur(), point));
					} else {

						tmp.add(new PointSurEcranSelectionnable((int) (point
								.getAlpha()), (int) (point.getBeta()),
								(int) function.getParametre().getMinValue(),
								point));

					}
				}
			}
			break;

		default:
			break;
		}

		return tmp;
	}

	public ArrayList<PointSurEcranSelectionnable> getTrancheSuivante(
			Coupe coupe, double trancheActuelle) {

		switch (coupe) {
		case ALPHA:
			if (trancheActuelle < function.getParametre().getAlphaMax())
				return this.getTranche(coupe, trancheActuelle
						+ function.getParametre().getAlphaStep());
			else
				return null;

		case BETA:
			if (trancheActuelle < function.getParametre().getBetaMax())
				return this.getTranche(coupe, trancheActuelle
						+ function.getParametre().getBetaStep());
			else
				return null;

		case GAMMA:
			if (trancheActuelle < function.getParametre().getGammaMax())
				return this.getTranche(coupe, trancheActuelle
						+ function.getParametre().getGammaStep());
			else
				return null;

		default:
			return null;
		}
	}

	public ArrayList<PointSurEcranSelectionnable> getTranchePrecedente(
			Coupe coupe, double trancheActuelle) {

		switch (coupe) {
		case ALPHA:
			if (trancheActuelle > function.getParametre().getAlphaMin())
				return this.getTranche(coupe, trancheActuelle
						- function.getParametre().getAlphaStep());
			else
				return null;

		case BETA:
			if (trancheActuelle > function.getParametre().getBetaMin())
				return this.getTranche(coupe, trancheActuelle
						- function.getParametre().getBetaStep());
			else
				return null;

		case GAMMA:
			if (trancheActuelle > function.getParametre().getGammaMin())
				return this.getTranche(coupe, trancheActuelle
						- function.getParametre().getGammaStep());
			else
				return null;

		default:
			return null;
		}
	}

}
