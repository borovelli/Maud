package ec.vector;

import ec.*;
import ec.util.*;

import java.io.Serializable;
import java.util.*;
import java.io.*;

/*
 * LongVectorIndividual.java
 * Created: Tue Mar 13 15:03:12 EST 2001
 */

/**
 * LongVectorIndividual is a VectorIndividual whose genome is an array of longs.
 * Gene values may range from species.mingene(x) to species.maxgene(x), inclusive.
 * The default mutation method randomizes genes to new values in this range,
 * with <tt>species.mutationProbability</tt>.
 *

 <p><b>Default Base</b><br>
 vector.long-vect-ind

 * @author Sean Luke
 * @version 1.0
 */

public class LongVectorIndividual extends VectorIndividual {
  public static final String P_LONGVECTORINDIVIDUAL = "long-vect-ind";
  public long[] genome;

  public Parameter defaultBase() {
    return VectorDefaults.base().push(P_LONGVECTORINDIVIDUAL);
  }

  public Object protoClone() throws CloneNotSupportedException {
    LongVectorIndividual myobj = (LongVectorIndividual) (super.protoClone());

    // must clone the genome
    myobj.genome = (long[]) (genome.clone());

    return myobj;
  }

  public void setup(final EvolutionState state, final Parameter base) {
    Parameter def = defaultBase();

    if (!(species instanceof IntegerVectorSpecies))
      state.output.fatal("LongVectorIndividual requires an IntegerVectorSpecies", base, def);
    IntegerVectorSpecies s = (IntegerVectorSpecies) species;

    genome = new long[s.genomeSize];
  }

  public void defaultCrossover(EvolutionState state, int thread, VectorIndividual ind) {
    IntegerVectorSpecies s = (IntegerVectorSpecies) species;
    LongVectorIndividual i = (LongVectorIndividual) ind;
    long tmp;
    int point;

    if (genome.length != i.genome.length)
      state.output.fatal("Genome lengths are not the same for fixed-length vector crossover");
    switch (s.crossoverType) {
      case VectorSpecies.C_ONE_POINT:
        point = state.random[thread].nextInt((genome.length / s.chunksize) + 1);
        for (int x = 0; x < point * s.chunksize; x++) {
          tmp = i.genome[x];
          i.genome[x] = genome[x];
          genome[x] = tmp;
        }
        break;
      case VectorSpecies.C_TWO_POINT:
        int point0 = state.random[thread].nextInt((genome.length / s.chunksize) + 1);
        point = state.random[thread].nextInt((genome.length / s.chunksize) + 1);
        if (point0 > point) {
          int p = point0;
          point0 = point;
          point = p;
        }
        for (int x = point0 * s.chunksize; x < point * s.chunksize; x++) {
          tmp = i.genome[x];
          i.genome[x] = genome[x];
          genome[x] = tmp;
        }
        break;
      case VectorSpecies.C_ANY_POINT:
        for (int x = 0; x < genome.length / s.chunksize; x++)
          if (state.random[thread].nextBoolean(s.crossoverProbability))
            for (int y = x * s.chunksize; y < (x + 1) * s.chunksize; y++) {
              tmp = i.genome[y];
              i.genome[y] = genome[y];
              genome[y] = tmp;
            }
        break;
    }
  }

  /** Splits the genome into n pieces, according to points, which *must* be sorted.
   pieces.length must be 1 + points.length */
  public void split(int[] points, Object[] pieces) {
    int point0, point1;
    point0 = 0;
    point1 = points[0];
    for (int x = 0; x < pieces.length; x++) {
      pieces[x] = new long[point1 - point0];
      System.arraycopy(genome, point0, pieces[x], point0, point1 - point0);
      point0 = point1;
      if (x == pieces.length - 2)
        point1 = genome.length;
      else
        point1 = points[x + 1];
    }
  }

  /** Joins the n pieces and sets the genome to their concatenation.*/
  public void join(Object[] pieces) {
    int sum = 0;
    for (int x = 0; x < pieces.length; x++)
      sum += ((long[]) (pieces[x])).length;

    int runningsum = 0;
    long[] newgenome = new long[sum];
    for (int x = 0; x < pieces.length; x++) {
      System.arraycopy(pieces[x], 0, newgenome, runningsum, ((long[]) (pieces[x])).length);
      runningsum += ((long[]) (pieces[x])).length;
    }
    // set genome
    genome = newgenome;
  }

