package uk.co.blogspot.rafaelzig.core.datastructure.enumeration;

/**
 * Enum Type representing performance indicators utilised as measurements.
 */
public enum Indicator implements DescriptiveEnum
{
	ADDITIVE_EPSILON(
			"Additive Epsilon",
			"<html>Additive e-indicator for minimization problems. Finds the minimum<br>"
					+ "e-value for the approximation set to e-dominate the reference set.<html>"),
	CONTRIBUTION(
			"Contribution",
			"Measures the contribution of the approximation set to the reference set."),
	GENERATIONAL_DISTANCE(
			"Generational Distance",
			"<html>Represents average distance from solutions in an approximation<br>"
					+ "set to the nearest solution in the reference set.<html>"),
	HYPERVOLUME("Hypervolume", "<html>Represents the volume of objective space<br>"
			+ "dominated by solutions in the approximation set.<html>"),
	INVERTED_GENERATIONAL_DISTANCE(
			"Inverted Generational Distance",
			"<html>Represents average distance from solutions in the reference<br>"
					+ "set to the nearest solution in an approximation set.<html>"),
	MAXIMUM_PARETO_FRONT_ERROR(
			"Maximum Pareto Front Error",
			"<html>Represents the maximum distance from solutions in an approximation<br>"
					+ "set to the nearest solution in the reference set.<html>"),
	SPACING("Spacing", "Represents the spread of the Pareto front.");

	/**
	 * Display value of this indicator.
	 */
	private final String	displayValue;

	/**
	 * Description of this indicator.
	 */
	private final String	description;

	/**
	 * Constructs a new instance of Indicator with the specified display and
	 * description.
	 *
	 * @param display
	 *            : Display value of this indicator.
	 * @param description
	 *            : Description of this indicator.
	 */
	private Indicator(String display, String description)
	{
		displayValue = display;
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
