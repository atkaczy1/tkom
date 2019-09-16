/**
 * 
 */
package interpreter;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.Timer;

import parser.*;
import view.View;

/**
 * @author Artur
 * Interpreter
 */
public class Interpreter {
	
	// Widok w ktorym wyswietla wynik
	private View view;
	
	// Tablica aktualnych wartosci zmiennych
	Hashtable<String, VariableValue> variablesTable;
	
	// Timer do animacji
	private Timer animTimer;
	
	// Wlaczona animacja
	private Animation activeAnimation;
	
	public Interpreter(View v)
	{
		view = v;
		variablesTable = new Hashtable<>();
	}
	public void start(String path)
	{
		// Listy w ktorym umieszcze dane do przekazania widokowi
		ArrayList<Shape> shapesToDraw = new ArrayList<>();
		ArrayList<AffineTransform> transformsToUse = new ArrayList<>();
		ArrayList<Color> colorsToUse = new ArrayList<>();
		
		try
		{
			Parser p = new Parser(path);
			Command com;
			
			// Dopoki nie osiagnie konca pliku
			while((com = p.getCommand()) != null)
			{
				// Wybiera typ pobranej komendy i ja wykonuje
				if (com instanceof AnimateCommand)
				{
					// Pobiera animacje z tablicy zmiennych
					AnimateCommand animCom = (AnimateCommand) com;
					String variableName = animCom.getVariable().getName();
					VariableValue varval = variablesTable.get(variableName);
					if (varval == null)
					{
						throw new InterpretingException("Nie znaleziono zmiennej " + variableName);
					}
					if (!(varval instanceof Animation))
					{
						throw new InterpretingException("Zmienna " + variableName + " nie wskazuje na animacje");
					}
					Animation animation = (Animation) varval;
					
					// Ustawia animacje
					activeAnimation = animation;
					animTimer = new Timer(1000 / Animation.FPS, null);
					animTimer.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent e)
					    {
					        StaticVariableValue svv = activeAnimation.time();
							view.show(svv.getShapes(), svv.getTransforms(), svv.getColors());
					    }
					});
					
					// Wlacza animacje
					animTimer.start();
				}
				else if (com instanceof AnimationCommand)
				{
					// Pobiera z klasy polecenia dane
					AnimationCommand animcom = (AnimationCommand) com;
					String animationName = animcom.getVariable().getName();
					FirstTimePoint ftp = animcom.getFirstTimePoint();
					ArrayList<RestTimePoint> rtp = animcom.getRestTimePointList();
					
					// Ustawia tablice momentow czasowych animacji
					int[] timepoints = new int[rtp.size() + 1];
					timepoints[0] = ftp.getTime();
					for (int i = 0; i < rtp.size(); i++)
					{
						timepoints[i + 1] = rtp.get(i).getTime();
					}
					
					// Tworzy statyczny obiekt startowy programu
					StaticVariableValue staticvarval = getObjectElement(ftp.getObjectElement());
					
					// Tworzy tablice wypadkowych transformacji geometrycznych dla kazdego punktu czasowego
					AffineTransform[] nexttrans = new AffineTransform[rtp.size()];
					
					// Uzupelnia ta tablice danymi, wyliczajac dla kazdego punktu animacji macierz wypadkowa
					for (int i = 0; i < rtp.size(); i++)
					{
						nexttrans[i] = getTransformList(rtp.get(i).getTransformList());
					}
					
					// Tworzy animacje i wstawia ja do tablicy zmiennych
					Animation animation = new Animation(animcom.getLoop(), timepoints, staticvarval, nexttrans);
					variablesTable.put(animationName, animation);
				}
				else if (com instanceof SimpleCommand)
				{
					// Pobiera z polecenia nazwe zmiennej, a z prawej strony przypisania tworzy nowy obiekt statyczny
					// Na koniec uzyskane dane wstawiane sa do tablicy zmiennych
					
					SimpleCommand simplecom = (SimpleCommand) com;
					String varName = simplecom.getVariable().getName();
					
					StaticVariableValue staticvarval = getObjectElement(simplecom.getObjectElement());
					
					variablesTable.put(varName, staticvarval);
					
				}
				else if (com instanceof ObjectCommand)
				{
					ObjectCommand objectcom = (ObjectCommand) com;
					String objectName = objectcom.getVariable().getName();
					
					// Laczna lista figur nalezacych do tego obiektu zlozonego
					ArrayList<Shape> s = new ArrayList<>();
					ArrayList<AffineTransform> t = new ArrayList<>();
					ArrayList<Color> c = new ArrayList<>();
					
					// Dla kazdego elementu skladowego obiektu zlozonego, pobierana jest lista figur z ktorych sie sklada
					// a nastepnie dodawana jest ona do lacznej listy
					ArrayList<ObjectElement> array = objectcom.getObjectElementList();
					for (int i = 0; i < array.size(); i++)
					{
						StaticVariableValue svv = getObjectElement(array.get(i));
						s.addAll(svv.getShapes());
						t.addAll(svv.getTransforms());
						c.addAll(svv.getColors());
					}
					
					// Tworzy obiekt statyczny umieszcza go do tablicy zmiennych
					StaticVariableValue staticvarval = new StaticVariableValue(s, t, c);
					variablesTable.put(objectName, staticvarval);
				}
				else
				{
					// Interpretuje komende to wyswietlenia obiektu
					ShowCommand showCom = (ShowCommand) com;
					String variableName = showCom.getVariable().getName();
					
					// Pobiera zmienna z tablicy zmiennych
					VariableValue varval = variablesTable.get(variableName);
					if (varval == null)
					{
						throw new InterpretingException("Nie znaleziono zmiennej " + variableName);
					}
					if (!(varval instanceof StaticVariableValue))
					{
						throw new InterpretingException("Zmienna " + variableName + " wskazuje na animacje");
					}
					StaticVariableValue staticvarval = (StaticVariableValue) varval;
					
					// Wysyla dane do wyswietlenia
					shapesToDraw = staticvarval.getShapes();
					transformsToUse = staticvarval.getTransforms();
					colorsToUse = staticvarval.getColors();
					view.show(shapesToDraw, transformsToUse, colorsToUse);
				}
			}
			System.out.println("End of file");
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File not found");
			e.printStackTrace();
			System.exit(1);
		}
		catch(IOException e)
		{
			System.err.println("IO exception");
			e.printStackTrace();
			System.exit(1);
		}
		catch(ParsingException e)
		{
			System.err.println("Parsing exception");
			e.printStackTrace();
			System.exit(1);
		}
		catch(InterpretingException e)
		{
			System.err.println("Interpreting exception");
			System.err.println(e.getInfo());
			e.printStackTrace();
			System.exit(1);
		}
		catch(Throwable e)
		{
			System.err.println("Unknown exception");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Tworzy obiekt klasy Shape z obiektu klasy Figure
	 * @param figure
	 * @return
	 */
	private Shape getShape(Figure figure)
	{
		Shape result;
		int[] params = figure.getParameters();
		if (figure.getType() == Figure.CIRCLE)
		{
			result = new Ellipse2D.Double(params[0], params[1], 2 * params[2], 2 * params[2]);
		}
		else if (figure.getType() == Figure.RECTANGLE)
		{
			int[] xpoints = new int[]{params[0], params[0] + params[2], params[0] + params[2], params[0]};
			int[] ypoints = new int[]{params[1], params[1], params[1] + params[3], params[1] + params[3]};
			result = new Polygon(xpoints, ypoints, 4);
		}
		else
		{
			// Dla wielokatu i trojkata
			int[] xpoints = new int[params.length / 2];
			int[] ypoints = new int[params.length / 2];
			for (int i = 0; i < xpoints.length; i++)
			{
				xpoints[i] = params[i * 2];
			}
			for (int i = 0; i < ypoints.length; i++)
			{
				ypoints[i] = params[i * 2 + 1];
			}
			result = new Polygon(xpoints, ypoints, xpoints.length);
		}
		return result;
	}
	
	/**
	 * Tworzy obiekt klasy AffineTransform z obiektu klasy Transform
	 * @param t
	 * @return
	 */
	private AffineTransform getTransform(Transform t)
	{
		AffineTransform result;
		int[] params = t.getParameters();
		if (t.getType() == Transform.MOVE)
		{
			result = AffineTransform.getTranslateInstance(params[0], params[1]);
		}
		else if (t.getType() == Transform.ROTATE)
		{
			result = AffineTransform.getRotateInstance(Math.toRadians(params[0]));
		}
		else if (t.getType() == Transform.SCALE)
		{
			result = AffineTransform.getScaleInstance(params[0], params[1]);
		}
		else if (t.getType() == Transform.SHEARX)
		{
			result = AffineTransform.getShearInstance(params[0], 0);
		}
		else if (t.getType() == Transform.SHEARY)
		{
			result = AffineTransform.getShearInstance(0, params[0]);
		}
		else
		{
			double[] dparams = new double[params.length];
			for (int i = 0; i < params.length; i++)
			{
				dparams[i] = (double) params[i];
			}
			result = new AffineTransform(dparams);
		}
		return result;
	}
	
	/**
	 * Tworzy obiekt klasy AffineTransform ktory jest wypadkowa transformacja
	 * stworzona z listy obiektow klasy Transform
	 * @param tl
	 * @return
	 */
	private AffineTransform getTransformList(ArrayList<Transform> tl)
	{
		AffineTransform second = new AffineTransform();
		AffineTransform first;
		for (int i = tl.size() - 1; i >= 0; i--)
		{
			first = getTransform(tl.get(i));
			first.concatenate(second);
			second = first;
		}
		return second;
	}
	
	/**
	 * Tworzy obiekt statyczny na podstawie otrzymanego obiektu ObjectElement
	 * @param oe
	 * @return
	 * @throws InterpretingException
	 */
	private StaticVariableValue getObjectElement(ObjectElement oe) throws InterpretingException
	{
		// Lista figur skladowych nowego obiektu
		ArrayList<Shape> s = new ArrayList<>();
		ArrayList<AffineTransform> t = new ArrayList<>();
		ArrayList<Color> c = new ArrayList<>();
		
		// W zaleznosci od typu obiektu pokazywanego przez ObjectElement
		// wywolywana jest jedna z 4 opcji
		if (oe instanceof Figure)
		{
			Figure figure = (Figure) oe;
			s.add(getShape(figure));
			t.add(new AffineTransform());
			int[] params = figure.getColor().getParameters();
			c.add(new Color(params[0], params[1], params[2]));
		}
		else if (oe instanceof TranFigList)
		{
			TranFigList tfl = (TranFigList) oe;
			s.add(getShape(tfl.getFigure()));
			t.add(getTransformList(tfl.getTransformList()));
			int[] params = tfl.getFigure().getColor().getParameters();
			c.add(new Color(params[0], params[1], params[2]));
		}
		else if (oe instanceof TranVarList)
		{
			TranVarList tvl = (TranVarList) oe;
			String varName = tvl.getVariable().getName();
			
			// Pobiera wartosc zmiennej z tablicy
			VariableValue varval = variablesTable.get(varName);
			if (!(varval instanceof StaticVariableValue))
			{
				throw new InterpretingException("Zmienna " + varName + " wskazuje na animacje");
			}
			StaticVariableValue staticvarval = (StaticVariableValue) varval;
			
			// Dodaje wszystkie transformacje ze zmiennej do wynikowej listy
			for (int i = 0; i < staticvarval.getTransforms().size(); i++)
			{
				t.add((AffineTransform) staticvarval.getTransforms().get(i).clone());
			}
			
			// Dodaje wartosci ze zmiennej do pozostalych list
			s = staticvarval.getShapes();
			c = staticvarval.getColors();
			
			// Mnozy liste transformacji pobrana ze zmiennej z transformacja
			// wypadkowa uzyta przy odwolaniu do zmiennej
			AffineTransform t2 = getTransformList(tvl.getTransformList());
			for (int j = 0; j < t.size(); j++)
			{
				AffineTransform temp = (AffineTransform) t2.clone();
				temp.concatenate(t.get(j));
				t.set(j, temp);
			}
		}
		else
		{
			// Gdy oe jest typu Variable
			Variable v = (Variable) oe;
			String varName = v.getName();
			
			// Pobiera wartosc zmiennej z tablicy zmiennych
			VariableValue varval = variablesTable.get(varName);
			if (!(varval instanceof StaticVariableValue))
			{
				throw new InterpretingException("Zmienna " + varName + " wskazuje na animacje");
			}
			StaticVariableValue staticvarval = (StaticVariableValue) varval;
			
			// Uzupelnia listy wartosciami ze zmiennej
			for (int i = 0; i < staticvarval.getTransforms().size(); i++)
			{
				t.add((AffineTransform) staticvarval.getTransforms().get(i).clone());
			}
			s = staticvarval.getShapes();
			c = staticvarval.getColors();
		}
		
		return new StaticVariableValue(s, t, c);
	}
}
