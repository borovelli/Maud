package fr.ensicaen.odfplot.engine;

public class PointSurEcranSelectionnable extends PointSurEcran{
	
	private ODFPoint point = null;
	
	private boolean selectionne = false;

	public PointSurEcranSelectionnable(int x, int y, int valeur, ODFPoint p) {
		
		super(x, y, valeur);
		this.point = p;
	}

	public ODFPoint getPoint() {
		return point;
	}

	public void setPoint(ODFPoint point) {
		this.point = point;
	}

	public boolean isSelectionne() {
		return selectionne;
	}

	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
	}
	
	



}
