package fr.ensicaen.odfplot.engine;

public class PointSurEcran implements Comparable {

	protected int x, y;

	protected double valeur;

	private Tri tri = null;

	public PointSurEcran copie() {
		return new PointSurEcran(this.x, this.y, this.valeur);
	}

	public PointSurEcran(int x, int y, double valeur) {
		this.x = x;
		this.y = y;

		this.valeur = valeur;
	}

	public String toString() {
		return "x=" + this.x + " y=" + this.y + " valeur=" + this.valeur;
	}

	public double getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int compareTo(Object autre) {
		int nombre1 = 0;
		int nombre2 = 0;

		switch (tri) {
		case X:
			nombre1 = ((PointSurEcran) autre).getX();
			nombre2 = this.getX();
			if (nombre1 > nombre2)
				return -1;
			else if (nombre1 == nombre2)
				return 0;
			else
				return 1;

		case Y:
			nombre1 = ((PointSurEcran) autre).getY();
			nombre2 = this.getY();
			if (nombre1 > nombre2)
				return -1;
			else if (nombre1 == nombre2)
				return 0;
			else
				return 1;

		default:
			return 0;

		}

	}

	public void setTri(Tri tri) {
		this.tri = tri;
	}

	public PointSurEcran estAudessus(int x, int y) {

		if (Math.abs(this.x - x) < 3 && Math.abs(this.y - y) < 3)
			return this;
		else
			return null;

	}

}
