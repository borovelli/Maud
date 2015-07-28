package ec.spea2;

import ec.*;
import ec.util.*;

import java.io.Serializable;

import ec.steadystate.*;

/*
 * SPEA2TournamentSelection.java
 *
 * Created: Wed Jun 26 11:20:32 PDT 2002
 * By: Robert Hubley, Institute for Systems Biology
 *     (based on TournamentSelection.java by Sean Luke)
 */

/**
 * Does a simple tournament selection, limited to the subpopulation it's
 * working in at the time and only within the boundry of the SPEA2 archive
 * (between 0-archiveSize).
 *
 * <p>Tournament selection works like this: first, <i>size</i> individuals
 * are chosen at random from the population.  Then of those individuals,
 * the one with the best fitness is selected.
 *
 * <p><i>size</i> can also be a floating-point value between 1.0 and 2.0,
 * exclusive of them. In this situation, two individuals are chosen at random, and
 * the better one is selected with a probability of <i>size/2</i>
 *
 * <p>Common sizes for <i>size</i> include: 2, popular in Genetic Algorithms
 * circles, and 7, popularized in Genetic Programming by John Koza.
 * If the size is 1, then individuals are picked entirely at random.
 *
 * <p>Tournament selection is so simple that it doesn't need to maintain
 * a cache of any form, so many of the SelectionMethod methods just
 * don't do anything at all.
 *
 * NOTE: The SPEA2Breeder class leaves the individuals
 * vector with only archiveSize number indviduals.  All positions in
 * the individuals vector above archiveSize are null.
 *

 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 Always 1.

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base.</i><tt>size</tt><br>
 <font size=-1>int &gt;= 1 <b>or</b> 1.0 &lt; float &lt; 2.0</font></td>
 <td valign=top>(the tournament size)</td></tr>

 <tr><td valign=top><i>base.</i><tt>pick-worst</tt><br>
 <font size=-1> bool = <tt>true</tt> or <tt>false</tt> (default)</font></td>
 <td valign=top>(should we pick the <i>worst</i> individual in the tournament instead of the <i>best</i>?)</td></tr>

 </table>

 <p><b>Default Base</b><br>
 spea2.tournament

 *
 * @author Robert Hubley (based on TournamentSelection by Sean Luke)
 * @version 1.0
 */

public class SPEA2TournamentSelection extends SelectionMethod implements SteadyStateBSourceForm {
  /** default base */
  public static final String P_TOURNAMENT = "SPEA2tournament";

  public static final String P_PICKWORST = "pick-worst";

  /** size parameter */
  public static final String P_SIZE = "size";

  /* Default size */
  public static final int DEFAULT_SIZE = 7;

  /** Size of the tournament*/
  public int size;

  /** What's our probability of selection? If 1.0, we always pick the "good" individual. */
  public double probabilityOfSelection;

  /** Do we pick the worst instead of the best? */
  public boolean pickWorst;

  public Parameter defaultBase() {
    return new Parameter(P_TOURNAMENT);
  }

  public void setup(final EvolutionState state, final Parameter base) {
    super.setup(state, base);

    Parameter def = defaultBase();

    double val = state.parameters.getDouble(base.push(P_SIZE), def.push(P_SIZE), 1.0);
    if (val < 1.0)
      state.output.fatal("Tournament size must be >= 1.", base.push(P_SIZE), def.push(P_SIZE));
    else if (val > 1 && val < 2) // pick with probability
    {
      size = 2;
      probabilityOfSelection = (val / 2);
    } else if (val != (int) val)  // it's not an integer
      state.output.fatal("If >= 2, Tournament size must be an integer.", base.push(P_SIZE), def.push(P_SIZE));
    else {
      size = (int) val;
      probabilityOfSelection = 1.0;
    }

    pickWorst = state.parameters.getBoolean(base.push(P_PICKWORST), def.push(P_PICKWORST), false);
  }


  public int produce(final int subpopulation,
                     final EvolutionState state,
                     final int thread) {
    // pick size random individuals, then pick the best.
    /*
Individual[] oldinds = state.population.subpops[subpopulation].individuals;
int i = state.random[thread].nextInt(oldinds.length) ;
int bad = i;

for (int x=1;x<size;x++)
    {
    int j = state.random[thread].nextInt(oldinds.length);
    if (pickWorst)
{ if (!(oldinds[j].fitness.betterThan(oldinds[i].fitness))) { bad = i; i = j; } else bad = j; }
    else
{ if (oldinds[j].fitness.betterThan(oldinds[i].fitness)) { bad = i; i = j;} else bad = j; }
    }

    if (probabilityOfSelection != 1.0 && !state.random[thread].nextBoolean(probabilityOfSelection))
        i = bad;
    return i;
    */
    return 0;
  }


  public int produce(final int min,
                     final int max,
                     final int start,
                     final int subpopulation,
                     final Individual[] inds,
                     final EvolutionState state,
                     final int thread) throws CloneNotSupportedException {
    int n = 1;
    if (n > max) n = max;
    if (n < min) n = min;

    for (int q = 0; q < n; q++) {
      // pick size random individuals, then pick the best.
      Individual[] oldinds = state.population.subpops[subpopulation].individuals;
      int i = state.random[thread].nextInt(((SPEA2Subpopulation) state.population.subpops[subpopulation]).archiveSize);
      int bad = i;

      for (int x = 1; x < size; x++) {
        int j = state.random[thread].
                nextInt(((SPEA2Subpopulation) state.population.subpops[subpopulation]).archiveSize);
        if (pickWorst) {
          if (!(oldinds[j].fitness.betterThan(oldinds[i].fitness))) {
            bad = i;
            i = j;
          } else {
            bad = j;
          }
        } else {
          if (oldinds[j].fitness.betterThan(oldinds[i].fitness)) {
            bad = i;
            i = j;
          } else {
            bad = j;
          }
        }
      }
      if (probabilityOfSelection != 1.0 && !state.random[thread].nextBoolean(probabilityOfSelection)) {
        i = bad;
      }
      inds[start + q] = oldinds[i];  // note it's a pointer transfer, not a copy!
    }
    return n;
  }

  public void individualReplaced(final SteadyStateEvolutionState state,
                                 final int subpopulation,
                                 final int thread,
                                 final int individual) {
    return;
  }

  public void sourcesAreProperForm(final SteadyStateEvolutionState state) {
    return;
  }

}
