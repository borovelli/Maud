package ec.multiobjective.spea2;

import java.io.*;

import ec.util.DecodeReturn;
import ec.util.Parameter;
import ec.util.Code;
import ec.Fitness;
import ec.EvolutionState;
import ec.Subpopulation;
import ec.Prototype;

/*
 * SPEA2MultiObjectiveFitness.java
 *
 * Created: Wed Jun 26 11:20:32 PDT 2002
 * By: Robert Hubley, Institute for Systems Biology
 *     (based on MultiObjectiveFitness.java by Sean Luke)
 */

/**
 * SPEA2MultiObjectiveFitness is a subclass of Fitness which implements
 * basic multiobjective fitness functions along with support for the
 * ECJ SPEA2 (Strength Pareto Evolutionary Algorithm) extensions.
 *
 * <p>The object contains two items: an array of floating point values
 * representing the various multiple fitnesses (ranging from 0.0 (worst)
 * to infinity (best)), and a single SPEA2 fitness value which represents
 * the individual's overall fitness ( a function of the number of
 * individuals it dominates and it's raw score where 0.0 is the best).

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>numobjectives</tt><br>
 (else)<tt>multi.numobjectives</tt><br>
 <font size=-1>int &gt;= 1</font></td>
 <td valign=top>(the number of fitnesses in the multifitness array)</td></tr>
 </table>

 <p><b>Default Base</b><br>
 spea2.fitness

 * @author Robert Hubley (based on MultiObjectiveFitness by Sean Luke)
 * @version 1.0
 */

public class SPEA2MultiObjectiveFitness implements Fitness {
  public static final String FITNESS_PREAMBLE = "Multifitness: [";
  public static final String FITNESS_POSTAMBLE = "]";
  public static final String SPEA2FIT_PREAMBLE = "SPEA2Fitness: ";

  /** base parameter for defaults */
  public static final String P_FITNESS = "SPEA2fitness";

  /** parameter for size of multifitness */
  public static final String P_NUMFITNESSES = "numobjectives";

  /** The various fitnesses. */
  public float[] multifitness; // values range from 0 (worst) to infinity (best)

  /** SPEA2 overall fitness */
  public double SPEA2Fitness;

  /** SPEA2 strength (# of nodes it dominates) */
  public double SPEA2Strength;

  /** SPEA2 RAW fitness */
  public double SPEA2RawFitness;

  /** SPEA2 NN distance */
  public double SPEA2kthNNDistance;

  public final Parameter defaultBase() {
    return new Parameter(P_FITNESS);
  }

  public Object protoClone() throws CloneNotSupportedException {
    SPEA2MultiObjectiveFitness f = (SPEA2MultiObjectiveFitness) (super.clone());
    f.multifitness = (float[]) (multifitness.clone());  // cloning an array
    return f;
  }

  /** Returns the sum of the squared differences between the vector
   fitness values.
   */
  public float calcDistance(SPEA2MultiObjectiveFitness otherFit) {
    float s = 0;
    for (int i = 0; i < multifitness.length; i++) {
      s += (multifitness[i] - otherFit.multifitness[i]) *
              (multifitness[i] - otherFit.multifitness[i]);
    }
    return s;
  }

  public Object protoCloneSimple() {
    try {
      return protoClone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    } // never happens
  }

  /** Returns the Max() of multifitnesses, which adheres to Fitness.java's
   protocol for this method. Though you should not rely on a selection
   or statistics method which requires this.
   */
  public float fitness() {
    float fit = multifitness[0];
    for (int x = 1; x < multifitness.length; x++) {
      if (fit < multifitness[x]) {
        fit = multifitness[x];
      }
    }
    return fit;
  }

  /** Sets up.  This must be called at least once in the prototype
   before instantiating any fitnesses that will actually be used
   in evolution. */
  public void setup(EvolutionState state, Parameter base) {
    Parameter def = defaultBase();
    int numFitnesses;

    numFitnesses = state.parameters.getInt(
            base.push(P_NUMFITNESSES), def.push(P_NUMFITNESSES), 0);
    if (numFitnesses <= 0)
      state.output.fatal("The number of objectives must be an integer >0.",
              base.push(P_NUMFITNESSES), def.push(P_NUMFITNESSES));

    multifitness = new float[numFitnesses];
  }

  /** Returns false.  In this multiobjective implementation we have no idea
   what is ideal (since scores go between 0-infinity) so
   we punt this to our subclasses and always return false. */
  public boolean isIdealFitness() {
    // Punt.
    return false;
  }

  /** Returns true if I'm equivalent in fitness (neither better nor worse)
   to _fitness. The rule I'm using is this:
   If one of us is better in one or more criteria, and we are equal in
   the others, then equivalentTo is false.  If each of us is better in
   one or more criteria each, or we are equal in all criteria, then
   equivalentTo is true.
   */
  public boolean equivalentTo(Fitness _fitness) {
    boolean abeatsb = false;
    boolean bbeatsa = false;
    for (int x = 0; x <
            // just to be safe...
            Math.min(multifitness.length,
                    ((SPEA2MultiObjectiveFitness) _fitness).multifitness.length);
         x++) {
      if (multifitness[x] >
              ((SPEA2MultiObjectiveFitness) _fitness).multifitness[x])
        abeatsb = true;
      if (multifitness[x] <
              ((SPEA2MultiObjectiveFitness) _fitness).multifitness[x])
        bbeatsa = true;
      if (abeatsb && bbeatsa) return true;
    }
    if (abeatsb || bbeatsa) return false;
    return true;
  }

