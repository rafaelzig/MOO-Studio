package uk.co.blogspot.rafaelzig.core.datastructure.enumeration;

/**
 * Enum Type representing a Mathematical operator.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public enum Operator implements DescriptiveEnum
{
	ADDITION("+", "Addition"),
	SUBTRACTION("-", "Substraction"),
	MULTPLICATION("*", "Multiplication"),
	DIVISION("/", "Division"),
	EXPONENTIATION("^", "Exponentiation"),
	MODULO("%", "Modulo");

	/**
	 * Display value of this operator.
	 */
	private final String	displayValue;

	/**
	 * Description of this operator.
	 */
	private final String	description;

	/**
	 * Constructs a new instance of Operator with the specified display value
	 * and description.
	 *
	 * @param displayValue
	 *            : Display value of this operator.
	 * @param description
	 *            : Description of this operator.
	 */
	private Operator(String displayValue, String description)
	{
		this.displayValue = displayValue;
		this.description = description;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public String toString()
	{
		return displayValue;
	}
}