package uk.co.blogspot.rafaelzig.core.datastructure.template;

/**
 * Abstract class representing a variable template, a variable has lower and
 * upper bounds and a corresponding type.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public abstract class VariableTemplate
{
	/*
	 * Enumeration of all types of valid variable templates.
	 */
	public enum Type
	{
		BINARY,
		REAL,
		INTEGER;
	}

	/**
	 * Type of variable.
	 */
	private final Type		type;

	/**
	 * Lower bound of variable.
	 */
	private final Number	lowerBound;

	/**
	 * Upper bound of variable.
	 */
	private final Number	upperBound;

	/**
	 * Constructs a new instance of VariableTemplate with the provided type,
	 * lower and upper bounds.
	 *
	 * @param type
	 *            : Type of the variable.
	 * @param lowerBound
	 *            : Lower bound of the variable.
	 * @param upperBound
	 *            : Upper bound of the variable.
	 */
	VariableTemplate(Type type, Number lowerBound, Number upperBound)
	{
		this.type = type;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * Returns a dummy value corresponding to this template, which is to be used
	 * for evaluating expression.
	 *
	 * @return
	 */
	public double getDummyValue()
	{
		return 1.0;
	}

	/**
	 * Returns the lower bound of this variable.
	 *
	 * @return Lower bound of variable.
	 */
	public Number getLowerBound()
	{
		return lowerBound;
	}

	/**
	 * Returns the type of variable.
	 *
	 * @return Type of variable.
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * Returns the upper bound of variable.
	 *
	 * @return Upper bound of variable.
	 */
	public Number getUpperBound()
	{
		return upperBound;
	}

	@Override
	public abstract String toString();
}