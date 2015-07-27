package fr.ensicaen.odfplot.sectionsVisualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.FunctionODF;
import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;

public class SEchelle {

	private SECanvas ecran = null;
	
	private double pointValeur = 1.0;

	private FunctionODF function = null;

	private Point position = null;

	private GenerateurPlageCouleur genCouleur = null;
	
	private Couleur typeCouleur = Couleur.COULEUR;

	public SEchelle(SECanvas e, FunctionODF f) {
		this.ecran = e;
		this.function = f;
		position = new Point(ecran.getWidth() - 150, 25);
		genCouleur = new GenerateurPlageCouleur(function);
	}

	public void dessineDegrade(Graphics g) {

		/*
		 * on definit les minimum est maximum de la function ODF et des
		 * coordonees a l'ecran sur l'echelle.
		 */
		double ymin = this.function.getParametre().getMinValue();
		double ymax = this.function.getParametre().getMaxValue();

		double xmin = this.position.y;
		double xmax = ecran.getHeight() - 100 + this.position.y;

		/*
		 * on determine une focntion afine qui nous permettra de passer des
		 * coordonnees sur l'axe de l'echelle au valeurs de la function ODF.
		 */

		double a = (ymax - ymin) / (xmax - xmin);

		double b = ymax - (a * xmax);

		/*
		 * l'axe des y est inverser, on balaye donc de la valeur maximum a la
		 * valeur minimum.
		 */
		for (int i = (int) xmin; i <= (int) xmax; i++) {

			/*
			 * on affiche e chaque pixel de l'axe de l'echelle, la valeur
			 * coorespondant a celle de la function. On affiche la couleur qui
			 * coorespond.
			 */

			/*
			 * determinason de la couleur.
			 */
			Color c;
			switch(typeCouleur){
			case NOIR_BLANC:
				c = this.genCouleur.rechercherNoirBlanc(i * a + b);
				g.setColor(c);
				break;
			case BLANC_NOIR:
				c = this.genCouleur.rechercherBlancNoir(i * a + b);
				g.setColor(c);
				break;
			case COULEUR:
				c = this.genCouleur.doubleVersCouleur(i * a + b);
				g.setColor(c);
				break;
			}

			g.drawLine(this.position.x, ((int) xmax - i + (int) xmin),
					this.position.x + 50, (int) xmax - i + (int) xmin);
		}

	}

	public void dessineUnMRD(Graphics g) {
		double xmin = this.function.getParametre().getMinValue();
		double xmax = this.function.getParametre().getMaxValue();

		double ymin = this.position.y;
		double ymax = ecran.getHeight() - 100 + this.position.y;

		/*
		 * on determine une function afine qui nous permettra de passer des
		 * coordonnees sur l'axe de l'echelle au valeurs de la function ODF.
		 */

		double a = (ymax - ymin) / (xmax - xmin);
		double b = ymax - (a * xmax);

		int coord = (int) Math.round(a + b);
		
		g.setColor(Color.green);
		g.drawLine(this.position.x, ((int) ymax - coord + (int) ymin),
				this.position.x + 50, (int) ymax - coord + (int) ymin);
		g.drawString("1 mrd", this.position.x + 60, (int) ymax - coord
				+ (int) ymin);
		
		
		
		int point = (int) Math.round((a * pointValeur) + b);
		
		int i = -1;
		g.setColor(Color.red);
		g.drawLine(this.position.x, ((int) ymax - point + (int) ymin+i),
				this.position.x + 50, (int) ymax - point + (int) ymin+i);
		
		i++;
		g.setColor(Color.green);
		g.drawLine(this.position.x, ((int) ymax - point + (int) ymin+i),
				this.position.x + 50, (int) ymax - point + (int) ymin+i);
		
		i++;
		g.setColor(Color.blue);
		g.drawLine(this.position.x, ((int) ymax - point + (int) ymin+i),
				this.position.x + 50, (int) ymax - point + (int) ymin+i);
		
		g.setColor(Color.green);
		g.drawString(pointValeur + " mrd", this.position.x + 60, (int) ymax - point
				+ (int) ymin);
		
	}

	public void draw(Graphics g) {

		position = new Point(ecran.getWidth() - 150, 10);

		this.dessineDegrade(g);
		this.dessineUnMRD(g);
		/*
		 * dessin du texte de la legende. valeur minimum valeur maximum le 1
		 * mrd.
		 */
		g.setColor(Color.green);
		g.drawRect(this.position.x, this.position.y, 50,
				ecran.getHeight() - 100);

		g
				.drawString(Double.toString(function.getParametre()
						.getMinValue()), this.position.x + 60, this.position.y
						+ ecran.getHeight() - 100);

		g.drawString(Double.toString(function.getParametre().getMaxValue()),
				this.position.x + 60, this.position.y);

	}

	public void setTypeCouleur(Couleur typeCouleur) {
		this.typeCouleur = typeCouleur;
	}

	public void setPointValeur(double pointValeur) {
		this.pointValeur = pointValeur;
	}

}
