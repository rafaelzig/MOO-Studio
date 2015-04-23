package uk.co.blogspot.rafaelzig.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.moeaframework.core.Solution;

import uk.co.blogspot.rafaelzig.core.datastructure.SavedSolutionSet;
import uk.co.blogspot.rafaelzig.core.datastructure.template.ProblemTemplate;
import uk.co.blogspot.rafaelzig.core.parsing.Operations;
import uk.co.blogspot.rafaelzig.gui.component.SolutionSetTable;
import uk.co.blogspot.rafaelzig.gui.component.model.SolutionSetModel;

/**
 * A subclass of JFrame which encapsulates several subcomponents to display the
 * Pareto Optimal Solution Set in various graphical representations.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
class SolutionViewer extends JFrame implements ActionListener
{
	private static final long		serialVersionUID			= -5553916720308065085L;

	/**
	 * Several constants utilised throughout this application.
	 */
	private final String			EXPORT_SOLUTIONS			= "Export solutions";
	private final String			CLOSE						= "Close Window";
	private final String			SAVE_SOLUTIONS				= "Save Solution Set";
	private final String			TRADEOFF_CURVE				= "Trade-off Curve";
	private final String			VARIABLES_CHART				= "Variables Chart";
	private final String			DUAL_CHART					= "Objectives & Variables Chart";
	private final String			TRADEOFF_CURVE_STYLE_FILE	= "tradeoff-curve-style.css";
	private final String			DUAL_CHART_STYLE_FILE		= "dual-chart-style.css";
	private final int				PANEL_WIDTH					= 1200;
	private final int				PANEL_HEIGHT				= 600;
	private final int				TABLE_PANEL_HEIGHT			= 100;
	private final int				STAGE_HEIGHT				= PANEL_HEIGHT
																		- TABLE_PANEL_HEIGHT
																		- 20;
	private final int				PADDING						= 5;

	/**
	 * Several fields utilised throughout this application.
	 */
	private final ProblemTemplate	problem;
	private final List<Solution>	solutions;
	private final String			algorithmName;
	private final SolutionSetModel	mdlResults;
	private final JFXPanel			fxpResults					= new JFXPanel();
	private final long				elapsed;
	private JFileChooser			flcSave, flcExport;
	private Scene					dualChartScene, tradeoffScene, varChartScene;

	/**
	 * Constructs a new instance of SolutionViewer initialising its components
	 * with the specified parameters.
	 *
	 * @param problem
	 *            : Problem which was evaluated.
	 * @param solutions
	 *            : List containing the Pareto Optimal Solutions.
	 * @param algorithmName
	 *            : Name of algorithm utilised during evaluation.
	 * @param elapsed
	 *            : Elapsed time taken to evaluate the Pareto Optimal Solutions.
	 */
	SolutionViewer(ProblemTemplate problem, List<Solution> solutions,
			String algorithmName, Long elapsed)
	{
		super(problem.getName() + " solved with " + algorithmName);

		this.problem = problem;
		this.solutions = solutions;
		this.algorithmName = algorithmName;
		this.elapsed = elapsed;
		mdlResults = new SolutionSetModel(solutions, problem);
		Platform.setImplicitExit(false);
		setIconImage(new ImageIcon(getClass().getResource("res/icon.png")).getImage());
		setLayout(new BorderLayout(PADDING, PADDING));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setScenes();
	}

	/**
	 * Constructs a new instance of SolutionViewer with the specified
	 * SavedSolutionSet object.
	 *
	 * @param solutionSet
	 *            : SavedSolutionSet object containing the details of the
	 *            evaluation.
	 */
	SolutionViewer(SavedSolutionSet solutionSet)
	{
		this(solutionSet.getProblem(), solutionSet.getSolutions(), solutionSet
				.getAlgorithm(), solutionSet.getElapsed());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case DUAL_CHART:
				swapScene(dualChartScene);
				break;
			case VARIABLES_CHART:
				swapScene(varChartScene);
				break;
			case TRADEOFF_CURVE:
				swapScene(tradeoffScene);
				break;
			case SAVE_SOLUTIONS:
				saveSolution();
				break;
			case EXPORT_SOLUTIONS:
				exportSolutions();
				break;
			case CLOSE:
				dispose();
				break;
		}
	}

	/**
	 * Adds the tool tips to nodes in the specified chart.
	 *
	 * @param chart
	 *            : Chart to be manipulated.
	 * @param isTradeoffCurve
	 *            : Whether this chart is a tradeoff curve chart.
	 * @param <X>
	 *            : Type of the XAxis of the chart.
	 * @param <Y>
	 *            : Type of the yAxis of the chart.
	 */
	private void addTooltips(XYChart<?, Number> chart, String chartType)
	{
		if (chartType.equals(TRADEOFF_CURVE))
		{
			chart.getData()
					.stream()
					.forEach(
							s -> s.getData()
									.stream()
									.forEach(
											d -> Tooltip.install(
													d.getNode(),
													new Tooltip(
															s.getName()
																	+ " : "
																	+ String.format(
																			"%4.2f",
																			((Number) d
																					.getXValue())
																					.doubleValue())
																	+ " , "
																	+ String.format(
																			"%4.2f",
																			d.getYValue()
																					.doubleValue())))));
		}
		else
		{
			chart.getData()
					.stream()
					.forEach(
							s -> s.getData()
									.stream()
									.forEach(
											d -> Tooltip.install(
													d.getNode(),
													new Tooltip(s.getName()
															+ " : "
															+ String.format("%4.2f", d
																	.getYValue()
																	.doubleValue())))));
		}
	}

	/**
	 * Exports the chart to the specified file.
	 *
	 * @param file
	 *            : File to be written to.
	 */
	private void exportAsImage(final File file)
	{
		final LoadingWindow loading = new LoadingWindow(this, "Exporting...");
		Platform.runLater(() -> {
			final WritableImage snapshot = fxpResults.getScene().snapshot(null);
			SwingUtilities.invokeLater(() -> {
				if (Operations.write(file, snapshot))
				{
					JOptionPane.showMessageDialog(this,
							"Solutions successfully exported!");
				}

				loading.dispose();
			});
		});
	}

	/**
	 * Displays a dialog to allow the user to export the solutions to a .png or
	 * .csv file.
	 */
	private void exportSolutions()
	{
		if (flcExport == null)
		{
			flcExport = new JFileChooser();
			flcExport.addChoosableFileFilter(new FileNameExtensionFilter(
					"PNG Image File (*.png)", "png"));
			flcExport.addChoosableFileFilter(new FileNameExtensionFilter(
					"Comma-separated Values (*.csv)", "csv"));
			flcExport.setAcceptAllFileFilterUsed(false);
		}

		flcExport.setSelectedFile(new File(algorithmName + "_" + problem.getName()));

		if (flcExport.showDialog(this, "Export") == JFileChooser.APPROVE_OPTION)
		{
			final String extension = ((FileNameExtensionFilter) flcExport.getFileFilter())
					.getExtensions()[0];

			File selected = flcExport.getSelectedFile();

			if (!selected.getName().endsWith("." + extension))
			{
				selected = new File(flcExport.getSelectedFile() + "." + extension);
			}

			if (!selected.exists()
					|| JOptionPane.showConfirmDialog(this,
							"Overwrite the existing file?", "Action Required",
							JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			{
				if (extension.equals("csv"))
				{
					if (Operations.write(selected, mdlResults.asCSV()))
					{
						JOptionPane.showMessageDialog(this,
								"Solutions successfully exported!");
					}
				}
				else
				// if (extension.equals("png"))
				{
					exportAsImage(selected);
				}
			}
		}
	}

	/**
	 * Returns the Chart Panel.
	 *
	 * @param jfxPanel
	 *            : JFXPanel to be included in the Chart Panel.
	 * @return Chart Panel object.
	 */
	private JScrollPane getChartPanel(JFXPanel jfxPanel)
	{
		jfxPanel.setPreferredSize(new Dimension(mdlResults.getRowCount() * 20,
				PANEL_HEIGHT - 20));
		final JScrollPane sclChart = new JScrollPane(jfxPanel);
		sclChart.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

		return sclChart;
	}

	/**
	 * Returns the Dual Chart StackPane object.
	 *
	 * @return Dual Chart StackPane object.
	 */
	private StackPane getDualChart()
	{
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories(FXCollections.<String> observableArrayList(mdlResults
				.getRowNames()));
		xAxis.setLabel("Objective Function Evaluations and Decision Variables");

		final double tic = mdlResults.getTickUnit();
		final NumberAxis yAxis = new NumberAxis("Values", mdlResults.getLowerBound()
				- tic, mdlResults.getUpperBound() + tic, tic);

		final StackedBarChart<String, Number> mainChart = new StackedBarChart<>(xAxis,
				yAxis, mdlResults.getObjectivesChartData());
		mainChart.setLegendVisible(false);
		mainChart.setAnimated(false);

		final ScatterChart<String, Number> overlayChart = new ScatterChart<>(xAxis,
				yAxis, mdlResults.getVariablesChartData());
		overlayChart.setLegendVisible(false);
		overlayChart.setAnimated(false);

		overlayChart.setAlternativeRowFillVisible(false);
		overlayChart.setAlternativeColumnFillVisible(false);
		overlayChart.setHorizontalGridLinesVisible(false);
		overlayChart.setVerticalGridLinesVisible(false);
		overlayChart.getXAxis().setVisible(false);
		overlayChart.getYAxis().setVisible(false);
		overlayChart.getStylesheets().addAll(
				getClass().getResource(DUAL_CHART_STYLE_FILE).toExternalForm());

		addTooltips(overlayChart, DUAL_CHART);

		return new StackPane(mainChart, overlayChart);
	}

	/**
	 * Returns the JPanel object which will be placed on the "PageEnd"
	 * placeholder of SolutionViewer.
	 *
	 * @return JPanel object which will be placed on the "PageEnd" placeholder
	 *         of SolutionViewer.
	 */
	private JPanel getPageEndPanel()
	{
		final JPanel pnlPageEnd = new JPanel(new GridLayout(2, 1, PADDING, PADDING));

		final JButton btnSave = new JButton(SAVE_SOLUTIONS);
		btnSave.setActionCommand(SAVE_SOLUTIONS);
		btnSave.addActionListener(this);

		final JButton btnExport = new JButton(EXPORT_SOLUTIONS);
		btnExport.setActionCommand(EXPORT_SOLUTIONS);
		btnExport.addActionListener(this);

		final JPanel pnlSaveExport = new JPanel(new GridLayout(1, 2, PADDING, PADDING));
		pnlSaveExport.add(btnSave);
		pnlSaveExport.add(btnExport);

		final JButton btnClose = new JButton(CLOSE);
		btnClose.setActionCommand(CLOSE);
		btnClose.addActionListener(this);

		pnlPageEnd.add(pnlSaveExport);
		pnlPageEnd.add(btnClose);

		return pnlPageEnd;
	}

	/**
	 * Returns the JPanel object which will be placed on the "PageStart"
	 * placeholder of SolutionViewer.
	 *
	 * @return JPanel object which will be placed on the "PageStart" placeholder
	 *         of SolutionViewer.
	 */
	private JPanel getPageStartPanel()
	{
		final JPanel pnlPageStart = new JPanel(new BorderLayout(PADDING, PADDING));

		final JPanel pnlLabels = new JPanel(); // FlowLayout
		pnlLabels.add(new JLabel(mdlResults.getRowCount()
				+ " Pareto Optimal solution(s) found in " + elapsed + "ms."));

		final JPanel pnlButtons = new JPanel(); // FlowLayout

		final JRadioButton rbtViewDualChart = new JRadioButton(DUAL_CHART);
		rbtViewDualChart.setActionCommand(DUAL_CHART);
		rbtViewDualChart.setSelected(true);
		rbtViewDualChart.addActionListener(this);
		final JRadioButton rbtVariablesChart = new JRadioButton(VARIABLES_CHART);
		rbtVariablesChart.setActionCommand(VARIABLES_CHART);
		rbtVariablesChart.addActionListener(this);
		final JRadioButton rbtViewTradeoff = new JRadioButton(TRADEOFF_CURVE);
		rbtViewTradeoff.setActionCommand(TRADEOFF_CURVE);
		rbtViewTradeoff.setEnabled(problem.getNumberOfObjectives() == 2);
		rbtViewTradeoff.addActionListener(this);

		final ButtonGroup btgViewMode = new ButtonGroup();
		btgViewMode.add(rbtViewDualChart);
		btgViewMode.add(rbtVariablesChart);
		btgViewMode.add(rbtViewTradeoff);

		pnlButtons.add(rbtViewDualChart);
		pnlButtons.add(rbtVariablesChart);
		pnlButtons.add(rbtViewTradeoff);

		pnlPageStart.add(pnlLabels, BorderLayout.PAGE_START);
		pnlPageStart.add(pnlButtons, BorderLayout.PAGE_END);
		return pnlPageStart;
	}

	/**
	 * Returns a JScrollPane object containing the table of solutions.
	 *
	 * @param tableModel
	 *            : TableModel object to be used on the table.
	 * @return JScrollPane object containing the table of solutions.
	 */
	private JScrollPane getResultsTable(TableModel tableModel)
	{
		final JTable tblResults = new JTable(tableModel);
		final JTable tblRowColumn = new SolutionSetTable(tblResults, "Solution");
		final JScrollPane sclTable = new JScrollPane(tblResults);
		sclTable.setRowHeaderView(tblRowColumn);
		sclTable.setCorner(JScrollPane.UPPER_LEFT_CORNER, tblRowColumn.getTableHeader());
		sclTable.setPreferredSize(new Dimension(PANEL_WIDTH, TABLE_PANEL_HEIGHT));

		return sclTable;
	}

	/**
	 * Returns the Tradeoff Curve Chart.
	 *
	 * @return Tradeoff Curve Chart.
	 */
	private ScatterChart<Number, Number> getTradeOffCurve()
	{
		String title = mdlResults.getColumnName(mdlResults.getOffset());
		double min = mdlResults.getLowerBound(mdlResults.getOffset());
		double max = mdlResults.getUpperBound(mdlResults.getOffset());
		double tic = mdlResults.getTickUnit(min, max);
		final NumberAxis xAxis = new NumberAxis(title, min - tic, max + tic, tic);

		title = mdlResults.getColumnName(mdlResults.getOffset() + 1);
		min = mdlResults.getLowerBound(mdlResults.getOffset() + 1);
		max = mdlResults.getUpperBound(mdlResults.getOffset() + 1);
		tic = mdlResults.getTickUnit(min, max);
		final NumberAxis yAxis = new NumberAxis(title, min - 1, max + 1, tic);

		final ScatterChart<Number, Number> tradeoffCurve = new ScatterChart<>(xAxis,
				yAxis, mdlResults.getTradeoffCurveData());

		tradeoffCurve.setLegendVisible(false);
		tradeoffCurve.getStylesheets().addAll(
				getClass().getResource(TRADEOFF_CURVE_STYLE_FILE).toExternalForm());
		addTooltips(tradeoffCurve, TRADEOFF_CURVE);

		return tradeoffCurve;
	}

	/**
	 * Returns the Variables Chart.
	 *
	 * @return Variables Chart.
	 */
	private BarChart<String, Number> getVariablesChart()
	{
		final double min = mdlResults.getVarLowerBound();
		final double max = mdlResults.getVarUpperBound();
		final double tic = mdlResults.getTickUnit(min, max);

		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Decision Variables");

		final NumberAxis yAxis = new NumberAxis("Values", min - tic, max + tic, tic);
		yAxis.setLabel("Values");

		final BarChart<String, Number> variablesChart = new BarChart<>(xAxis, yAxis,
				mdlResults.getVariablesChartData());

		addTooltips(variablesChart, VARIABLES_CHART);

		return variablesChart;
	}

	/**
	 * Saves the Pareto Optimal Solution Set to disk.
	 */
	private void saveSolution()
	{
		if (flcSave == null)
		{
			flcSave = new JFileChooser();
			flcSave.setFileFilter(new FileNameExtensionFilter(
					"Pareto Optimal Solution Set (*.sol)", "sol"));
			flcSave.setAcceptAllFileFilterUsed(false);
		}

		flcSave.setSelectedFile(new File(algorithmName + "_" + problem.getName() + ".sol"));

		if (flcSave.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File selected = flcSave.getSelectedFile();

			if (!selected.getName().endsWith(".sol"))
			{
				selected = new File(selected + ".sol");
			}

			if (!selected.exists()
					|| JOptionPane.showConfirmDialog(this,
							"Overwrite the existing file?", "Action Required",
							JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			{
				if (Operations.write(selected, new SavedSolutionSet(problem, solutions,
						algorithmName, elapsed)))
				{
					JOptionPane.showMessageDialog(this,
							"Solutions were successfully saved!", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	/**
	 * Sets the Scene objects to be used on this application.
	 */
	private void setScenes()
	{
		final LoadingWindow load = new LoadingWindow(this, "Drawing...");
		load.setVisible(true);
		Platform.runLater(() -> {
			dualChartScene = new Scene(getDualChart(), PANEL_WIDTH, STAGE_HEIGHT);
			varChartScene = new Scene(getVariablesChart(), PANEL_WIDTH, STAGE_HEIGHT);

			if (problem.getNumberOfObjectives() == 2)
			{
				tradeoffScene = new Scene(getTradeOffCurve(), PANEL_WIDTH, STAGE_HEIGHT);
			}

			fxpResults.setScene(dualChartScene);
			SwingUtilities.invokeLater(() -> {
				load.dispose();
				setWidgets();
			});
		});
	}

	/**
	 * Sets the components of SolutionViewer.
	 */
	private void setWidgets()
	{
		add(getPageStartPanel(), BorderLayout.PAGE_START);
		final JSplitPane sptResults = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				getChartPanel(fxpResults), getResultsTable(mdlResults));
		add(sptResults, BorderLayout.CENTER);
		add(getPageEndPanel(), BorderLayout.PAGE_END);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Swaps the currently displayed Scene object to the specified one.
	 *
	 * @param scene
	 *            : Scene object to be swapped in.
	 */
	private void swapScene(Scene scene)
	{
		Platform.runLater(() -> fxpResults.setScene(scene));
	}
}