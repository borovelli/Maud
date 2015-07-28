package ec.coevolve;

import ec.simple.SimpleEvaluator;
import ec.simple.SimpleFitness;
import ec.*;
import ec.util.*;

/**
 * CompetitiveEvaluator.java
 *

 <p>CompetitiveEvaluator is a Evaluator which performs <i>competitive fitness evaluations</i>.
 Competitive fitness is where individuals' fitness is determined by testing them against
 other members of the same subpopulation.  Competitive fitness topologies differ from
 co-evolution topologies in that co-evolution is a term I generally reserve for
 multiple sbupopulations which breed separately but compete against other subpopulations
 during evaluation time.  Individuals are evaluated regardless of whether or not they've
 been evaluated in the past.

 <p>Your Problem is responsible for setting up the fitness appropriately.
 CompetitiveEvaluator expects to use Problems which adhere to the GroupedProblemForm interface,
 which defines a new evaluate(...) function, plus a preprocess(...) and postprocess(...) function.

 <p>This competitive fitness evaluator is single-threaded -- maybe we'll hack in multithreading later.
 And it only has two individuals competing during any fitness evaluation.  The order of individuals in the
 subpopulation will be changed during the evaluation process.  There are five evaluation topologies
 presently supported:

 <p><dl>
 <dt><b>Single Elimination Tournament</b><dd>
 All members of the population are paired up and evaluated.  In each pair, the "winner" is the individual
 which winds up with the superior fitness.  If neither fitness is superior, then the "winner" is picked
 at random.  Then all the winners are paired up and evaluated, and so on, just like in a single elimination
 tournament.  The fitness of all individuals is then changed as follows:

 <dt><b>Round Robin</b><dd>
 Every member of the population are paired up and evaluated with all other members of the population, not
 not including the member itself (we might add in self-play as a future later if people ask for it, it's
 easy to hack in).

 <dt><b>Pseudo Round Robin</b><dd>
 The population is split into groups with <i>group-size</i> individuals, and there is a round robin
 tournament for each such group.

 <dt><b>K-Random-Opponents-One-Way</b><dd>
 Each individual's fitness is calculated based on K competitions against random opponents.
 For details, see "A Comparison of Two Competitive Fitness Functions" by Liviu Panait and
 Sean Luke in the Proceedings of GECCO 2002.

 <dt><b>K-Random-Opponents-Two-Ways</b><dd>
 Each individual's fitness is calculated based on K competitions against random opponents. The advantage of
 this method over <b>K-Random-Opponents-One-Way</b> is a reduced number of competitions (when I competes
 against J, both I's and J's fitnesses are updated, while in the previous method only one of the individuals
 has its fitness updated).
 For details, see "A Comparison of Two Competitive Fitness Functions" by Liviu Panait and
 Sean Luke in the Proceedings of GECCO 2002.
 </dl>

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base.</i><tt>style</tt><br>
 <font size=-1>string with possible values: </font></td>
 <td valign=top>(the style of the tournament)<br>
 <i>single-elim-tournament</i> (a single elimination tournament)<br>
 <i>round-robin</i> (a round robin tournament)<br>
 <i>pseudo-round-robin</i> (population is split into groups with <i>group-size</i> individuals, and there is a round robin tournament for each such group)<br>
 <i>rand-1-way</i> (K-Random-Opponents, each game counts for only one of the players)<br>
 <i>rand-2-ways</i> (K-Random-Opponents, each game counts for both players)<br>
 </td></tr>

 <tr><td valign=top><i>base.</i><tt>group-size</tt><br>
 <font size=-1> int</font></td>
 <td valign=top>(how many individuals per group, used in <i>pseudo-round-robin, rand-1-way</i> and <i>rand-2-ways</i> tournaments)<br>
 <i>group-size</i> &gt;= 2 for <i>pseudo-round-robin</i><br>
 <i>group-size</i> &gt;= 1 for <i>rand-1-way</i> or <i>rand-2-ways</i><br>
 </td></tr>

 <tr><td valign=top><i>base.</i><tt>over-eval</tt><br>
 <font size=-1> bool = <tt>true</tt> or <tt>false</tt> (default)</font></td>
 <td valign=top>(if the tournament style leads to an individual playing more games than others, should the extra games be used for his fitness evaluatiuon?)</td></tr>

 <tr><td valign=top><i>base.</i><tt>repeat-play</tt><br>
 <font size=-1> int &gt;= 1 (default)</font></td>
 <td valign=top>(the number of games played between two individuals when they are compared, used for noisy competitions)
 </td></tr>
 </table>

 *
 * @author Sean Luke & Liviu Panait
 * @version 1.0
 */

