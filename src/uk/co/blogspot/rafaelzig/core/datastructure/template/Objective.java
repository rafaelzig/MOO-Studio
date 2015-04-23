package uk.co.blogspot.rafaelzig.core.datastructure.template;

/**
 * Class representing an objective function of an optimisation problem.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class Objective
{
	/**
	 * Mathematical expression representing the objective function.
	 */
	private final String	expression;

	/**
	 * Boolean representing whether this objective function is a Maximisation
	 * type.
	 */
	private final boolean	isMaximisation;

	/**
	 * Constructs a new instance of Objective with the specified expression and
	 * boolean isMaximisation.
	 *
	 * @param expression
	 *            : Mathematical expression representing the objective function.
	 * @param isMaximisation
	 *            : Boolean representing whether this objective function is a
	 *            Maximisation type.
	 */
	public Objective(String expression, boolean isMaximisation)
	{
		this.expression = expression;
		this.isMaximisation = isMaximisation;
	}

	/**
	 * Returns the Mathematical expression representing the objective function.
	 *
	 * @return Mathematical expression representing the objective function.
	 */
	public String getExpression()
	{
		return expression;
	}

	/**
	 * Returns true if this objective is of a maximisation type, false
	 * otherwise.
	 *
	 * @return True if this objective is of a maximisation type, false
	 *         otherwise.
	 */
	public boolean isMaximisation()
	{
		return isMaximisation;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();

		if (isMaximisation())
		{
			builder.append("Maximise: ");
		}
		else
		{
			builder.append("Minimise: ");
		}

		builder.append(getExpression());

		return builder.toString();
	}
}