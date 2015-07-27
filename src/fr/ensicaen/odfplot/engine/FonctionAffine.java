package fr.ensicaen.odfplot.engine;

public class FonctionAffine {
	
	private double coefDir = 0.0;
	private int b= 0;
	
	public int applique (int x){
		return (int) this.coefDir*x + this.b;
	}
	
	
	public FonctionAffine(){
	
	}


	public int getB() {
		return b;
	}


	public void setB(int b) {
		this.b = b;
	}


	public double getCoefDir() {
		return coefDir;
	}


	public void setCoefDir(double coefDir) {
		this.coefDir = coefDir;
	}
}
