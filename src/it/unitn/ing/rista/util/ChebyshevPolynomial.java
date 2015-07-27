/*
 * @(#)ChebyshevPolynomial.java 5/09/1999 Pergine
 *
 * Copyright (c) 1999 Luca Lutterotti All Rights Reserved.
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

import java.lang.*;

/**
 *  The ChebyshevPolynomial is a class providing static methods for
 *  Chebyshev polynomial computation.
 *
 *
 * @version $Revision: 1.3 $, $Date: 2004/08/12 09:36:10 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */


public class ChebyshevPolynomial {

  // to be completed

  static final short Cm[][] = {
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {-1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, -3, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 0, -8, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 5, 0, -20, 0, 16, 0, 0, 0, 0, 0, 0, 0},
    {-1, 0, 18, 0, -48, 0, 32, 0, 0, 0, 0, 0, 0},
    {0, -7, 0, 56, 0, -112, 0, 64, 0, 0, 0, 0, 0},
    {1, 0, -32, 0, 160, 0, -256, 0, 128, 0, 0, 0, 0},
    {0, 9, 0, -120, 0, 432, 0, -576, 0, 256, 0, 0, 0},
    {-1, 0, 50, 0, -400, 0, 1120, 0, -1280, 0, 512, 0, 0},
    {0, -11, 0, 220, 0, -1232, 0, 2816, 0, -2816, 0, 1024, 0},
    {1, 0, -72, 0, 840, 0, -3584, 0, 6912, 0, -6144, 0, 2048}
  };

  private ChebyshevPolynomial() {
  }

  public static final double getT(int n, double x) {

    if (n > 12)
      return 0.0;

    double t = 0.0;

    for (int i = n; i >= 0; i -= 2) {
      t += Cm[n][i] * MoreMath.pow(x, i);
    }

    return t;
  }
}
