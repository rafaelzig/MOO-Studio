package uk.co.blogspot.rafaelzig.gui.component.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Subclass of DefaultListCellRenderer with added support for displaying
 * variables and their descriptions.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class VariableRenderer extends DefaultListCellRenderer
{
	private static final long	serialVersionUID	= -2384094571046359459L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		String displayValue;

		displayValue = "x" + (index + 1) + ": " + value.toString();

		return super.getListCellRendererComponent(list, displayValue, index, isSelected,
				cellHasFocus);
	}
}