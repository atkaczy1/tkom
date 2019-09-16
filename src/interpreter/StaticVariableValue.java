/**
 * 
 */
package interpreter;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * @author Artur
 * Klasa reprezentujaca obiekt statyczny w programie
 */
public class StaticVariableValue implements VariableValue{
	
	// Figury proste wchodzace w sklad tego obiektu
	private ArrayList<Shape> shapes;
	
	// Laczne transformacje dla kazdej z figur
	private ArrayList<AffineTransform> transforms;
	
	// Kolory dla kazdej z figur
	private ArrayList<Color> colors;
	
	public StaticVariableValue(ArrayList<Shape> s, ArrayList<AffineTransform> t, ArrayList<Color> c)
	{
		shapes = s;
		transforms = t;
		colors = c;
	}
	public ArrayList<Shape> getShapes()
	{
		return shapes;
	}
	public ArrayList<AffineTransform> getTransforms()
	{
		return transforms;
	}
	public ArrayList<Color> getColors()
	{
		return colors;
	}
}
