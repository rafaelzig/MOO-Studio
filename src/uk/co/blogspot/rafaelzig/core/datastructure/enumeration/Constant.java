package uk.co.blogspot.rafaelzig.core.datastructure.enumeration;

/**
 * Enum Type representing a Mathematical constant.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public enum Constant implements DescriptiveEnum
{
	E("e", "Base of the natural logarithms"),
	PI("pi", "Ratio of the circumference of a circle to its diameter");

	/**
	 * Text to be displayed.
	 */
	private final String	displayValue;

	/**
	 * Description of constant.
	 */
	private final String	description;

	/**
	 * Constructs a new instance of this enumeration with the respective text
	 * and description.
	 *
	 * @param displayValue
	 *            : Text to be displayed.
	 * @param description
	 *            : Description of constant.
	 */
	private Constant(String displayValue, String description)
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