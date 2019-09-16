/**
 * 
 */
package parser;

import java.util.ArrayList;

/**
 * @author Artur
 * Klasa reprezentujaca dalszy punkt animacji
 */
public class RestTimePoint implements TimePoint{
	
	// Czas umiejscowienia punktu
	private int time;
	
	// Wykonane transformacje
	private ArrayList<Transform> list;
	
	public RestTimePoint(int t, ArrayList<Transform> l)
	{
		time = t;
		list = l;
	}
	public ArrayList<Transform> getTransformList()
	{
		return list;
	}
	public int getTime()
	{
		return time;
	}
	public String toString()
	{
		String result = "(";
		result += time;
		result += " : ";
		for (int i = 0; i < list.size(); i++)
		{
			result += list.get(i);
			if (i != list.size() - 1)
				result += ", ";
		}
		result += ")";
		return result;
	}
}
