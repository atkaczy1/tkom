/**
 * 
 */
package parser;


/**
 * @author Artur
 * Klasa reprezentujaca komende: $var = ...;
 */
public class SimpleCommand extends Command{
	
	// Zmienna do ktorej przypisujemy
	private Variable var;
	
	// Prawa strona przypisania
	private ObjectElement elem;
	
	public SimpleCommand(int t, Variable v, ObjectElement e)
	{
		super(t);
		var = v;
		elem = e;
	}
	public Variable getVariable()
	{
		return var;
	}
	public ObjectElement getObjectElement()
	{
		return elem;
	}
	public String toString()
	{
		String result = "[";
		result += super.toString();
		result += ": VAR: ";
		result += var;
		result += elem;
		result += "]";
		return result;
	}
}
