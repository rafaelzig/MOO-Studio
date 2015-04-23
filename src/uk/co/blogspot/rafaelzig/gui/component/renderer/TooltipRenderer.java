package uk.co.blogspot.rafaelzig.gui.component.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.DescriptiveEnum;

/**
 * A subclass of DefaultListCellRenderer with added functionality to display
 * tooltips based on the object's description.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class TooltipRenderer extends DefaultListCellRenderer
{
	private static final long	serialVersionUID	= -8836078599796263615L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		if (index >= 0)
		{
			list.setToolTipText(((DescriptiveEnum) value).getDescription());
		}

		return super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
	}
}