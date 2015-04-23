package uk.co.blogspot.rafaelzig.core.algorithm;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang3.ArrayUtils;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;

/**
 * Class responsible for generating all permutations, with repetitions, of
 * solutions of a binary valued problem.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
class BinaryBruteForceInitialization extends RecursiveTask<Solution[]> implements
		Initialization
{
	/**
	 * Generated serial version.
	 */
	private static final long	serialVersionUID	= 2709261090543619534L;

	/**
	 * Binary valued problem.
	 */
	private final Problem		problem;

	/**
	 * Initial cursor to be checked.
	 */
	private final int			start;

	/**
	 * Number of iterations to be performed.
	 */
	private final int			length;

	/**
	 * Limit of permutations to be calculated at one time.
	 */
	private final int			threshold			= 65536;

	/**
	 * Constructs a new instance with the specified problem and initialised
	 * values for cursor and length.
	 *
	 * @param problem
	 */
	BinaryBruteForceInitialization(Problem problem)
	{
		this(problem, 0, (int) Math.pow(2, problem.getNumberOfVariables()));
	}

	/**
	 * Constructs a new instance with the specified problem, initial cursor and
	 * max cursor.
	 *
	 * @param problem
	 *            : Binary-valued problem.
	 * @param start
	 *            : Initial cursor to be checked.
	 * @param length
	 *            : Number of iterations to be performed.
	 */
	BinaryBruteForceInitialization(Problem problem, int start, int length)
	{
		this.problem = problem;
		this.start = start;
		this.length = length;
	}

	@Override
	protected Solution[] compute()
	{
		if (length < threshold)
		{
			return computeDirectly();
		}

		return splitTask();
	}

	/**
	 * Computes the permutations directly.
	 *
	 * @return
	 */
	private Solution[] computeDirectly()
	{
		final Solution[] solutions = new Solution[length];

		for (int i = start; i < start + length; i++)
		{
			final String binaryString = Long.toBinaryString(i);
			final Solution solution = problem.newSolution();

			final int target = problem.getNumberOfVariables() - binaryString.length();

			for (int j = problem.getNumberOfVariables() - 1; j >= target; j--)
			{
				final BinaryVariable binary = (BinaryVariable) solution.getVariable(j);
				binary.set(
						0,
						binaryString.charAt(j - problem.getNumberOfVariables()
								+ binaryString.length()) == '0' ? false : true);
				solution.setVariable(j, binary);
			}

			for (int j = problem.getNumberOfVariables() - binaryString.length() - 1; j >= 0; j--)
			{
				final BinaryVariable binary = (BinaryVariable) solution.getVariable(j);
				binary.set(0, false);
				solution.setVariable(j, binary);
			}

			solutions[i % length] = solution;
		}

		return solutions;
	}

	@Override
	public Solution[] initialize()
	{
		return new ForkJoinPool().invoke(this);
	}

	/**
	 * Splits the task in two separate threads and returns the resulting
	 * permutations found by the two.
	 *
	 * @return Solution array containing the combined permutations found by the
	 *         two threads.
	 */
	private Solution[] splitTask()
	{
		final int split = length / 2;
		final BinaryBruteForceInitialization left = new BinaryBruteForceInitialization(
				problem, start, split);
		final BinaryBruteForceInitialization right = new BinaryBruteForceInitialization(
				problem, start + split, length - split);

		left.fork();
		final Solution[] rightSolutions = right.compute();
		final Solution[] leftSolutions = left.join();

		return ArrayUtils.addAll(leftSolutions, rightSolutions);
	}
}