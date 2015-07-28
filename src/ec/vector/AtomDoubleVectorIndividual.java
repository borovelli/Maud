/*
 * @(#)AtomDoubleVectorIndividual.java created 28/10/2001 Le Mans
 *
 * Copyright (c) 2001 Luca Lutterotti All Rights Reserved.
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

import ec.*;
import ec.util.*;

import java.io.Serializable;
import java.util.*;
import java.io.*;

/**
 * AtomDoubleVectorIndividual is a VectorIndividual whose genome is an array of doubles.
 * Gene values may range from species.mingene(x) to species.maxgene(x), inclusive.
 * The default mutation method randomizes genes to new values in this range,
 * with <tt>species.mutationProbability</tt>.
 *
 <p><b>Default Base</b><br>
 vector.atom-double-vect-ind

 * @version $Revision: 1.3 $, $Date: 2003/03/24 21:00:03 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */

public class AtomDoubleVectorIndividual extends DoubleVectorIndividual {

  public static final String P_ATOMDOUBLEVECTORINDIVIDUAL = "atom-double-vect-ind";

  public Parameter defaultBase() {
    return VectorDefaults.base().push(P_ATOMDOUBLEVECTORINDIVIDUAL);
  }

  public Object protoClone() throws CloneNotSupportedException {
    AtomDoubleVectorIndividual myobj = (AtomDoubleVectorIndividual) (super.protoClone());

    // must clone the genome
    myobj.genome = (double[]) (genome.clone());

    return myobj;
  }

  public void setup(final EvolutionState state, final Parameter base) {
    // since VectorSpecies set its constraint values BEFORE it called
    // super.setup(...) [which in turn called our setup(...)], we know that
    // stuff like genomeSize has already been set...

    Parameter def = defaultBase();

    if (!(species instanceof AtomFloatVectorSpecies))
      state.output.fatal("AtomDoubleVectorIndividual requires a AtomFloatVectorSpecies", base, def);
    AtomFloatVectorSpecies s = (AtomFloatVectorSpecies) species;

    genome = new double[s.genomeSize];
  }

  /** Destructively mutates the individual in some default manner.  The default form
   simply randomizes genes to a uniform distribution from the min and max of the gene values. */
  public void defaultPermutate(EvolutionState state, int thread) {
    AtomFloatVectorSpecies s = (AtomFloatVectorSpecies) species;
    int atomNumber = genome.length / s.chunksize;
    if (s.permutationProbability > 0.0 && atomNumber > 1) {
      if (state.random[thread].nextBoolean(s.permutationProbability)) {
        int point = 0;
        int secondpoint = 1;
        if (atomNumber > 2) {
          point = state.random[thread].nextInt(atomNumber);
          secondpoint = point;
          while (secondpoint == point)
            point = state.random[thread].nextInt(atomNumber);
        }
        int x1 = point * s.chunksize;
        int y1 = secondpoint * s.chunksize;
        for (int x = 0; x < s.chunksize; x++) {
          double tmp = genome[x1 + x];
          genome[x1 + x] = genome[y1 + x];
          genome[y1 + x] = tmp;
        }
      }
    }

  }

  public boolean equals(Object ind) {
    if (!(this.getClass().equals(ind.getClass()))) return false; // SimpleRuleIndividuals are special.
    AtomDoubleVectorIndividual i = (AtomDoubleVectorIndividual) ind;
    if (genome.length != i.genome.length)
      return false;
    for (int j = 0; j < genome.length; j++)
      if (genome[j] != i.genome[j])
        return false;
    return true;
  }

}
