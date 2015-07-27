package fr.ensicaen.odfplot.isometricVisualizer;


import fr.ensicaen.odfplot.graphicInterface.Filter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.*;

import fr.ensicaen.odfplot.engine.*;

public class IsometricFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private IOutil outil = null;

	private IECanvas ecran = null;

	private Controller parent = null;

	private Coupe coupe = null;

	private double trancheActuelle = 0.0;

	private SelectionneurTranche st = null;

	private FunctionODF function = null;

	public IsometricFrame(Controller p, String titre) {
		super(titre);/*, true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable
*/
    parent = p;
		function = parent.getSelectedFunction();

		this.setLayout(new GridLayout(1, 0));
		this.setSize(800, 600);

		this.coupe = Coupe.ALPHA;
		this.st = new SelectionneurTranche(function);

		this.initOutil();
		this.initEcran();

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				this.outil, this.ecran);
		this.add(split);

	}
	public void setTypeCouleur(Couleur t) {
		this.ecran.setTypeCouleur(t);
	}
	

	private void initOutil() {
		this.outil = new IOutil(this, this.function.getParametre()
				.getMinValue(), this.function.getParametre().getMaxValue());
	}
	public void changeVue(View v){
		
		this.ecran.changeVue(v);
		this.ecran.repaint();
	}

	private void initEcran() {

		GenerateurPlageCouleur gen = new GenerateurPlageCouleur(this.function);

		this.ecran = new IECanvas(this, gen, this.function.getParametre());
		

		// taille de l'ecran predefinie pour dimensionner le graphique.
		this.ecran.setSize(new Dimension(575, 572));

	}

	public void setMinMax(double min, double max) {
		st.setMinMax(min, max);
		this.ecran.repaint();
	}

	public void exporter(){
		Image img = this.ecran.createImage(ecran.getWidth(), ecran
				.getHeight());
		Graphics g = img.getGraphics();
		this.ecran.paint(g);

		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("."));
		Filter f = new Filter();
		f.addExtension("jpg");
		f.setDescription("Image Jpeg");
		jfc.setFileFilter(f);

		int returnValue = jfc.showSaveDialog(this);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// exportation du fichier
			File fich = jfc.getSelectedFile();

			this.parent.export(fich, img);

		}
	}

  public void exporter(File f, Image i) {
    parent.export(f, i);
  }

	public FunctionODF getFonction() {
		return function;
	}

	public Coupe getCoupe() {
		return coupe;
	}
	public void setCoupe(Coupe coupe) {
		this.coupe = coupe;
	}

}
