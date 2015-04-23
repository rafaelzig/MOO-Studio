package uk.co.blogspot.rafaelzig.core.parsing;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.RealVariable;

import uk.co.blogspot.rafaelzig.core.datastructure.SavedSolutionSet;
import uk.co.blogspot.rafaelzig.core.datastructure.template.ProblemTemplate;
import uk.co.blogspot.rafaelzig.core.datastructure.template.VariableTemplate;

import com.fathzer.soft.javaluator.AbstractVariableSet;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Helper class containing various static methods which assist other classes,
 * these range from reading and writing to a file, parsing a JSON string into a
 * JSONObject.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 */
public class Operations
{
	/**
	 * Name of file from which problems are saved.
	 */
	private static final String				PROBLEM_FILENAME	= "MOPS.prb";

	/**
	 * String representing a valid evaluated expression.
	 */
	public static final String				VALID				= "Valid Expression";

	/**
	 * DoubleEvaluator object utilised to evaluate mathematical expressions from
	 * String objects.
	 */
	private static final DoubleEvaluator	evaluator			= new ExtendedDoubleEvaluator();

	/**
	 * GsonBuilder object utilised to serialise and deserialise objects from and
	 * to JSON files.
	 */
	private static final Gson				gson				= new GsonBuilder()
																		.registerTypeAdapter(
																				VariableTemplate.class,
																				new InterfaceAdapter<VariableTemplate>(
																						VariableTemplate.class
																								.getPackage()
																								.getName()))
																		.registerTypeAdapter(
																				Variable.class,
																				new InterfaceAdapter<Variable>(
																						RealVariable.class
																								.getPackage()
																								.getName()))
																		.serializeSpecialFloatingPointValues()
																		.create();

	/**
	 * Checks the specified mathematical expression utilising the evaluation
	 * context, returning the error message.
	 *
	 * @param expression
	 *            : String object representing a mathematical expression.
	 * @param evaluationContext
	 *            : Evaluation context to be utilised
	 * @return : True if valid, false otherwise.
	 */
	public static String checkExpression(String expression,
			AbstractVariableSet<Double> evaluationContext)
	{
		try
		{
			evaluator.evaluate(expression, evaluationContext);
		}
		catch (final IllegalArgumentException e)
		{
			String errorMessage = e.getMessage();

			if (errorMessage == null)
			{
				errorMessage = "Invalid Expression";
			}

			return "Error: " + errorMessage;
		}

		return VALID;
	}

	/**
	 * Evaluates the specified mathematical expression utilising the evaluation
	 * context.
	 *
	 * @param expression
	 *            : String object representing a mathematical expression.
	 * @param evaluationContext
	 *            : Evaluation context to be utilised
	 * @return : Result of evaluation.
	 */
	public synchronized static double evaluate(String expression,
			AbstractVariableSet<Double> evaluationContext)
	{
		double result;

		try
		{
			result = evaluator.evaluate(expression, evaluationContext);
		}
		catch (final IllegalArgumentException e)
		{
			result = Double.NaN;
		}

		return result;
	}

