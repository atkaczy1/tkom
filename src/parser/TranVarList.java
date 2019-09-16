/**
 * 
 */
package parser;

import java.util.ArrayList;

/**
 * @author Artur
 * Obiekt graficzny w postaci listy transformacji geometrycznych i zmiennej
 */
public class TranVarList implements ObjectElement{
	
	// Lista transforacji
	private ArrayList<Transform> list;
	
	// Zmienna
	private Variable var;
	
	public TranVarList(ArrayList<Transform> l, Variable v)
	{
		list = l;
		var = v;
	}
	public ArrayList<Transform> getTransformList()
	{
		return list;
	}
	public Variable getVariable()
	{
		return var;
	}
	public String toString()
	{
		String result = "{";
		for (int i = 0; i < list.size(); i++)
		{
			result += list.get(i);
			result += " * ";
		}
		result += var.toString();
		result += "}";
		return result;
	}
}
