/*
 * @(#)EDXRFAngularCalibration.java created Apr 11, 2007 Casalino
 *
 * Copyright (c) 2006 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is
 * provided as it is as confidential and proprietary information.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with the author.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package it.unitn.ing.rista.diffr.cal;

import it.unitn.ing.rista.diffr.*;
import it.unitn.ing.rista.util.ParameterPreferences;
import it.unitn.ing.rista.awt.JOptionsDialog;
import it.unitn.ing.rista.awt.JParameterListPane;

import java.awt.*;

/**
 * The EDXRFAngularCalibration is a class to
 *
 * @author Luca Lutterotti
 * @version $Revision: 1.00 $, $Date: Apr 11, 2007 8:16:19 AM $
 * @since JDK1.1
 */
public class EDXRFAngularCalibration extends AngularCalibration {
  public static String modelID = "XRF/EDXRF channel calibration";
  public static String descriptionID = "Channel calibration of the XRF/EDXRF detector";

  public static String[] diclistc = {"_inst_channel_calibration_zero", "_inst_channel_calibration_gain"};
  public static String[] diclistcrm = {"Energy of channel zero ", "Gain in KeV per channel "};

  public static String[] classlistc = {};
  public static String[] classlistcs = {};

  boolean refreshCalibration = true;

  double zero = 0.0;
  double gain = 0.02;

  public EDXRFAngularCalibration(XRDcat obj, String alabel) {
    super(obj, alabel);
    initXRD();
    identifier = modelID;
    IDlabel = modelID;
    description = descriptionID;
  }

  public EDXRFAngularCalibration(XRDcat afile) {
    this(afile, modelID);
  }

  public EDXRFAngularCalibration() {
    identifier = modelID;
    IDlabel = modelID;
    description = descriptionID;
  }

  public void initConstant() {
    Nstring = 0;
    Nstringloop = 0;
    Nparameter = 2;
    Nparameterloop = 0;
    Nsubordinate = 0;
    Nsubordinateloop = 0;
  }

  public void initDictionary() {
    for (int i = 0; i < totsubordinateloop; i++)
      diclist[i] = diclistc[i];
    System.arraycopy(diclistcrm, 0, diclistRealMeaning, 0, totsubordinateloop);
    for (int i = 0; i < totsubordinateloop - totsubordinate; i++)
      classlist[i] = classlistc[i];
    for (int i = 0; i < totsubordinate - totparameterloop; i++)
      classlists[i] = classlistcs[i];
  }

  public void initParameters() {
    super.initParameters();
    parameterField[0] = new Parameter(this, getParameterString(0), 0,
            ParameterPreferences.getDouble(getParameterString(0) + ".min", -10),
            ParameterPreferences.getDouble(getParameterString(0) + ".max", 10));
    parameterField[1] = new Parameter(this, getParameterString(1), 200.0,
            ParameterPreferences.getDouble(getParameterString(1) + ".min", 0.0001),
            ParameterPreferences.getDouble(getParameterString(1) + ".max", 1));

  }

  public void updateParametertoDoubleBuffering(boolean firstLoading) {
    if (getFilePar().isLoadingFile() || !isAbilitatetoRefresh)
      return;
    super.updateParametertoDoubleBuffering(firstLoading);
    zero = getParameterValue(0);
    gain = getParameterValue(1);
  }

  public boolean freeAllBasicParameters() {
    parameterField[0].setRefinableCheckBound();
    return true;
  }

  public void calibrateX(DiffrDataFile datafile) {
    int datanumber = datafile.getTotalNumberOfData();
    updateParametertoDoubleBuffering(false);
    double angcal;
    for (int i = 0; i < datanumber; i++) {
      double value = datafile.getXDataForCalibration(i);

      angcal = zero + gain * value;
//	    if (i == 0)
//	      System.out.println(i + " " + value + " " + angcal);

      datafile.setCalibratedXDataOnly(i, angcal);
    }
  }

  public double notCalibrated(DiffrDataFile datafile, double x) {
    return x;
  }

  public JOptionsDialog getOptionsDialog(Frame parent) {
    JOptionsDialog adialog = new JPolAngOptionsD(parent, this);
    return adialog;
  }

  class JPolAngOptionsD extends JOptionsDialog {

    JParameterListPane coeffPanel;

    public JPolAngOptionsD(Frame parent, XRDcat obj) {

      super(parent, obj);

      principalPanel.setLayout(new GridLayout(0, 2, 3, 3));

      addParField(principalPanel, "Zero channel (KeV): ", parameterField[0]);
      addParField(principalPanel, "Gain (KeV/channel): ", parameterField[1]);

      setTitle("Energy channel calibration");
      initParameters();

      pack();
    }

    public void initParameters() {
    }

  }


}
