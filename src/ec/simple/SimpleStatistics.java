package ec.simple;

import ec.*;

import java.io.IOException;

import ec.util.*;

import java.io.File;

/*
 * SimpleStatistics.java
 *
 * Created: Tue Aug 10 21:10:48 1999
 * By: Sean Luke
 */

/**
 * A basic Statistics class suitable for simple problem applications.
 *
 * SimpleStatistics prints out the best individual, per subpopulation,
 * each generation.  At the end of a run, it also prints out the best
 * individual of the run.  SimpleStatistics outputs this data to a log
 * which may either be a provided file or stdout.
 *

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base.</i><tt>file</tt><br>
 <font size=-1>String (a filename), or nonexistant (signifies stdout)</font></td>
 <td valign=top>(the log for statistics)</td></tr>
 </table>

 *
 * @author Sean Luke
 * @version 1.0
 */

public class SimpleStatistics extends Statistics {
  /** log file parameter */
  public static final String P_STATISTICS_FILE = "file";

  /** The Statistics' log */
  public int statisticslog;

  /** The best individual we've found so far */
  public Individual[] best_of_run;


  public SimpleStatistics() {
    best_of_run = null;
    statisticslog = 0; /* stdout */
  }

  public void setup(final EvolutionState state, final Parameter base) {
    super.setup(state, base);

    File statisticsFile = state.parameters.getFile(
            base.push(P_STATISTICS_FILE), null);

    if (statisticsFile != null)
      try {
        statisticslog = state.output.addLog(statisticsFile, Output.V_NO_GENERAL - 1, false, true);
      } catch (IOException i) {
        state.output.fatal("An IOException occurred while trying to create the log " + statisticsFile + ":\n" + i);
      }
  }

  public void postInitializationStatistics(final EvolutionState state) {
    super.postInitializationStatistics(state);

// set up our best_of_run array -- can't do this in setup, because
// we don't know if the number of subpopulations has been determined yet
    best_of_run = new Individual[state.population.subpops.length];
  }

  /** Logs the best individual of the generation. */
  public void postEvaluationStatistics(final EvolutionState state) {
    super.postEvaluationStatistics(state);

// for now we just print the best fitness per subpopulation.
    Individual[] best_i = new Individual[state.population.subpops.length];  // quiets compiler complaints
    for (int x = 0; x < state.population.subpops.length; x++) {
      best_i[x] = state.population.subpops[x].individuals[0];
      for (int y = 1; y < state.population.subpops[x].individuals.length; y++)
        if (state.population.subpops[x].individuals[y].fitness.betterThan(best_i[x].fitness))
          best_i[x] = state.population.subpops[x].individuals[y];

      // now test to see if it's the new best_of_run
      if (best_of_run[x] == null || best_i[x].fitness.betterThan(best_of_run[x].fitness))
        best_of_run[x] = best_i[x].deepClone();
    }

// print the best-of-generation individual
    state.output.println("\nGeneration: " + state.generation, Output.V_NO_GENERAL, statisticslog);
    state.output.println("Best Individual:", Output.V_NO_GENERAL, statisticslog);
    for (int x = 0; x < state.population.subpops.length; x++)
      best_i[x].printIndividualForHumans(state, statisticslog, Output.V_NO_GENERAL);
  }

  /** Logs the best individual of the run. */
  public void finalStatistics(final EvolutionState state, final int result) {
    super.finalStatistics(state, result);

// for now we just print the best fitness

    state.output.println("\nBest Individual of Run:", Output.V_NO_GENERAL, statisticslog);
    for (int x = 0; x < state.population.subpops.length; x++)
      best_of_run[x].printIndividualForHumans(state, statisticslog, Output.V_NO_GENERAL);
  }
}
