package fr.ensicaen.odfplot.engine;

public enum Coupe {

	ALPHA, BETA, GAMMA;

	private Coupe() {
	}

	public Coupe alpha() {

		return Coupe.ALPHA;
	}

	public Coupe beta() {

		return Coupe.BETA;
	}

	public Coupe gamma() {

		return Coupe.GAMMA;
	}

}
