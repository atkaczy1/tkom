/**
 * 
 */
package parser;
import java.util.ArrayList;
/**
 * @author Artur
 * Klasa reprezentujaca komende object $var {...};
 */
public class ObjectCommand extends Command{
	
	// Zmienna do ktorej przypiszemy utworzony obiekt zlozony
	private Variable var;
	
	// Elementy skladowe obiektu zlozonego
	private ArrayList<ObjectElement> list;
	
	public ObjectCommand(int t, Variable v, ArrayList<ObjectElement> l)
	{
		super(t);
		var = v;
		list = l;
	}
	public String toString()
	{
		String result = "[";
		result += super.toString();
		result += ": VAR: ";
		result += var;
		for (int i = 0; i < list.size(); i++)
		{
			result += list.get(i);
			if (i != list.size() - 1)
				result += ", ";
		}
		result += "]";
		return result;
	}
	public Variable getVariable()
	{
		return var;
	}
	public ArrayList<ObjectElement> getObjectElementList()
	{
		return list;
	}
}
