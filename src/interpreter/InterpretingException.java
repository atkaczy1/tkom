/**
 * 
 */
package interpreter;

/**
 * @author Artur
 * Blad podczas interpretowania zparsowanego tekstu
 */
public class InterpretingException extends Throwable {
	public static final long serialVersionUID = 1;
	private String info;
	public String getInfo()
	{
		return info;
	}
	public InterpretingException(String i)
	{
		info = i;
	}
}
