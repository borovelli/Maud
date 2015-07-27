package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Selection {
	
	
	private Point point1 = null;
	private Point point2 = null;
	
	public Selection(){
		point1 = new Point();
		point2 = new Point();
	}
	
	public void dessiner(Graphics g){
		g.setColor(Color.green);
		
		g.drawLine(point1.x,point1.y,point2.x, point1.y);
		g.drawLine(point1.x,point1.y,point1.x, point2.y);
		
		g.drawLine(point2.x,point1.y,point2.x, point2.y);
		g.drawLine(point1.x,point2.y,point2.x, point2.y);
		
	}

	public Point getPoint1() {
		return point1;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}
	
	
	
	private void reorganiserPoint(){
				
		if (point1.x >=point2.x ){
			int tmp = point1.x;
			this.point1.x = point2.x;
			point2.x = tmp;
		}
		if ( point1.y >= point2.y){
			int tmp = point1.y;
			this.point1.y = point2.y;
			point2.y = tmp;
		}
	}

	public Point getPoint2() {
		return point2;
		
	}

	public void setPoint2(Point point2) {
		this.point2 = point2;
	}
	
	public void setPoint2Fin(Point point2) {
		this.point2 = point2;
		this.reorganiserPoint();
	}
	
	
	
	
}
