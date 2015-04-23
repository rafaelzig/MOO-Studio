package uk.co.blogspot.rafaelzig.gui.component.model;

import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate;

import com.fathzer.soft.javaluator.AbstractVariableSet;

/**
 * Subclass of ExtendedComboBoxModel adding implementation of interface
 * AbstractVariableSet<Double> facilitating integration with JAVALUATOR library
 * for adding evaluation contexts.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 * @param <T>
 *            : Type of elements stored in this model.
 */
public class VariablesComboBoxModel<T extends VariableTemplate> extends
		ExtendedComboBoxModel<T> implements AbstractVariableSet<Double>
{
	private static final long	serialVersionUID	= 8394311743200699649L;

	/**
	 * Constructs a new instance of VariablesComboBoxModel.
	 */
	public VariablesComboBoxModel()
	{
		super();
	}

	@Override
	public Double get(String variableName)
	{
		if (variableName.length() > 1)
		{
			if (variableName.startsWith("x"))
			{
				final T element = getElementAt(Integer.valueOf(variableName.substring(1)) - 1);

				if (element != null)
				{
					return element.getDummyValue();
				}
			}
		}

		return null;
	}
}