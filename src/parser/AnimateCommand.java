/**
 * 
 */
package parser;

/**
 * @author Artur
 * Klasa reprezentujaca komende: animate $var;
 * 
 */
public class AnimateCommand extends Command {
	private Variable var;
	public AnimateCommand(int t, Variable v)
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
		result += ": ";
		result += var;
		result += "]";
		return result;
	}
}
