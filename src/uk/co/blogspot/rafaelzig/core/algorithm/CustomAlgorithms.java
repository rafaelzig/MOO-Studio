package uk.co.blogspot.rafaelzig.core.algorithm;

import java.util.Properties;

import org.moeaframework.core.Algorithm;
import org.moeaframework.core.FrameworkException;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.spi.AlgorithmProvider;
import org.moeaframework.core.spi.ProviderNotFoundException;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.util.TypedProperties;

/**
 * Custom algorithm provider which handles creation and instantiation of the
 * corresponding algorithms.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class CustomAlgorithms extends AlgorithmProvider
{
	/**
	 * Constructs the custom algorithm provider.
	 */
	public CustomAlgorithms()
	{
		super();
	}

	/**
	 * Returns {@code true} if all decision variables are assignment-compatible
	 * with the specified type; {@code false} otherwise.
	 *
	 * @param type
	 *            the type of decision variable
	 * @param problem
	 *            the problem
	 * @return {@code true} if all decision variables are assignment-compatible
	 *         with the specified type; {@code false} otherwise
	 */
	private boolean checkType(Class<? extends Variable> type, Problem problem)
	{
		final Solution solution = problem.newSolution();

		for (int i = 0; i < solution.getNumberOfVariables(); i++)
		{
			if (!type.isInstance(solution.getVariable(i)))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public Algorithm getAlgorithm(String name, Properties properties, Problem problem)
	{
		final TypedProperties typedProperties = new TypedProperties(properties);

		try
		{
			if (name.equalsIgnoreCase("BruteForce"))
			{
				return newBruteForceSearch(typedProperties, problem);
			}
			else if (name.equalsIgnoreCase("SuperMOEA"))
			{
				return newSuperMOEA(typedProperties, problem); // TODO
			}
			else
			{
				return null;
			}
		}
		catch (final FrameworkException e)
		{
			throw new ProviderNotFoundException(name, e);
		}
	}

	/**
	 * Instantiates and returns a brute force algorithm to be used by the
	 * specified problem.
	 *
	 * @param properties
	 * @param problem
	 * @return
	 */
	private Algorithm newBruteForceSearch(TypedProperties properties, Problem problem)
	{
		Initialization generator;

		if (checkType(BinaryVariable.class, problem))
		{
			if (problem.getNumberOfVariables() >= 20)
			{
				throw new FrameworkException("too many decision variables");
			}

			generator = new BinaryBruteForceInitialization(problem);
		}
		else
		{
			if (problem.getNumberOfVariables() >= 8)
			{
				throw new FrameworkException("too many decision variables");
			}
			else
			{
				final double[] min = new double[problem.getNumberOfVariables()];
				final double[] max = new double[problem.getNumberOfVariables()];
				final double[] increment = new double[problem.getNumberOfVariables()];

				final Solution tmp = problem.newSolution();
				final String[] types = properties
						.getStringArray(
								uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Algorithm.BruteForce
										.name(), new String[0]);

				for (int i = 0; i < problem.getNumberOfVariables(); i++)
				{
					switch (types[i])
					{
						case "BINARY":
							min[i] = 0;
							max[i] = 1;
							increment[i] = 1.0;
							break;
						case "INTEGER":
							min[i] = ((RealVariable) tmp.getVariable(i)).getLowerBound();
							max[i] = Math.floor(((RealVariable) tmp.getVariable(i))
									.getUpperBound());
							increment[i] = 1.0;
							break;
						case "REAL":
							min[i] = ((RealVariable) tmp.getVariable(i)).getLowerBound();
							max[i] = ((RealVariable) tmp.getVariable(i)).getUpperBound();
							increment[i] = 0.015625;
							break;
					}
				}

				generator = new MixedVariableBruteForceInitialization(problem, min, max,
						increment);
			}
		}

		return new BruteForceSearch(problem, generator, new NondominatedPopulation());
	}

	/**
	 * TODO
	 *
	 * @param typedProperties
	 * @param problem
	 * @return
	 */
	private Algorithm newSuperMOEA(TypedProperties typedProperties, Problem problem)
	{
		// TODO Auto-generated method stub
		return null;
	}
}