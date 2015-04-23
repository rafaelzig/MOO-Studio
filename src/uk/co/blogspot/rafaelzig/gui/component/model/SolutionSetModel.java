package uk.co.blogspot.rafaelzig.gui.component.model;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import javax.swing.table.AbstractTableModel;

import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;

import uk.co.blogspot.rafaelzig.core.datastructure.template.Objective;
import uk.co.blogspot.rafaelzig.core.datastructure.template.ProblemTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate;

/**
 * A subclass of AbstractTableModel, implementing required abstract methods and
 * adding support to generate chart data based on the table data.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class SolutionSetModel extends AbstractTableModel
{
	private static final long	serialVersionUID	= 4706282550136761692L;

	/**
	 * Table data.
	 */
	private final double[][]	tableData;

	/**
	 * Column names.
	 */
	private final String[]		colNames;

	/**
	 * Row names.
	 */
	private final String[]		rowNames;

	/**
	 * Lower bounds of objective function evaluations.
	 */
	private double				objLowerBound;

	/**
	 * Upper bounds of objective function evaluations.
	 */
	private double				objUpperBound;

	/**
	 * Lower bounds of variables.
	 */
	private double				varLowerBound;

	/**
	 * Upper bounds of variables.
	 */
	private double				varUpperBound;

	/**
	 * Column offset representing division in the table data between variables
	 * and objective functions.
	 */
	private final int			offset;

	/**
	 * Name of the problem from which the data belongs.
	 */
	private final String		problemName;

	/**
	 * Constructs a new instance of SolutionSetModel with the specified list of
	 * solutions and problem.
	 *
	 * @param solutions
	 *            : List of solutions
	 * @param problem
	 */
	public SolutionSetModel(List<Solution> solutions, ProblemTemplate problem)
	{
		final int numberOfObjectives = problem.getNumberOfObjectives();
		offset = problem.getNumberOfVariables();
		problemName = problem.getName();

		tableData = new double[solutions.size()][offset + numberOfObjectives];
		colNames = new String[offset + numberOfObjectives];
		rowNames = new String[solutions.size()];

		buildTableData(solutions, problem.getObjectives(), problem.getVariables());

		objLowerBound = Math.floor(objLowerBound);
		objUpperBound = Math.ceil(objUpperBound);
		varLowerBound = Math.floor(varLowerBound);
		varUpperBound = Math.ceil(varUpperBound);
	}

	/**
	 * Returns the data stored in this model as CSV format.
	 *
	 * @return String object representing the data in this model as CSV format.
	 */
	public String asCSV()
	{
		final StringBuilder builder = new StringBuilder(problemName + ",");

		Arrays.stream(colNames).forEach(str -> builder.append(str + ","));
		builder.append("\n");

		for (int row = 0; row < rowNames.length; row++)
		{
			builder.append(rowNames[row] + ",");

			for (int col = 0; col < colNames.length; col++)
			{
				builder.append(tableData[row][col] + ",");
			}

			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * Builds the table data with the specified solutions, objectives and
	 * variables.
	 *
	 * @param solutions
	 *            : List of Solution objects to be stored in the model.
	 * @param objectives
	 *            : Array of Objective objects to be used as reference.
	 * @param variables
	 *            : Array of VariableTemplate objects to be used as reference.
	 */
	private void buildTableData(List<Solution> solutions, Objective[] objectives,
			VariableTemplate[] variables)
	{
		final int offset = variables.length;

		for (int row = 0; row < tableData.length; row++)
		{
			final Solution solution = solutions.get(row);

			rowNames[row] = "S" + (row + 1);

			for (int col = 0; col < offset; col++)
			{
				final Variable variable = solution.getVariable(col);

				colNames[col] = "x" + (col + 1) + " : " + variables[col];

				switch (variables[col].getType())
				{
					case BINARY:
						tableData[row][col] = EncodingUtils
								.decode((BinaryVariable) variable);
						break;
					case INTEGER:
						tableData[row][col] = EncodingUtils.getInt(variable);
						break;
					case REAL:
						tableData[row][col] = EncodingUtils.getReal(variable);
						break;
				}

				if (row + col == 0 || tableData[row][col] < varLowerBound)
				{
					varLowerBound = tableData[row][col];
				}

				if (row + col == 0 || tableData[row][col] > varUpperBound)
				{
					varUpperBound = tableData[row][col];
				}
			}

			double subMin = 0, subMax = 0;
			for (int col = offset; col < tableData[row].length; col++)
			{
				final int realIndex = col - offset;
				final String type = objectives[realIndex].isMaximisation() ? "Maximisation"
						: "Minimisation";
				final double value = objectives[realIndex].isMaximisation() ? -solution
						.getObjective(realIndex) : solution.getObjective(realIndex);

				colNames[col] = "f" + (realIndex + 1) + " : " + type;
				tableData[row][col] = value;

				if (value >= 0)
				{
					subMax += value;
				}
				else
				{
					subMin += value;
				}
			}

			objLowerBound = subMin < objLowerBound ? subMin : objLowerBound;
			objUpperBound = subMax > objUpperBound ? subMax : objUpperBound;
		}
	}

	@Override
	public Class<? extends Object> getColumnClass(int col)
	{
		return getValueAt(0, col).getClass();
	}

	@Override
	public int getColumnCount()
	{
		return colNames.length;
	}

	@Override
	public String getColumnName(int col)
	{
		return colNames[col];
	}

	/**
	 * Returns the names of the columns of this model.
	 *
	 * @return Names of the columns of this model.
	 */
	public String[] getColumnNames()
	{
		return colNames;
	}

	/**
	 * Returns the absolute lower bound of this model.
	 *
	 * @return Absolute lower bound of this model.
	 */
	public double getLowerBound()
	{
		return varLowerBound < objLowerBound ? varLowerBound : objLowerBound;
	}

	/**
	 * Returns the lower bound of the specified column of this model.
	 *
	 * @param column
	 *            : Column index to be used.
	 * @return Lower bound of the specified column of this model.
	 */
	public double getLowerBound(int column)
	{
		double min = tableData[0][column];

		for (int row = 1; row < tableData.length; row++)
		{
			min = tableData[row][column] < min ? tableData[row][column] : min;
		}

		return Math.floor(min);
	}

	/**
	 * Builds and returns the objectives chart data.
	 *
	 * @return The objectives chart data.
	 */
	public ObservableList<XYChart.Series<String, Number>> getObjectivesChartData()
	{
		final ObservableList<XYChart.Series<String, Number>> objectivesChartData = FXCollections
				.<XYChart.Series<String, Number>> observableArrayList();

		for (int col = offset; col < colNames.length; col++)
		{
			final XYChart.Series<String, Number> series = new XYChart.Series<>();

			series.setName(colNames[col]);

			for (int row = 0; row < rowNames.length; row++)
			{
				series.getData().add(
						new XYChart.Data<String, Number>(rowNames[row],
								tableData[row][col]));
			}

			objectivesChartData.add(series);
		}

		return objectivesChartData;
	}

	/**
	 * Returns the column offset representing division in the table data between
	 * variables and objective functions.
	 *
	 * @return Column offset representing division in the table data between
	 *         variables and objective functions.
	 */
	public int getOffset()
	{
		return offset;
	}

	@Override
	public int getRowCount()
	{
		return tableData.length;
	}

	/**
	 * Returns the names of the rows of this model.
	 *
	 * @return Names of the rows of this model.
	 */
	public String[] getRowNames()
	{
		return rowNames;
	}

	/**
	 * Returns the absolute tick unit of this table model.
	 *
	 * @return Absolute tick unit of this table model.
	 */
	public double getTickUnit()
	{
		return getTickUnit(objLowerBound, objUpperBound);
	}

	/**
	 * Returns the tick unit between the specified bounds.
	 *
	 * @param lowerBound
	 *            : Lower bounds to be used.
	 * @param upperBound
	 *            : Upper bounds to be used.
	 * @return Tick unit between specified bounds.
	 */
	public double getTickUnit(double lowerBound, double upperBound)
	{
		return (Math.abs(lowerBound) + Math.abs(upperBound)) / 20;
	}

	/**
	 * Builds and returns the tradeoff curve chart data.
	 *
	 * @return Tradeoff curve chart data.
	 */
	public ObservableList<Series<Number, Number>> getTradeoffCurveData()
	{
		final ObservableList<XYChart.Series<Number, Number>> tradeoffCurveData = FXCollections
				.<XYChart.Series<Number, Number>> observableArrayList();

		for (int row = 0; row < tableData.length; row++)
		{
			final XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName(rowNames[row]);
			series.getData().add(
					new XYChart.Data<Number, Number>(tableData[row][offset],
							tableData[row][offset + 1], rowNames[row]));
			tradeoffCurveData.add(series);
		}

		return tradeoffCurveData;
	}

	/**
	 * Returns the absolute upper bound of this model.
	 *
	 * @return Absolute upper bound of this model.
	 */
	public double getUpperBound()
	{
		return varUpperBound > objUpperBound ? varUpperBound : objUpperBound;
	}

	/**
	 * Returns the upper bound of the specified column of this model.
	 *
	 * @param column
	 *            : Column index to be used.
	 * @return Upper bound of the specified column of this model.
	 */
	public double getUpperBound(int column)
	{
		double max = tableData[0][column];

		for (int row = 1; row < tableData.length; row++)
		{
			max = tableData[row][column] > max ? tableData[row][column] : max;
		}

		return Math.ceil(max);
	}

	@Override
	public Number getValueAt(int row, int col)
	{
		return tableData[row][col];
	}

	/**
	 * Builds and returns the variables chart data.
	 *
	 * @return The variables chart data.
	 */
	public ObservableList<XYChart.Series<String, Number>> getVariablesChartData()
	{
		final ObservableList<XYChart.Series<String, Number>> variablesChartData = FXCollections
				.<XYChart.Series<String, Number>> observableArrayList();

		for (int col = 0; col < offset; col++)
		{
			final XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(colNames[col]);

			for (int row = 0; row < rowNames.length; row++)
			{
				series.getData().add(
						new XYChart.Data<String, Number>(rowNames[row],
								tableData[row][col]));
			}

			variablesChartData.add(series);
		}

		return variablesChartData;
	}

	/**
	 * Returns the lower bounds of the variables stored in this model.
	 *
	 * @return Lower bounds of the variables stored in this model.
	 */
	public double getVarLowerBound()
	{
		return varLowerBound;
	}

	/**
	 * Returns the upper bounds of the variables stored in this model.
	 *
	 * @return Upper bounds of the variables stored in this model.
	 */
	public double getVarUpperBound()
	{
		return varUpperBound;
	}
}