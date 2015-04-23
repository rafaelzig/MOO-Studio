package uk.co.blogspot.rafaelzig.core.datastructure.template;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;

import uk.co.blogspot.rafaelzig.core.datastructure.template.Constraint.ConstraintOperator;
import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate.Type;
import uk.co.blogspot.rafaelzig.core.parsing.Operations;

import com.fathzer.soft.javaluator.StaticVariableSet;

/**
 * Class representing a customised multi-objective optimisation problem.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class ProblemTemplate implements Problem
{
	/**
	 * Decision variables of the problem.
	 */
	private final VariableTemplate[]	variables;

	/**
	 * Objectives of the problem.
	 */
	private final Objective[]			objectives;

	/**
	 * Constraints of the problem.
	 */
	private final Constraint[]			constraints;

	/**
	 * User-friendly name of the problem.
	 */
	private final String				name;

	/**
	 * Constructs a new instance of ProblemTemplate taking its values from the
	 * provided ProblemTemplate object.
	 *
	 * @param problem
	 *            : ProblemTemplate object to take the values from.
	 */
	public ProblemTemplate(ProblemTemplate problem)
	{
		this(problem.name, problem.variables, problem.objectives, problem.constraints);
	}

	/**
	 * Constructs a new instance of ProblemTemplate with the provided name,
	 * variables, objectives and constraints.
	 *
	 * @param name
	 *            : User-friendly name of the problem.
	 * @param variables
	 *            : Decision variables of the problem.
	 * @param objectives
	 *            : Objectives of the problem.
	 * @param constraints
	 *            : Constraints of the problem.
	 */
	public ProblemTemplate(String name, List<VariableTemplate> variables,
			List<Objective> objectives, List<Constraint> constraints)
	{
		this(name, variables.toArray(new VariableTemplate[variables.size()]), objectives
				.toArray(new Objective[objectives.size()]), constraints
				.toArray(new Constraint[constraints.size()]));
	}

	/**
	 * Constructs a new instance of ProblemTemplate with the provided name,
	 * variables, objectives.
	 *
	 * @param name
	 *            : User-friendly name of the problem.
	 * @param variables
	 *            : Decision variables of the problem.
	 * @param objectives
	 *            : Objectives of the problem.
	 */
	public ProblemTemplate(String name, VariableTemplate[] variables,
			Objective[] objectives)
	{
		this(name, variables, objectives, new Constraint[0]);
	}

	/**
	 * Constructs a new instance of ProblemTemplate with the provided name,
	 * variables, objectives and constraints.
	 *
	 * @param name
	 *            : User-friendly name of the problem.
	 * @param variables
	 *            : Decision variables of the problem.
	 * @param objectives
	 *            : Objectives of the problem.
	 * @param constraints
	 *            : Constraints of the problem.
	 */
	public ProblemTemplate(String name, VariableTemplate[] variables,
			Objective[] objectives, Constraint[] constraints)
	{
		super();

		this.name = name;
		this.variables = variables;
		this.objectives = objectives;
		this.constraints = constraints;
	}

	@Override
	public void close()
	{
		// do nothing
	}

	@Override
	public void evaluate(Solution solution)
	{
		final StaticVariableSet<Double> varValues = getVariableValues(solution);

		final double[] objectiveEvaluations = getObjectiveEvaluations(varValues);

		if (objectiveEvaluations != null)
		{
			solution.setObjectives(objectiveEvaluations);
			solution.setConstraints(getConstraintEvaluations(varValues));
		}
		else
		{
			invalidate(solution);
		}
	}

	/**
	 * Evaluates and returns the constraints for this problem.
	 *
	 * @param solution
	 *            : Current solution being evaluated.
	 * @param values
	 *            : Values for the variables being used.
	 * @return Array containing the results of the constraint evaluations.
	 */
	private double[] getConstraintEvaluations(StaticVariableSet<Double> values)
	{
		final double[] constraintEvaluations = new double[getNumberOfConstraints()];

		for (int i = 0; i < getNumberOfConstraints(); i++)
		{
			final ConstraintOperator operator = constraints[i].getOperator();
			final double evaluatedLhs = Operations.evaluate(constraints[i].getLhs(),
					values);
			final double evaluatedRhs = Operations.evaluate(constraints[i].getRhs(),
					values);

			if (operator == ConstraintOperator.LESS_OR_EQUAL
					&& evaluatedLhs <= evaluatedRhs
					|| operator == ConstraintOperator.NOT_EQUAL
					&& evaluatedLhs != evaluatedRhs
					|| operator == ConstraintOperator.GREATER_OR_EQUAL
					&& evaluatedLhs >= evaluatedRhs)
			{
				constraintEvaluations[i] = 0.0;
			}
			else if (operator == ConstraintOperator.LESS_OR_EQUAL
					&& evaluatedLhs >= evaluatedRhs
					|| operator == ConstraintOperator.GREATER_OR_EQUAL
					&& evaluatedLhs <= evaluatedRhs)
			{
				constraintEvaluations[i] = evaluatedLhs - evaluatedRhs;
				// Assign the difference for constraint violation
			}
			else
			// (operator == ConstraintOperator.NOT_EQUAL && evaluatedLhs ==
			// evaluatedRhs)
			{
				constraintEvaluations[i] = 1; // Assign a fixed value for
				// constraint violation
			}
		}

		return constraintEvaluations;
	}

	/**
	 * Returns the constraints of this problem.
	 *
	 * @return Array of Constraint objects of this problem.
	 */
	public Constraint[] getConstraints()
	{
		return constraints;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getNumberOfConstraints()
	{
		return constraints.length;
	}

	@Override
	public int getNumberOfObjectives()
	{
		return objectives.length;
	}

	@Override
	public int getNumberOfVariables()
	{
		return variables.length;
	}

	/**
	 * Evaluates and returns the objective functions for the specified solution.
	 *
	 * @param solution
	 *
	 * @param solution
	 *            : Current solution being evaluated.
	 * @param values
	 *            : Values for the variables being used.
	 * @return Array containing the results of the objective evaluations.
	 */
	private double[] getObjectiveEvaluations(StaticVariableSet<Double> varValues)
	{
		final double[] objectiveEvaluations = new double[getNumberOfObjectives()];

		for (int i = 0; i < getNumberOfObjectives(); i++)
		{
			final String expression = objectives[i].getExpression();

			objectiveEvaluations[i] = Operations.evaluate(expression, varValues);

			if (Double.isNaN(objectiveEvaluations[i]))
			{
				return null;
			}

			if (objectives[i].isMaximisation())
			{
				objectiveEvaluations[i] = -objectiveEvaluations[i];
			}
		}

		return objectiveEvaluations;
	}

	/**
	 * Returns the objectives of this problem.
	 *
	 * @return Array of Objective objects of this problem.
	 */
	public Objective[] getObjectives()
	{
		return objectives;
	}

	/**
	 * Returns a textual representation of this problem.
	 *
	 * @return Textual representation of this problem.
	 */
	public String getProblemDetails()
	{
		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < getNumberOfObjectives(); i++)
		{
			builder.append("f(" + (i + 1) + "): " + objectives[i].toString() + "\n");
		}

		if (constraints.length > 0)
		{
			builder.append("\nSubject To:\n");
		}

		for (int i = 0; i < getNumberOfConstraints(); i++)
		{
			builder.append(constraints[i].toString() + "\n");
		}

		builder.append("\nProblem Domain:\n");

		for (int i = 0; i < getNumberOfVariables(); i++)
		{
			builder.append("x(" + (i + 1) + "): " + variables[i].toString() + "\n");
		}

		return builder.toString();
	}

	/**
	 * Returns the types of variables contained in this problem.
	 *
	 * @return Collection containing the types of variables in this problem.
	 */
	public Collection<Type> getTypes()
	{
		final Set<Type> types = new HashSet<>();

		for (final VariableTemplate var : getVariables())
		{
			types.add(var.getType());
		}

		return types;
	}

	/**
	 * Returns the variable templates of this problem.
	 *
	 * @return Array of VariableTemplate objects of this problem.
	 */
	public VariableTemplate[] getVariables()
	{
		return variables;
	}

	/**
	 * Returns an array containing the types of each variable in this problem.
	 *
	 * @return Array of String objects representing the types of each of the
	 *         variables in this problem.
	 */
	public String[] getVariableTypes()
	{
		final String[] types = new String[getNumberOfVariables()];

		for (int i = 0; i < getNumberOfVariables(); i++)
		{
			types[i] = variables[i].getType().toString();
		}

		return types;
	}

	/**
	 * Returns the values for each of the variables for the provided solution.
	 *
	 * @param solution
	 *            : Solution object to take the values from.
	 * @return StaticVariableSet object containing the values for each of the
	 *         variables of this problem.
	 */
	private StaticVariableSet<Double> getVariableValues(Solution solution)
	{
		final StaticVariableSet<Double> values = new StaticVariableSet<>();

		for (int i = 0; i < getNumberOfVariables(); i++)
		{
			final Variable x = solution.getVariable(i);
			double value;

			switch (variables[i].getType())
			{
				case BINARY:
				{
					value = EncodingUtils.getBoolean(x) ? 1.0 : 0.0;
					break;
				}
				case INTEGER:
				{
					value = EncodingUtils.getInt(x);
					break;
				}
				case REAL:
				{
					value = EncodingUtils.getReal(x);
					break;
				}
				default:
				{
					value = 0.0;
				}
			}

			values.set("x" + (i + 1), value);
		}

		return values;
	}

	/**
	 * Invalidates the specified solution by setting all constraints to positive
	 * infinity.
	 *
	 * @param solution
	 *            : Solution to be invalidated.
	 */
	private void invalidate(Solution solution)
	{
		final double[] constraintEvaluations = new double[getNumberOfConstraints()];

		for (int i = 0; i < getNumberOfConstraints(); i++)
		{
			constraintEvaluations[i] = Double.POSITIVE_INFINITY;
		}

		solution.setConstraints(constraintEvaluations);
	}

	@Override
	public Solution newSolution()
	{
		final Solution solution = new Solution(getNumberOfVariables(),
				getNumberOfObjectives(), getNumberOfConstraints());

		for (int i = 0; i < variables.length; i++)
		{
			Variable x;

			switch (variables[i].getType())
			{
				case BINARY:
				{
					x = EncodingUtils.newBoolean();
					break;
				}
				case INTEGER:
				{
					x = EncodingUtils.newInt(variables[i].getLowerBound().intValue(),
							variables[i].getUpperBound().intValue());
					break;
				}
				case REAL:
				{
					x = EncodingUtils.newReal(variables[i].getLowerBound().doubleValue(),
							variables[i].getUpperBound().doubleValue());
					break;
				}
				default:
				{
					x = null;
				}
			}

			solution.setVariable(i, x);
		}

		return solution;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}