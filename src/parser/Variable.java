/**
 * 
 */
package parser;


/**
 * @author Artur
 * Zmienna
 */
public class Variable implements ObjectElement{
	
	// Nazwa zmiennej
	private String name;
	
	public Variable(String n)
	{
		name = n;
	}
	public String getName()
	{
		return name;
	}
	public String toString()
	{
		String result = "[";
		result += "$" + name;
		result += "]";
		return result;
	}
}
