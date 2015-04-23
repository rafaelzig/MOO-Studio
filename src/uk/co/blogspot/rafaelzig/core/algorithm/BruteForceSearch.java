package uk.co.blogspot.rafaelzig.core.algorithm;

import org.moeaframework.algorithm.AbstractAlgorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;

/**
 * Class responsible for evaluating and returning the Pareto optimal set by
 * means of brute force search.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 */
class BruteForceSearch extends AbstractAlgorithm
{
	/**
	 * The initialization routine used to generate the solutions.
	 */
	private final Initialization			generator;

	/**
	 * The archive of non-dominated solutions.
	 */
	private final NondominatedPopulation	archive;

	/**
	 * Constructs a new brute force search procedure for the given problem.
	 *
	 * @param problem
	 *            the problem being solved
	 * @param generator
	 *            the initialization routine used to generate solutions
	 * @param archive
	 *            the archive of non-dominated solutions
	 */
	BruteForceSearch(Problem problem, Initialization generator,
			NondominatedPopulation archive)
	{
		super(problem);
		this.generator = generator;
		this.archive = archive;
	}

	@Override
	public NondominatedPopulation getResult()
	{
		return archive;
	}

	@Override
	protected void initialize()
	{
		super.initialize();
		iterate();
	}

	@Override
	protected void iterate()
	{
		final Population solutions = new Population(generator.initialize());
		evaluateAll(solutions);
		archive.addAll(solutions);
	}
}