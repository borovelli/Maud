/*
 * @(#)GeometryTOFGeneric.java created 26/01/2000 Mesiano
 *
 * Copyright (c) 2000 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is
 * provided as it is as confidential and proprietary information.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Luca Lutterotti.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package it.unitn.ing.rista.diffr.geometry;

import java.lang.*;

import it.unitn.ing.rista.diffr.*;
import it.unitn.ing.rista.awt.*;
import it.unitn.ing.rista.util.*;

import java.awt.*;
import javax.swing.*;

/**
 *  The GeometryTOFGeneric is a class
 *
 *
 * @version $Revision: 1.6 $, $Date: 2004/08/12 09:36:07 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */

public class GeometryTOFGeneric extends GeometryDebyeScherrer {

  public GeometryTOFGeneric(XRDcat aobj, String alabel) {
    super(aobj, alabel);
    identifier = "Generic TOF";
    IDlabel = "Generic TOF";
    description = "Generic TOF instrument geometry";
  }

  public GeometryTOFGeneric(XRDcat aobj) {
    this(aobj, "Generic TOF");
  }

  public GeometryTOFGeneric() {
    identifier = "Generic TOF";
    IDlabel = "Generic TOF";
    description = "Generic TOF instrument geometry";
  }

  public double[] getTextureAngles(DiffrDataFile datafile, double[] tilting_angles,
                                  double[] sampleAngles, double twotheta) {

    // tilting_angles[0] = Omega
    // tilting_angles[1] = Chi
    // tilting_angles[2] = Phi
// tilting_angles[3] = Eta

    double[] newtilting_angles = new double[4];

    newtilting_angles[0] = twotheta / 2.0f;
    newtilting_angles[1] = tilting_angles[1];
    newtilting_angles[2] = tilting_angles[2];
    newtilting_angles[3] = tilting_angles[3];

//		double newTwoTheta = 0.0f;

    return super.getTextureAngles(datafile, newtilting_angles, sampleAngles, twotheta);

  }

  public double[][] getIncidentAndDiffractionAngles(DiffrDataFile datafile, double[] tilting_angles,
                                                   double[] sampleAngles, double[] position) {
    double tilting_angles0 = tilting_angles[0];
    double tilting_angles1 = tilting_angles[1];
    double tilting_angles2 = tilting_angles[2];
    int numberPositions = position.length;
    double[][] allAngles = new double[numberPositions][6];
    for (int j = 0; j < numberPositions; j++) {
      double tilting_angles3 = tilting_angles[3] + getEtaDetector(datafile);
      tilting_angles0 = position[j] / 2.0f;
      double[] incidentAngles = getTextureAnglesR(tilting_angles0, tilting_angles1, tilting_angles2,
              tilting_angles3, 90.0f, sampleAngles, false);

      double[] diffractionAngles = getTextureAnglesR(tilting_angles0, tilting_angles1, tilting_angles2,
              tilting_angles3, - 90.0f + position[j], sampleAngles, false);

      double[] textureAngles = getTextureAnglesR(tilting_angles0, tilting_angles1, tilting_angles2,
              tilting_angles3, position[j] / 2.0f, sampleAngles, false);

      for (int i = 0; i < 2; i++) {
        allAngles[j][i] = incidentAngles[i];
        allAngles[j][i + 2] = diffractionAngles[i];
        allAngles[j][i + 4] = textureAngles[i];
      }
    }
    return allAngles;
  }

  public double[] getTextureAngles(double[] tilting_angles, double twotheta) {

    double[] texture_angles = new double[2];

    texture_angles[0] = tilting_angles[1];
    texture_angles[1] = tilting_angles[2];
    return texture_angles;

  }

  public double LorentzPolarization(DiffrDataFile adatafile, Sample asample, double position, boolean dspacingbase, boolean energyDispersive) {

    double theta_detector = getThetaDetector(adatafile, position) / 2;
    double lp = Math.abs(Math.sin(theta_detector * Constants.DEGTOPI));
    lp *= position * position * position * position;

    return lp;
  }

  public JOptionsDialog getOptionsDialog(Frame parent) {
    JOptionsDialog adialog = new JGeometryILOptionsD(parent, this);
    return adialog;
  }

  public class JGeometryILOptionsD extends JOptionsDialog {

    public JGeometryILOptionsD(Frame parent, XRDcat obj) {

      super(parent, obj);

      principalPanel.setLayout(new FlowLayout());
      principalPanel.add(new JLabel("No options for this geometry"));

      setTitle("Options panel");
      initParameters();
      pack();
    }

    public void initParameters() {
    }

    public void retrieveParameters() {
    }
  }

}
