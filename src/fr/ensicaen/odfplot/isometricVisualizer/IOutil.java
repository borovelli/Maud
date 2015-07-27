package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.Coupe;
import fr.ensicaen.odfplot.engine.Parametre;
import fr.ensicaen.odfplot.engine.View;

public class IOutil extends JPanel {

	private IsometricFrame parent = null;

	private static final long serialVersionUID = 1L;


	

	private IFiltreIntensite filtreIntensite = null;

	
	
	private IOptionCouleur optionCouleur = null;
	
	private FicherExport export = null;

	public IOutil(IsometricFrame p, double min, double max) {
		this.parent = p;

		
		
		this.filtreIntensite = new IFiltreIntensite(this, min, max);
		
		this.optionCouleur = new IOptionCouleur(this);
		this.export = new FicherExport(this);
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setLayout(gridbag);
		
		c.gridx = 0;
		c.gridy = 0;
		this.add(this.export, c);

		
		
		c.gridx = 0;
		c.gridy = 2;
		this.add(this.optionCouleur, c);

	


		c.gridx = 0;
		c.gridy = 5;
		this.add(this.filtreIntensite, c);

	}
	public void setTypeCouleur(Couleur t) {
		this.parent.setTypeCouleur(t);
		
	}
	
	public void exporter(){
		this.parent.exporter();
	}

	public void changeVue(View v) {
		this.parent.changeVue(v);
	}


	public void setMinMax(double min, double max) {
		this.parent.setMinMax(min, max);
	}

	public Parametre getParametre() {

		return this.parent.getFonction().getParametre();
	}

	public Coupe getCoupe() {
		return this.parent.getCoupe();
	}

}
