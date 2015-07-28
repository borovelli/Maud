/*
 * @(#)TXRFluorescence.java created May 28, 2013 Hermanville-sur-Mer
 *
 * Copyright (c) 2013 Luca Lutterotti All Rights Reserved.
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
package it.unitn.ing.rista.diffr.fluorescence;

import it.unitn.ing.rista.chemistry.AtomInfo;
import it.unitn.ing.rista.chemistry.XRayDataSqLite;
import it.unitn.ing.rista.diffr.*;
import it.unitn.ing.rista.diffr.detector.XRFDetector;
import it.unitn.ing.rista.diffr.geometry.GeometryXRFInstrument;
import it.unitn.ing.rista.io.cif.CIFtoken;
import it.unitn.ing.rista.util.*;

import java.util.Hashtable;
import java.util.Vector;

/**
 * The GIXRFModel is a class to calculate the Grazing Incidence
 * fluorescence model by De Boer.
 * D. K. G. de Boer, Phys. Review B, 44[2], 498, 1991.
 *
 * @author Luca Lutterotti
 * @version $Revision: 1.00 $, $Date: Feb 21, 2013 8:27:36 PM $
 * @since JDK1.1
 */
public class GIXRFModel extends FluorescenceBase {

	public static String[] diclistc = {
			"_maud_gixrf_smoothing_points",
			"_maud_gixrf_smoothing_divergence",
			"_fluorescence_atom_intensity_correction"
	};
	public static String[] diclistcrm = {
			"_maud_gixrf_smoothing_points",
			"_maud_gixrf_smoothing_divergence",
			"_fluorescence_atom_intensity_correction"
	};

	public static String[] classlistc = {};
	public static String[] classlistcs = {};

	public static String modelID = "GIXRF";
	public static String descriptionID = "Grazing Incidence X-Ray Fluorescence model, for bulk or multilayers";

	public GIXRFModel(XRDcat obj, String alabel) {
		super(obj, alabel);
		initXRD();
		identifier = modelID;
		IDlabel = modelID;
		description = descriptionID;
	}

	public GIXRFModel(XRDcat afile) {
		this(afile, modelID);
	}

	public GIXRFModel() {
		identifier = modelID;
		IDlabel = modelID;
		description = descriptionID;
	}

	public void initConstant() {
		Nstring = 1;
		Nstringloop = 0;
		Nparameter = 1;
		Nparameterloop = 1;
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

	int maxAtomsNumber = 130;

	public void initParameters() {
		super.initParameters();
		setString(0, "11");
		initializeParameter(0, 0.001, 0.0001, 0.1);
		for (int i = 0; i < maxAtomsNumber; i++)
			addparameterloopField(0, new Parameter(this, getParameterString(0, i), 1,
				ParameterPreferences.getDouble(getParameterString(0, i) + ".min", 0.8),
				ParameterPreferences.getDouble(getParameterString(0, i) + ".max", 1.2)));
	}

	public void updateStringtoDoubleBuffering(boolean firstLoading) {
		super.updateStringtoDoubleBuffering(false);

		phiIntegrationNumber = Integer.parseInt(getString(0));
	}

	public void updateParametertoDoubleBuffering(boolean firstLoading) {
		// to be implemented by subclasses

		if (getFilePar().isLoadingFile() || !isAbilitatetoRefresh)
			return;
		super.updateParametertoDoubleBuffering(false);

		phiDelta = getParameterValue(0);
		if (phiIntegrationNumber > 1)
			phiStep = phiDelta / (phiIntegrationNumber - 1);
		else
			phiStep = 0;
	}

	public double getIntensityCorrection(int atomNumber) {
		return getParameterLoopValues(0, atomNumber - 1);
	}

}
