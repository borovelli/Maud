package ec.coevolve;

import ec.simple.*;

/*
 * IncrementalAveragedFitness.java
 *
 */

/**
 * IncrementalAveragedFitness is a SimpleFitness which adheres to the IncrementalFitness
 * protocol, then bases its fitness value on the average results of each of the updateFitness()
 * methods received.
 *
 * @author Liviu Panait
 * @version 1.0
 */

public class IncrementalAveragedFitness extends SimpleFitness
        implements IncrementalFitness {

  // the number of evaluations stored in the fitness variable
  int numEvals;

  /**
   A function to be called before the repeated fitness assessment starts.
   It initializes the variables needed to calculate the averaged fitness.
   */
  public void preprocessFitness() {
// reset the number of evaluations and the total fitness
    numEvals = 0;
    fitness = 0.0f;
  }

  /**
   A function to be called for finalizing the calculation of the fitness.
   It transforms the cumulative fitness to a mean fitness
   */
  public void postprocessFitness() {
// if more then 1 evaluation, divide the cumulative
// fitness by the number of evaluations

    if (numEvals != 0) {
      fitness = fitness / numEvals;
    }
    numEvals = 0;
  }

  /**
   Update the cumulative fitness of the individual
   */
  public void updateFitness(float newFitness) {

    fitness = fitness + newFitness;
    numEvals++;
  }

}
