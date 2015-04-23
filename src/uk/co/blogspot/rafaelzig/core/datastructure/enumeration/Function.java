package uk.co.blogspot.rafaelzig.core.datastructure.enumeration;

/**
 * Enum Type representing a Mathematical function.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public enum Function implements DescriptiveEnum
{
	ABSOLUTE("abs( )", "Absolute value of argument"),
	AVERAGE("avg( )", "Average of arguments"),
	CEILING("ceil( )", "Largest integer to argument"),
	FLOOR("floor( )", "Smallest integer to argument"),
	MAXIMUM("max( )", "Greatest value of arguments"),
	MINIMUN("min( )", "Smallest value of arguments"),
	RANDOM("random( )", "Pseudo random number (range 0-1)"),
	ROUND("round( )", "Closest integer to argument"),
	COSINE("cos( )", "Trigonometric cosine of argument"),
	ARC_COSINE("acos( )", "Arc cosine of argument (range 0.0 - pi)"),
	HYPERBOLIC_COSINE("cosh( )", "Hyperbolic cosine of argument"),
	SINE("sin( )", "Trigonometric sine of argument"),
	ARC_SINE("asin( )", "Arc sine of argument (range -pi/2 - pi/2)"),
	HYPERBOLIC_SINE("sinh( )", "Hyperbolic sine of argument"),
	TANGENT("tan( )", "Trigonometric tangent of argument"),
	ARC_TANGENT("atan( )", "Arc tangent of argument (range -pi/2 - pi/2)"),
	HYPERBOLIC_TANGENT("tanh( )", "Hyperbolic tangent of argument"),
	NATURAL_LOGARITHM("ln( )", "Natural logarithm of argument"),
	BASE10_LOGARITHM("log( )", "Base 10 logarithm of argument"),
	SUM("sum( )", "Sum of elements"),
	SQUARE_ROOT("sqrt( )", "Square root of argument"),
	CUBE_ROOT("cbrt( )", "Cube root of argument");

	/**
	 * Text to be displayed.
	 */
	private final String	displayValue;

	/**
	 * Description of this function.
	 */
	private final String	description;

	/**
	 * Constructs a new instance of this enumeration with the provided text and
	 * description.
	 *
	 * @param displayValue
	 *            : Text to be displayed.
	 * @param description
	 *            : Description of this function.
	 */
	private Function(String displayValue, String description)
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
