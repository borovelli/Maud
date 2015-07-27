package fr.ensicaen.odfplot.functionSelector;

import java.awt.GridLayout;

import javax.swing.*;

import fr.ensicaen.odfplot.engine.Controller;
import fr.ensicaen.odfplot.engine.FunctionODF;

public class FunctionSelector extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller controller = null;

	private FSPanel ecran = null;

	public FunctionSelector(Controller c) {
		super("Function selector");/*, true, // resizable
				false, // closable
				false, // maximizable
				true);// iconifiable
*/
		this.controller = c;
		this.ecran = new FSPanel(this);

		this.setSize(200, 500);
		this.setLayout(new GridLayout(1,0));
		this.add(ecran);

	}
	
	public void selectionnerFonction(FunctionODF f){
		this.controller.setFonctionSelectionnee(f);
	}

	public void mettreAjourFonction() {

		this.ecran.mettreAjourFonction(this.controller.getListeFonctions());
	}
	
	public void ajouterFonction(FunctionODF f){
		this.ecran.ajouterFonction(f);
	}

}
