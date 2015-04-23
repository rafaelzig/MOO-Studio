package uk.co.blogspot.rafaelzig.gui.component;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * A subclass of JFormattedTextField which handles numeric formats accordingly.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class NumericTextField extends JFormattedTextField
{
	private static final long	serialVersionUID	= 8120984319758753877L;

	/**
	 * Constructs a new instance of NumericTextField.
	 *
	 * @param isReal
	 *            : Boolean representing whether this object will hold
	 *            real-valued numbers.
	 */
	public NumericTextField(boolean isReal)
	{
		this(isReal, null, null);
	}

	/**
	 * Constructs a new instance of NumericTextField with the specified lower
	 * bound.
	 *
	 * @param isReal
	 *            : Boolean representing whether this object will hold
	 *            real-valued numbers.
	 * @param lowerBound
	 *            : Lower bound of numbers.
	 */
	public NumericTextField(boolean isReal, Number lowerBound)
	{
		this(isReal, lowerBound, null);
	}

	/**
	 * Constructs a new instance of NumericTextField with the specified lower
	 * bound and upper bound.
	 *
	 * @param isReal
	 *            : Boolean representing whether this object will hold
	 *            real-valued numbers.
	 * @param lowerBound
	 *            : Lower bound of numbers.
	 * @param upperBound
	 *            : Upper bound of numbers.
	 */
	public NumericTextField(boolean isReal, Number lowerBound, Number upperBound)
	{
		super();

		final NumberFormat format = isReal ? new DecimalFormat("0.0##########")
				: NumberFormat.getIntegerInstance();

		format.setGroupingUsed(false);
		final NumberFormatter formatter = new NumberFormatter(format);

		if (lowerBound != null)
		{
			formatter.setMinimum((Comparable<?>) lowerBound);
		}
		if (upperBound != null)
		{
			formatter.setMaximum((Comparable<?>) upperBound);
		}

		if (formatter.getValueClass() == null)
		{
			formatter.setValueClass(isReal ? Double.class : Integer.class);
		}

		setFormatterFactory(new DefaultFormatterFactory(formatter));
	}

	/**
	 * Constructs a new instance of NumericTextField with the specified upper
	 * bound.
	 *
	 * @param isReal
	 *            : Boolean representing whether this object will hold
	 *            real-valued numbers.
	 * @param upperBound
	 *            : Upper bound of numbers.
	 */
	public NumericTextField(Number upperBound, boolean isReal)
	{
		this(isReal, null, upperBound);
	}
}