/**
 * 
 */
package lekser;

/**
 * @author Artur
 * Token przechowujacy liczbe calkowita
 */
public class NumberToken implements Token {
	private int type;
	private int value;
	public int getType()
	{
		return type;
	}
	public int getValue()
	{
		return value;
	}
	public NumberToken(int type, int value)
	{
		this.type = type;
		this.value = value;
	}
}
