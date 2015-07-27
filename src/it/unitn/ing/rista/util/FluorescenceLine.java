/*
 * @(#)FluorescenceLine.java created Mar 9, 2009 Caen
 *
 * Copyright (c) 2009 Luca Lutterotti All Rights Reserved.
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
package it.unitn.ing.rista.util;

import it.unitn.ing.rista.chemistry.XRayDataSqLite;

/**
 * The FluorescenceLine is a class to
 *
 * @author Luca Lutterotti
 * @version $Revision: 1.00 $, $Date: Mar 9, 2009 11:20:32 AM $
 * @since JDK1.1
 */
public class FluorescenceLine {

  double dgx = 1.0;
  double dcx = 1.0;

  double energy;
  double intensity = 1.0;
  double eta;
  double hwhm;
	double one_over_hwhm;
	private double transitionProbability;
	int innerShellID = -1;
	double fluorescenceYeld = 0;

	public FluorescenceLine(double energyPosition, int inner_shell_ID) {
    energy = energyPosition;
    intensity = 1.0;
		innerShellID = inner_shell_ID;
  }

	public FluorescenceLine(FluorescenceLine lineToCopy) {
		energy = lineToCopy.energy;
		intensity = lineToCopy.intensity;
		eta = lineToCopy.eta;
		hwhm = lineToCopy.hwhm;
		innerShellID = lineToCopy.innerShellID;
		transitionProbability = lineToCopy.transitionProbability;
		fluorescenceYeld = lineToCopy.fluorescenceYeld;
		one_over_hwhm = 1.0 / hwhm;
		dgx = (1.0 - eta) * Constants.sqrtln2pi * one_over_hwhm;
		dcx = eta * one_over_hwhm / Math.PI;
	}

  public void setIntensity(double intensity) {
    this.intensity = intensity;
  }

  public double getIntensity() {
    return intensity;
  }

  public void setEnergy(double energy) {
    this.energy = energy;
  }

  public double getEnergy() {
    return energy;
  }

  public void setShape(double hwhm, double eta) {
    this.hwhm = hwhm;
    this.eta = eta;
	  one_over_hwhm = 1.0 / hwhm;
	  dgx = (1.0 - eta) * Constants.sqrtln2pi * one_over_hwhm;
	  dcx = eta * one_over_hwhm / Math.PI;
  }

  public double getIntensity(double x) {
    double dx = x - getEnergy();
    dx *= one_over_hwhm;
    dx *= dx;
    if (dx > 30.0)
	    return getIntensity() *  dcx / (1.0 + dx);
    else
	    return getIntensity() *  (dcx / (1.0 + dx) + dgx * Math.exp(-Constants.LN2 * dx));
	}

  public void multiplyIntensityBy(double atomsQuantity) {
    setIntensity(getIntensity() * atomsQuantity);
  }

	public void setTransitionProbability(double transitionProbability) {
		this.transitionProbability = transitionProbability;
	}

	public double getTransitionProbability() {
		return transitionProbability;
	}

	public int getInnerShellID() {
		return innerShellID;
	}

	public void setFluorescenceYeld(double value) {
		fluorescenceYeld = value;
	}

	public double getFluorescenceYeld() {
		return fluorescenceYeld;
	}

	public String toString() {
		return XRayDataSqLite.shellIDs[getInnerShellID()] + " " + getEnergy() + " " + getTransitionProbability();
	}

}