public class CompetitiveEvaluator extends Evaluator {
  public static final int STYLE_SINGLE_ELIMINATION = 1;
  public static final int STYLE_ROUND_ROBIN = 2;
  public static final int STYLE_N_RANDOM_COMPETITORS_ONEWAY = 3;
  public static final int STYLE_N_RANDOM_COMPETITORS_TWOWAY = 4;
  public static final int STYLE_PSEUDO_ROUND_ROBIN = 5;
  public static final String competeStyle = "style";
  public int style;

  public static final String size = "group-size";
  public int groupSize;

  public static final String overEval = "over-eval";
  public boolean allowOverEvaluation;

  /**
   How many times to repeat the play against one opponent.
   Default value: 1
   To be adjusted in noisy problems!
   */
  public static final String P_N_GAMES = "repeat-play";
  public int nGames;

  public void setup(final EvolutionState state, final Parameter base) {
    super.setup(state, base);
    String temp;
    temp = state.parameters.getStringWithDefault(base.push(competeStyle), null, "");
    if (temp.equalsIgnoreCase("single-elim-tournament")) {
      style = STYLE_SINGLE_ELIMINATION;
    } else if (temp.equalsIgnoreCase("round-robin")) {
      style = STYLE_ROUND_ROBIN;
    } else if (temp.equalsIgnoreCase("pseudo-round-robin")) {
      style = STYLE_PSEUDO_ROUND_ROBIN;
    } else if (temp.equalsIgnoreCase("rand-1-way")) {
      style = STYLE_N_RANDOM_COMPETITORS_ONEWAY;
    } else if (temp.equalsIgnoreCase("rand-2-ways")) {
      style = STYLE_N_RANDOM_COMPETITORS_TWOWAY;
    } else {
      state.output.fatal("Incorrect value for parameter. Acceptable values: " +
              "single-elim-tournament, round-robin, rand-1-way, rand-2-ways", base.push(competeStyle));
    }

    if (style == STYLE_N_RANDOM_COMPETITORS_ONEWAY || style == STYLE_N_RANDOM_COMPETITORS_TWOWAY) {
      groupSize = state.parameters.getInt(base.push(size), null, 1);
      if (groupSize < 1) {
        state.output.fatal("Incorrect value for parameter", base.push(size));
      }
    }
    if (style == STYLE_PSEUDO_ROUND_ROBIN) {
      groupSize = state.parameters.getInt(base.push(size), null, 2);
      if (groupSize < 2) {
        state.output.fatal("Incorrect value for parameter. It should be >= 2.", base.push(size));
      }
    }

    nGames = state.parameters.getInt(base.push(P_N_GAMES), null, 1);
    if (nGames < 1) {
      state.output.message("Incorrect value for parameter " +
              base.push(P_N_GAMES).toString() +
              ", using default value 1.");
      nGames = 1;
    }

    allowOverEvaluation = state.parameters.getBoolean(base.push(overEval), null, false);

  }

  public boolean runComplete(final EvolutionState state) {
    return false;
  }

  public void randomizeOrder(final EvolutionState state, final Individual[] individuals) {
    // copy the inds into a new array, then dump them randomly into the
    // subpopulation again
    Individual[] queue = new Individual[individuals.length];
    int len = queue.length;
    System.arraycopy(individuals, 0, queue, 0, len);

    for (int x = len; x > 0; x--) {
      int i = state.random[0].nextInt(x);
      individuals[x - 1] = queue[i];
      // get rid of queue[i] by swapping the highest guy there and then
      // decreasing the highest value  :-)
      queue[i] = queue[x - 1];
    }
  }

  public void evaluatePopulation(final EvolutionState state) {
    randomizeOrder(state, state.population.subpops[0].individuals);

    GroupedProblemForm prob = (GroupedProblemForm) (p_problem.protoCloneSimple());
    for (int i = 0; i < state.population.subpops.length; i++)
      prob.preprocessPopulation(state, state.population);

    switch (style) {
      case STYLE_SINGLE_ELIMINATION:
        evalSingleElimination(state, state.population.subpops[0].individuals, prob);
        break;
      case STYLE_ROUND_ROBIN:
        evalRoundRobin(state, state.population.subpops[0].individuals, prob);
        break;
      case STYLE_N_RANDOM_COMPETITORS_ONEWAY:
        evalNRandomOneWay(state, state.population.subpops[0].individuals, prob);
        break;
      case STYLE_N_RANDOM_COMPETITORS_TWOWAY:
        evalNRandomTwoWay(state, state.population.subpops[0].individuals, prob);
        break;
      case STYLE_PSEUDO_ROUND_ROBIN:
        evalPseudoRoundRobin(state, state.population.subpops[0].individuals, prob);
        break;
    }

    prob.postprocessPopulation(state, state.population);

  }

