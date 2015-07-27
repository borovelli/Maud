package fr.ensicaen.odfplot.functionVisualizer;

import javax.swing.*;

import fr.ensicaen.odfplot.engine.FunctionODF;

public class Frame3D extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * variable d'intance
	 */
	private Graph3D graphe = null;

	private FunctionODF function = null;

	

	/*
	 * constructeur
	 */

	public Frame3D(FunctionODF f, String titre, double min, double max) {
		super(titre); /*, true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable
		*/
		this.function = f;

		// this.setLayout(new GridLayout(1, 0));
		this.setSize(800, 600);

		graphe = new Graph3D(this, min, max);

		this.add(graphe);

	}

	public FunctionODF getFonction() {
		return function;
	}

	/*
	 * methode
	 */

}
