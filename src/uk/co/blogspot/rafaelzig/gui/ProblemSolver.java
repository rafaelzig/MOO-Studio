package uk.co.blogspot.rafaelzig.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javafx.util.Pair;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Algorithm;
import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Indicator;
import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Parameter;
import uk.co.blogspot.rafaelzig.core.datastructure.template.Constraint;
import uk.co.blogspot.rafaelzig.core.datastructure.template.Objective;
import uk.co.blogspot.rafaelzig.core.datastructure.template.ProblemTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate;
import uk.co.blogspot.rafaelzig.core.parsing.Operations;
import uk.co.blogspot.rafaelzig.gui.component.JMetricsPanel;
import uk.co.blogspot.rafaelzig.gui.component.NumericTextField;
import uk.co.blogspot.rafaelzig.gui.component.model.ExtendedComboBoxModel;
import uk.co.blogspot.rafaelzig.gui.component.model.VariablesComboBoxModel;
import uk.co.blogspot.rafaelzig.gui.component.renderer.TooltipRenderer;
import uk.co.blogspot.rafaelzig.gui.component.renderer.VariableRenderer;

/**
 * A subclass of JFrame encapsulating several other components allowing the user
 * to create, evaluate and analyse multi-objective optimisation problems.
 *
 * @author Rafael da Silva Costa - Computer Science BSc 3rd Year
 */
