package uk.co.blogspot.rafaelzig.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * A subclass of JTable with added support for a row header column to simulate a
 * spreadsheet look and feel.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class SolutionSetTable extends JTable implements ChangeListener,
		PropertyChangeListener, TableModelListener
{
	/**
	 * Inner class utilised as a custom cell renderer for the numeric values
	 * stored on this component.
	 */
	private class DecimalFormatRenderer extends DefaultTableCellRenderer
	{
		private static final long	serialVersionUID	= 1917043117329276841L;

		/**
		 * Format to be used with values stored.
		 */
		private final DecimalFormat	formatter			= new DecimalFormat("0.0###");

		/**
		 * Constructs a new instance of DecimalFormatRenderer setting the
		 * horizontal alignment to CENTER.
		 */
		private DecimalFormatRenderer()
		{
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			value = formatter.format(value);
			return super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
		}
	}

	/**
	 * Inner class utilised as a custom cell renderer for the row header column
	 * to simulate the spreadsheet look and feel.
	 */
	private class RowCellRenderer extends DefaultTableCellRenderer
	{
		private static final long	serialVersionUID	= -199518556891809255L;

		/**
		 * Constructs a new instance of RowCellRenderer setting the horizontal
		 * alignment to CENTER.
		 */
		private RowCellRenderer()
		{
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (table != null)
			{
				final JTableHeader columnHeader = table.getTableHeader();

				if (columnHeader != null)
				{
					setForeground(columnHeader.getForeground());
					setBackground(columnHeader.getBackground());
					setFont(columnHeader.getFont());
				}
			}

			if (isSelected)
			{
				setFont(getFont().deriveFont(Font.BOLD));
			}

			setText(value == null ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));

			return super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
		}
	}

	private static final long	serialVersionUID	= -2416032723596572722L;

	/**
	 * JTable containing the main table data.
	 */
	private final JTable		mainTable;

	/**
	 * Title of the row headers.
	 */
	private final String		rowHeaderTitle;

	/**
	 * Constructs a new instance of SpreadsheetTable setting the main table and
	 * row header title.
	 *
	 * @param mainTable
	 * @param rowHeaderTitle
	 */
	public SolutionSetTable(JTable mainTable, String rowHeaderTitle)
	{
		super();

		this.mainTable = mainTable;
		this.mainTable.addPropertyChangeListener(this);
		this.mainTable.getModel().addTableModelListener(this);

		this.rowHeaderTitle = rowHeaderTitle;

		setFocusable(false);
		setAutoCreateColumnsFromModel(false);
		setSelectionModel(mainTable.getSelectionModel());

		final DefaultTableCellRenderer renderer = new DecimalFormatRenderer();

		for (int i = 0; i < mainTable.getColumnCount(); i++)
		{
			this.mainTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		final TableColumn rowHeaderColumn = new TableColumn();
		rowHeaderColumn.setHeaderValue(new String());
		rowHeaderColumn.setCellRenderer(new RowCellRenderer());
		addColumn(rowHeaderColumn);

		this.mainTable.setGridColor(Color.DARK_GRAY);
		((JLabel) this.mainTable.getTableHeader().getDefaultRenderer())
				.setHorizontalAlignment(JLabel.CENTER);
		setPreferredScrollableViewportSize(getPreferredSize());
	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		final Component parent = getParent();

		if (parent instanceof JViewport)
		{
			final JViewport viewport = (JViewport) parent;
			viewport.addChangeListener(this);
		}
	}

	@Override
	public int getRowCount()
	{
		return mainTable.getRowCount();
	}

	@Override
	public int getRowHeight(int row)
	{
		final int rowHeight = mainTable.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row))
		{
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		return rowHeaderTitle + " " + (row + 1);
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		if (e.getPropertyName().equals("selectionModel"))
		{
			setSelectionModel(mainTable.getSelectionModel());
		}

		if (e.getPropertyName().equals("rowHeight"))
		{
			repaint();
		}

		if (e.getPropertyName().equals("model"))
		{
			mainTable.getModel().addTableModelListener(this);
			revalidate();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		final JViewport viewport = (JViewport) e.getSource();
		final JScrollPane scrollPane = (JScrollPane) viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		revalidate();
	}
}