package fr.ensicaen.odfplot.engine;

import java.awt.Color;
import java.util.HashMap;

public class GenerateurPlageCouleur {

	/**
	 * cette classe genere des objets contenant toutes les couleurs affichable
	 * dans le graphe.
	 */

	/*
	 * Variable d'instance
	 */
	private HashMap<Double, Color> couleurs = null;

	private FunctionODF function = null;

	/*
	 * constructeur
	 */
	public GenerateurPlageCouleur(FunctionODF f) {
		couleurs = new HashMap<Double, Color>();
		this.function = f;

	}

	public Color doubleVersCouleur(double d) {

		double max;
		double valeur;

		if (this.function.getParametre().getMinValue() < 0) {

			valeur = d + Math.abs(this.function.getParametre().getMinValue());
			max = this.function.getParametre().getMaxValue()
					+ Math.abs(this.function.getParametre().getMinValue());

		} else {

			valeur = d;
			max = this.function.getParametre().getMaxValue();
		}

		valeur = valeur * (255 * 5) / max;
		int valInt = (int) Math.round(valeur);
		int swi = valInt / 255;
		Color cou;
		int comp = valInt - (255 * swi);

		switch (swi) {
		case 0:
			cou = new Color(0, 0, comp);
			break;
		case 1:
			cou = new Color(0, comp, 255);
			break;
		case 2:
			cou = new Color(0, 255, 255 - comp);
			break;
		case 3:
			cou = new Color(comp, 255, 0);
			break;
		case 4:
			cou = new Color(255, 255 - comp, 0);
			break;
		case 5:
			cou = new Color(255, 0, 0);
			break;
		default:
			cou = new Color(0, 0, 0);

		}
		return cou;

	}

	public Color rechercherBlancNoir(double v) {
		double tmp;
		double max;

		if (this.function.getParametre().getMinValue() < 0) {

			tmp = v + Math.abs(this.function.getParametre().getMinValue());
			max = this.function.getParametre().getMaxValue()
					+ Math.abs(this.function.getParametre().getMinValue());

		} else {

			tmp = v;
			max = this.function.getParametre().getMaxValue();
		}

		tmp = tmp * 255 / max;
		int c = (int) Math.round(tmp);
		c = 255 - c;
		return new Color(c, c, c);
	}

	public Color rechercherNoirBlanc(double v) {

		double tmp;
		double max;

		if (this.function.getParametre().getMinValue() < 0) {

			tmp = v + Math.abs(this.function.getParametre().getMinValue());
			max = this.function.getParametre().getMaxValue()
					+ Math.abs(this.function.getParametre().getMinValue());

		} else {

			tmp = v;
			max = this.function.getParametre().getMaxValue();
		}

		tmp = tmp * 255 / max;
		int c = (int) Math.round(tmp);
		return new Color(c, c, c);

	}

	/*
	 * Getter
	 */

	public Color getCouleurs(double i) {
		return this.couleurs.get(i);
	}

	public int getTaille() {
		return this.couleurs.size();
	}

}
