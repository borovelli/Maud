/*
 * @(#)QualitativeXRF.java created May 15, 2009 Caen
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
package it.unitn.ing.rista.diffr.fluorescence;

import it.unitn.ing.rista.diffr.XRDcat;

/**
 * The QuantitativeXRF is a class to
 *
 * @author Luca Lutterotti
 * @version $Revision: 1.00 $, $Date: May 15, 2009 7:03:18 PM $
 * @since JDK1.1
 */
public class QuantitativeXRF extends FluorescenceBase {

  public static String modelID = "Quantitative XRF";
  public static String descriptionID = "Perform quantitative fitting of XRF data";

  public QuantitativeXRF(XRDcat obj, String alabel) {
    super(obj, alabel);
    identifier = modelID;
    IDlabel = modelID;
    description = descriptionID;
  }

  public QuantitativeXRF(XRDcat afile) {
    this(afile, modelID);
  }

  public QuantitativeXRF() {
    identifier = modelID;
    IDlabel = modelID;
    description = descriptionID;
  }

}
