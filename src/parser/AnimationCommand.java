/**
 * 
 */
package parser;
import java.util.ArrayList;
/**
 * @author Artur
 * Klasa reprezentujaca komende animation $var {...};
 */
public class AnimationCommand extends Command {
	
	// Zmienna do ktorej przypisujemy utworzona animacje
	private Variable var;
	
	// Czy animacja zapetlona
	private boolean loop;
	
	// Pierwszy punktowy(startowy) czasowy animacji
	private FirstTimePoint first;
	
	// Pozostale punkty czasowe animacji
	private ArrayList<RestTimePoint> list;
	
	public AnimationCommand(int t, Variable v, boolean aloop, ArrayList<RestTimePoint> l, FirstTimePoint f)
	{
		super(t);
		var = v;
		loop = aloop;
		list = l;
		first = f;
	}
	public ArrayList<RestTimePoint> getRestTimePointList()
	{
		return list;
	}
	public Variable getVariable()
	{
		return var;
	}
	public FirstTimePoint getFirstTimePoint()
	{
		return first;
	}
	public boolean getLoop()
	{
		return loop;
	}
	public String toString()
	{
		String result = "[";
		result += super.toString();
		result += ": ";
		result += "VAR: " + var + ", " + "LOOP: " + loop + ", ";
		result += first + ", ";
		for (int i = 0; i < list.size(); i++)
		{
			result += list.get(i);
			if (i != list.size() - 1)
				result += ", ";
		}
		result += "]";
		return result;
	}
}