  /** Destructively mutates the individual in some default manner.  The default form
   simply randomizes genes to a uniform distribution from the min and max of the gene values. */
  public void defaultMutate(EvolutionState state, int thread) {
    IntegerVectorSpecies s = (IntegerVectorSpecies) species;
    if (s.individualGeneMinMaxUsed()) {
      if (s.mutationProbability > 0.0)
        for (int x = 0; x < genome.length; x++)
          if (state.random[thread].nextBoolean(s.mutationProbability))
            genome[x] = (s.minGene(x) + state.random[thread].nextLong(s.maxGene(x) - s.minGene(x) + 1));
    } else  // quite a bit faster
    {
      if (s.mutationProbability > 0.0)
        for (int x = 0; x < genome.length; x++)
          if (state.random[thread].nextBoolean(s.mutationProbability))
            genome[x] = (s.minGene + state.random[thread].nextLong(s.maxGene - s.minGene + 1));
    }
  }


  /** Initializes the individual by randomly choosing Longs uniformly from mingene to maxgene. */
  public void reset(EvolutionState state, int thread) {
    IntegerVectorSpecies s = (IntegerVectorSpecies) species;
    if (s.individualGeneMinMaxUsed())
      for (int x = 0; x < genome.length; x++)
        genome[x] = (s.minGene(x) + state.random[thread].nextLong(s.maxGene(x) - s.minGene(x) + 1));
    else // quite a bit faster
      for (int x = 0; x < genome.length; x++)
        genome[x] = (s.minGene + state.random[thread].nextLong(s.maxGene - s.minGene + 1));
  }

  public int hashCode() {
// stolen from GPIndividual.  It's a decent algorithm.
    int hash = this.getClass().hashCode();

    hash = (hash << 1 | hash >>> 31);
    for (int x = 0; x < genome.length; x++)
      hash = (hash << 1 | hash >>> 31) ^ (int) ((genome[x] >>> 16) & 0xFFFFFFFF) ^ (int) (genome[x] & 0xFFFF);

    return hash;
  }

  public void readIndividual(final EvolutionState state,
                             final LineNumberReader reader)
          throws IOException, CloneNotSupportedException {
// First, was I evaluated?
    int linenumber = reader.getLineNumber();
    String s = reader.readLine();
    if (s == null || s.length() < EVALUATED_PREAMBLE.length()) // uh oh
      state.output.fatal("Reading Line " + linenumber + ": " +
              "Bad 'Evaluated?' line.");
    DecodeReturn d = new DecodeReturn(s, EVALUATED_PREAMBLE.length());
    Code.decode(d);
    if (d.type != DecodeReturn.T_BOOLEAN)
      state.output.fatal("Reading Line " + linenumber + ": " +
              "Bad 'Evaluated?' line.");
    evaluated = (d.l != 0);

// Next, what's my fitness?
    fitness.readFitness(state, reader);

// get the size of the genome
    int lll = Integer.parseInt(reader.readLine());

    genome = new long[lll];

    for (int i = 0; i < genome.length; i++)
      genome[i] = Long.parseLong(reader.readLine());

  }

  public void printIndividualForHumans(final EvolutionState state,
                                       final int log,
                                       final int verbosity) {
    state.output.println(EVALUATED_PREAMBLE + Code.encode(evaluated),
            verbosity, log);
    fitness.printFitnessForHumans(state, log, verbosity);
    String s = "";
    for (int i = 0; i < genome.length; i++)
      s = s + " " + genome[i];
    state.output.println(s, verbosity, log);
  }

  public void printIndividual(final EvolutionState state,
                              final int log,
                              final int verbosity) {
    state.output.println(EVALUATED_PREAMBLE + Code.encode(evaluated),
            verbosity, log);
    fitness.printFitness(state, log, verbosity);
    state.output.println("" + genome.length, verbosity, log);
    for (int i = 0; i < genome.length; i++)
      state.output.println("" + genome[i], verbosity, log);
  }

  public void printIndividual(final EvolutionState state,
                              final PrintWriter writer) {
    writer.println(EVALUATED_PREAMBLE + Code.encode(evaluated));
    fitness.printFitness(state, writer);
    writer.println(genome.length);
    for (int i = 0; i < genome.length; i++)
      writer.println(genome[i]);
  }

  public boolean equals(Object ind) {
    if (!(this.getClass().equals(ind.getClass()))) return false; // SimpleRuleIndividuals are special.
    LongVectorIndividual i = (LongVectorIndividual) ind;
    if (genome.length != i.genome.length)
      return false;
    for (int j = 0; j < genome.length; j++)
      if (genome[j] != i.genome[j])
        return false;
    return true;
  }

  public Object getGenome() {
    return genome;
  }

  public void setGenome(Object gen) {
    genome = (long[]) gen;
  }

  public long genomeLength() {
    return genome.length;
  }
}
