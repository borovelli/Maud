package ec.app.numbits;

import ec.util.*;
import ec.*;
import ec.simple.*;
import ec.vector.*;

/*
 * NumBits.java
 *
 * Created: Thu MAr 22 16:27:15 2001
 * By: Liviu Panait
 */

/*
 * @author Liviu Panait
 * @version 1.0
 */

public class NumBits extends Problem implements SimpleProblemForm {
  EvolutionState state;

  // nothing....
  public void setup(final EvolutionState state_, final Parameter base) {

    state = state_;

  }

  public Parameter defaultBase() {
    return null;
  } // we'll never use it

  public void evaluate(final EvolutionState state,
                       final Individual ind,
                       final int threadnum) {

    if (!(ind instanceof BitVectorIndividual))
      state.output.fatal("The individuals for this problem should be BitVectorIndividuals.");

    BitVectorIndividual temp = (BitVectorIndividual) ind;

    int numar = 0;

    for (int i = 0; i < temp.genome.length; i++)
      if (temp.genome[i])
        numar++;

    ((SimpleFitness) (ind.fitness)).setFitness(state, ((float) numar) / temp.genome.length);
    ind.evaluated = true;

  }

  public void describe(final Individual ind,
                       final EvolutionState state,
                       final int threadnum,
                       final int log,
                       final int verbosity) {
    return;
  }

  public Object protoClone() throws CloneNotSupportedException {
    NumBits prob = (NumBits) (super.clone());
    return prob;
  }

  public final Object protoCloneSimple() {
    try {
      return protoClone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    } // never happens
  }


}
