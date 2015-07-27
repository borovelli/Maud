package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fr.ensicaen.odfplot.engine.Couleur;


public class IOptionCouleur extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IOutil parent = null;

	private JComboBox type = null;

	private String[] typeStrings = { "Colored", "GrayScaled", "Inverted GrayScaled" };

	public IOptionCouleur(IOutil p) {
		this.parent = p;

		this.setBorder(new TitledBorder("View Options"));

		this.setLayout(new GridLayout(1, 0));

		this.type = new JComboBox(typeStrings);
		type.addActionListener(this);

		this.add(this.type);

	}

	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String colName = (String) cb.getSelectedItem();

		if (colName.equals("GrayScaled")) {
			this.parent.setTypeCouleur(Couleur.NOIR_BLANC);
		}
		
		if (colName.equals("Inverted GrayScaled")) {
			this.parent.setTypeCouleur(Couleur.BLANC_NOIR);
		}

		if (colName.equals("Colored")) {
			this.parent.setTypeCouleur(Couleur.COULEUR);
		}

	}

}
