package fr.ensicaen.odfplot.graphicInterface;

import java.awt.Graphics;


public class Legend {
	
	private ECanvas parent = null;
	
	private int x,y , h, l;
	
	public void dessinne(Graphics g){
		
	}
	
	public void setBounds ( int x, int y , int l, int h){
		this.x = x;
		this.y = y ;
		this.h = h;
		this.l = l;
	}

}
