package fr.ensicaen.odfplot.graphicInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fr.ensicaen.odfplot.engine.Controller;
import fr.ensicaen.odfplot.engine.FunctionODF;
import fr.ensicaen.odfplot.functionVisualizer.OptionsFrame;
import fr.ensicaen.odfplot.isometricVisualizer.IsometricFrame;
import fr.ensicaen.odfplot.sectionsVisualizer.SectionsFrame;
import fr.ensicaen.odfplot.functionSelector.FunctionSelector;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JMenuItem boutonQuitter, boutonCharger, boutonOdf, boutonOdfIso,
			boutonOdfSection, aPropos;

	// private FunctionODF fonctionSelectionnee;

	private JMenu menuView = new JMenu("Views");

//l	private JDesktopPane desktop = null;

//l	private Dimension resolutionEcran;

	private String titreTemp = "";

	private JMenuBar menubar = null;

	private Controller controller = null;

	private FunctionSelector selectionneurFonction = null;

	public MainWindow(String titre) {
		super(titre);

//l		resolutionEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setSize(10,10);
		this.setLocationRelativeTo(this.getParent());

		this.controller = new Controller(this);
		this.selectionneurFonction = new FunctionSelector(this.controller);
		this.creerZoneAffichage();
		this.creerBarreMenus();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	private void creerZoneAffichage() {
/*		desktop = new JDesktopPane();
		desktop.setBackground(Color.LIGHT_GRAY);

		setContentPane(desktop);*/
	}

	private void initMenuVue() {
		boutonOdf = new JMenuItem("Visualise Odf");
		boutonOdf.addActionListener(this);
		menuView.add(boutonOdf);

		boutonOdfSection = new JMenuItem("Visualise Odf Sections");
		boutonOdfSection.addActionListener(this);
		menuView.add(boutonOdfSection);

		boutonOdfIso = new JMenuItem("Visualise Odf (Isometric view)");
		boutonOdfIso.addActionListener(this);
		menuView.add(boutonOdfIso);

		menubar.add(menuView);

		menuView.setEnabled(false);
	}

	private void initMenufichier() {
		// menu 'fichier'
		JMenu menuFichier = new JMenu("File");

		// boutons charger et quitter
		boutonCharger = new JMenuItem("Load");
		boutonCharger.addActionListener(this);
		menuFichier.add(boutonCharger);

		menuFichier.addSeparator();

		boutonQuitter = new JMenuItem("Quit");
		boutonQuitter.addActionListener(this);
		menuFichier.add(boutonQuitter);

		menubar.add(menuFichier);

	}

	private void initMenupropos() {
		JMenu pro = new JMenu("?");

		this.aPropos = new JMenuItem("About ODF plot");
		this.aPropos.addActionListener(this);
		pro.add(aPropos);
		pro.add(aPropos);
		menubar.add(pro);
	}

	private void creerBarreMenus() {
		// la barre de menu
		menubar = new JMenuBar();
		setJMenuBar(menubar);

		this.initMenufichier();
		this.initMenuVue();
		this.initMenupropos();

	}

	private void charger() {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("."));
		Filter f = new Filter();
		f.addExtension("xod");
		f.setDescription("Beartex XOD");
		jfc.setFileFilter(f);

		Filter f2 = new Filter();
		f2.addExtension("par");
		f2.setDescription("Par File");
		jfc.setFileFilter(f2);

		int i = jfc.showOpenDialog(this);

		if (i == JFileChooser.APPROVE_OPTION) {
			// ouverture du fichier
			File fich = jfc.getSelectedFile();
			this.titreTemp = fich.getName();
			this.controller.identifierFichier(fich);
			this.menuView.setEnabled(true);
			// this.boutonOdf.setSelected(true);

			this.selectionneurFonction.mettreAjourFonction();

			if (!selectionneurFonction.isVisible()) {
				this.selectionneurFonction.setVisible(true);
//l				desktop.add(selectionneurFonction);

/*				try {
					selectionneurFonction.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {
				}*/
			}
		}
	}

	private void apropos() {
		JOptionPane.showMessageDialog(this, "CHATELARD Wilfried", "Author",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void quitter() {
		this.dispose();
	}

	public void afficherErreur(String mes, String titre) {
		JOptionPane.showMessageDialog(this, mes, titre,
				JOptionPane.WARNING_MESSAGE);
	}

	public void afficherMessage(String mes, String titre) {
		JOptionPane.showMessageDialog(this, mes, titre,
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void afficherOdfSection() {
		SectionsFrame frame = new SectionsFrame(controller, this.titreTemp);
		frame.setVisible(true);
//l		desktop.add(frame);

/*		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}*/

	}

	private void afficherOdf() {

		OptionsFrame frame = new OptionsFrame(controller, this.titreTemp);
		frame.setVisible(true);
//l		desktop.add(frame);

/*		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}*/

	}

	private void afficherOdfIso() {
		IsometricFrame frame = new IsometricFrame(controller, this.titreTemp);
		frame.setVisible(true);
//l		desktop.add(frame);

/*		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}*/

	}

	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		// clic sur le bouton charger
		if (source == boutonCharger) {
			this.charger();
		}
		if (source == this.aPropos) {
			this.apropos();
		}

		// clique sur le bouton Quitter.
		if (source == this.boutonQuitter) {
			this.quitter();
		}

		if (source == this.boutonOdfSection) {
			this.afficherOdfSection();
		}

		if (source == this.boutonOdf) {
			this.afficherOdf();
		}

		if (source == this.boutonOdfIso) {
			this.afficherOdfIso();
		}

	}

	public FunctionODF getFonction() {
		return this.controller.getSelectedFunction();
	}

/*	public JDesktopPane getDesktop() {
		return desktop;
	}*/

}
