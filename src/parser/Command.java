/**
 * 
 */
package parser;

/**
 * @author Artur
 * Klasa ktora jest dziedziczona przez wszystkie mozliwe komendy w programie
 * 
 */
public class Command {
	
	// Wszystkie rodzaje polecen jezyka
	public final static int SIMPLE_COMMAND = 0;
	public final static int OBJECT_COMMAND = 1;
	public final static int ANIMATION_COMMAND = 2;
	public final static int SHOW_COMMAND = 3;
	public final static int ANIMATE_COMMAND = 4;
	// public final static int ERROR_COMMAND = 5;
	// public final static int EOF_COMMAND = 6;
	
	// Rodzaj komendy
	private int type;
	
	public Command(int t)
	{
		type = t;
	}
	public int getType()
	{
		return type;
	}
	public String toString()
	{
		String result = "[";
		if (type == SIMPLE_COMMAND)
			result += "SIMPLE_COMMAND";
		else if (type == OBJECT_COMMAND)
			result += "OBJECT_COMMAND";
		else if (type == ANIMATION_COMMAND)
			result += "ANIMATION_COMMAND";
		else if (type == SHOW_COMMAND)
			result += "SHOW_COMMAND";
		else if (type == ANIMATE_COMMAND)
			result += "ANIMATE_COMMAND";
		result += "]";
		return result;
	}
}
