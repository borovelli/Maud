package ec.parsimony;

import ec.*;
import ec.util.*;

import java.io.Serializable;

import ec.steadystate.*;
import ec.select.*;

/*
 * TournamentSelection.java
 *
 * Created: Mon Aug 30 19:27:15 1999
 * By: Sean Luke
 */

/**
 * Does a simple tournament selection, limited to the subpopulation it's
 * working in at the time.
 *
 * <p>Tournament selection works like this: first, <i>size</i> individuals
 * are chosen at random from the population.  Then of those individuals,
 * the one with the best fitness is selected.
 *
 * <p>Common sizes for <i>size</i> include: 2, popular in Genetic Algorithms
 * circles, and 7, popularized in Genetic Programming by John Koza.
 * If the size is 1, then individuals are picked entirely at random.
 * The default value here is 7.
 *
 * <p>Tournament selection is so simple that it doesn't need to maintain
 * a cache of any form, so many of the SelectionMethod methods just
 * don't do anything at all.
 *

 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 Always 1.

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base.</i><tt>size</tt><br>
 <font size=-1>int &gt;= 1</font></td>
 <td valign=top>(the tournament size)</td></tr>

 <tr><td valign=top><i>base.</i><tt>pick-worst</tt><br>
 <font size=-1> bool = <tt>true</tt> or <tt>false</tt> (default)</font></td>
 <td valign=top>(should we pick the <i>worst</i> individual in the tournament instead of the <i>best</i>?)</td></tr>

 </table>

 <p><b>Default Base</b><br>
 select.tournament

 *
 * @author Sean Luke
 * @version 1.0
 */

public class ParsimonyBinaryTournament extends SelectionMethod implements SteadyStateBSourceForm {
  /** default base */
  public static final String P_TOURNAMENT = "tournament";

  public static final String P_PICKWORST = "pick-worst";

  public static final String P_SIZEPROB = "size-prob";

  /** size parameter */
  public static final String P_SIZE = "size";

  /** Default size */
  public static final int DEFAULT_SIZE = 2;

  /** Size of the tournament*/
  public int size;

  /** The probability to choose individuals based on size and independent of fitness */
  public float sizeprob;

  /** Do we pick the worst instead of the best? */
  public boolean pickWorst;

  public Parameter defaultBase() {
    return SelectDefaults.base().push(P_TOURNAMENT);
  }

  public void setup(final EvolutionState state, final Parameter base) {
    super.setup(state, base);

    Parameter def = defaultBase();

    size = state.parameters.getInt(base.push(P_SIZE), def.push(P_SIZE), 1);
    if (size < 1)
      state.output.fatal("Tournament size must be >= 1.", base.push(P_SIZE), def.push(P_SIZE));

    sizeprob = state.parameters.getFloat(base.push(P_SIZEPROB), null, 0);
    if (sizeprob < 0 || sizeprob > 1)
      state.output.fatal("The size-prob parameter should be between 0 and 1.", base.push(P_SIZEPROB));

    pickWorst = state.parameters.getBoolean(base.push(P_PICKWORST), def.push(P_PICKWORST), false);
  }


  // I hard-code both produce(...) methods for efficiency's sake

  public int produce(final int subpopulation,
                     final EvolutionState state,
                     final int thread) {
// pick size random individuals, then pick the best.
    Individual[] oldinds = state.population.subpops[subpopulation].individuals;
    int i = state.random[thread].nextInt(oldinds.length);
    long si = oldinds[i].size();

    for (int x = 1; x < size; x++) {
      int j = state.random[thread].nextInt(oldinds.length);
      if (pickWorst) {
        if (state.random[thread].nextBoolean(sizeprob)) {
          long sj = oldinds[j].size();
          if (si < sj) {
            si = sj;
            i = j;
          }
        } else {
          if (!(oldinds[j].fitness.betterThan(oldinds[i].fitness))) {
            i = j;
            si = oldinds[i].size();
          }
        }
      } else {
        if (state.random[thread].nextBoolean(sizeprob)) {
          long sj = oldinds[j].size();
          if (si > sj) {
            si = sj;
            i = j;
          }
        } else {
          if (oldinds[j].fitness.betterThan(oldinds[i].fitness)) {
            i = j;
            si = oldinds[i].size();
          }
        }
      }
    }
    return i;
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
