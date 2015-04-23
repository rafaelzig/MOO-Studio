package uk.co.blogspot.rafaelzig.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Constant;
import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Function;
import uk.co.blogspot.rafaelzig.core.datastructure.enumeration.Operator;
import uk.co.blogspot.rafaelzig.core.datastructure.template.BinaryTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.Constraint;
import uk.co.blogspot.rafaelzig.core.datastructure.template.Constraint.ConstraintOperator;
import uk.co.blogspot.rafaelzig.core.datastructure.template.IntegerTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.Objective;
import uk.co.blogspot.rafaelzig.core.datastructure.template.ProblemTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.RealTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate;
import uk.co.blogspot.rafaelzig.core.parsing.Operations;
import uk.co.blogspot.rafaelzig.gui.component.NumericTextField;
import uk.co.blogspot.rafaelzig.gui.component.model.ExtendedComboBoxModel;
import uk.co.blogspot.rafaelzig.gui.component.model.VariablesComboBoxModel;
import uk.co.blogspot.rafaelzig.gui.component.renderer.CompactVariableRenderer;
import uk.co.blogspot.rafaelzig.gui.component.renderer.TooltipRenderer;
import uk.co.blogspot.rafaelzig.gui.component.renderer.VariableRenderer;

/**
 * A subclass of JDialog which encapsulates several subcomponents, guiding the
 * user through the creation of multi-objective optimisation problems.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
class ProblemDesigner extends JDialog implements ActionListener, ChangeListener,
		CaretListener
{
	private static final long			serialVersionUID	= -63965450778679394L;

	/**
	 * Several constants utilised throughout this application.
	 */
	private final int					PADDING				= 5;
	private final String				EMPTY				= " ";
	private final String				REAL				= "Real Variable";
	private final String				INTEGER				= "Integer Variable";
	private final String				BINARY				= "Binary Variable";
	private final String				CLOSE				= "Close";
	private final String				SAVE				= "Save";
	private final String				REMOVE_ELEMENT		= "Remove Element";
	private final String				ADD_ELEMENT			= "Add Element";
	private final String				ADD_OPERATOR		= "Add Operator";
	private final String				ADD_FUNCTION		= "Add Function";
	private final String				ADD_CONSTANT		= "Add Constant";
	private final String				ADD_VARIABLE		= "Add Variable";
	private final String				CONSTRAINTS			= "Problem Constraints";
	private final String				OBJECTIVES			= "Objective Functions";
	private final String				VARIABLES			= "Decision Variables";

	/**
	 * Several fields utilised throughout this application.
	 */
	private JTabbedPane					pnlTabs;
	private JPanel						pnlVariableBounds, pnlOperators;
	private ButtonGroup					btgVariableTypes, btgMaxMin, btgLessGreater;
	private JList<VariableTemplate>		lstVariables;
	private JList<Objective>			lstObjectives;
	private JList<Constraint>			lstConstraints;
	private JLabel						lblStatus;
	private JComboBox<VariableTemplate>	cbbVariables;
	private JFormattedTextField			txtIntLower, txtIntUpper, txtRealLower,
			txtRealUpper;
	private JTextArea					txtObjectiveBuilder, txtLhs, txtRhs, txtCurrent;

	/**
	 * Constructs a new instance of ProblemDesigner and initialises its
	 * components.
	 *
	 * @param owner
	 *            : Frame from which the dialog is displayed.
	 */
	ProblemDesigner(Frame owner)
	{
		this(owner, null);
	}

	/**
	 * Constructs a new instance of ProblemDesigner and initialises its to
	 * display the specified problem.
	 *
	 * @param owner
	 *            : Frame from which the dialog is displayed.
	 */
	ProblemDesigner(Frame owner, ProblemTemplate problem)
	{
		super(owner, "Problem Designer");
		setLayout(new BorderLayout(PADDING, PADDING));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		});
		setWidgets();

		if (problem != null)
		{
			loadProblem(problem);
		}

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		final String action = e.getActionCommand();

		switch (action)
		{
			case ADD_ELEMENT:
				addElement();
				break;

			case REMOVE_ELEMENT:
				removeElement();
				break;
			case ADD_OPERATOR:
			case ADD_FUNCTION:
			case ADD_CONSTANT:
				addExpression(((JComboBox<?>) e.getSource()).getSelectedItem());
				break;
			case ADD_VARIABLE:
				addExpression("x"
						+ (((JComboBox<?>) e.getSource()).getSelectedIndex() + 1));
				break;
			case BINARY:
			case INTEGER:
			case REAL:
				swapPanels(action);
				break;

			case SAVE:
				if (save())
				{
					resetForm();
					dispose();
				}
				break;

			case CLOSE:
				close();
				break;
		}
	}

	/**
	 * Adds the currently selected constraint to the list of constraints.
	 */
	private void addConstraint()
	{
		final String lhs = txtLhs.getText();
		final String rhs = txtRhs.getText();

		if (checkExpression(lhs).equals(Operations.VALID)
				&& checkExpression(rhs).equals(Operations.VALID))
		{
			ConstraintOperator type;

			if (getSelectedAction(btgLessGreater).equals(
					ConstraintOperator.LESS_OR_EQUAL.toString()))
			{
				type = ConstraintOperator.LESS_OR_EQUAL;
			}
			else if (getSelectedAction(btgLessGreater).equals(
					ConstraintOperator.NOT_EQUAL.toString()))
			{
				type = ConstraintOperator.NOT_EQUAL;
			}
			else
			{
				type = ConstraintOperator.GREATER_OR_EQUAL;
			}

			final Constraint cons = new Constraint(type, lhs, rhs);

			((DefaultComboBoxModel<Constraint>) lstConstraints.getModel())
					.addElement(cons);

			txtLhs.setText("");
			txtRhs.setText("");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Invalid expression inserted.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Adds the currently selected element to the application.
	 */
	private void addElement()
	{
		final String currTab = pnlTabs.getTitleAt(pnlTabs.getSelectedIndex());

		switch (currTab)
		{
			case VARIABLES:
				addVariable(getSelectedAction(btgVariableTypes));
				break;

			case OBJECTIVES:
				addObjective();
				break;

			case CONSTRAINTS:
				addConstraint();
				break;
		}
	}

	/**
	 * Adds the specified expression to the active text field.
	 *
	 * @param expression
	 *            : Expression to be added to the active text field.
	 */
	private void addExpression(Object expression)
	{
		txtCurrent.insert(expression.toString(), txtCurrent.getCaretPosition());
	}

	/**
	 * Adds the currently selected objective to the list of objectives.
	 */
	private void addObjective()
	{
		final String expression = txtObjectiveBuilder.getText();

		if (checkExpression(expression).equals(Operations.VALID))
		{
			final boolean isMaximisation = getSelectedAction(btgMaxMin) == "Maximise";
			final Objective obj = new Objective(expression, isMaximisation);

			((DefaultComboBoxModel<Objective>) lstObjectives.getModel()).addElement(obj);

			txtObjectiveBuilder.setText("");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Invalid expression inserted.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Adds the currently selected variable to the list of variables.
	 *
	 * @param varType
	 *            : Type of variable to be added.
	 */
	private void addVariable(String varType)
	{
		if (checkBounds(varType))
		{
			VariableTemplate tmp;

			switch (varType)
			{
				case BINARY:
					tmp = new BinaryTemplate();
					break;

				case INTEGER:
					tmp = new IntegerTemplate((int) txtIntLower.getValue(),
							(int) txtIntUpper.getValue());
					txtIntLower.setValue(0);
					txtIntUpper.setValue(0);
					break;

				case REAL:
					tmp = new RealTemplate((double) txtRealLower.getValue(),
							(double) txtRealUpper.getValue());
					txtRealLower.setValue(0.0);
					;
					txtRealUpper.setValue(0.0);
					break;

				default:
					tmp = null;
					break;
			}

			cbbVariables.removeActionListener(this);

			((DefaultComboBoxModel<VariableTemplate>) lstVariables.getModel())
					.addElement(tmp);

			cbbVariables.addActionListener(this);
		}
	}

	@Override
	public void caretUpdate(CaretEvent e)
	{
		txtCurrent = (JTextArea) e.getSource();
		lblStatus.setText(checkExpression(txtCurrent.getText()));

		if (lblStatus.getText().equals(Operations.VALID))
		{
			lblStatus.setForeground(Color.GREEN);
		}
		else
		{
			lblStatus.setForeground(Color.RED);
		}
	}

	/**
	 * Checks the bounds of the currently selected variable, returning true if
	 * within bounds, false otherwise.
	 *
	 * @param varType
	 *            : Type of the selected variable.
	 * @return True if within bounds, false otherwise.
	 */
	private boolean checkBounds(String varType)
	{
		if (!varType.equals(BINARY))
		{
			String strLower, strUpper;

			if (varType.equals(INTEGER))
			{
				strLower = txtIntLower.getText();
				strUpper = txtIntUpper.getText();
			}
			else
			// if (varType.equals(REAL_VAR))
			{
				strLower = txtRealLower.getText();
				strUpper = txtRealUpper.getText();
			}

			if (strLower.isEmpty() || strUpper.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Bounds required.", "Error",
						JOptionPane.ERROR_MESSAGE);

				return false;
			}

			if (Double.valueOf(strLower) > Double.valueOf(strUpper))
			{
				JOptionPane.showMessageDialog(this, "Invalid variable bounds.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks the syntax of specified mathematical expression, returning the
	 * results of the check.
	 *
	 * @param expression
	 *            : Mathematical expression to be checked.
	 * @return Message containing the results of the check.
	 */
	private String checkExpression(String expression)
	{
		if (!expression.isEmpty())
		{
			return Operations.checkExpression(expression,
					(VariablesComboBoxModel<VariableTemplate>) lstVariables.getModel());
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Closes the ProblemDesigner window disposing of its resources.
	 */
	private void close()
	{
		final int userResponse = JOptionPane.showConfirmDialog(this,
				"Save the problem before closing?", "Confirm Action",
				JOptionPane.YES_NO_OPTION);

		if (userResponse == JOptionPane.YES_OPTION && save()
				|| userResponse == JOptionPane.NO_OPTION)
		{
			resetForm();
			dispose();
		}
	}

	/**
	 * Creates and returns a ProblemTemplate object with the selected elements.
	 *
	 * @return ProblemTemplate object created.
	 */
	private ProblemTemplate createProblem()
	{
		final List<VariableTemplate> variables = ((ExtendedComboBoxModel<VariableTemplate>) lstVariables
				.getModel()).getElements();

		final List<Objective> objectives = ((ExtendedComboBoxModel<Objective>) lstObjectives
				.getModel()).getElements();

		final List<Constraint> constraints = ((ExtendedComboBoxModel<Constraint>) lstConstraints
				.getModel()).getElements();

		String error;

		if (!(variables.isEmpty() || objectives.isEmpty()))
		{
			final String problemName = JOptionPane.showInputDialog(this,
					"Enter the name of this problem:", "Input Required",
					JOptionPane.QUESTION_MESSAGE);

			if (!(problemName == null || problemName.isEmpty()))
			{
				return new ProblemTemplate(problemName, variables, objectives,
						constraints);
			}
			else
			{
				error = "Problem name is required.";
			}
		}
		else
		{
			error = "Problem variables and objectives required.";
		}

		JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
		return null;
	}

	/**
	 * Returns the Constraint Builder JPanel.
	 *
	 * @return Constraint Builder JPanel.
	 */
	private JPanel getConstraintBuilderPanel()
	{
		final JPanel pnlConstraintBuilder = new JPanel(new GridLayout(3, 1, PADDING,
				PADDING));
		final JPanel pnlLessGreater = new JPanel(new GridLayout(1, 3, PADDING, PADDING));

		// Initialise the JTextField object
		txtLhs = new JTextArea();
		txtLhs.addCaretListener(this);

		// Initialise the JTextField object
		txtRhs = new JTextArea();
		txtRhs.addCaretListener(this);

		// Sets the JRadioButtons
		final JRadioButton rbtLess = new JRadioButton(
				ConstraintOperator.LESS_OR_EQUAL.toString());
		rbtLess.setActionCommand(rbtLess.getText());
		rbtLess.setSelected(true);
		final JRadioButton rbtNotEqual = new JRadioButton(
				ConstraintOperator.NOT_EQUAL.toString());
		rbtNotEqual.setActionCommand(rbtNotEqual.getText());
		final JRadioButton rbtGreater = new JRadioButton(
				ConstraintOperator.GREATER_OR_EQUAL.toString());
		rbtGreater.setActionCommand(rbtGreater.getText());
		pnlLessGreater.add(rbtLess);
		pnlLessGreater.add(rbtNotEqual);
		pnlLessGreater.add(rbtGreater);

		// Sets the ButtonGroup object to hold the radio buttons
		btgLessGreater = new ButtonGroup();
		btgLessGreater.add(rbtLess);
		btgLessGreater.add(rbtNotEqual);
		btgLessGreater.add(rbtGreater);

		// Sets the JPanel which will hold the constraints
		pnlConstraintBuilder.add(new JScrollPane(txtLhs));
		pnlConstraintBuilder.add(pnlLessGreater);
		pnlConstraintBuilder.add(new JScrollPane(txtRhs));

		return pnlConstraintBuilder;
	}

	/**
	 * Returns the Constraints JPanel.
	 *
	 * @return Constraints JPanel.
	 */
	private JPanel getConstraintsPanel()
	{
		final JPanel pnlConstraints = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlConstraints.add(getConstraintBuilderPanel(), BorderLayout.CENTER);
		pnlConstraints.add(getCurrentElementsPanel(CONSTRAINTS), BorderLayout.LINE_END);

		return pnlConstraints;
	}

	/**
	 * Returns a JPanel object of the specified type.
	 *
	 * @param type
	 *            : Type of the JPanel object to be returned.
	 * @return JPanel object of the specified type.
	 */
	private JPanel getCurrentElementsPanel(String type)
	{
		final JPanel pnlCurrentElements = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlCurrentElements.add(new JLabel("Current Elements"), BorderLayout.PAGE_START);
		pnlCurrentElements.setPreferredSize(new Dimension(300, 50));

		switch (type)
		{
			case VARIABLES:
				lstVariables = new JList<>(new VariablesComboBoxModel<VariableTemplate>());
				lstVariables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstVariables.setCellRenderer(new VariableRenderer());
				pnlCurrentElements
						.add(new JScrollPane(lstVariables), BorderLayout.CENTER);
				break;
			case OBJECTIVES:
				lstObjectives = new JList<>(new ExtendedComboBoxModel<Objective>());
				lstObjectives.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				pnlCurrentElements.add(new JScrollPane(lstObjectives),
						BorderLayout.CENTER);
				break;
			case CONSTRAINTS:
				lstConstraints = new JList<>(new ExtendedComboBoxModel<Constraint>());
				lstConstraints.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				pnlCurrentElements.add(new JScrollPane(lstConstraints),
						BorderLayout.CENTER);
				break;
		}

		return pnlCurrentElements;
	}

	/**
	 * Returns a JPanel object which will hold the Integer related elements.
	 *
	 * @return JPanel object which will hold the Integer related elements.
	 */
	private JPanel getIntegerPanel()
	{
		final JPanel pnlInteger = new JPanel();

		txtIntLower = new NumericTextField(false);
		txtIntUpper = new NumericTextField(false);

		pnlInteger.setLayout(new BoxLayout(pnlInteger, BoxLayout.Y_AXIS));
		pnlInteger.add(new JLabel("Integer Lower Bound:"));
		pnlInteger.add(txtIntLower);
		pnlInteger.add(new JLabel("Integer Upper Bound:"));
		pnlInteger.add(txtIntUpper);

		return pnlInteger;
	}

	/**
	 * Returns the Objective Builder JPanel.
	 *
	 * @return Objective Builder JPanel.
	 */
	private JPanel getObjectiveBuilderPanel()
	{
		final JPanel pnlObjectiveBuilder = new JPanel(new GridLayout(2, 1, PADDING,
				PADDING));

		// Initialise the JTextField object along with the custom filter
		txtObjectiveBuilder = new JTextArea();
		txtObjectiveBuilder.addCaretListener(this);

		// Sets the JRadioButtons
		final JRadioButton rbtMin = new JRadioButton("Minimise", true);
		final JRadioButton rbtMax = new JRadioButton("Maximise");

		// Sets the ButtonGroup object to hold the radio buttons
		btgMaxMin = new ButtonGroup();
		btgMaxMin.add(rbtMin);
		btgMaxMin.add(rbtMax);

		final JPanel pnlMaxMinButtons = new JPanel();
		pnlMaxMinButtons.add(rbtMin);
		pnlMaxMinButtons.add(rbtMax);

		// Adds the elements to the Objective Builder JPanel
		pnlObjectiveBuilder.add(pnlMaxMinButtons);
		pnlObjectiveBuilder.add(new JScrollPane(txtObjectiveBuilder));

		return pnlObjectiveBuilder;
	}

	/**
	 * Returns the Objectives JPanel.
	 *
	 * @return Objectives JPanel.
	 */
	private JPanel getObjectivesPanel()
	{
		final JPanel pnlObjectives = new JPanel(new BorderLayout(PADDING, PADDING));
		pnlObjectives.add(pnlOperators, BorderLayout.LINE_START);
		pnlObjectives.add(getObjectiveBuilderPanel(), BorderLayout.CENTER);
		pnlObjectives.add(getCurrentElementsPanel(OBJECTIVES), BorderLayout.LINE_END);

		return pnlObjectives;
	}

	/**
	 * Returns the Operators JPanel.
	 *
	 * @return Operators JPanel.
	 */
	private JPanel getOperatorsPanel()
	{
		final JPanel pnlOperators = new JPanel(new GridLayout(4, 2, PADDING, PADDING));

		final DefaultListCellRenderer renderer = new TooltipRenderer();

		// Sets the JComboBox objects for the operators
		final JComboBox<Operator> cbbOperators = new JComboBox<>(Operator.values());
		cbbOperators.setRenderer(renderer);
		cbbOperators.setActionCommand(ADD_OPERATOR);
		cbbOperators.addActionListener(this);

		// Sets the JComboBox objects for the functions
		final JComboBox<Function> cbbFunctions = new JComboBox<>(Function.values());
		cbbFunctions.setRenderer(renderer);
		cbbFunctions.setActionCommand(ADD_FUNCTION);
		cbbFunctions.addActionListener(this);

		// Sets the JComboBox objects for the constants
		final JComboBox<Constant> cbbConstants = new JComboBox<>(Constant.values());
		cbbConstants.setRenderer(renderer);
		cbbConstants.setActionCommand(ADD_CONSTANT);
		cbbConstants.addActionListener(this);

		// Sets the JComboBox objects for the variables
		cbbVariables = new JComboBox<>(
				(VariablesComboBoxModel<VariableTemplate>) lstVariables.getModel());
		cbbVariables.setRenderer(new CompactVariableRenderer());
		cbbVariables.setActionCommand(ADD_VARIABLE);
		cbbVariables.addActionListener(this);

		pnlOperators.add(new JLabel("Operators:"));
		pnlOperators.add(cbbOperators);
		pnlOperators.add(new JLabel("Functions:"));
		pnlOperators.add(cbbFunctions);
		pnlOperators.add(new JLabel("Constants:"));
		pnlOperators.add(cbbConstants);
		pnlOperators.add(new JLabel("Variables:"));
		pnlOperators.add(cbbVariables);
		return pnlOperators;
	}

	/**
	 * Returns the JPanel object which will be placed on the "PageEnd"
	 * placeholder of ProblemDesigner.
	 *
	 * @returns JPanel object which will be placed on the "PageEnd" placeholder
	 *          of ProblemDesigner.
	 */
	private JPanel getPageEndPanel()
	{
		final JPanel pnlPageEnd = new JPanel(new BorderLayout(PADDING, PADDING));
		final JPanel pnlButtons = new JPanel(new GridLayout(2, 2, PADDING, PADDING));

		// Sets the JButton objects to be placed on the JPanel
		final JButton btnAdd = new JButton(ADD_ELEMENT);
		btnAdd.setActionCommand(ADD_ELEMENT);
		btnAdd.addActionListener(this);

		final JButton btnRemove = new JButton(REMOVE_ELEMENT);
		btnRemove.setActionCommand(REMOVE_ELEMENT);
		btnRemove.addActionListener(this);

		final JButton btnSave = new JButton(SAVE);
		btnSave.setActionCommand(SAVE);
		btnSave.addActionListener(this);

		final JButton btnClose = new JButton(CLOSE);
		btnClose.setActionCommand(CLOSE);
		btnClose.addActionListener(this);

		// Sets the JPanel located at the PageEnd placeholder
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnRemove);
		pnlButtons.add(btnSave);
		pnlButtons.add(btnClose);

		lblStatus = new JLabel(EMPTY, JLabel.CENTER);
		pnlPageEnd.add(lblStatus, BorderLayout.PAGE_START);
		pnlPageEnd.add(pnlButtons, BorderLayout.CENTER);

		return pnlPageEnd;
	}

	/**
	 * Returns the JPanel object which will hold the Real Variable related
	 * elements.
	 *
	 * @return JPanel object which will hold the Real Variable related elements.
	 */
	private JPanel getRealPanel()
	{
		final JPanel pnlReal = new JPanel();

		txtRealLower = new NumericTextField(true);
		txtRealUpper = new NumericTextField(true);

		// Sets the JPanel which will hold Real Variable specific parameters
		pnlReal.setLayout(new BoxLayout(pnlReal, BoxLayout.Y_AXIS));
		pnlReal.add(new JLabel("Real Lower Bound:"));
		pnlReal.add(txtRealLower);
		pnlReal.add(new JLabel("Real Upper Bound:"));
		pnlReal.add(txtRealUpper);

		return pnlReal;
	}

	/**
	 * Returns the selected action command of the specified ButtonGroup object.
	 *
	 * @param buttonGroup
	 *            : ButtonGroup object to be inspected.
	 * @return Selected action command of the specified ButtonGroup object.
	 */
	private String getSelectedAction(ButtonGroup buttonGroup)
	{
		for (final Enumeration<AbstractButton> e = buttonGroup.getElements(); e
				.hasMoreElements();)
		{
			final AbstractButton btn = e.nextElement();

			if (btn.isSelected())
			{
				return btn.getActionCommand();
			}
		}

		return null;
	}

	/**
	 * Returns the Variables Bounds JPanel object.
	 *
	 * @return Variables Bounds JPanel object.
	 */
	private JPanel getVariableBoundsPanel()
	{
		pnlVariableBounds = new JPanel(new CardLayout(5, 5));

		pnlVariableBounds.add(new JPanel(), BINARY);
		pnlVariableBounds.add(getIntegerPanel(), INTEGER);
		pnlVariableBounds.add(getRealPanel(), REAL);

		return pnlVariableBounds;
	}

	/**
	 * Returns the Variables JPanel object.
	 *
	 * @return Variables JPanel object.
	 */
	private JPanel getVariablesPanel()
	{
		final JPanel pnlVariables = new JPanel(new BorderLayout(PADDING, PADDING));

		pnlVariables.add(getVariableTypesPanel(), BorderLayout.LINE_START);
		pnlVariables.add(getVariableBoundsPanel(), BorderLayout.CENTER);
		pnlVariables.add(getCurrentElementsPanel(VARIABLES), BorderLayout.LINE_END);

		return pnlVariables;
	}

	/**
	 * Returns the Variable Types JPanel object.
	 *
	 * @return Variable Types JPanel object.
	 */
	private JPanel getVariableTypesPanel()
	{
		final JPanel pnlVariableTypes = new JPanel();

		pnlVariableTypes.setLayout(new BoxLayout(pnlVariableTypes, BoxLayout.Y_AXIS));
		pnlVariableTypes.add(new JLabel("Variable Type"));

		final JRadioButton rbtBinary = new JRadioButton(BINARY, true);
		final JRadioButton rbtInteger = new JRadioButton(INTEGER);
		final JRadioButton rbtReal = new JRadioButton(REAL);

		// Sets the ButtonGroup object to hold the radio buttons
		btgVariableTypes = new ButtonGroup();
		btgVariableTypes.add(rbtBinary);
		btgVariableTypes.add(rbtInteger);
		btgVariableTypes.add(rbtReal);

		for (final Enumeration<AbstractButton> e = btgVariableTypes.getElements(); e
				.hasMoreElements();)
		{
			final AbstractButton btn = e.nextElement();
			btn.setActionCommand(btn.getText());
			btn.addActionListener(this);
			pnlVariableTypes.add(btn);
		}

		return pnlVariableTypes;
	}

	/**
	 * Loads the specified problem to the Problem Designer.
	 *
	 * @param problem
	 *            : Problem to be loaded to Problem Designer
	 */
	void loadProblem(ProblemTemplate problem)
	{
		cbbVariables.removeActionListener(this);
		((ExtendedComboBoxModel<VariableTemplate>) lstVariables.getModel())
				.addAll(problem.getVariables());
		((ExtendedComboBoxModel<Objective>) lstObjectives.getModel()).addAll(problem
				.getObjectives());
		((ExtendedComboBoxModel<Constraint>) lstConstraints.getModel()).addAll(problem
				.getConstraints());
		cbbVariables.addActionListener(this);
	}

	/**
	 * Removes the currently selected element from the application.
	 */
	private void removeElement()
	{
		final String currTab = pnlTabs.getTitleAt(pnlTabs.getSelectedIndex());
		JList<?> current;

		switch (currTab)
		{
			case VARIABLES:
				current = lstVariables;
				break;

			case OBJECTIVES:
				current = lstObjectives;
				break;

			case CONSTRAINTS:
				current = lstConstraints;
				break;

			default:
				current = null;
				break;
		}

		if (current != null)
		{
			final int index = current.getSelectedIndex();

			if (current.getSelectedIndex() >= 0)
			{
				cbbVariables.removeActionListener(this);

				((DefaultComboBoxModel<?>) current.getModel()).removeElementAt(index);

				cbbVariables.addActionListener(this);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select an element first.");
			}
		}
	}

	/**
	 * Resets the components utilised by ProblemDesigner.
	 */
	private void resetForm()
	{
		resetModels();
		resetTextFields();
		resetRadioButtons();
		pnlTabs.setSelectedIndex(0);
	}

	/**
	 * Resets the models utilised by ProblemDesigner.
	 */
	private void resetModels()
	{
		cbbVariables.removeActionListener(this);
		((DefaultComboBoxModel<VariableTemplate>) lstVariables.getModel())
				.removeAllElements();
		((DefaultComboBoxModel<Objective>) lstObjectives.getModel()).removeAllElements();
		((DefaultComboBoxModel<Constraint>) lstConstraints.getModel())
				.removeAllElements();

		cbbVariables.addActionListener(this);
	}

	/**
	 * Resets the RadioButton objects utilised by ProblemDesigner.
	 */
	private void resetRadioButtons()
	{
		btgVariableTypes.getElements().nextElement().setSelected(true);
		btgMaxMin.getElements().nextElement().setSelected(true);
		btgLessGreater.getElements().nextElement().setSelected(true);
		swapPanels(BINARY);
	}

	/**
	 * Resets the TextField objects utilised by ProblemDesigner.
	 */
	private void resetTextFields()
	{
		txtIntLower.setValue(0);
		txtIntUpper.setValue(0);
		txtRealLower.setValue(0);
		txtRealUpper.setValue(0);
		txtObjectiveBuilder.setText("");
		txtLhs.setText("");
		txtRhs.setText("");
	}

	/**
	 * Saves the currently selected problem, returning true if successful, false
	 * otherwise.
	 *
	 * @return True if successful, false otherwise.
	 */
	private boolean save()
	{
		final ProblemTemplate problem = createProblem();

		if (problem != null)
		{
			Map<String, ProblemTemplate> problems;
			problems = Operations.loadProblems();

			final String name = problem.getName();

			if (!problems.containsKey(name))
			{
				return writeToFile(problem, problems);
			}
			else
			{
				final int response = JOptionPane.showConfirmDialog(this, "Problem \""
						+ name + "\" already exists, would you like to overwrite it?",
						"Action Required", JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION)
				{
					return writeToFile(problem, problems);
				}
			}
		}

		return false;
	}

	/**
	 * TODO
	 */
	private void setWidgets()
	{
		pnlTabs = new JTabbedPane();
		pnlTabs.addChangeListener(this);
		pnlTabs.add(getVariablesPanel(), VARIABLES);
		pnlOperators = getOperatorsPanel();
		pnlTabs.add(getObjectivesPanel(), OBJECTIVES);
		pnlTabs.add(getConstraintsPanel(), CONSTRAINTS);
		add(pnlTabs, BorderLayout.CENTER);
		add(getPageEndPanel(), BorderLayout.PAGE_END);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		final String title = pnlTabs.getTitleAt(pnlTabs.getSelectedIndex());

		if (!title.equals(VARIABLES))
		{
			final JPanel current = (JPanel) pnlTabs.getSelectedComponent();
			current.add(pnlOperators, BorderLayout.LINE_START);

			if (title.equals(OBJECTIVES))
			{
				txtCurrent = txtObjectiveBuilder;
			}
			else
			// if (title.equals(CONSTRAINTS))
			{
				txtCurrent = txtLhs;
			}
		}
	}

	/**
	 * Swaps the panels to the specified key.
	 *
	 * @param key
	 *            : Key to the next panel to be displayed.
	 */
	private void swapPanels(String key)
	{
		final CardLayout layout = (CardLayout) pnlVariableBounds.getLayout();
		layout.show(pnlVariableBounds, key);
	}

	/**
	 * Appends the specified problem to the map of problems and writes it to
	 * disk.
	 *
	 * @param problem
	 *            : Problem to be appended to the Map.
	 * @param problems
	 *            : Map of problems to be written to disk.
	 * @return True if problems were successfully saved, false otherwise.
	 */
	private boolean writeToFile(ProblemTemplate problem,
			Map<String, ProblemTemplate> problems)
	{
		problems.put(problem.getName(), problem);

		if (Operations.writeProblems(problems))
		{
			JOptionPane.showMessageDialog(this, "Problem successfully Saved.");
			return true;
		}

		return false;
	}
}