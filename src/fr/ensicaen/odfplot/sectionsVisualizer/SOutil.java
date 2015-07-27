package fr.ensicaen.odfplot.sectionsVisualizer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.TitledBorder;

import fr.ensicaen.odfplot.engine.Couleur;
import fr.ensicaen.odfplot.engine.Coupe;
import fr.ensicaen.odfplot.engine.Parametre;
import fr.ensicaen.odfplot.engine.View;

public class SOutil extends JPanel {

	private SectionsFrame parent = null;

	private static final long serialVersionUID = 1L;

  private SoChangetranche changeTranche = null;

  public SOutil(SectionsFrame p, double min, double max) {
		parent = p;

    SoChangeCoupe changeCoupe = new SoChangeCoupe();
    changeTranche = new SoChangetranche();
    SoFiltreIntensite filtreIntensite = new SoFiltreIntensite(min, max);
    SoOptionVuePolaire optionAffichage = new SoOptionVuePolaire();
    SOOptionCouleur optionCouleur = new SOOptionCouleur();
    FicherExport export = new FicherExport();

    GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
//
//
// 		c.fill = GridBagConstraints.HORIZONTAL;
		setLayout(gridbag);
		
		c.gridx = 0;
		c.gridy = 0;
		add(export, c);

		c.gridx = 0;
		c.gridy = 1;
		add(optionAffichage, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(optionCouleur, c);

		c.gridx = 0;
		c.gridy = 3;
		add(changeCoupe, c);

		c.gridx = 0;
		c.gridy = 4;
		add(changeTranche, c);

		c.gridx = 0;
		c.gridy = 5;
		add(filtreIntensite, c);

	}
	public void setTypeCouleur(Couleur t) {
		parent.setTypeCouleur(t);
		
	}
	
	public void exporter(){
		parent.exporter();
	}

	public void changeVue(View v) {
		parent.changeView(v);
	}

	public void setInterpoler(boolean b) {
		parent.setInterpolator(b);
	}

	public void setPolaire(boolean b) {
		parent.setPolar(b);
	}

	public void setMinMax(double min, double max) {
		parent.setMinMax(min, max);
	}

	public Parametre getParametre() {

		return parent.getFonction().getParametre();
	}

	public Coupe getCoupe() {
		return parent.getCoupe();
	}
	
	public void getTranche(double d){
		parent.getTranche(d);
	}

	public void setTranche(double d) {
		parent.getTranche(d);
	}

	public void changeCoupe(Coupe p) {
		parent.changeCoupe(p);
		changeTranche.mettreAJourItem();
	}

  class SoChangeCoupe extends JPanel implements ActionListener {

    private String[] planStrings = { "Alpha", "Beta", "Gamma", };

    public SoChangeCoupe() {
      setBorder(new TitledBorder("Section"));
      setLayout(new GridLayout(1, 0));

      initCombobox();

    }

    private void initCombobox() {
      JComboBox listePlan = new JComboBox(planStrings);
      listePlan.addActionListener(this);
      add(listePlan);
    }

    public void actionPerformed(ActionEvent e) {
      JComboBox cb = (JComboBox) e.getSource();
      String planName = (String) cb.getSelectedItem();

      if (planName.equals("Alpha")) {
        changeCoupe(Coupe.ALPHA);
      }

      if (planName.equals("Beta")) {
        changeCoupe(Coupe.BETA);
      }

      if (planName.equals("Gamma")) {
        changeCoupe(Coupe.GAMMA);
      }

    }
  }

  class SoChangetranche extends JPanel implements ActionListener {

    private JButton avant, arriere;

    private JComboBox valPlan = null;

    private JTextField planActuel = null;

    private String contenuTPlanactuel = " Alpha = ";

    public SoChangetranche() {
      setBorder(new TitledBorder("Tools"));

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      setLayout(gridbag);

      avant = new JButton("Next");
      avant.addActionListener(this);
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 0;
      add(avant, c);

      planActuel = new JTextField(contenuTPlanactuel);
      planActuel.setEditable(false);
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 1;
      add(planActuel, c);

      valPlan = new JComboBox();
      mettreAJourItem();
      valPlan.addActionListener(this);
      c.gridx = 1;
      c.gridy = 1;
      add(valPlan, c);

      arriere = new JButton("Previous");
      arriere.addActionListener(this);
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 2;
      add(arriere, c);
    }

    public void mettreAJourItem() {

      Parametre p = getParametre();
      valPlan.removeAllItems();

      switch (parent.getCoupe()) {
      case ALPHA:
        for (double i = p.getAlphaMin(); i < p.getAlphaMax(); i += p
            .getAlphaStep()) {
          valPlan.addItem(i);
        }
        contenuTPlanactuel = " Alpha = ";
        planActuel.setText(contenuTPlanactuel);
        break;
      case BETA:

        for (double i = p.getBetaMin(); i < p.getBetaMax(); i += p
            .getBetaStep()) {
          valPlan.addItem(i);
        }
        contenuTPlanactuel = " Beta = ";
        planActuel.setText(contenuTPlanactuel);
        break;
      case GAMMA:

        for (double i = p.getGammaMin(); i < p.getGammaMax(); i += p
            .getGammaStep()) {
          valPlan.addItem(i);
        }
        contenuTPlanactuel = " Gamma = ";
        planActuel.setText(contenuTPlanactuel);
        break;
      default:

      }

    }

    private void avancer() {
      int tmp = valPlan.getSelectedIndex() + 1;
      if (tmp < valPlan.getItemCount()) {
        valPlan.setSelectedIndex(tmp);
        getTranche((Double)valPlan.getSelectedItem());
      }
    }

    private void reculer() {
      int tmp = valPlan.getSelectedIndex() - 1;
      if (tmp > -1) {
        valPlan.setSelectedIndex(tmp);
        getTranche((Double)valPlan.getSelectedItem());
      }
    }

    public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();

      if (source == valPlan) {
        if (valPlan.getItemCount() > 0) {
          Double tmp = (Double) valPlan.getSelectedItem();
          setTranche(tmp);
        }
      }

      if (source == avant)
        avancer();

      if (source == arriere)
        reculer();

    }

  }

