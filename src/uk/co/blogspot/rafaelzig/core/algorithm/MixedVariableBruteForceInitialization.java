package uk.co.blogspot.rafaelzig.core.algorithm;

import java.util.LinkedList;
import java.util.List;

import org.moeaframework.core.Initialization;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

/**
 * Class responsible for generating all permutations, with repetitions, of
 * solutions of a problem with mixed variables.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
class MixedVariableBruteForceInitialization implements Initialization
{
	/**
	 * Problem with mixed variables.
	 */
	private final Problem			problem;

	/**
	 * List to be used to store permutations of solutions found.
	 */
	private final List<Solution>	solutions;

	/**
	 * Array containing the current permutation of variables.
	 */
	private final double[]			current;

	/**
	 * Array containing the lower bounds of the variables of this problem.
	 */
	private final double[]			min;

	/**
	 * Array containing the upper bounds of the variables of this problem.
	 */
	private final double[]			max;

	/**
	 * Array containing the increments to be used for each of the variable of
	 * this problem.
	 */
	private final double[]			increment;

	/**
	 * Constructs a new instance with the specified problem, lower bounds and
	 * upper bounds and increment of variables, and initialises a new array to
	 * be used to hold the current permutation.
	 *
	 * @param problem
	 *            : Problem with mixed variables.
	 * @param solutions
	 *            : List to be used to store permutations of solutions found.
	 * @param min
	 *            : Array containing the lower bounds of the variables of this
	 *            problem.
	 * @param max
	 *            : Array containing the upper bounds of the variables of this
	 *            problem.
	 * @param increment
	 *            : Array containing the increments to be used for each of the
	 *            variable of this problem.
	 */
	MixedVariableBruteForceInitialization(Problem problem, double[] min, double[] max,
			double[] increment)
	{
		this(problem, new LinkedList<>(), min, max, increment, new double[problem
				.getNumberOfVariables()]);
	}

	/**
	 * * Constructs a new instance with the specified problem, list to be used
	 * to store solutions generated, lower bounds and upper bounds and increment
	 * of variables and current permutation.
	 *
	 * @param problem
	 *            : Problem with mixed variables.
	 * @param solutions
	 *            : List to be used to store permutations of solutions found.
	 * @param min
	 *            : Array containing the lower bounds of the variables of this
	 *            problem.
	 * @param max
	 *            : Array containing the upper bounds of the variables of this
	 *            problem.
	 * @param increment
	 *            : Array containing the increments to be used for each of the
	 *            variable of this problem.
	 * @param current
	 *            : Array containing the current permutation of variables.
	 */
	MixedVariableBruteForceInitialization(Problem problem, List<Solution> solutions,
			double[] min, double[] max, double[] increment, double[] current)
	{
		this.problem = problem;
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.solutions = solutions;
		this.current = current;
	}

	@Override
	public Solution[] initialize()
	{
		recurse(0);
		return solutions.toArray(new Solution[solutions.size()]);
	}

	/**
	 * Performs another recursion of the algorithm starting at the specified
	 * cursor.
	 *
	 * @param cursor
	 *            : Current cursor to be used during the iteration.
	 */
	private void recurse(int cursor)
	{
		for (double i = min[cursor]; i <= max[cursor]; i += increment[cursor])
		{
			current[cursor] = i;

			if (cursor == current.length - 1)
			{
				final Solution solution = problem.newSolution();

				for (int j = 0; j < current.length; j++)
				{
					final RealVariable var = (RealVariable) solution.getVariable(j);
					var.setValue(current[j]);
					solution.setVariable(j, var);
				}

				solutions.add(solution);

				continue;
			}

			recurse(cursor + 1);
		}
	}
}