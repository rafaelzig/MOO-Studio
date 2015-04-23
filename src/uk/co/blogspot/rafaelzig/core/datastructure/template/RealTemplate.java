package uk.co.blogspot.rafaelzig.core.datastructure.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class representing a real-valued variable template containing lower and upper
 * bounds.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class RealTemplate extends VariableTemplate
{
	/**
	 * Constructs a new instance of RealTemplate with the provided lower and
	 * upper bounds.
	 *
	 * @param lowerBound
	 *            : Lower bound of the variable.
	 * @param upperBound
	 *            : Upper bound of the variable.
	 */
	public RealTemplate(double lowerBound, double upperBound)
	{
		super(Type.REAL, lowerBound, upperBound);
	}

	@Override
	public String toString()
	{
		final NumberFormat f = new DecimalFormat("0.00");

		return new String("Real (" + f.format(getLowerBound()) + " <= x <= "
				+ f.format(getUpperBound()) + ")");
	}
}