  public void evalSingleElimination(final EvolutionState state,
                                    final Individual[] individuals,
                                    final GroupedProblemForm prob) {
    // for a single-elimination tournament, the subpop[0] size must be 2^n for
    // some value n.  We don't check that here!  Check it in setup.

    // create the tournament array
    Individual[] tourn = new Individual[individuals.length];
    int len = tourn.length;
    System.arraycopy(individuals, 0, tourn, 0, len);

    Individual[] competition = new Individual[2];
    boolean[] updates = new boolean[2];
    updates[0] = updates[1] = true;

    // the "top half" of our array will be losers.
    // the bottom half will be winners.  Then we cut our array in half and repeat.

    while (len > 1) {
      for (int x = 0; x < len / 2; x++) {
        competition[0] = tourn[x];
        competition[1] = tourn[x + len / 2];
// reset the fitnesses to 0
        ((SimpleFitness) (tourn[x].fitness)).setFitness(state, 0.0f, false);
        ((SimpleFitness) (tourn[x + len / 2].fitness)).setFitness(state, 0.0f, false);
        for (int z = 0; z < nGames; z++) {
          prob.evaluate(state, competition, updates, 0);
        }
// if the first individual has worse fitness than last one, swap them
        if (tourn[x + len / 2].fitness.betterThan(tourn[x].fitness) ||
                (tourn[x].fitness.equivalentTo(tourn[x + len / 2].fitness) &&
                state.random[0].nextBoolean())) {
          Individual temp = tourn[x];
          tourn[x] = tourn[x + len / 2];
          tourn[x + len / 2] = temp;
        }
// set a low value for the losers (smaller than all players than won this round, but larger than
// the one of all players that lost in previous round). 1/len seems to be a good value!
        ((SimpleFitness) (tourn[x + len / 2].fitness)).setFitness(state, 1.0f / len, false);
      }

      // last part of the tournament: deal with odd values of len!
      if (len % 2 != 0) {
// swap the last element with a random one (the last element should play next round)
// and move first looser in the position of the last element
        int pos = state.random[0].nextInt(len / 2);
        Individual temp = tourn[len - 1];
        tourn[len - 1] = tourn[len / 2];
        tourn[len / 2] = tourn[pos];
        tourn[pos] = temp;
        len = 1 + len / 2;
      } else {
        len /= 2;
      }
    }

// set 1 as the fitness of the first individual
    ((SimpleFitness) (tourn[0].fitness)).setFitness(state, 1.0f, false);

  }

  public void evalRoundRobin(final EvolutionState state,
                             final Individual[] individuals,
                             final GroupedProblemForm prob) {
    Individual[] competition = new Individual[2];
    boolean[] updates = new boolean[2];
    updates[0] = updates[1] = true;

    for (int x = 0; x < individuals.length; x++)
      for (int y = x + 1; y < individuals.length; y++) {
        competition[0] = individuals[x];
        competition[1] = individuals[y];
        for (int z = 0; z < nGames; z++) {
          prob.evaluate(state, competition, updates, 0);
        }
      }
  }

  public void evalPseudoRoundRobin(final EvolutionState state,
                                   final Individual[] individuals,
                                   final GroupedProblemForm prob) {

    Individual[] competition = new Individual[2];
    boolean[] updates = new boolean[2];
    updates[0] = updates[1] = true;

    for (int i = 0; i < individuals.length; i += groupSize) {
      int last = Math.min(i + groupSize, individuals.length);

      // if there would be only one individual left after this tournament, include him here....
      if (last == individuals.length - 1)
        last = individuals.length;

      for (int x = i; x < last; x++)
        for (int y = x + 1; y < last; y++) {
          competition[0] = individuals[x];
          competition[1] = individuals[y];
          for (int z = 0; z < nGames; z++) {
            prob.evaluate(state, competition, updates, 0);
          }
        }
    }
  }