public class ProblemSolver extends JFrame implements ActionListener
{
	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
			Operations.handleError(null, e);
		}

		javax.swing.SwingUtilities.invokeLater(() -> new ProblemSolver());
	}

	/**
	 * Several constants utilised throughout this application.
	 */
	private final String						REVIEW_SAVED		= "Review Saved Solution Sets";
	private final String						STOP				= "Stop Evaluation";
	private final String						ANALYSE				= "Analyse Algorithms";
	private final String						EXIT				= "Exit Application";
	private final String						NEW_PROBLEM			= "New Problem";
	private final String						EDIT_PROBLEM		= "Edit Problem";
	private final String						DELETE_PROBLEM		= "Delete Problem";
	private final String						PROBLEM_SELECTED	= "Problem Selected";
	private final String						ALGORITHM_SELECTED	= "Algorithm Selected";
	private final String						EVALUATE			= "Evaluate Solution Set";
	private final String						USE_CUSTOM			= "Use Custom Parameters";
	private final int							PADDING				= 5;
	private final int							THRESHOLD			= 400;

	/**
	 * Several fields utilised throughout this application.
	 */
	private JFileChooser						flcLoad;
	private Map<JCheckBox, JFormattedTextField>	paramMap;
	private LoadingWindow						loading;
	private ProblemDesigner						problemDesigner;
	private JComboBox<ProblemTemplate>			cbbProblems;
	private JComboBox<Algorithm>				cbbAlgorithms;
	private JList<VariableTemplate>				lstVariables;
	private JList<Objective>					lstObjectives;
	private JList<Constraint>					lstConstraints;
	private JPanel								pnlParameters, pnlLineEnd;
	private JFormattedTextField					txtMaxEvaluations;
	private JButton								btnEvaluate, btnAnalyse, btnStop;
	private SwingWorker<?, ?>					worker;

	// private ProgressListener progressListener = e -> loading
	// .setProgress(e
	// .getCurrentNFE());

	/**
	 * Constructs a new instance of ProblemSolver and initialises its
	 * components.
	 */
	private ProblemSolver()
	{
		super("Multi-Objective Optimisation Studio");
		setLayout(new BorderLayout(PADDING, PADDING));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(getClass().getResource("res/icon.png")).getImage());

		setWidgets();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case NEW_PROBLEM:
				if (problemDesigner == null)
				{
					problemDesigner = new ProblemDesigner(this);
				}
				else
				{
					problemDesigner.setVisible(true);
				}

				loadProblems();
				break;

			case EDIT_PROBLEM:
				if (isProblemSelected())
				{
					final ProblemTemplate problem = (ProblemTemplate) cbbProblems
							.getSelectedItem();
					if (problemDesigner == null)
					{
						problemDesigner = new ProblemDesigner(this, problem);
					}
					else
					{
						problemDesigner.loadProblem(problem);
						problemDesigner.setVisible(true);
					}

					loadProblems();
				}
				break;

			case DELETE_PROBLEM:
				if (isProblemSelected())
				{
					removeProblem((ProblemTemplate) cbbProblems.getSelectedItem());
				}
				break;

			case EVALUATE:
				if (isProblemSelected())
				{
					evaluateSolutions((ProblemTemplate) cbbProblems.getSelectedItem(),
							(Algorithm) cbbAlgorithms.getSelectedItem());
				}
				break;

			case ANALYSE:
				JMetricsPanel pnl = null;
				pnl = new JMetricsPanel();
				if (JOptionPane.showConfirmDialog(this, pnl, "Algorithm Analysis",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					if (!pnl.isSelectionEmpty())
					{
						analyseAlgorithms(
								cbbProblems.getItemAt(pnl.getSelectedProblemIndex()),
								pnl.getSelectedAlgorithms(), pnl.getSelectedIndicators(),
								pnl.getMaxEvaluations(), pnl.getSeeds());
					}
					else
					{
						JOptionPane.showMessageDialog(this, "No selections made.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				break;

			case REVIEW_SAVED:
				loadSavedSolutionSet();
				break;

			case PROBLEM_SELECTED:
				loadProblemDetails((ProblemTemplate) cbbProblems.getSelectedItem());
				break;

			case ALGORITHM_SELECTED:
				loadAlgorithmDetails((Algorithm) cbbAlgorithms.getSelectedItem());
				break;

			case USE_CUSTOM:
				final JFormattedTextField txtParam = paramMap.get(e.getSource());
				txtParam.setEnabled(!txtParam.isEnabled());
				break;

			case STOP:
				worker.cancel(true);
				break;

			case EXIT:
				System.exit(0);
				break;
		}
	}

	/**
	 * Analyses the specified algorithms using the specified configurations.
	 *
	 * @param problem
	 *            : Problem to be used during the analysis.
	 * @param algorithms
	 *            : Algorithms to be analysed.
	 * @param indicators
	 *            : Indicators to be tested during analysis.
	 * @param maxEvaluations
	 *            : Maximum amount of evaluations to be considered.
	 * @param seeds
	 *            : Number of iterations during analysis.
	 */
	private void analyseAlgorithms(ProblemTemplate problem, Algorithm[] algorithms,
			Indicator[] indicators, int maxEvaluations, int seeds)
	{
		if (isCompatible(problem, algorithms))
		{
			// loading = new LoadingWindow(this, "Analysing...",
			// maxEvaluations);
			loading = new LoadingWindow(this, "Analysing...");
			toggleButtons();

			worker = new SwingWorker<String, Object>()
			{
				@Override
				public String doInBackground() throws IOException
				{
					return getStatistics(problem, algorithms, indicators, maxEvaluations,
							seeds);
				}

				@Override
				protected void done()
				{
					try
					{
						final JTextArea txtStatistics = new JTextArea(get());
						final JScrollPane container = new JScrollPane(txtStatistics);
						container.setPreferredSize(new Dimension(400, 400));

						JOptionPane.showMessageDialog(getParent(), container,
								"Analysis Results", JOptionPane.INFORMATION_MESSAGE);
					}
					catch (CancellationException | InterruptedException
							| ExecutionException | OutOfMemoryError e)
					{
						Operations.handleError(getParent(), e);
					}
					finally
					{
						toggleButtons();
						loading.dispose();
					}
				}
			};

			worker.execute();
		}
	}

	/**
	 * Evaluates the specified problem using the specified algorithm.
	 *
	 * @param problem
	 *            : Problem to be evaluated.
	 * @param algorithm
	 *            : Algorithm utilised during evaluation.
	 */
	private void evaluateSolutions(ProblemTemplate problem, Algorithm algorithm)
	{
		if (isCompatible(problem, algorithm))
		{
			final int maxEvaluations = (int) txtMaxEvaluations.getValue();
			final Executor executor = getExecutor(problem, algorithm, maxEvaluations);

			loading = new LoadingWindow(this, "Evaluating...");
			toggleButtons();

			worker = new SwingWorker<Pair<String, Pair<NondominatedPopulation, Long>>, Object>()
			{
				@Override
				public Pair<String, Pair<NondominatedPopulation, Long>> doInBackground()
				{
					final long start = System.currentTimeMillis();
					final NondominatedPopulation result = executor.run();
					final long ellapsed = System.currentTimeMillis() - start;
					return new Pair<>(algorithm.toString(), new Pair<>(result, ellapsed));
				}

				@Override
				protected void done()
				{
					try
					{
						showSolutions(get());
					}
					catch (CancellationException | InterruptedException
							| ExecutionException | OutOfMemoryError e)
					{
						Operations.handleError(getParent(), e);
					}
					finally
					{
						toggleButtons();
						loading.dispose();
					}
				}
			};

			worker.execute();
		}
	}

	/**
	 * Returns an Analyzer object configured for the specified settings.
	 *
	 * @param problem
	 *            : Problem to be utilised during analysis.
	 * @param algorithms
	 *            : Algorithms to be analysed.
	 * @param indicators
	 *            : Performance indicators to be utilised.
	 * @param maxEvaluations
	 *            : Max evaluations of each algorithm.
	 * @return
	 */
	private Analyzer getAnalyzer(ProblemTemplate problem, Algorithm[] algorithms,
			Indicator[] indicators, int maxEvaluations)
	{
		final Analyzer analyzer = new Analyzer().withProblemClass(problem.getClass(),
				problem).showStatisticalSignificance();

		for (final Indicator indicator : indicators)
		{
			switch (indicator)
			{
				case ADDITIVE_EPSILON:
					analyzer.includeAdditiveEpsilonIndicator();
					break;
				case CONTRIBUTION:
					analyzer.includeContribution();
					break;
				case GENERATIONAL_DISTANCE:
					analyzer.includeGenerationalDistance();
					break;
				case HYPERVOLUME:
					analyzer.includeHypervolume();
					break;
				case INVERTED_GENERATIONAL_DISTANCE:
					analyzer.includeInvertedGenerationalDistance();
					break;
				case MAXIMUM_PARETO_FRONT_ERROR:
					analyzer.includeMaximumParetoFrontError();
					break;
				case SPACING:
					analyzer.includeSpacing();
					break;
			}
		}

		return analyzer;
	}

	/**
	 * Returns the JPanel object which will be placed in the "Center"
	 * placeholder of this window.
	 *
	 * @return JPanel object which will be placed in the "Center" placeholder of
	 *         this window.
	 */
	private JPanel getCenterPanel()
	{
		cbbProblems = new JComboBox<>(new ExtendedComboBoxModel<ProblemTemplate>());
		cbbProblems.setActionCommand(PROBLEM_SELECTED);
		cbbProblems.setActionCommand(PROBLEM_SELECTED);
		cbbProblems.addActionListener(this);

		lstVariables = new JList<>(new VariablesComboBoxModel<VariableTemplate>());
		lstVariables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstVariables.setCellRenderer(new VariableRenderer());
		lstObjectives = new JList<>(new ExtendedComboBoxModel<Objective>());
		lstObjectives.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstConstraints = new JList<>(new ExtendedComboBoxModel<Constraint>());
		lstConstraints.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Create and set the JButton objects
		final JButton btnNewProblem = new JButton(NEW_PROBLEM);
		btnNewProblem.setActionCommand(NEW_PROBLEM);
		btnNewProblem.addActionListener(this);
		final JButton btnEditProblem = new JButton(EDIT_PROBLEM);
		btnEditProblem.setActionCommand(EDIT_PROBLEM);
		btnEditProblem.addActionListener(this);
		final JButton btnDeleteProblem = new JButton(DELETE_PROBLEM);
		btnDeleteProblem.setActionCommand(DELETE_PROBLEM);
		btnDeleteProblem.addActionListener(this);

		final JPanel pnlHeader = new JPanel(new GridLayout(1, 4, PADDING, PADDING));
		pnlHeader.add(new JLabel("Select a Problem:", JLabel.CENTER));
		pnlHeader.add(cbbProblems);
		pnlHeader.add(btnNewProblem);
		pnlHeader.add(btnEditProblem);
		pnlHeader.add(btnDeleteProblem);

		final JPanel pnlLabels = new JPanel(new GridLayout(1, 3, PADDING, PADDING));
		pnlLabels.add(new JLabel("Variables", JLabel.CENTER));
		pnlLabels.add(new JLabel("Objectives", JLabel.CENTER));
		pnlLabels.add(new JLabel("Constraints", JLabel.CENTER));

		final JPanel pnlDetails = new JPanel(new GridLayout(1, 3, PADDING, PADDING));
		pnlDetails.setPreferredSize(new Dimension(600, 100));
		pnlDetails.add(new JScrollPane(lstVariables));
		pnlDetails.add(new JScrollPane(lstObjectives));
		pnlDetails.add(new JScrollPane(lstConstraints));

		// Populates the Center JPanel
		final JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.add(pnlHeader);
		pnlCenter.add(pnlLabels);
		pnlCenter.add(pnlDetails);

		return pnlCenter;
	}

	/**
	 * Returns the Executor object configured for the specified algorithm,
	 * problem and max evaluations.
	 *
	 * @param algorithm
	 *            : Algorithm to be utilised during evaluation.
	 * @param problem
	 *            : Problem to be evaluated.
	 * @param maxEvaluations
	 *            : Maximum evaluations to be performed by the algorithm.
	 * @return
	 */
	private Executor getExecutor(ProblemTemplate problem, Algorithm algorithm,
			Integer maxEvaluations)
	{
		final Executor executor = new Executor()
				.withProblemClass(problem.getClass(), problem)
				.withAlgorithm(algorithm.name()).distributeOnAllCores();

		if (algorithm.equals(Algorithm.BruteForce))
		{
			maxEvaluations = 1;
			executor.withProperty(Algorithm.BruteForce.name(), problem.getVariableTypes());
		}

		executor.withMaxEvaluations(maxEvaluations);

		if (isMixedtype(problem))
		{
			executor.withProperty("operator", "sbx+pm+hux+bf");
		}

		for (final Entry<String, Number> param : getParameters().entrySet())
		{
			if (param.getValue() instanceof Double)
			{
				executor.withProperty(param.getKey(), param.getValue().doubleValue());
			}
			else
			{
				executor.withProperty(param.getKey(), param.getValue().intValue());
			}
		}

		return executor;
	}

	/**
	 * Returns the JPanel object which will be placed on the "LineEnd"
	 * placeholder of the ProblemSolver.
	 *
	 * @return JPanel object which will be placed on the "LineEnd" placeholder
	 *         of the ProblemSolver.
	 */
	private JPanel getLineEndPanel()
	{
		// Sets the JComboBox objects
		cbbAlgorithms = new JComboBox<Algorithm>(Algorithm.values());
		cbbAlgorithms.setActionCommand(ALGORITHM_SELECTED);
		cbbAlgorithms.setRenderer(new TooltipRenderer());
		cbbAlgorithms.addActionListener(this);

		txtMaxEvaluations = new NumericTextField(false, 1);

		final JPanel pnlSelector = new JPanel(new GridLayout(1, 4, PADDING, PADDING));
		pnlSelector.add(new JLabel("Algorithm:"));
		pnlSelector.add(cbbAlgorithms);
		pnlSelector.add(new JLabel("Max Evaluations:"));
		pnlSelector.add(txtMaxEvaluations);

		pnlLineEnd = new JPanel();
		pnlLineEnd.setLayout(new BoxLayout(pnlLineEnd, BoxLayout.Y_AXIS));
		pnlLineEnd.add(pnlSelector);

		return pnlLineEnd;
	}

	/**
	 * Returns the JPanel object which will be placed on the "PageEnd"
	 * placeholder of the ProblemSolver.
	 *
	 * @return JPanel object which will be placed on the "PageEnd" placeholder
	 *         of the ProblemSolver.
	 */
	private JPanel getPageEndPanel()
	{
		btnEvaluate = new JButton(EVALUATE);
		btnEvaluate.setActionCommand(EVALUATE);
		btnEvaluate.addActionListener(this);

		btnAnalyse = new JButton(ANALYSE);
		btnAnalyse.setActionCommand(ANALYSE);
		btnAnalyse.addActionListener(this);

		btnStop = new JButton(STOP);
		btnStop.setEnabled(false);
		btnStop.setActionCommand(STOP);
		btnStop.addActionListener(this);

		final JButton btnExit = new JButton(EXIT);
		btnExit.setActionCommand(EXIT);
		btnExit.addActionListener(this);

		final JButton btnReviewSolutions = new JButton(REVIEW_SAVED);
		btnReviewSolutions.setActionCommand(REVIEW_SAVED);
		btnReviewSolutions.addActionListener(this);

		// Populates the PageEnd JPanel
		final JPanel pnlCenterButtons = new JPanel(new GridLayout(2, 2, PADDING, PADDING));
		pnlCenterButtons.add(btnEvaluate);
		pnlCenterButtons.add(btnAnalyse);
		pnlCenterButtons.add(btnStop);
		pnlCenterButtons.add(btnExit);

		final JPanel pnlPageEnd = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlPageEnd.add(btnReviewSolutions, BorderLayout.PAGE_START);
		pnlPageEnd.add(pnlCenterButtons, BorderLayout.CENTER);

		return pnlPageEnd;
	}

	/**
	 * Returns the currently selected parameters and their values.
	 *
	 * @return Map containing the currently selected parameters and values.
	 */
	private Map<String, Number> getParameters()
	{
		final Map<String, Number> parameters = new HashMap<>();

		for (final Entry<JCheckBox, JFormattedTextField> entry : paramMap.entrySet())
		{
			if (entry.getKey().isSelected() && entry.getValue().getValue() != null)
			{
				final JFormattedTextField txtParam = entry.getValue();
				parameters.put(txtParam.getName(), (Number) txtParam.getValue());
			}
		}

		return parameters;
	}

	/**
	 * Returns the statistics of the analysis of the specified parameters.
	 *
	 * @param problem
	 *            : Problem to be utilised during analysis.
	 * @param algorithms
	 *            : Algorithms to be analysed.
	 * @param indicators
	 *            : Performance indicators to be utilised.
	 * @param maxEvaluations
	 *            : Max evaluations of each of the algorithms.
	 * @param seeds
	 *            : Number of iterations of each of the algorithms.
	 * @return Statistics of the analysis.
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 */
	private String getStatistics(ProblemTemplate problem, Algorithm[] algorithms,
			Indicator[] indicators, int maxEvaluations, int seeds) throws IOException
	{
		final Analyzer analyzer = getAnalyzer(problem, algorithms, indicators,
				maxEvaluations);

		final ByteArrayOutputStream output = new ByteArrayOutputStream();

		for (final Algorithm algorithm : algorithms)
		{
			final StringBuilder bdr = new StringBuilder();
			bdr.append(algorithm.toString() + " average runtime per seed: ");
			final Executor executor = getExecutor(problem, algorithm, maxEvaluations);
			final long start = System.currentTimeMillis();
			if (algorithm.equals(Algorithm.BruteForce))
			{
				analyzer.addAll(algorithm.toString(), executor.runSeeds(1));
			}
			else
			{
				analyzer.addAll(algorithm.toString(), executor.runSeeds(seeds));
			}
			bdr.append(Long.toString((System.currentTimeMillis() - start) / seeds)
					+ "ms\n");
			output.write(bdr.toString().getBytes());
		}

		output.write(new String("\n").getBytes());

		analyzer.printAnalysis(new PrintStream(output));

		return output.toString();
	}

	/**
	 * Returns true if the specified algorithms are compatible with the
	 * specified problem, false otherwise.
	 *
	 * @param problem
	 *            : Problem to be checked for compatibility.
	 * @param algorithms
	 *            : Algorithms to be checked for compatibility.
	 * @return True if the specified algorithms are compatible with the
	 *         specified problem, false otherwise.
	 */
	private boolean isCompatible(ProblemTemplate problem, Algorithm... algorithms)
	{
		for (final Algorithm algorithm : algorithms)
		{
			if (!Arrays.asList(algorithm.getCompatibleTypes()).containsAll(
					problem.getTypes()))
			{
				JOptionPane.showMessageDialog(this, algorithm.toString()
						+ " is incompatible with selected problem.");
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true if the problem has mixed variable types, false otherwise.
	 *
	 * @param problem
	 *            : Problem to be checked.
	 * @return True if the problem has mixed variable types, false otherwise.
	 */
	private boolean isMixedtype(ProblemTemplate problem)
	{
		return problem.getTypes().size() > 1;
	}

	/**
	 * Returns true if a problem has been selected a problem, false otherwise.
	 *
	 * @return True if a problem has been selected a problem, false otherwise.
	 */
	private boolean isProblemSelected()
	{
		if (cbbProblems.getSelectedIndex() >= 0)
		{
			return true;
		}

		JOptionPane.showMessageDialog(this, "Select a problem first.");
		return false;
	}

	/**
	 * Loads the information and parameters for the specified algorithm.
	 *
	 * @param alg
	 *            : Algorithm to have the information loaded.
	 */
	private void loadAlgorithmDetails(Algorithm alg)
	{
		if (alg.equals(Algorithm.BruteForce))
		{
			txtMaxEvaluations.setValue(1);
			txtMaxEvaluations.setEnabled(false);
		}
		else
		{
			txtMaxEvaluations.setValue(25000);
			txtMaxEvaluations.setEnabled(true);
		}

		if (pnlParameters != null)
		{
			pnlLineEnd.remove(pnlParameters);
		}

		final Parameter[] parameters = alg.getParameters();
		paramMap = new HashMap<>(parameters.length);
		pnlParameters = new JPanel(new GridLayout(parameters.length, 2, PADDING, PADDING));

		for (final Parameter param : parameters)
		{
			final JFormattedTextField txtParameter = new NumericTextField(param.isReal(),
					param.getLowerBound(), param.getUpperBound());
			txtParameter.setName(param.getCode());
			txtParameter.setToolTipText(param.getDescription());
			txtParameter.setEnabled(false);

			final JCheckBox cbxCustom = new JCheckBox(param.toString());
			cbxCustom.setActionCommand(USE_CUSTOM);
			cbxCustom.setToolTipText(param.getDescription());
			cbxCustom.addActionListener(this);

			pnlParameters.add(cbxCustom);
			pnlParameters.add(txtParameter);
			paramMap.put(cbxCustom, txtParameter);
		}

		pnlLineEnd.add(pnlParameters);
		revalidate();
		repaint();
		pack();
	}

	/**
	 * Loads the specified problem into the application.
	 *
	 * @param problem
	 *            : Problem to be loaded.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadProblemDetails(ProblemTemplate problem)
	{
		resetModels();

		ExtendedComboBoxModel model = (ExtendedComboBoxModel) lstVariables.getModel();
		model.addAll(problem.getVariables());

		model = (ExtendedComboBoxModel) lstObjectives.getModel();
		model.addAll(problem.getObjectives());

		model = (ExtendedComboBoxModel) lstConstraints.getModel();
		model.addAll(problem.getConstraints());
	}

	/**
	 * Loads the problems from disk and adds them to the application.
	 */
	private void loadProblems()
	{
		Collection<ProblemTemplate> problems;
		problems = Operations.loadProblems().values();

		if (problems.size() > 0)
		{
			final ProblemTemplate[] array = problems.toArray(new ProblemTemplate[problems
					.size()]);

			final ExtendedComboBoxModel<ProblemTemplate> model = (ExtendedComboBoxModel<ProblemTemplate>) cbbProblems
					.getModel();

			// Avoiding double events due to index being updated
			cbbProblems.removeActionListener(this);

			if (model.getSize() >= 0)
			{
				model.removeAllElements();
			}

			model.addAll(array);

			// Reenabling listener
			cbbProblems.addActionListener(this);

			cbbProblems.setSelectedIndex(0);
		}
	}

	/**
	 * Allows the user to select a previously saved Pareto Optimal Solution Set
	 * and display it.
	 */
	private void loadSavedSolutionSet()
	{
		if (flcLoad == null)
		{
			flcLoad = new JFileChooser();
			flcLoad.setFileFilter(new FileNameExtensionFilter(
					"Pareto Optimal Solution Set (*.sol)", "sol"));
			flcLoad.setAcceptAllFileFilterUsed(false);
		}

		if (flcLoad.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			new SolutionViewer(Operations.load(flcLoad.getSelectedFile()));
		}
	}

	/**
	 * Removes the specified problem from the model.
	 *
	 * @param problem
	 *            : Problem to be removed.
	 */
	private void removeProblem(ProblemTemplate problem)
	{
		if (JOptionPane.showConfirmDialog(this, "Delete problem " + problem.getName()
				+ "?", "Confirm Action", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
		{
			final ExtendedComboBoxModel<ProblemTemplate> model = (ExtendedComboBoxModel<ProblemTemplate>) cbbProblems
					.getModel();

			// Avoiding double events due to index being updated
			cbbProblems.removeActionListener(this);

			model.removeElement(problem);

			// Reenabling listener
			cbbProblems.addActionListener(this);

			Operations.writeProblems(model.getElements());

			if (model.getSize() > 0)
			{
				cbbProblems.setSelectedIndex(0);
			}
			else
			{
				resetModels();
			}
		}
	}

	/**
	 * Resets the models utilised in this application.
	 */
	private void resetModels()
	{
		ExtendedComboBoxModel<?> model = (VariablesComboBoxModel<?>) lstVariables
				.getModel();
		model.removeAllElements();

		model = (ExtendedComboBoxModel<?>) lstObjectives.getModel();
		model.removeAllElements();

		model = (ExtendedComboBoxModel<?>) lstConstraints.getModel();
		model.removeAllElements();
	}

	/**
	 * Creates widgets, sets their properties and adds them to containers.
	 */
	private void setWidgets()
	{
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getLineEndPanel(), BorderLayout.LINE_END);
		add(getPageEndPanel(), BorderLayout.PAGE_END);

		loadProblems();

		loadAlgorithmDetails(cbbAlgorithms.getItemAt(0));

		if (cbbProblems.getModel().getSize() > 0)
		{
			loadProblemDetails(cbbProblems.getItemAt(0));
		}
	}

	/**
	 * Opens a new SolutionViewer window to display the result of evaluations.
	 *
	 * @param result
	 *            : Result of evaluation to be displayed.
	 */
	private void showSolutions(Pair<String, Pair<NondominatedPopulation, Long>> result)
	{
		NondominatedPopulation solutions = result.getValue().getKey();

		if (!solutions.get(0).violatesConstraints())
		{
			if (solutions.size() > THRESHOLD)
			{
				final NondominatedPopulation truncated = new NondominatedPopulation(
						solutions.getComparator());

				for (int i = 0; i < THRESHOLD; i++)
				{
					truncated.add(solutions.get(i));
				}

				solutions = truncated;

				JOptionPane.showMessageDialog(this,
						"Too many solutions found, only first " + THRESHOLD
								+ " solutions will be displayed");
			}

			final List<Solution> solutionList = new ArrayList<>(solutions.size());
			solutions.forEach(s -> s.clearAttributes());
			solutions.forEach(s -> solutionList.add(s));

			new SolutionViewer((ProblemTemplate) cbbProblems.getSelectedItem(),
					solutionList, result.getKey(), result.getValue().getValue());
		}
		else
		{
			JOptionPane.showMessageDialog(this, "No solutions found.");
		}
	}

	/**
	 * Toggle the "Evaluate", "Analyse" and "Stop" buttons.
	 */
	private void toggleButtons()
	{
		btnEvaluate.setEnabled(!btnEvaluate.isEnabled());
		btnAnalyse.setEnabled(!btnAnalyse.isEnabled());
		btnStop.setEnabled(!btnStop.isEnabled());
	}
}