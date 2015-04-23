package uk.co.blogspot.rafaelzig.core.datastructure.template;

/**
 * Class representing an integer variable template containing lower and upper
 * bounds.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class IntegerTemplate extends VariableTemplate
{
	/**
	 * Constructs a new instance of IntegerTemplate with the provided lower and
	 * upper bounds.
	 *
	 * @param lowerBound
	 *            : Lower bound of the variable.
	 * @param upperBound
	 *            : Upper bound of the variable.
	 */
	public IntegerTemplate(int lowerBound, int upperBound)
	{
		super(Type.INTEGER, lowerBound, upperBound);
	}

	@Override
	public String toString()
	{
		return new String("Integer (" + getLowerBound() + " <= x <= " + getUpperBound()
				+ ")");
	}
}