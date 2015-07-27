package fr.ensicaen.odfplot.functionVisualizer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.ensicaen.odfplot.engine.*;

public class OptionsFrame extends JFrame implements ChangeListener,
		ActionListener {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private JTextField textMax, textMin;
  private JSlider slidMax = null, slidMin = null;
  private FunctionODF function = null;
  private JTextField tnbPoint = null;
	private int nbPoint = 0;
  private LinkedList<ODFPoint> listePoints = new LinkedList<ODFPoint>();

	public OptionsFrame(Controller parent, String titre) {
		super("Threshold : " + titre);

    function = parent.getSelectedFunction();
		listePoints = function.getListePoints();

		setSize(250, 250);

    double min = function.getParametre().getMinValue();
    double max = function.getParametre().getMaxValue();

    GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		setLayout(gridbag);

		tnbPoint = new JTextField("Points Number : " + nbPoint);
		tnbPoint.setEditable(false);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(tnbPoint, c);

    JLabel labelMin = new JLabel("Min : ");
    c.gridx = 0;
		c.gridy = 1;
		add(labelMin, c);

		textMin = new JTextField(Double.toString(min));
		c.gridx = 1;
		c.gridy = 1;
		add(textMin, c);

		slidMin = new JSlider(JSlider.HORIZONTAL, (int) (min * 100),
				(int) (max * 100), 0);
		slidMin.setMajorTickSpacing(2000);
		slidMin.setPaintTicks(true);
		slidMin.setValue((int) (min * 100));
		slidMin.addChangeListener(this);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		add(slidMin, c);

    JLabel labelMax = new JLabel("Max :");
    c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		add(labelMax, c);

		textMax = new JTextField(Double.toString(max));
		c.gridx = 1;
		c.gridy = 3;
		add(textMax, c);

		slidMax = new JSlider(JSlider.HORIZONTAL, (int) (min * 100),
				(int) (max * 100), 0);
		slidMax.setMajorTickSpacing(2000);
		slidMax.setPaintTicks(true);
		slidMax.setValue((int) (max * 100));
		slidMax.addChangeListener(this);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		add(slidMax, c);

    JButton actualiser = new JButton("Launch");
    actualiser.addActionListener(this);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 5;
		add(actualiser, c);

	}

	private void afficherOdf(double min, double max) {
		Frame3D frame = new Frame3D(function, getTitle(), min, max);
		frame.setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();

		nbPoint = 0;
		for (ODFPoint p : listePoints) {
			if (p.getValeur() > Double.parseDouble(textMin.getText())
					&& p.getValeur() < Double.parseDouble(textMax
							.getText())) {
				nbPoint++;
			}
		}
		tnbPoint.setText("Points Number : " + nbPoint);

		if (source == slidMax) {
			textMax.setText(Double
					.toString(slidMax.getValue() / 100.00));
			if (slidMax.getValue() < slidMin.getValue()) {
				slidMin.setValue(slidMax.getValue());
			}
		}

		if (source == slidMin) {
			textMin.setText(Double
					.toString(slidMin.getValue() / 100.00));
			if (slidMin.getValue() > slidMax.getValue()) {
				slidMax.setValue(slidMin.getValue());
			}
		}

	}

	public void actionPerformed(ActionEvent e) {
			afficherOdf(Double.parseDouble(textMin.getText()), Double
					.parseDouble(textMax.getText()));
	}
}
