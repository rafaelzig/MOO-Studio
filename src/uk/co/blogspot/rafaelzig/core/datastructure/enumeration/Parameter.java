package uk.co.blogspot.rafaelzig.core.datastructure.enumeration;

/**
 * Enum Type representing various parameters utilised by the optimisation
 * algorithms.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public enum Parameter implements DescriptiveEnum
{
	INITIAL_POPULATION_SIZE(
			"initialPopulationSize",
			"Initial Population Size",
			"The initial size of the population.",
			1,
			Integer.MAX_VALUE),
	POPULATION_SIZE(
			"populationSize",
			"Population Size",
			"The number of candidate solutions generated per evaluation.",
			1,
			Integer.MAX_VALUE),
	MAX_POP_SIZE(
			"maxPopSize",
			"Maximum Population Size",
			"The maximum size of the population.",
			1,
			Integer.MAX_VALUE),
	A(
			"a",
			"Population Size Constant",
			"Constant controlling the population size.",
			Double.MIN_VALUE,
			Double.MAX_VALUE),
	B(
			"b",
			"Population Size Multiplier",
			"Multiplier controlling the population size.",
			Double.MIN_VALUE,
			Double.MAX_VALUE),
	C(
			"c",
			"Offspring Constant",
			"Constant controlling the number of offspring.",
			Double.MIN_VALUE,
			Double.MAX_VALUE),
	D(
			"d",
			"Offspring Multiplier",
			"Multiplier controlling the number of offspring.",
			Double.MIN_VALUE,
			Double.MAX_VALUE),
	TERMINATION(
			"termination",
			"Early Termination",
			"If 0, the algorithm terminates early if all solutions like on the Pareto optimal front.",
			0,
			1),
	MINIMUM_POPULATION_SIZE(
			"minimumPopulationSize",
			"Minimum Population Size",
			" The smallest possible population size when injecting new solutions after a randomized restart.",
			1,
			Integer.MAX_VALUE),
	MAXIMUM_POPULATION_SIZE(
			"maximumPopulationSize",
			"Maximum Population Size",
			"The largest possible population size when injecting new solutions after a randomized restart",
			1,
			Integer.MAX_VALUE),
	ARCHIVE_SIZE(
			"archiveSize",
			"Archive Size",
			"The size of the archive",
			1,
			Integer.MAX_VALUE),
	REF_SET1_SIZE(
			"refSet1Size",
			"1st Reference Set Size",
			"The size of the first reference set.",
			1,
			Integer.MAX_VALUE),
	REF_SET2_SIZE(
			"refSet2Size",
			"2nd Reference Set Size",
			"The size of the second reference set.",
			1,
			Integer.MAX_VALUE),
	IMPROVEMENT_ROUNDS(
			"improvementRounds",
			"Improvement Rounds",
			"The number of iterations that the local search operator is applied.",
			1,
			Integer.MAX_VALUE),
	FEEDBACK(
			"feedBack",
			"Feedback",
			"Controls the number of solutions from the archive that are fed back into the population.",
			0,
			Integer.MAX_VALUE),
	EPSILON(
			"epsilon",
			"Epsilon",
			"The e values used by the e-dominance archive.",
			Double.MIN_VALUE,
			Double.MAX_VALUE),
	SBX_RATE(
			"sbx.rate",
			"SBX Rate",
			"The crossover rate for simulated binary crossover.",
			0.0,
			1.0),
	SBX_DISTRIBUTION_INDEX(
			"sbx.distributionIndex",
			"SBX Distribution",
			"The distribution index for simulated binary crossover.",
			0.0,
			Double.MAX_VALUE),
	PMX_RATE("pmx.rate", "PMX Rate", "The crossover rate for PMX crossover.", 0.0, 1.0),
	DE_CROSSOVER_RATE(
			"de.crossoverRate",
			"DEX Rate",
			"The crossover rate for differential evolution.",
			0.0,
			1.0),
	DE_STEP_SIZE(
			"de.stepSize",
			"DE Step Size",
			"Control the size of each step taken by differential evolution.",
			0.0,
			Double.MAX_VALUE),
	SX_RATE(
			"1x.rate",
			"LX Rate",
			"The crossover rate for single-point crossover.",
			0.0,
			1.0),
	HUX_RATE(
			"hux.rate",
			"HUX Rate",
			"The crossover rate for the highly disruptive recombination operator.",
			0.0,
			1.0),
	PM_RATE("pm.rate", "PM Rate", "The mutation rate for polynomial mutation", 0.0, 1.0),
	MUTATION_PROBABILITY(
			"mutationProbability",
			"Mutation Probability",
			"The mutation probability for uniform and nonuniform mutation",
			0.0,
			1.0),
	PERTURBATION_INDEX(
			"perturbationIndex",
			"Perturbation Index",
			"Controls the shape of the distribution for uniform and non-uniform mutation",
			0.0,
			Double.MAX_VALUE),
	PM_DISTRIBUTION_INDEX(
			"pm.distributionIndex",
			"PM Distribution",
			"The distribution index for polynomial mutation",
			0.0,
			Double.MAX_VALUE),
	BF_RATE("bf.rate", "BFM Rate", "The mutation rate for bit flip mutation", 0.0, 1.0),
	SWAP_RATE(
			"swap.rate",
			"Swap Rate",
			"The mutation rate for the swap operator.",
			0.0,
			1.0),
	DIVISIONS("divisions", "Divisions", "The number of divisions.", 0, Integer.MAX_VALUE),
	INJECTION_RATE(
			"injectionRate",
			"Injection Rate",
			"Controls the percentage of the population after a restart this is \"injected\", or copied, from the e-dominance archive.",
			0.0,
			1.0),
	WINDOW_SIZE(
			"windowSize",
			"Window Size",
			"Frequency of checking if a randomized restart should be triggered (number of iterations)",
			0,
			Integer.MAX_VALUE),
	MAX_WINDOW_SIZE(
			"maxWindowSize",
			"Max Window Size",
			"The maximum number of iterations between successive randomized restarts",
			0,
			Integer.MAX_VALUE),
	NEIGHBORHOOD_SIZE(
			"neighborhoodSize",
			"Neighbourhood Size",
			"The size of the neighborhood used for mating, given as a percentage of the population size.",
			0,
			Integer.MAX_VALUE),
	DELTA(
			"delta",
			"Delta",
			"The probability of mating with an individual from the neighborhood versus the entire population.",
			0.0,
			1.0),
	ETA(
			"eta",
			"ETA",
			"The maximum number of spots in the population that an offspring can replace, given as a percentage of the population size.",
			0.0,
			1.0),
	UPDATE_UTILITY(
			"updateUtility",
			"Update Utility",
			"The frequency, in generations, at which utility values are updated, set to -1 to disable utility-based search",
			-1,
			Integer.MAX_VALUE),
	INITIAL_CONVERGENCE_COUNT(
			"initialConvergenceCount",
			"Initial Convergence Count",
			"The threshold (as a percent of the number of bits in the encoding) used to determine similarity between solutions",
			0.0,
			1.0),
	PRESERVED_POPULATION(
			"preservedPopulation",
			"Preserved Population",
			"The percentage of the population that does not undergo cataclysmic mutation.",
			0.0,
			1.0),
	CONVERGENCE_VALUE(
			"convergenceValue",
			"Convergence Value",
			"The convergence threshold that determines when cataclysmic mutation is applied.",
			0,
			Integer.MAX_VALUE),
	BISECTIONS(
			"bisections",
			"Bisections",
			"The number of bisections in the adaptive grid archive.",
			0,
			Integer.MAX_VALUE),
	OFFSET("offset", "Offset", "Offset", 0.0, Double.MAX_VALUE);

	/**
	 * Code of the parameter, which is utilised by MOEAFRAMEWORK to identify the
	 * parameter being passed during optimisation.
	 */
	private final String	code;

	/**
	 * Display value of this parameter.
	 */
	private final String	displayValue;

	/**
	 * Description of this parameter.
	 */
	private final String	description;

	/**
	 * Lower bound of this parameter.
	 */
	private final Number	lowerBound;

	/**
	 * Upper bound of this parameter.
	 */
	private final Number	upperBound;

	/**
	 * Constructs a new instance of Parameter with the specified code, display
	 * value and description.
	 *
	 * @param code
	 *            : Code of the parameter, which is utilised by MOEAFRAMEWORK to
	 *            identify the parameter being passed during optimisation.
	 * @param displayValue
	 *            : Display value of this parameter.
	 * @param description
	 *            : Description of this parameter.
	 */
	private Parameter(String code, String displayValue, String description,
			Number lowerBound, Number upperBound)
	{
		this.code = code;
		this.displayValue = displayValue;
		this.description = description;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * Returns the code of the parameter, which is utilised by MOEAFRAMEWORK to
	 * identify the parameter being passed during optimisation.
	 *
	 * @return Code of the parameter, which is utilised by MOEAFRAMEWORK to
	 *         identify the parameter being passed during optimisation.
	 */
	public String getCode()
	{
		return code;
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

	/**
	 * Returns whether this parameter takes real values.
	 * 
	 * @return True if this parameter takes real values, false otherwise.
	 */
	public boolean isReal()
	{
		return (lowerBound instanceof Double);
	}

	/**
	 * Returns the lower bound of this parameter.
	 * 
	 * @return Lower bound of this parameter.
	 */
	public Number getLowerBound()
	{
		return lowerBound;
	}

	/**
	 * Returns the upper bound of this parameter.
	 * 
	 * @return Lower bound of this parameter.
	 */
	public Number getUpperBound()
	{
		return upperBound;
	}
}