/**
 * 
 */
package parser;


/**
 * @author Artur
 * Klasa reprezentujaca komende: show $var;
 * 
 */
public class ShowCommand extends Command {
	
	// Zmienna dla ktorej wywolywane jest show
	private Variable var;
	
	public ShowCommand(int t, Variable v)
	{
		super(t);
		var = v;
	}
	public Variable getVariable()
	{
		return var;
	}
	public String toString()
	{
		String result = "[";
		result += super.toString();
		result += ": VAR: ";
		result += var;
		result += "]";
		return result;
	}
}
