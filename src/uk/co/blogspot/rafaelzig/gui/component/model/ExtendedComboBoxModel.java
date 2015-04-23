package uk.co.blogspot.rafaelzig.gui.component.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

/**
 * A subclass of DefaultComboBoxModel, adding convenient operations like adding
 * elements to the model from an array and retrieving the elements of the model
 * to a List.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 * @param <T>
 *            : Type of elements stored in this model.
 */
public class ExtendedComboBoxModel<T> extends DefaultComboBoxModel<T>
{
	private static final long	serialVersionUID	= 121292698553244853L;

	/**
	 * Constructs a new instance of ExtendedComboBoxModel with no elements.
	 */
	public ExtendedComboBoxModel()
	{
		super();
	}

	/**
	 * Constructs a new instance of ExtendedComboBoxModel with the specified
	 * elements from the array.
	 *
	 * @param elements
	 *            : Array of elements to be added to this model.
	 */
	public ExtendedComboBoxModel(T[] elements)
	{
		super(elements);
	}

	/**
	 * Adds all of the elements from the specified array to this model.
	 *
	 * @param elements
	 *            : Array of elements to be added to this model.
	 */
	public void addAll(T[] elements)
	{
		Arrays.stream(elements).forEach(element -> addElement(element));
	}

	/**
	 * Returns a List object containing all the elements stored in this model.
	 *
	 * @return List containing all the elements stored in this model.
	 */
	public List<T> getElements()
	{
		final List<T> elements = new LinkedList<T>();

		for (int i = 0; i < getSize(); i++)
		{
			elements.add(getElementAt(i));
		}

		return elements;
	}
}