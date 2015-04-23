package uk.co.blogspot.rafaelzig.core.parsing;

import java.util.Iterator;

import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;

/**
 * A subclass of DoubleEvaluator, which is provided by the JAVALUATOR library,
 * supporting additional Mathematical functions.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 */
class ExtendedDoubleEvaluator extends DoubleEvaluator
{
	/** Defines the new function (square root). */
	private static final Function	SQRT	= new Function("sqrt", 1);
	/** Defines the new function (cube root). */
	private static final Function	CBRT	= new Function("cbrt", 1);
	private static final Parameters	PARAMS;

	static
	{
		// Gets the default DoubleEvaluator's parameters
		PARAMS = DoubleEvaluator.getDefaultParameters();

		// add the new functions to these parameters
		PARAMS.add(SQRT);
		PARAMS.add(CBRT);

		// Add square brackets [] to the list of supported brackets
		PARAMS.addExpressionBracket(BracketPair.BRACKETS);
	}

	/**
	 * Constructs a new instance of ExtendedDoubleEvaluator with the specified
	 * parameters.
	 */
	ExtendedDoubleEvaluator()
	{
		super(PARAMS);
	}

	@Override
	protected Double evaluate(Function function, Iterator<Double> arguments,
			Object evaluationContext)
	{
		if (function == SQRT)
		{
			return Math.sqrt(arguments.next());
		}
		else if (function == CBRT)
		{
			return Math.cbrt(arguments.next());
		}
		else
		{
			// If it's another function, pass it to DoubleEvaluator
			return super.evaluate(function, arguments, evaluationContext);
		}
	}
}