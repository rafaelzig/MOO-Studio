package uk.co.blogspot.rafaelzig.core.datastructure.template;

/**
 * Class representing a constraint imposed on an optimisation problem.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class Constraint
{
	/**
	 * Enumeration representing the types of constraint operators which are
	 * valid.
	 */
	public enum ConstraintOperator
	{
		GREATER_OR_EQUAL(">="),
		LESS_OR_EQUAL("<="),
		NOT_EQUAL("!=");

		/**
		 * Display value of the constraint operator.
		 */
		private String	displayValue;

		/**
		 * Constructs a new instance of this enumeration with the provided
		 * display value.
		 *
		 * @param displayValue
		 *            : Display value of the constraint operator.
		 */
		private ConstraintOperator(String displayValue)
		{
			this.displayValue = displayValue;
		}

		@Override
		public String toString()
		{
			return displayValue;
		}
	}

	/**
	 * Operator of this constraint.
	 */
	private final ConstraintOperator	operator;

	/**
	 * Left-hand side expression of this constraint.
	 */
	private final String				lhs;

	/**
	 * Right-hand side expression of this constraint.
	 */
	private final String				rhs;

	/**
	 * Constructs a new instance of Constraint with the specified operator,
	 * left-hand and right-hand side expressions.
	 *
	 * @param operator
	 *            : Operator of this constraint.
	 * @param lhs
	 *            : Left-hand side expression of this constraint.
	 * @param rhs
	 *            : Right-hand side expression of this constraint.
	 */
	public Constraint(ConstraintOperator operator, String lhs, String rhs)
	{
		super();
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	/**
	 * Returns the left-hand side expression of this constraint.
	 *
	 * @return Left-hand side expression of this constraint.
	 */
	public String getLhs()
	{
		return lhs;
	}

	/**
	 * Returns the operator of this constraint.
	 *
	 * @return Operator of this constraint.
	 */
	public ConstraintOperator getOperator()
	{
		return operator;
	}

	/**
	 * Returns the left-hand side expression of this constraint.
	 *
	 * @return Right-hand side expression of this constraint.
	 */
	public String getRhs()
	{
		return rhs;
	}

	@Override
	public String toString()
	{
		return lhs + " " + operator + " " + rhs;
	}
}