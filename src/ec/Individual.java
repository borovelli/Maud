package ec;

import ec.util.Parameter;

import java.io.*;

/*
 * Individual.java
 * Created: Tue Aug 10 19:58:13 1999
 */

/**
 * An Individual is an item in the EC population stew which is evaluated
 * and assigned a fitness which determines its likelihood of selection.
 * Individuals are created most commonly by the newIndividual(...) method
 * of the ec.Species class.
 *
 * In general Individuals are immutable.  That is, once they are created
 * they should not be modified.  This protocol helps insure that they are
 * safe to read under multithreaded conditions.
 *
 * @author Sean Luke
 * @version 1.0
 */

public abstract class Individual implements Prototype {
  /** The fitness of the Individual. */
  public Fitness fitness;

  /** The species of the Individual.*/
  public Species species;

  /** Has the individual been evaluated and its fitness determined yet? */
  public boolean evaluated;

  /** Guaranteed DEEP-CLONES the individual.  You may need to override this
   method in Individual subclasses to guarantee this fact.   Some individuals
   (notably GPIndividuals) do not have protoClone() deep-clone them because
   that is expensive.  But if you need to guarantee that you have a unique
   individual, this is the way to do it.*/

  public Individual deepClone() {
    return (Individual) protoCloneSimple();
  }

  public Object protoClone() throws CloneNotSupportedException {
    Individual myobj = (Individual) (super.clone());

    if (myobj.fitness != null) myobj.fitness = (Fitness) (fitness.protoClone());
// put your deep-cloning code here...
    return myobj;
  }

  public final Object protoCloneSimple() {
    try {
      return protoClone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    } // never happens
  }

  /** Returns true if I am genetically "equal" to ind.  This should
   mostly be interpreted as saying that we are of the same class
   and that we hold the same data. It should NOT be a pointer comparison. */
  public abstract boolean equals(Object ind);

  /** Returns a hashcode for the individual, such that individuals which
   are equals(...) each other always return the same
   hash code. */
  public abstract int hashCode();

  /** This should be used to set up only those things which you share in common
   with all other individuals in your species; individual-specific items
   which make you <i>you</i> should be filled in by Species.newIndividual(...),
   and modified by breeders.
   @see Prototype#setup(EvolutionState,Parameter)
   */

  public abstract void setup(final EvolutionState state, final Parameter base);

  /** Should print the individual out in a pleasing way for humans,
   including its
   fitness, using state.output.println(...,verbosity,log)
   You can get fitness to print itself at the appropriate time by calling
   fitness.printFitnessForHumans(state,log,verbosity);
   */

  public abstract void printIndividualForHumans(final EvolutionState state,
                                                final int log,
                                                final int verbosity);

  /** Should print the individual in a way that can be read by computer,
   including its fitness, using state.output.println(...,verbosity,log)
   You can get fitness to print itself at the appropriate time by calling
   fitness.printFitness(state,log,verbosity); */

  public abstract void printIndividual(final EvolutionState state,
                                       final int log,
                                       final int verbosity);

  /** Should print the individual in a way that can be read by computer,
   including its fitness.  You can get fitness to print itself at the
   appropriate time by calling fitness.printFitness(state,log,writer);
   Usually you should try to use printIndividual(state,log,verbosity)
   instead -- use this method only if you can't print through the
   Output facility for some reason.
   */

  public abstract void printIndividual(final EvolutionState state,
                                       final PrintWriter writer);

  /** Reads in the individual from a form printed by printIndividual().*/

  public abstract void readIndividual(final EvolutionState state,
                                      final LineNumberReader reader)
          throws IOException, CloneNotSupportedException;

  /** Returns the "size" of the individual.  This is used for things like
   parsimony pressure.  The default form of this method returns 0 --
   if you care about parsimony pressure, you'll need to override the
   default to provide a more descriptive measure of size. */

  public long size() {
    return 0;
  }
}

