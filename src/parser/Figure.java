/**
 * 
 */
package parser;

/**
 * @author Artur
 * Klasa reprezentujaca figure prosta w programie
 * Dostepne figury to: prostokat, kolo, wielokat, trojkat
 */
public class Figure implements ObjectElement{
	
	// Typy figur prostych
	public static final int RECTANGLE = 0;
	public static final int CIRCLE = 1;
	public static final int POLYGON = 2;
	public static final int TRIANGLE = 3;
	
	// Typ figury
	private int type;
	
	// Parametry figury
	// Ilosc parametrow zalezy od typu figury
	private int[] parameters;
	
	// Kolor figury
	private Color2 color;
	
	public Figure(int t, int[] p, Color2 c)
	{
		type = t;
		parameters = new int[p.length];
		for (int i = 0; i < p.length; i++)
		{
			parameters[i] = p[i];
		}
		color = c;
	}
	public int getType()
	{
		return type;
	}
	public Color2 getColor()
	{
		return color;
	}
	public int[] getParameters()
	{
		return parameters.clone();
	}
	public String toString()
	{
		String result = "(";
		
		if (type == CIRCLE)
			result += "CIRCLE: ";
		else if (type == TRIANGLE)
			result += "TRIANGLE: ";
		else if (type == POLYGON)
			result += "POLYGON: ";
		else if (type == RECTANGLE)
			result += "RECTANGLE: ";
		
		for (int i = 0; i < parameters.length; i++)
		{
			result += parameters[i];
			result += ", ";
		}
		result += color;
		result += ")";
		return result;
	}
}
