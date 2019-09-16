/**
 * 
 */
package lekser;

/**
 * @author Artur
 * Token przechowujacy lancuch znakowy
 * Wykorzystywany przez token zmiennej
 */
public class StringToken implements Token {
	private int type;
	private String value;
	public int getType()
	{
		return type;
	}
	public String getValue()
	{
		return value;
	}
	public StringToken(int type, String value)
	{
		this.type = type;
		this.value = value;
	}
}
