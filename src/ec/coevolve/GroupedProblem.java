package ec.coevolve;

import ec.*;

/*
 * GroupedProblem.java
 *
 * Created: March 2, 2001
 * By: Sean Luke & Liviu Panait
 */


/*

*/

public abstract class GroupedProblem extends Problem {
  /** Set up the population (such as fitness information) prior to evaluation.
   Although this method is not static, you should not use it to write to any instance
   variables in the GroupedProblem instance; this is because it's possible that
   the instance used is in fact the prototype, and you will have no guarantees that
   your instance variables will remain valid during the evaluate(...) process.  */
  public abstract void preprocessPopulation(final EvolutionState state,
                                            final Individual[] individuals);

  /** Finish processing the population (such as fitness information) after evaluation.
   Although this method is not static, you should not use it to write to any instance
   variables in the GroupedProblem instance; this is because it's possible that
   the instance used is in fact the prototype, and you will have no guarantees that
   your instance variables will remain valid during the evaluate(...) process.  */
  public abstract void postprocessPopulation(final EvolutionState state,
                                             final Individual[] individuals);

  public abstract void evaluate(final EvolutionState state,
                                final Individual[] ind, // the individuals to evaluate together
                                final boolean[] updateFitness, // should this individuals' fitness be updated?
                                final int threadnum);

  public Object protoClone() throws CloneNotSupportedException {
    return this.clone();
  }

  public Object protoCloneSimple() {
    try {
      return protoClone();
    } catch (CloneNotSupportedException e) {
      return null;
    } // never happens
  }

}