	/**
	 * Informs the user regarding the specified error.
	 * 
	 * @param container
	 *
	 * @param e
	 *            : error to be printed.
	 */
	public static void handleError(Container owner, Throwable e)
	{
		final StringBuilder builder = new StringBuilder();

		if (e instanceof OutOfMemoryError)
		{
			builder.append("Unable to evaluate the problem, too many calculations were required, consider increasing the Java heap space.");
		}
		else if (e instanceof CancellationException || e instanceof InterruptedException
				|| e instanceof ExecutionException)
		{
			builder.append("Operation Aborted.");
		}
		else if (e instanceof ClassNotFoundException
				|| e instanceof InstantiationException
				|| e instanceof IllegalAccessException
				|| e instanceof UnsupportedLookAndFeelException)
		{
			builder.append("An error has ocurred whilst setting the look and feel of this application.");
		}
		else if (e instanceof IOException)
		{
			builder.append("There has been a problem while attempting to read/write from disk.");
		}

		if (e.getMessage() == null)
		{
			builder.append("\nInformation regarding the error is unavailable.");
		}
		else
		{
			builder.append("\n" + e.getMessage());
		}

		JOptionPane.showMessageDialog(owner, builder.toString(), "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Loads and returns the SavedSolutionSet object from the specified file.
	 *
	 * @return SavedSolutionSet object loaded from the specified file.
	 */
	public static SavedSolutionSet load(File file)
	{
		SavedSolutionSet solutionSet = null;

		try (BufferedReader reader = Files.newBufferedReader(file.toPath()))
		{
			solutionSet = gson.fromJson(reader, SavedSolutionSet.class);
		}
		catch (IOException e)
		{
			handleError(null, e);
		}

		return solutionSet;
	}

	/**
	 * Loads and returns the CustomProblem objects from the JSON file on disk.
	 *
	 * @return Map of CustomProblem objects identified by their names.
	 */
	public static Map<String, ProblemTemplate> loadProblems()
	{
		Map<String, ProblemTemplate> problems = null;

		final File file = new File(PROBLEM_FILENAME);

		if (file.length() == 0L)
		{
			problems = new HashMap<>();
		}
		else
		{
			try (BufferedReader reader = Files.newBufferedReader(file.toPath()))
			{
				final Type type = new TypeToken<Map<String, ProblemTemplate>>()
				{
				}.getType();
				problems = gson.fromJson(reader, type);
			}
			catch (IOException e)
			{
				handleError(null, e);
			}
		}

		return problems;
	}

	/**
	 * Writes the SavedSolutionSet object to the specified file.
	 *
	 * @param file
	 *            : File object to be written.
	 * @param solutionSet
	 *            : SavedSolutionSet object to be written to file.
	 * @return True if successfully written, false otherwise.
	 */
	public static boolean write(File file, SavedSolutionSet solutionSet)
	{
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath()))
		{
			gson.toJson(solutionSet, writer);
			return true;
		}
		catch (IOException e)
		{
			handleError(null, e);
			return false;
		}
	}

	/**
	 * Writes the specified content to the specified file.
	 *
	 * @param file
	 *            : File object to be written.
	 * @param content
	 *            : Content to be written to file.
	 * @return True if successfully written, false otherwise.
	 */
	public static boolean write(File file, String content)
	{
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath()))
		{
			writer.write(content);
			return true;
		}
		catch (IOException e)
		{
			handleError(null, e);
			return false;
		}
	}

	/**
	 * Writes the specified image to the specified file.
	 *
	 * @param file
	 *            : File object to be written.
	 * @param image
	 *            : Image to be written to file.
	 * @throws IOException
	 *             : Signals that an I/O exception of some sort has occurred.
	 *             This class is the general class of exceptions produced by
	 *             failed or interrupted I/O operations.
	 * @return True if sucessfully written, false otherwise.
	 */
	public static boolean write(File file, WritableImage image)
	{
		try
		{
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			return true;
		}
		catch (IOException e)
		{
			handleError(null, e);
			return false;
		}
	}

	/**
	 * Writes the specified list of CustomProblem objects to the JSON file on
	 * disk.
	 *
	 * @param problemList
	 *            : List of CustomProblem objects to be written to the JSON
	 *            file.
	 */
	public static void writeProblems(List<ProblemTemplate> problemList)
	{
		final Map<String, ProblemTemplate> problemMap = new HashMap<>(problemList.size());

		for (final ProblemTemplate problem : problemList)
		{
			problemMap.put(problem.getName(), problem);
		}

		writeProblems(problemMap);
	}

	/**
	 * Writes the specified Map of CustomProblem objects to the JSON file on
	 * disk.
	 *
	 * @param problems
	 *            : Map of CustomProblem objects to be written to the JSON file.
	 * @return True if successfully written, false otherwise.
	 */
	public static boolean writeProblems(Map<String, ProblemTemplate> problems)
	{
		final Path path = new File(PROBLEM_FILENAME).toPath();

		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			gson.toJson(problems, writer);
			return true;
		}
		catch (IOException e)
		{
			handleError(null, e);
			return false;
		}
	}
}