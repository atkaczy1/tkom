/**
 * 
 */
package parser;


/**
 * @author Artur
 * Klasa reprezentujaca pierwszy (startowy) punkt czasowy w animacji
 */
public class FirstTimePoint implements TimePoint{
	
	// Moment wystapienia w czasie
	private int time;
	
	// Obiekt graficzny dla ktorego dla ktorego wywolana bedzie animacja
	private ObjectElement elem;
	
	public FirstTimePoint(int t, ObjectElement e)
	{
		time = t;
		elem = e;
	}
	public ObjectElement getObjectElement()
	{
		return elem;
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
		result += elem;
		result += ")";
		return result;
	}
}
