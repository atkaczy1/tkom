/**
 * 
 */
package parser;

/**
 * @author Artur
 * Transformacja geometryczna
 */
public class Transform {
	
	// Typy transformacji
	public static final int TRANSFORM = 0;
	public static final int MOVE = 1;
	public static final int ROTATE = 2;
	public static final int SCALE = 3;
	public static final int SHEARX = 4;
	public static final int SHEARY = 5;
	
	// Typ transformacji
	private int type;
	
	// Parametry transformacji
	private int[] parameters;
	
	public Transform(int t, int[] p)
	{
		type = t;
		parameters = new int[p.length];
		for (int i = 0; i < p.length; i++)
		{
			parameters[i] = p[i];
		}
	}
	public int getType()
	{
		return type;
	}
	public int[] getParameters()
	{
		return parameters.clone();
	}
	public String toString()
	{
		String result = "(";
		
		if (type == TRANSFORM)
			result += "TRANSFORM: ";
		else if (type == MOVE)
			result += "MOVE: ";
		else if (type == ROTATE)
			result += "ROTATE: ";
		else if (type == SCALE)
			result += "SCALE: ";
		else if (type == SHEARX)
			result += "SHEARX: ";
		else if (type == SHEARY)
			result += "SHEARY: ";
		
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
