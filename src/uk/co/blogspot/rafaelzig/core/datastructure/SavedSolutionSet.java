package uk.co.blogspot.rafaelzig.core.datastructure;

import java.util.List;

import org.moeaframework.core.Solution;

import uk.co.blogspot.rafaelzig.core.datastructure.template.ProblemTemplate;

public class SavedSolutionSet
{

	private final ProblemTemplate	problem;
	private final List<Solution>	solutions;
	private final String			algorithm;
	private final long				elapsed;

	public SavedSolutionSet(ProblemTemplate problem, List<Solution> solutions,
			String algorithm, long elapsed)
	{
		this.problem = problem;
		this.solutions = solutions;
		this.algorithm = algorithm;
		this.elapsed = elapsed;
	}

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm()
	{
		return algorithm;
	}

	/**
	 * @return the elapsed
	 */
	public long getElapsed()
	{
		return elapsed;
	}

	/**
	 * @return the problem
	 */
	public ProblemTemplate getProblem()
	{
		return problem;
	}

	/**
	 * @return the result
	 */
	public List<Solution> getSolutions()
	{
		return solutions;
	}
}
