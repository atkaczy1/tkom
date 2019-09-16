/**
 * 
 */
package parser;

import java.util.ArrayList;


/**
 * @author Artur
 * Obiekt graficzny w postaci listy transformacji geometrycznych i figury prostej
 */
public class TranFigList implements ObjectElement{
	
	// Lista transforacji
	private ArrayList<Transform> list;
	
	// Figura prosta
	private Figure fig;
	
	public TranFigList(ArrayList<Transform> l, Figure f)
	{
		list = l;
		fig = f;
	}
	public ArrayList<Transform> getTransformList()
	{
		return list;
	}
	public Figure getFigure()
	{
		return fig;
	}
	public String toString()
	{
		String result = "{";
		for (int i = 0; i < list.size(); i++)
		{
			result += list.get(i);
			result += " * ";
		}
		result += fig.toString();
		result += "}";
		return result;
	}
}
