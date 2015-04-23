package uk.co.blogspot.rafaelzig.core.datastructure.enumeration;

import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate.Type;

/**
 * Enum Type representing an optimisation algorithm, contains compatible
 * variable types and parameters.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public enum Algorithm implements DescriptiveEnum
{
	eMOEA(
			"e-MOEA",
			"<html>e-MOEA is a steady-state MOEA that uses e-dominance archiving to record a diverse set of Pareto<br>"
					+ "optimal solutions.Full details of this algorithm are available in the following technical report:<br><br>"
					+ "Deb, K. et al. \"A Fast Multi-Objective Evolutionary Algorithm for Finding Well-Spread Pareto-Optimal<br>"
					+ "Solutions.\" KanGAL Report No 2003002, Feb 2003.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.EPSILON,
					Parameter.SBX_RATE, Parameter.SBX_DISTRIBUTION_INDEX,
					Parameter.PM_RATE, Parameter.PM_DISTRIBUTION_INDEX }),
	NSGAII(
			"NSGA-II",
			"<html>NSGA-II is one of the most widely used MOEAs and was introduced in the following paper:<br><br>"
					+ "Deb, K. et al. \"A Fast Elitist Multi-Objective Genetic Algorithm: NSGA-II.\" IEEE Transactions<br>"
					+ "on Evolutionary Computation, 6:182-197, 2000.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.SBX_RATE,
					Parameter.SBX_DISTRIBUTION_INDEX, Parameter.PM_RATE,
					Parameter.PM_DISTRIBUTION_INDEX }),
	NSGAIII(
			"NSGA-III",
			"<html>NSGA-III is the many-objective successor to NSGA-II, using reference<br>"
					+ "points to direct solutions towards a diverse set. Full details are described in:<br><br>"
					+ "Deb, K. and Jain, H. \"An Evolutionary Many-Objective Optimization Algorithm Using<br>"
					+ "Reference-Point-Based Nondominated Sorting Approach, Part I: Solving Problems With Box<br>"
					+ "Constraints.\" IEEE Transactions on Evolutionary Computation, 18(4):577-601, 2014.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.DIVISIONS,
					Parameter.SBX_RATE, Parameter.SBX_DISTRIBUTION_INDEX,
					Parameter.PM_RATE, Parameter.PM_DISTRIBUTION_INDEX }),
	eNSGAII(
			"e-NSGA-II",
			"<html>e-NSGA-II is an extension of NSGA-II that uses an e-dominance archive<br>"
					+ "and randomized restart to enhance search and find a diverse set of Pareto<br>"
					+ "optimal solutions. Full details of this algorithm are given in the following paper:<br><br>"
					+ "Kollat, J. B., and Reed, P. M. \"Comparison of Multi-Objective Evolutionary Algorithms<br>"
					+ "for Long-Term Monitoring Design.\" Advances in Water Resources, 29(6):792-807, 2006.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.EPSILON,
					Parameter.SBX_RATE, Parameter.SBX_DISTRIBUTION_INDEX,
					Parameter.PM_RATE, Parameter.PM_DISTRIBUTION_INDEX,
					Parameter.INJECTION_RATE, Parameter.WINDOW_SIZE,
					Parameter.MAX_WINDOW_SIZE, Parameter.MINIMUM_POPULATION_SIZE,
					Parameter.MAXIMUM_POPULATION_SIZE }),
	MOEAD(
			"MOEA/D",
			"<html>MOEA/D is a relatively new optimization algorithm based on the concept of decomposing<br>"
					+ "the problem into many single-objective formulations. Two versions of MOEA/D exist<br>"
					+ "in the literature. The first, based on the paper cited below, is the original incarnation:<br><br>"
					+ "Li, H. and Zhang, Q. \"Multiobjective Optimization problems with Complicated Pareto Sets,<br>"
					+ "MOEA/D and NSGA-II.\" IEEE Transactions on Evolutionary Computation, 13(2):284-302, 2009.<br><br>"
					+ "An extension to the original MOEA/D algorithm introduced a utility function that aimed to reduce the amount<br>"
					+ "of \"wasted\" effort by the algorithm. Full details of this extension are available in the following paper:<br><br>"
					+ "Zhang, Q., et al. \"The Performance of a New Version of 110 CHAPTER 9. OPTIMIZATION ALGORITHMS<br>"
					+ "MOEA/D on CEC09 Unconstrained MOP Test Instances.\" IEEE Congress on Evolutionary Computation, 2009.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.DE_CROSSOVER_RATE,
					Parameter.DE_STEP_SIZE, Parameter.PM_RATE,
					Parameter.PM_DISTRIBUTION_INDEX, Parameter.NEIGHBORHOOD_SIZE,
					Parameter.DELTA, Parameter.ETA, Parameter.UPDATE_UTILITY }),
	GDE3(
			"GDE3",
			"<html>GDE3 is the extension of differential evolution for multiobjective<br>"
					+ "problems. It was originally introduced in the following technical report: Kukkonen and<br><br>"
					+ "Lampinen (2005). \"GDE3: The Third Evolution Step of Generalized Differential Evolution.\"<br>"
					+ "KanGAL Report Number 2005013.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.DE_CROSSOVER_RATE,
					Parameter.DE_STEP_SIZE }),
	Random(
			"Random",
			"<html>The random search algorithm simply randomly generates new solutions uniformly<br>"
					+ "throughout the search space. It is not intended as an \"optimization algorithm” per se,<br>"
					+ "but as a way to compare the performance of other MOEAs against random search. If an<br>"
					+ "optimization algorithm can not beat random search, then continued use of that optimization<br>"
					+ "algorithm should be questioned.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.EPSILON }),
	ABYSS(
			"AbYSS",
			"<html>AbYSS is a hybrid scatter search algorithm that uses genetic algorithm<br>"
					+ "operators.It was originally introduced in the following paper:<br><br>"
					+ "Nebro, A. J., et al. \"AbYSS: Adapting Scatter Search to Multiobjective Optimization.<br>"
					+ "\" IEEE Transactions on Evolutionary Computation, 12(4):349-457, 2008.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE,
					Parameter.REF_SET1_SIZE, Parameter.REF_SET2_SIZE,
					Parameter.IMPROVEMENT_ROUNDS }),
	CellDE(
			"CellDE",
			"<html>CellDE is a hybrid cellular genetic algorithm (meaning mating only occurs among neighbors)<br>"
					+ "combined with differential evolution. CellDE was introduced in the following study:<br><br>"
					+ "Durillo, J. J., et al. \"Solving Three-Objective Optimization Problems Using a new Hybrid Cellular<br>"
					+ "Genetic Algorithm.\" Parallel Problem Solving from Nature - PPSN X, Springer, 661-370, 2008.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE,
					Parameter.FEEDBACK, Parameter.DE_CROSSOVER_RATE,
					Parameter.DE_STEP_SIZE }),
	DENSEA(
			"DENSEA",
			"<html>DENSEA is the duplicate elimination non-domination sorting<br>"
					+ "evolutionary algorithm discussed in the following paper:<br><br>"
					+ "D. Greiner, et al. \"Enhancing the multiobjective optimum design of<br>"
					+ "structural trusses with evolutionary algorithms using DENSEA.\" 44th AIAA<br>"
					+ "(American Institute of Aeronautics and Astronautics) Aerospace Sciences Meeting<br>"
					+ "and Exhibit, AIAA2006-1474, 2006.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE }),
	FastPGA(
			"FastPGA",
			"<html>FastPGA is a genetic algorithm that uses adaptive population sizing to solve time<br>"
					+ "consumping problems more efficiently. It was introduced in the following paper:<br><br>"
					+ "Eskandari, H., et al. \"FastPGA: A Dynamic Population Sizing Approach for Solving Expensive<br>"
					+ "Multiobjective Optimization Problems.\" Evolutionary Multi-Criterion Optimization, Springer, 141-155, 2007.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.MAX_POP_SIZE, Parameter.INITIAL_POPULATION_SIZE,
					Parameter.A, Parameter.B, Parameter.C, Parameter.D,
					Parameter.TERMINATION }),
	IBEA(
			"IBEA",
			"<html>IBEA is a indicator-based MOEA that uses the hypervolume performance indicator as a<br>"
					+ "means to rank solutions. IBEA was introduced in the following paper:<br><br>"
					+ "Zitzler, E. and Ku¨nzli, S. \"Indicator-based selection in multiobjective search.\"<br>"
					+ "In Parallel Problem Solving from Nature (PPSN VIII), Lecture Notes in Computer Science,<br>"
					+ "pages 832842, Berlin / Heidelberg, Springer, 2004.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE }),
	MOCell(
			"MOCell",
			"<html>MOCell is the multiobjective version of a cellular genetic<br>"
					+ "algorithm. It was originally introduced at the following workshop:<br><br>"
					+ "Nebro, A. J., et al. \"A Cellular Genetic Algorithm for Multiobjective<br>"
					+ "Optimization.\" Proceedings of the Workshop on Nature Inspired Cooperative<br>"
					+ "Strategies for Optimization, Granada, Spain, 25-36, 2006.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE,
					Parameter.FEEDBACK }),
	MOCHC(
			"MOCHC",
			"<html>MOCHC is a genetic algorithm that combines a conservative selection strategy<br>"
					+ "with highly disruptive recombination, which unlike traditional MOEAs aims to produce<br>"
					+ "offspring that are maximally different from both parents. It was<br>"
					+ "introduced in the following conference proceedings:<br><br>"
					+ "Nebro, A. J., et al. \"Optimal Antenna Placement using a New Multi-objective<br>"
					+ "CHC Algorithm.\" Proceedings of the 9th Annual Conference on Genetic and<br>"
					+ "Evolutionary Computation, London, England, 876-883, 2007.<html>",
			new Type[] { Type.BINARY },
			new Parameter[] { Parameter.INITIAL_CONVERGENCE_COUNT,
					Parameter.PRESERVED_POPULATION, Parameter.CONVERGENCE_VALUE,
					Parameter.POPULATION_SIZE, Parameter.HUX_RATE, Parameter.BF_RATE }),
	OMOPSO(
			"OMOPSO",
			"<html>OMOPSO is a multiobjective particle swarm optimization algorithm that includes<br>"
					+ "an e-dominance archive to discover a diverse set of Pareto optimal<br>"
					+ "solutions. OMOPSO was originally introduced at the following conference:<br><br>"
					+ "Sierra, M. R. and Coello Coello, C. A. \"Improving PSO-based multi-objective<br>"
					+ "optimization using crowding, mutation and e-dominance.\" Evolutionary Multi-Criterion<br>"
					+ "Optimization, Berlin, Germany, 505-519, 2005.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE,
					Parameter.MUTATION_PROBABILITY, Parameter.PERTURBATION_INDEX,
					Parameter.EPSILON }),
	PAES(
			"PAES",
			"<html>PAES is a multiobjective version of evolution strategy. PAES tends to<br>"
					+ "underperform when compared to other MOEAs, but it is often used as a baseline<br>"
					+ "algorithm for comparisons. PAES was introduced in the following conference proceedings:<br><br>"
					+ "Knowles, J. and Corne, D. \"The Pareto Archived Evolution Strategy: A New Baseline<br>"
					+ "Algorithm for Multiobjective Optimization.\" Proceedings of the 1999 Congress on<br>"
					+ "Evolutionary Computation, Piscataway, NJ, 98-105, 1999.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.ARCHIVE_SIZE, Parameter.BISECTIONS }),
	PESA2(
			"PESA-II",
			"<html>PESA-II is another multiobjective evolutionary algorithm<br>"
					+ "that tends to underperform other MOEAs but is often used as a<br>"
					+ "baseline algorithm. PESA-II was introduced in the following paper:<br><br>"
					+ "Corne, D. W., et al. \"PESA-II: Region-based Selection in Evolutionary<br>"
					+ "Multiobjective Optimization.\" Proceedings of the Genetic and<br>"
					+ "Evolutionary Computation Conference, 283-290, 2001.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE,
					Parameter.BISECTIONS }),
	SMPSO(
			"SMPSO",
			"<html>SMPSO is a multiobjective particle swarm optimization algorithm<br>"
					+ "that was originally presented at the following conference:<br><br>"
					+ "Nebro, A. J., et al. \"SMPSO: A New PSO-based Metaheuristic for Multi-objective Optimization.\"<br>"
					+ "2009 IEEE Symposium on Computational Intelligence in Multicriteria Decision-Making, 6673, 2009.<html>",
			new Type[] { Type.REAL, Type.INTEGER },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE,
					Parameter.PM_RATE, Parameter.PM_DISTRIBUTION_INDEX }),
	SMSEMOA(
			"SMSEMOA",
			"<html>SMSEMOA is an indicator-based MOEA that uses the volume of the dominated<br>"
					+ "hypervolume to rank individuals. SMSEMOA is discussed in detail in the following paper:<br><br>"
					+ "Beume, N., et al. \"SMS-EMOA: Multiobjective selection based on dominated hypervolume.\"<br>"
					+ "European Journal of Operational Research, 181(3):1653-1669, 2007.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.OFFSET }),
	SPEA2(
			"SPEA2",
			"<html>SPEA2 is an older but popular benchmark MOEA that uses the so-called \"strength-based\"<br>"
					+ "method for ranking solutions. SPEA2 was introduced in the following conference proceedings:<br><br>"
					+ "Zitzler, E., et al. \"SPEA2: Improving the Strength Pareto Evolutionary Algorithm For<br>"
					+ "Multiobjective Optimization. CIMNE, Barcelona, Spain, 2002.<html>",
			new Type[] { Type.REAL, Type.INTEGER, Type.BINARY },
			new Parameter[] { Parameter.POPULATION_SIZE, Parameter.ARCHIVE_SIZE }),

	BruteForce(
			"Brute Force",
			"<html>A brute-force search or exhaustive search, also known as generate and test, is a very general<br>"
					+ "problem-solving technique that consists of systematically enumerating all possible candidates<br>"
					+ "for the solution and checking whether each candidate satisfies the problem's statement.<html>",
			new Type[] { Type.INTEGER, Type.REAL, Type.BINARY },
			new Parameter[] {});

	/**
	 * Display value of algorithm.
	 */
	private String		displayValue;

	/**
	 * Description of algorithm.
	 */
	private String		description;

	/**
	 * Compatible variable types with algorithm.
	 */
	private Type[]		compatibleTypes;

	/**
	 * Compatible parameters with algorithm.
	 */
	private Parameter[]	parameters;

	/**
	 * Constructs a new instance of with provided display, description,
	 * compatible types and parameters.
	 *
	 * @param displayValue
	 *            : Display value of algorithm.
	 * @param description
	 *            : Description of algorithm.
	 * @param compatibleTypes
	 *            : Compatible variable types with algorithm.
	 * @param parameters
	 *            : Compatible parameters with algorithm.
	 */
	private Algorithm(String displayValue, String description, Type[] compatibleTypes,
			Parameter[] parameters)
	{
		this.displayValue = displayValue;
		this.description = description;
		this.compatibleTypes = compatibleTypes;
		this.parameters = parameters;
	}

	/**
	 * @return Compatible parameters with algorithm.
	 */
	public Type[] getCompatibleTypes()
	{
		return compatibleTypes;
	}

	/**
	 * Returns the description of algorithm.
	 *
	 * @return Description of algorithm.
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * Returns the compatible parameters with algorithm.
	 *
	 * @return Compatible parameters with algorithm.
	 */
	public Parameter[] getParameters()
	{
		return parameters;
	}

	@Override
	public String toString()
	{
		return displayValue;
	}
}