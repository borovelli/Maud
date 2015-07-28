/*
 * @(#)VectorPermutationPipeline.java created Mar 22, 2003 Berkeley
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

package ec.vector.breed;

import ec.*;
import ec.vector.*;
import ec.util.Parameter;


/**
 VectorPermutationPipeline is a BreedingPipeline which implements a simple default Permutation
 for VectorIndividuals.  Normally it takes an individual and returns a permutated
 child individual. VectorPermutationPipeline works by calling defaultPermutate(...) on the
 parent individual.

 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 (however many its source produces)

 <p><b>Number of Sources</b><br>
 1

 <p><b>Default Base</b><br>
 vector.permutate (not that it matters)
 *
 * @version $Revision: 1.1 $, $Date: 2003/03/24 21:00:03 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */

public class VectorPermutationPipeline  extends BreedingPipeline {
  public static final String P_PERMUTATION = "permutate";
  public static final int NUM_SOURCES = 1;

  public Parameter defaultBase() {
    return VectorDefaults.base().push(P_PERMUTATION);
  }

  /** Returns 1 */
  public int numSources() {
    return NUM_SOURCES;
  }

  public int produce(final int min,
                     final int max,
                     final int start,
                     final int subpopulation,
                     final Individual[] inds,
                     final EvolutionState state,
                     final int thread) throws CloneNotSupportedException {
    // grab individuals from our source and stick 'em right into inds.
    // we'll modify them from there
    int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);

    // clone the individuals if necessary
    if (!(sources[0] instanceof BreedingPipeline))
      for (int q = start; q < n + start; q++)
        inds[q] = inds[q].deepClone();

    // mutate 'em
    for (int q = start; q < n + start; q++) {
      ((AtomDoubleVectorIndividual) inds[q]).defaultPermutate(state, thread);
      ((VectorIndividual) inds[q]).evaluated = false;
    }

    return n;
  }


}
