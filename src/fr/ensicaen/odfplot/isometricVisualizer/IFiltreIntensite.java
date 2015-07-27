package fr.ensicaen.odfplot.isometricVisualizer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class IFiltreIntensite extends JPanel implements ChangeListener,
		ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textMax, textMin;

	private JLabel labelMax, labelMin;

	private JSlider slidMax = null, slidMin = null;

	private JButton actualiser = null;

	private IOutil parent = null;

	public IFiltreIntensite(IOutil p, double min, double max) {
		this.parent = p;

		this.setBorder(new TitledBorder("Tools"));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setLayout(gridbag);

		this.labelMin = new JLabel("Min : ");
		c.gridx = 0;
		c.gridy = 0;
		this.add(this.labelMin, c);

		this.textMin = new JTextField(Double.toString(min));
		c.gridx = 1;
		c.gridy = 0;
		this.add(this.textMin, c);

		slidMin = new JSlider(JSlider.HORIZONTAL, (int)(min*100), (int)(max*100), 0);
		slidMin.setMajorTickSpacing(2000);
		slidMin.setPaintTicks(true);
		slidMin.setValue((int)(min*100));
		slidMin.addChangeListener(this);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		this.add(this.slidMin, c);

		this.labelMax = new JLabel("Max :");
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		this.add(this.labelMax, c);

		this.textMax = new JTextField(Double.toString(max));
		c.gridx = 1;
		c.gridy = 2;
		this.add(this.textMax, c);

		slidMax = new JSlider(JSlider.HORIZONTAL,(int)(min*100), (int)(max*100), 0);
		slidMax.setMajorTickSpacing(2000);
		slidMax.setPaintTicks(true);
		slidMax.setValue((int)(max*100));
		slidMax.addChangeListener(this);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 3;
		this.add(this.slidMax, c);

		this.actualiser = new JButton("Update");
		this.actualiser.addActionListener(this);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		this.add(this.actualiser, c);

	}

	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source == this.slidMax) {
			this.textMax.setText(Double
					.toString(this.slidMax.getValue() / 100.00));
			if (this.slidMax.getValue() < this.slidMin.getValue()) {
				this.slidMin.setValue(this.slidMax.getValue());
			}

		}

		if (source == this.slidMin) {
			this.textMin.setText(Double
					.toString(this.slidMin.getValue() / 100.00));
			if (this.slidMin.getValue() > this.slidMax.getValue()) {
				this.slidMax.setValue(this.slidMin.getValue());
			}
		}

	}

	public void actionPerformed(ActionEvent e) {
		this.slidMax
				.setValue((int) (Double.parseDouble(this.textMax.getText()) * 100));
		this.slidMin
				.setValue((int) (Double.parseDouble(this.textMin.getText()) * 100));

		this.parent.setMinMax(Double.parseDouble(this.textMin.getText()),
				Double.parseDouble(this.textMax.getText()));

	}

}
