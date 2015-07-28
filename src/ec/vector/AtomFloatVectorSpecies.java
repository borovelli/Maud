/*
 * @(#)AtomFloatVectorSpecies.java created Mar 22, 2003 Berkeley
 *
 * Copyright (c) 1996-2003 Luca Lutterotti All Rights Reserved.
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

package ec.vector;

import java.io.Serializable;

import ec.*;
import ec.util.*;

import java.util.*;
import java.io.*;


/**
 * AtomFloatVectorSpecies is a subclass of VectorSpecies with special
 * constraints for floating-point vectors, namely AtomDoubleVectorIndividual.
 *
 * <p>FloatVectorSpecies can specify numeric constraints on gene values
 * in one of two ways.  First, they can simply specify a default min and max value.  Or
 * they can specify an array of min/max pairs, one pair per gene.  FloatVectorSpecies
 * will check to see if the second approach is to be used by looking for parameter
 * <i>base.n</i>.<tt>max-gene</tt>.0 in the
 * array -- if it exists, FloatvectorSpecies will assume all such parameters
 * exist, and will load up to the genome length.  If a parameter is missing, in this
 * range, a warning will be issued during Individual setup.  If the array is shorter
 * than the genome, then the default min/max
 * values will be used for the remaining genome values.  This means that even if you
 * specify the array, you need to still specify the default min/max values just in case.
 *
 * @version $Revision: 1.1 $, $Date: 2003/03/24 21:00:03 $
 * @author Luca Lutterotti
 * @since JDK1.1
 *
 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>mutation-prob</tt><br>
 <font size=-1>0.0 &lt;= float &lt;= 1.0 </font></td>
 <td valign=top>(probability that a gene will get mutated over default mutation)</td></tr>
 </table>
 */

public class AtomFloatVectorSpecies extends FloatVectorSpecies {

  public final static String P_PERMUTATIONPROB = "permutation-prob";

  /** Probability that a gene will permutate two chunks */
  public float permutationProbability;

  public boolean inRange(double geneVal) {
    boolean in_range = super.inRange(geneVal);
    if (in_range || i_prototype instanceof AtomDoubleVectorIndividual)
      return true;  // geneVal is valid for all double
    else
      return false;  // dunno what the individual is...
  }

  public void setup(final EvolutionState state, final Parameter base) {
    super.setup(state, base);

    permutationProbability = state.parameters.getFloat(
            base.push(P_PERMUTATIONPROB), null, 0.0, 1.0);
    if (permutationProbability == -1.0)
      state.output.error("AtomFloatVectorSpecies must have a permutation probability between 0.0 and 1.0 inclusive",
              base.push(P_PERMUTATIONPROB), null);

  }

}
