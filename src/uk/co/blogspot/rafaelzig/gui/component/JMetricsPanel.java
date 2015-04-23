package uk.co.blogspot.rafaelzig.gui.component;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Algorithm;
import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Indicator;
import uk.co.blogspot.rafaelzig.core.parsing.Operations;
import uk.co.blogspot.rafaelzig.gui.component.renderer.TooltipRenderer;

/**
 * A subclass of JPanel aggregating various components to be displayed to the
 * user representing performance metrics of optimisation algorithms.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class JMetricsPanel extends JPanel
{
	private static final long			serialVersionUID	= -5159411904058956423L;

	/**
	 * Padding utilised for this panel.
	 */
	private final int					PADDING				= 10;

	/**
	 * JComboBox storing the problems.
	 */
	private final JComboBox<Object>		cbbProblems			= new JComboBox<>(Operations
																	.loadProblems()
																	.keySet().toArray());

	/**
	 * JList storing the algorithms.
	 */
	private final JList<Algorithm>		lstAlgorithms		= new JList<>(
																	Algorithm.values());

	/**
	 * JList storing the performance indicators.
	 */
	private final JList<Indicator>		lstIndicators		= new JList<>(
																	Indicator.values());

	/**
	 * NumericTextField representing the max evaluations.
	 */
	private final JFormattedTextField	txtMaxEvaluations	= new NumericTextField(false,
																	1);

	/**
	 * NumericTextField representing the max seeds.
	 */
	private final JFormattedTextField	txtSeeds			= new NumericTextField(false,
																	2);

	/**
	 * Constructs a new instance of JMetricsPanel.
	 */
	public JMetricsPanel()
	{
		super();
		setLayout(new BorderLayout(PADDING, PADDING));

		final JPanel pnlProblems = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlProblems.add(new JLabel("Problem to be used:", JLabel.CENTER),
				BorderLayout.LINE_START);
		pnlProblems.add(cbbProblems, BorderLayout.CENTER);

		lstAlgorithms.setCellRenderer(new TooltipRenderer());
		lstAlgorithms.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		lstIndicators.setCellRenderer(new TooltipRenderer());
		lstIndicators.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		final JPanel pnlAlgorithms = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlAlgorithms.add(new JLabel("Optimisation Algorithms:", JLabel.CENTER),
				BorderLayout.PAGE_START);
		pnlAlgorithms.add(new JScrollPane(lstAlgorithms), BorderLayout.CENTER);

		final JPanel pnlIndicators = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlIndicators.add(new JLabel("Performance Indicators:", JLabel.CENTER),
				BorderLayout.PAGE_START);
		pnlIndicators.add(new JScrollPane(lstIndicators), BorderLayout.CENTER);

		txtMaxEvaluations.setValue(25000);
		txtSeeds.setValue(10);

		final JPanel pnlTextFields = new JPanel(new GridLayout(2, 2));
		pnlTextFields.add(txtMaxEvaluations);
		pnlTextFields.add(txtSeeds);
		pnlTextFields.add(new JLabel("Max Evaluations", JLabel.CENTER));
		pnlTextFields.add(new JLabel("Number of Iterations", JLabel.CENTER));

		add(pnlProblems, BorderLayout.PAGE_START);
		add(pnlAlgorithms, BorderLayout.LINE_START);
		add(pnlIndicators, BorderLayout.LINE_END);
		add(pnlTextFields, BorderLayout.PAGE_END);
	}

	/**
	 * Returns the max evaluations selected.
	 *
	 * @return Max evaluations selected.
	 */
	public int getMaxEvaluations()
	{
		return (int) txtMaxEvaluations.getValue();
	}

	/**
	 * Returns the max seeds selected.
	 *
	 * @return Max seeds selected.
	 */
	public int getSeeds()
	{
		return (int) txtSeeds.getValue();
	}

	/**
	 * Returns the selected algorithms.
	 *
	 * @return Selected algorithms.
	 */
	public Algorithm[] getSelectedAlgorithms()
	{
		final List<Algorithm> selection = lstAlgorithms.getSelectedValuesList();

		return selection.toArray(new Algorithm[selection.size()]);
	}

	/**
	 * Returns the selected performance indicators.
	 *
	 * @return Max evaluations selected.
	 */
	public Indicator[] getSelectedIndicators()
	{
		final List<Indicator> selection = lstIndicators.getSelectedValuesList();

		return selection.toArray(new Indicator[selection.size()]);
	}

	/**
	 * Returns the index of the selected problem.
	 *
	 * @return Index of selected problem.
	 */
	public int getSelectedProblemIndex()
	{
		return cbbProblems.getSelectedIndex();
	}

	/**
	 * Returns true if the selection is empty, false otherwise.
	 *
	 * @return True if the selection is empty, false otherwise.
	 */
	public boolean isSelectionEmpty()
	{
		return cbbProblems.getSelectedIndex() < 0 || lstAlgorithms.isSelectionEmpty()
				|| lstIndicators.isSelectionEmpty()
				|| txtMaxEvaluations.getText().isEmpty() || txtSeeds.getText().isEmpty();
	}
}