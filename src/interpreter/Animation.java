/**
 * 
 */
package interpreter;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;

import parser.*;

/**
 * @author Artur
 * Animacja
 */
public class Animation implements VariableValue {
	
	// Wartosci czasowe kolejnych punktow
	private int[] timepoints;
	
	// Calkowita dlugosc animacji
	private int animLength;
	
	// Ilosc klatek wyswietlona od punktu startowego
	private int timePassed;
	
	// Szybkosc animacji
	public final static int FPS = 20;
	
	// Czy zapetlona
	private boolean loop;
	
	// Animowany obiekt
	private StaticVariableValue animatedObject;
	
	// Laczne transformacje w kolejnych punktach animacji
	private AffineTransform[] transforms;
	
	public Animation(boolean l, int[] tp, StaticVariableValue ao, AffineTransform[] t)
	{
		timePassed = 0;
		loop = l;
		timepoints = tp;
		transforms = t;
		animLength = timepoints[timepoints.length - 1];
		animatedObject = ao;
	}
	public int[] getTimes()
	{
		return timepoints;
	}
	public int getTimePassed()
	{
		return timePassed;
	}
	public boolean getLoop()
	{
		return loop;
	}
	public StaticVariableValue getAnimatedObject()
	{
		return animatedObject;
	}
	public AffineTransform[] getTransforms()
	{
		return transforms;
	}
	
	/**
	 * Zwraca kolejna klatke animacji
	 * @return
	 */
	public StaticVariableValue time()
	{
		// Zbiera w liste wszystkie transformacje, ktore trzeba wykonac obiekcie
		
		// Dla kazdego punktu animacji sprawdza czy animacja juz osiagnela ta animacje
		ArrayList<AffineTransform> list = new ArrayList<>();
		for (int i = 1; i < timepoints.length; i++)
		{
			if (timePassed * 1000 / FPS >= 1000 * timepoints[i])
			{
				// Animacja minela dany punkt animacji, a wiec cala transformacja
				// geometryczna zostanie wykonana
				
				list.add(transforms[i - 1]);
			}
			else
			{
				// Nastepny punkt animacji, ktorego jeszcze nie osiagnieto
				// W zwiazku z tym zostanie wykonana tylko czesciowa transformacja
				
				// Wylicza macierz czesciowej transformacji i dodaje ja do listy
				double[] matrix = new double[6];
				double[] baseMatrix = new double[6];
				double[] differenceMatrix = new double[6];
				transforms[i - 1].getMatrix(matrix);
				new AffineTransform().getMatrix(baseMatrix);
				for (int j = 0; j < matrix.length; j++)
				{
					differenceMatrix[j] = matrix[j] - baseMatrix[j];
				}
				double x = timePassed * 1000 / FPS - timepoints[i - 1] * 1000;
				double y = timepoints[i] * 1000 - timepoints[i - 1] * 1000;
				double ratio = x / y;
				for (int j = 0; j < matrix.length; j++)
				{
					matrix[j] = baseMatrix[j] + differenceMatrix[j] * ratio;
				}
				list.add(new AffineTransform(matrix));
				break;
			}
		}
		
		// Mnozy przez siebie wszystkie wybrane transformacje,
		// laczac je w jedna wynikowa
		AffineTransform second = new AffineTransform();
		AffineTransform first;
		for (int i = list.size() - 1; i >= 0; i--)
		{
			first = (AffineTransform) list.get(i).clone();
			first.concatenate(second);
			second = first;
		}
		// Transformacja wynikowa jest teraz w second
		
		// Umieszcza kopie transformacji obiektu startowego w copy
		ArrayList<AffineTransform> t = animatedObject.getTransforms();
		ArrayList<AffineTransform> copy = new ArrayList<>();
		for (int i = 0; i < t.size(); i++)
		{
			copy.add((AffineTransform) t.get(i).clone());
		}
		
		// Mnozy transformacje obiekty startowego z uzyskana transformacja animacji
		for (int j = 0; j < copy.size(); j++)
		{
			AffineTransform temp = (AffineTransform) second.clone();
			temp.concatenate(copy.get(j));
			copy.set(j, temp);
		}
		
		// Wylicza nastepna wartosc timePassed
		if (timePassed * 1000 / FPS == animLength * 1000 && loop == true)
		{
			timePassed = 0;
		}
		else if (timePassed * 1000 / FPS < animLength * 1000)
		{
			timePassed++;
		}
		return new StaticVariableValue(animatedObject.getShapes(), copy, animatedObject.getColors());
	}
}