  /** Returns true if I'm better than _fitness. The rule I'm using is this:
   if I am better in one or more criteria, and we are equal in the others,
   then betterThan is true, else it is false. */
  public boolean betterThan(Fitness _fitness) {
    boolean abeatsb = false;
    for (int x = 0; x <
            // just to be safe...
            Math.min(multifitness.length,
                    ((SPEA2MultiObjectiveFitness) _fitness).multifitness.length);
         x++) {
      if (multifitness[x] >
              ((SPEA2MultiObjectiveFitness) _fitness).multifitness[x])
        abeatsb = true;
      if (multifitness[x] <
              ((SPEA2MultiObjectiveFitness) _fitness).multifitness[x])
        return false;
    }
    return abeatsb;
  }

  /** Prints the fitness in the computer-readable form:
   <p><tt> Fitness: [</tt><i>fitness values encoded with ec.util.Code, separated by spaces</i><tt>]</tt>
   */
  public void printFitness(EvolutionState state, final int log,
                           final int verbosity) {
    String s = FITNESS_PREAMBLE;
    for (int x = 0; x < multifitness.length; x++) {
      if (x > 0) s = s + " ";
      s = s + Code.encode(multifitness[x]);
    }
    state.output.println(s + FITNESS_POSTAMBLE, verbosity, log);
    // Write out SPEA2Fitness
    state.output.println(SPEA2FIT_PREAMBLE + Code.encode(SPEA2Fitness), verbosity, log);
  }

  /** Prints the fitness in the computer-readable form:
   <p><tt> Fitness: [</tt><i>fitness values encoded with ec.util.Code, separated by spaces</i><tt>]</tt>
   */
  public void printFitness(final EvolutionState state,
                           final PrintWriter writer) {
    String s = FITNESS_PREAMBLE;
    for (int x = 0; x < multifitness.length; x++) {
      if (x > 0) s = s + " ";
      s = s + Code.encode(multifitness[x]);
    }
    writer.println(s + FITNESS_POSTAMBLE);

    // Write out SPEA2Fitness
    writer.println(SPEA2FIT_PREAMBLE + Code.encode(SPEA2Fitness));

  }

  /** Prints the fitness in the human-readable form:
   <p><tt> Fitness: [</tt><i>fitness values separated by spaces</i><tt>]</tt>
   */
  public void printFitnessForHumans(final EvolutionState state,
                                    final int log,
                                    final int verbosity) {
    String s = "[";
    for (int x = 0; x < multifitness.length; x++) {
      if (x > 0) s = s + " ";
      s = s + multifitness[x];
    }
    s = s + "] ";
    s = s + " S=" + SPEA2Strength;
    s = s + ", R=" + SPEA2RawFitness;
    s = s + ", D=" + SPEA2kthNNDistance;
    s = s + ", F=" + SPEA2Fitness;
    s = s + " ";
    state.output.print(s, verbosity, log);
  }


  public void readFitness(final EvolutionState state,
                          final LineNumberReader reader)
          throws IOException, CloneNotSupportedException {
    int linenumber = reader.getLineNumber();
    String s = reader.readLine();
    if (s == null || s.length() < FITNESS_PREAMBLE.length()) // uh oh
      state.output.fatal("Reading Line " + linenumber + ": " +
              "Bad Multifitness.");
    DecodeReturn d = new DecodeReturn(s, FITNESS_PREAMBLE.length());
    for (int x = 0; x < multifitness.length; x++) {
      Code.decode(d);
      if (d.type != DecodeReturn.T_FLOAT)
        state.output.fatal("Reading Line " + linenumber + ": " +
                "Bad Fitness (multifitness value #" + x + ").");
      multifitness[x] = (float) d.d;
    }

    // Read in the SPEA2Fitness
    linenumber++;
    s = reader.readLine();
    if (s == null || s.length() < SPEA2FIT_PREAMBLE.length()) // uh oh
      state.output.fatal("Reading Line " + linenumber + ": " +
              "Bad SPEA2Fitness.");
    d = new DecodeReturn(s, SPEA2FIT_PREAMBLE.length());
    Code.decode(d);
    if (d.type != DecodeReturn.T_DOUBLE)
      state.output.fatal("Reading Line " + linenumber + ": " +
              "Bad '" + SPEA2FIT_PREAMBLE + "' line.");
    SPEA2Fitness = d.d;

    // NOTE: At this time I am not reading/writing the SPEA2 strength, raw,
    //       and distance values.  These are intermediate values to the
    //       overal SPEA2Fitness and so are not really worth preserving.

  }


}
