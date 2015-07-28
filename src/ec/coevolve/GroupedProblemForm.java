package ec.coevolve;

import ec.*;

/*
 * GroupedProblemForm.java
 *
 * Created: March 2, 2001
 * By: Sean Luke & Liviu Panait
 */

/**
 * GroupedProblemForm is an interface which defines methods
 * for Problems to implement simple coevolutionary
 * evaluation.
 *
 * @author Sean Luke & Liviu Panait
 * @version 1.0
 */

public interface GroupedProblemForm {
  /** Set up the population <tt>pop</tt> (such as fitness information) prior to evaluation.
   Although this method is not static, you should not use it to write to any instance
   variables in the GroupedProblem instance; this is because it's possible that
   the instance used is in fact the prototype, and you will have no guarantees that
   your instance variables will remain valid during the evaluate(...) process.
   Do not assume that <tt>pop</tt> will be the same as <tt>state.pop</tt> -- it
   may not.  <tt>state</tt> is only provided to give you access to EvolutionState
   features. */
  public void preprocessPopulation(final EvolutionState state, Population pop);

  /** Finish processing the population (such as fitness information) after evaluation.
   Although this method is not static, you should not use it to write to any instance
   variables in the GroupedProblem instance; this is because it's possible that
   the instance used is in fact the prototype, and you will have no guarantees that
   your instance variables will remain valid during the evaluate(...) process.
   Do not assume that <tt>pop</tt> will be the same as <tt>state.pop</tt> -- it
   may not.  <tt>state</tt> is only provided to give you access to EvolutionState
   features. */
  public void postprocessPopulation(final EvolutionState state, Population pop);

  /** Evaluates the individuals found in ind together.  If updateFitness[i] is true,
   then you should use this evaluation to update the fitness of the individual in
   ind[i].  Individuals which are updated should have their fitnesses modified so
   that immediately after evaluation (and prior to postprocessPopulation(...) being
   called) individuals' fitnesses can be checked to see which is better than which.
   Do not assume that the individuals in <tt>ind</tt> will actually be in <tt>state.pop</tt>
   (they may not -- this method may be called at the end of a run to determine the
   best individual of the run in some kind of contest).
   */
  public void evaluate(final EvolutionState state,
                       final Individual[] ind, // the individuals to evaluate together
                       final boolean[] updateFitness, // should this individuals' fitness be updated?
                       final int threadnum);
}





