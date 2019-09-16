/**
 * 
 */
package parser;

/**
 * @author Artur
 *
 */
public class Color {
	private int[] parameters;
	public Color(int[] p)
	{
		parameters = new int[p.length];
		for (int i = 0; i < p.length; i++)
		{
			parameters[i] = p[i];
		}
	}
	public int[] getParameters()
	{
		return parameters.clone();
	}
	public String toString()
	{
		String result = "(COLOR: ";
		
		for (int i = 0; i < parameters.length; i++)
		{
			result += parameters[i];
			if (i != parameters.length - 1)
				result += ", ";
		}
		
		result += ")";
		return result;
	}
}
