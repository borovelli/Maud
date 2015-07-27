package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class FicherExport extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	private IOutil parent = null;

	private JButton expJpg = null;

	public FicherExport(IOutil p) {

		this.parent = p;

		this.setBorder(new TitledBorder("Exportations"));
		this.setLayout(new GridLayout(1, 0));

		expJpg = new JButton("Export");
		this.expJpg.addActionListener(this);
		this.add(this.expJpg);
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.expJpg){
			this.parent.exporter();
		}
		
	}
}
