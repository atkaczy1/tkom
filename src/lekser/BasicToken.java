/**
 * 
 */
package lekser;

/**
 * @author Artur
 * Token nie posiadajacy dodatkowych informacji, przechowuje tylko rodzaj tokenu
 */
public class BasicToken implements Token {
	private int type;
	public int getType()
	{
		return type;
	}
	public BasicToken(int type)
	{
		this.type = type;
	}
}
