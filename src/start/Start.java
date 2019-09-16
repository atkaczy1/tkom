/**
 * 
 */
package start;
import parser.*;
import interpreter.*;
import view.*;
import java.io.*;
import java.util.*;
/**
 * @author Artur
 * Klasa startowa, jej celem jest pobranie œcie¿ki do pliku i powolanie instancji
 * odpowiednich klas: Interpreter, View
 */
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path;
		if (args.length == 0)
		{
			System.out.println("file: ");
			Scanner s = new Scanner(System.in);
			path = s.nextLine();
		}
		else
		{
			path = args[0];
		}
		View view = new View();
		Interpreter interpreter = new Interpreter(view);
		interpreter.start(path);
	}

}
