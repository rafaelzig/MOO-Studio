package uk.co.blogspot.rafaelzig.core.datastructure.template;

/**
 * Class representing a binary variable template.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 */
public class BinaryTemplate extends VariableTemplate
{
	/**
	 * Constructs a new instance of BinaryTemplate.
	 */
	public BinaryTemplate()
	{
		super(Type.BINARY, 0, 1);
	}

	@Override
	public String toString()
	{
		return "Binary Variable";
	}
}