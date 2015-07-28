package ec.coevolve;

import ec.simple.*;

/**
 * IncrementalFitness.java
 *
 * Incremental Fitness is an interface which defines a way that a fitness can be based
 * on more than one assessment.  Before assessments for an individual begin,
 * preprocessFitness() is called.  Then updateFitness() is called for each assessment.
 * after all assessments are made, postprocessFitness() is then called.
 *
 * @author Liviu Panait
 * @version 1.0
 */

public interface IncrementalFitness {

  /**
   A function to be called before the repeated fitness assessment starts.
   */
  public void preprocessFitness();

  /**
   A function to be called for finalizing the calculation of the fitness.
   */
  public void postprocessFitness();

  /**
   Update the cumulative fitness of the individual
   */
  public void updateFitness(float newFitness);

}
