package fr.ensicaen.odfplot.sectionsVisualizer;


import fr.ensicaen.odfplot.graphicInterface.Filter;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import fr.ensicaen.odfplot.engine.*;

public class SectionsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private SOutil outil = null;

	private SECanvas drawer = null;

	private Controller parent = null;

	private Coupe coupe = null;

	private double trancheActuelle = 0.0;

	private SelectionneurTranche st = null;

	private FunctionODF function = null;

	public SectionsFrame(Controller p, String titre) {
		super(titre);/*, true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable
*/
    parent = p;
		function = parent.getSelectedFunction();

//		setLayout(new GridLayout(1, 0));
//		setSize(800, 600);
    setLayout(new BorderLayout(6, 6));

    coupe = Coupe.ALPHA;
		st = new SelectionneurTranche(function);

		initOutil();
		initDrawer();

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				outil, drawer);
		add(split);

    pack();

  }
	public void setTypeCouleur(Couleur t) {
		drawer.setTypeCouleur(t);
	}
	
	public void setInterpolator(boolean b) {
		drawer.setInterpolator(b);
	}

	public void setPolar(boolean b) {
		drawer.setPolar(b);
		actualiser();
	}

	private void initOutil() {
		outil = new SOutil(this, function.getParametre()
				.getMinValue(), function.getParametre().getMaxValue());
	}
	public void changeView(View v){
		
		drawer.changeView(v);
		actualiser();
	}

	private void initDrawer() {

		GenerateurPlageCouleur gen = new GenerateurPlageCouleur(function);

		drawer = new SECanvas(this, gen, function.getParametre());
		ArrayList<PointSurEcranSelectionnable> tmp = st.getTranche(coupe,
				0.0);

		// taille de l'ecran predefinie pour dimensionner le graphique.
		drawer.setMinimumSize(new Dimension(575, 572));

		if (tmp != null) {
			drawer.setListePoints(tmp);
		}

	}

	public void setMinMax(double min, double max) {
		st.setMinMax(min, max);
		actualiser();
	}

	public void exporter(){
		Image img = drawer.createImage(drawer.getWidth(), drawer
        .getHeight());
		Graphics g = img.getGraphics();
		drawer.paint(g);

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

			parent.export(fich, img);

		}
	}

	public void actualiser() {
		ArrayList<PointSurEcranSelectionnable> tmp = st.getTranche(coupe,
				trancheActuelle);
		drawer.setListePoints(tmp);
	}

	public void getTranche(double d) {
		ArrayList<PointSurEcranSelectionnable> tmp = st.getTranche(coupe, d);
		if (tmp != null) {
		drawer.setListePoints(tmp);
		trancheActuelle = d;
		}
	}

	

	public void changeCoupe(Coupe c) {
		coupe = c;
		trancheActuelle = 0.0;
		drawer.changeCoupe(c);

		ArrayList<PointSurEcranSelectionnable> tmp = st.getTranche(coupe,
				trancheActuelle);

		if (tmp != null) {
			drawer.setListePoints(tmp);
		}

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