  public void evalNRandomOneWay(final EvolutionState state,
                                final Individual[] individuals,
                                final GroupedProblemForm prob) {
    Individual[] queue = new Individual[individuals.length];
    int len = queue.length;
    System.arraycopy(individuals, 0, queue, 0, len);

    Individual[] competition = new Individual[2];
    boolean[] updates = new boolean[2];
    updates[0] = true;
    updates[1] = false;

    for (int x = 0; x < individuals.length; x++) {
      competition[0] = individuals[x];
      // fill up our tournament
      for (int y = 0; y < groupSize; y++) {
        // swap to end and remove
        int index = state.random[0].nextInt(len - y);
        competition[1] = queue[index];
        queue[index] = queue[len - y - 1];
        queue[len - y - 1] = competition[1];
        for (int z = 0; z < nGames; z++) {
          prob.evaluate(state, competition, updates, 0);
        }
      }
    }
  }

  public void evalNRandomTwoWay(final EvolutionState state,
                                final Individual[] individuals,
                                final GroupedProblemForm prob) {

    class EncapsulatedIndividual {
      public Individual ind;
      public int nOpponentsMet;

      public EncapsulatedIndividual(Individual ind_, int value_) {
        ind = ind_;
        nOpponentsMet = value_;
      }
    }
    ;

// the number of games played for each player
    EncapsulatedIndividual[] individualsOrdered = new EncapsulatedIndividual[individuals.length];
    EncapsulatedIndividual[] queue = new EncapsulatedIndividual[individuals.length];
    for (int i = 0; i < individuals.length; i++) {
      individualsOrdered[i] = new EncapsulatedIndividual(individuals[i], 0);
    }

    Individual[] competition = new Individual[2];
    boolean[] updates = new boolean[2];
    updates[0] = true;

    for (int x = 0; x < individuals.length; x++) {
      System.arraycopy(individualsOrdered, 0, queue, 0, queue.length);
      competition[0] = queue[x].ind;

      // if the rest of individuals is not enough to fill
      // all games remaining for the current individual
      // (meaning that the current individual has left a
      // lot of games to play versus players with index
      // greater than his own), then it should play with
      // all. In the end, we should check that he finished
      // all the games he needs. If he did, everything is
      // ok, otherwise he should play with some other players
      // with index smaller than his own, but all these games
      // will count only for his fitness evaluation, and
      // not for the opponents'.

      // if true, it means that he has to play against all opponents with greater index
      if (individuals.length - x - 1 <= groupSize - queue[x].nOpponentsMet) {
        for (int y = x + 1; y < queue.length; y++) {
          competition[1] = queue[y].ind;
          updates[1] = (queue[y].nOpponentsMet < groupSize) || allowOverEvaluation;
          for (int z = 0; z < nGames; z++) {
            prob.evaluate(state, competition, updates, 0);
          }
          queue[x].nOpponentsMet++;
          if (updates[1])
            queue[y].nOpponentsMet++;
        }
      } else // here he has to play against a selection of the opponents with greater index
      {
// we can use the queue structure because we'll just rearrange the indexes
// but we should make sure we also rearrange the other vectors referring to the individuals

        for (int y = 0; groupSize > queue[x].nOpponentsMet; y++) {
          // swap to the end and remove from list
          int index = state.random[0].nextInt(queue.length - x - 1 - y) + x + 1;
          competition[1] = queue[index].ind;

          updates[1] = (queue[index].nOpponentsMet < groupSize) || allowOverEvaluation;
          for (int z = 0; z < nGames; z++) {
            prob.evaluate(state, competition, updates, 0);
          }
          queue[x].nOpponentsMet++;
          if (updates[1])
            queue[index].nOpponentsMet++;

          // swap the players (such that a player will not be considered twice)
          EncapsulatedIndividual temp = queue[index];
          queue[index] = queue[queue.length - y - 1];
          queue[queue.length - y - 1] = temp;

        }

      }

      // if true, it means that the current player needs to play some games with other players with lower indexes.
      // this is an unfortunate situation, since all those players have already had their groupSize games for the evaluation
      if (queue[x].nOpponentsMet < groupSize) {
        for (int y = queue[x].nOpponentsMet; y < groupSize; y++) {
          // select a random opponent with smaller index (don't even care for duplicates)
          int index;
          if (x > 0) // if x is 0, then there are no players with smaller index, therefore pick a random one
            index = state.random[0].nextInt(x);
          else
            index = state.random[0].nextInt(queue.length - 1) + 1;
          // use the opponent for the evaluation
          competition[1] = queue[index].ind;
          updates[1] = (queue[index].nOpponentsMet < groupSize) || allowOverEvaluation;
          for (int z = 0; z < nGames; z++) {
            prob.evaluate(state, competition, updates, 0);
          }
          queue[x].nOpponentsMet++;
          if (updates[1])
            queue[index].nOpponentsMet++;

        }
      }

    }
  }

}