  class SoFiltreIntensite extends JPanel implements ChangeListener,
      ActionListener {

    private JTextField textMax, textMin;

    private JSlider slidMax = null, slidMin = null;

    public SoFiltreIntensite(double min, double max) {
     setBorder(new TitledBorder("Tools"));

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      setLayout(gridbag);

      JLabel labelMin = new JLabel("Min : ");
      c.gridx = 0;
      c.gridy = 0;
      add(labelMin, c);

      textMin = new JTextField(Double.toString(min));
      c.gridx = 1;
      c.gridy = 0;
      add(textMin, c);

      slidMin = new JSlider(JSlider.HORIZONTAL, (int)(min*100), (int)(max*100), 0);
      slidMin.setMajorTickSpacing(2000);
      slidMin.setPaintTicks(true);
      slidMin.setValue((int)(min*100));
      slidMin.addChangeListener(this);
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 1;
      add(slidMin, c);

      JLabel labelMax = new JLabel("Max :");
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 2;
      add(labelMax, c);

      textMax = new JTextField(Double.toString(max));
      c.gridx = 1;
      c.gridy = 2;
      add(textMax, c);

      slidMax = new JSlider(JSlider.HORIZONTAL,(int)(min*100), (int)(max*100), 0);
      slidMax.setMajorTickSpacing(2000);
      slidMax.setPaintTicks(true);
      slidMax.setValue((int)(max*100));
      slidMax.addChangeListener(this);
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 3;
      add(slidMax, c);

      JButton actualiser = new JButton("Update");
      actualiser.addActionListener(this);
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 4;
      add(actualiser, c);

    }

    public void stateChanged(ChangeEvent e) {
      Object source = e.getSource();
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
      slidMax
          .setValue((int) (Double.parseDouble(textMax.getText()) * 100));
      slidMin
          .setValue((int) (Double.parseDouble(textMin.getText()) * 100));

      setMinMax(Double.parseDouble(textMin.getText()),
          Double.parseDouble(textMax.getText()));

    }

  }

  class SOOptionCouleur extends JPanel implements ActionListener {

    private String[] typeStrings = { "Colored", "GrayScaled", "Inverted GrayScaled" };

    public SOOptionCouleur() {

      setBorder(new TitledBorder("View Options"));

      setLayout(new GridLayout(1, 0));

      JComboBox type = new JComboBox(typeStrings);
      type.addActionListener(this);

      add(type);

    }

    public void actionPerformed(ActionEvent e) {
      JComboBox cb = (JComboBox) e.getSource();
      String colName = (String) cb.getSelectedItem();

      if (colName.equals("GrayScaled")) {
        setTypeCouleur(Couleur.NOIR_BLANC);
      }

      if (colName.equals("Inverted GrayScaled")) {
        setTypeCouleur(Couleur.BLANC_NOIR);
      }

      if (colName.equals("Colored")) {
        setTypeCouleur(Couleur.COULEUR);
      }

    }

  }

  class FicherExport extends JPanel implements ActionListener{

    private JButton expJpg = null;

    public FicherExport() {
      setBorder(new TitledBorder("Exportations"));
      setLayout(new GridLayout(1, 0));

      expJpg = new JButton("Export");
      expJpg.addActionListener(this);
      add(expJpg);

    }

    public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      if (source == expJpg){
        exporter();
      }

    }
  }

  class SoOptionVuePolaire extends JPanel implements ActionListener,
      ItemListener {
    /**
     *
     */
    private String[] typeStrings = { "Cartesian", "Polar: equal area/Schmidt",
        "Polar: stereographic/Wulff" };

    private JCheckBox interpole = null;

    public SoOptionVuePolaire() {
      setBorder(new TitledBorder("View Options"));

      setLayout(new GridLayout(1, 0));

      interpole = new JCheckBox("Interpolate");
      interpole.setSelected(true);
      interpole.addItemListener(this);
      add(interpole);

      JComboBox type = new JComboBox(typeStrings);
      type.addActionListener(this);

      add(type);

    }

    private void switchInterpoler() {
      if (interpole.isSelected()) {
        setInterpoler(true);
      } else {
        setInterpoler(false);
      }
    }

    public void actionPerformed(ActionEvent e) {

      JComboBox cb = (JComboBox) e.getSource();
      String planName = (String) cb.getSelectedItem();

      if (planName.equals(typeStrings[0])) {
        changeVue(View.STANDARD);
      }

      if (planName.equals(typeStrings[1])) {
        changeVue(View.EQUAL_AREA_SCHMIDT);
      }

      if (planName.equals(typeStrings[2])) {
        changeVue(View.STEREOGRAPHIC_WULFF);
      }

    }

    public void itemStateChanged(ItemEvent e) {
      if (e.getSource() == interpole)
        switchInterpoler();
    }

  }
}
