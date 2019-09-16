/**
 * 
 */
package parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.*;

import lekser.*;

/**
 * @author Artur
 * Klasa implementujaca parser dla jezyka scen graficznych
 */
public class Parser {
	
	// Wykozystywany lekser do pobierania tokenow
	private Lekser lekser;
	
	// Ostatnio pobrany token z leksera
	private Token symbol;
	
	/**
	 * Konstruktor
	 * otwiera plik zrodlowy
	 * tworzy lekser
	 * pobiera pierwszy token z wejscia
	 */
	public Parser(String file) throws FileNotFoundException, IOException
	{
		PushbackReader reader = new PushbackReader(new FileReader(file));
		lekser = new Lekser(reader);
		symbol = lekser.getToken();
	}
	
	/**
	 * Glowna funkcja parsujaca plik wejsciowy
	 * W przypadku powodzenia zwraca nastepna komende programu
	 * Jezeli koniec pliku to zwraca null
	 * W przypadku bledu rzuca wyjatek ParsingException
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	public Command getCommand() throws IOException, ParsingException
	{
		// Pomija wszystkie komentarze miedzy poleceniami
		while(symbol.getType() == Token.T_COMMENT)
		{
			symbol = lekser.getToken();
		}
		if (symbol.getType() == Token.T_ANIMATE)
		{
			return animateCommand();
		}
		else if (symbol.getType() == Token.T_SHOW)
		{
			return showCommand();
		}
		else if (symbol.getType() == Token.T_OBJECT)
		{
			return objectCommand();
		}
		else if (symbol.getType() == Token.T_ANIMATION)
		{
			return animationCommand();
		}
		else if (symbol.getType() == Token.T_VARIABLE)
		{
			return simpleCommand();
		}
		else if (symbol.getType() == Token.T_EOF)
		{
			return null;
		}
		else
		{
			throw new ParsingException();
		}
	}
	
	/**
	 * Parsuje komende: animate $var;
	 * Zwraca zparsowana komende lub rzuca wyjatek
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Command animateCommand() throws IOException, ParsingException
	{
		symbol = lekser.getToken();
		Variable var = parseVariable();
		parseSimpleToken(Token.T_SEMICOLON);
		return new AnimateCommand(Command.ANIMATE_COMMAND, var);
	}
	
	/**
	 * Parsuje komende: show $var;
	 * Zwraca zparsowana komende lub rzuca wyjatek
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Command showCommand() throws IOException, ParsingException
	{
		symbol = lekser.getToken();
		Variable var = parseVariable();
		parseSimpleToken(Token.T_SEMICOLON);
		return new ShowCommand(Command.SHOW_COMMAND, var);
	}
	
	/**
	 * Parsuje komende: $var = ...;
	 * Zwraca zparsowana komende lub rzuca wyjatek
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Command simpleCommand() throws IOException, ParsingException
	{
		Variable var = parseVariable();
		parseSimpleToken(Token.T_EQUALS);
		ObjectElement elem = parseObjectElement();
		parseSimpleToken(Token.T_SEMICOLON);
		return new SimpleCommand(Command.SIMPLE_COMMAND, var, elem);
	}
	
	/**
	 * Parsuje komende: object $var {...};
	 * Zwraca zparsowana komende lub rzuca wyjatek
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Command objectCommand() throws IOException, ParsingException
	{
		// Parsuje do klamry wlacznie
		symbol = lekser.getToken();
		Variable var = parseVariable();
		parseSimpleToken(Token.T_LEFT_BUCKLE);
		
		// Parsuje wszystko to co jest miedzy klamrami
		boolean flag = false;
		ArrayList<ObjectElement> list = new ArrayList<>();
		while((symbol.getType() == Token.T_COMMA && flag == true) || (flag == false && (symbol.getType() == Token.T_CIRCLE || symbol.getType() == Token.T_RECTANGLE || symbol.getType() == Token.T_POLYGON || symbol.getType() == Token.T_TRIANGLE || symbol.getType() == Token.T_MOVE || symbol.getType() == Token.T_ROTATE || symbol.getType() == Token.T_SCALE || symbol.getType() == Token.T_SHEARX || symbol.getType() == Token.T_SHEARY || symbol.getType() == Token.T_TRANSFORM || symbol.getType() == Token.T_VARIABLE)))
		{
			// Nie jest to pierwszy element miedzy klamrami, wiec poprzedza go przecinek
			if (flag == true)
			{
				parseSimpleToken(Token.T_COMMA);
			}
			
			// Parsuje skladnik obiektu zlozonego
			list.add(parseObjectElement());
			flag = true;
		}
		
		// Parsuje pozostala czesc polecenia
		parseSimpleToken(Token.T_RIGHT_BUCKLE);
		parseSimpleToken(Token.T_SEMICOLON);
		return new ObjectCommand(Command.OBJECT_COMMAND, var, list);
	}
	
	/**
	 * Parsuje komende: animation $var {...};
	 * Zwraca zparsowana komende lub rzuca wyjatek
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Command animationCommand() throws IOException, ParsingException
	{
		// Parsuje do klamry wlacznie
		symbol = lekser.getToken();
		Variable var = parseVariable();
		boolean loop;
		parseSimpleToken(Token.T_LEFT_BUCKLE);
		
		// Parsuje informacje o zapetleniu animacji
		parseSimpleToken(Token.T_LOOP);
		parseSimpleToken(Token.T_COLON);
		if (symbol.getType() == Token.T_YES)
		{
			loop = true;
			parseSimpleToken(Token.T_YES);
		}
		else
		{
			loop = false;
			parseSimpleToken(Token.T_NO);
		}
		
		// Parsuje startowy moment animacji
		parseSimpleToken(Token.T_COMMA);
		FirstTimePoint ftp = parseFirstTimePoint();
		
		// Parsuje pozostale momenty animacji
		ArrayList<RestTimePoint> list = new ArrayList<>();
		while(symbol.getType() == Token.T_COMMA)
		{
			parseSimpleToken(Token.T_COMMA);
			list.add(parseRestTimePoint());
		}
		
		// Parsuje reszte polecenia
		parseSimpleToken(Token.T_RIGHT_BUCKLE);
		parseSimpleToken(Token.T_SEMICOLON);
		return new AnimationCommand(Command.ANIMATION_COMMAND, var, loop, list, ftp);
	}
	
	/**
	 * Parsuje transformacje geometryczna: rotate, scale, ...
	 * Zwraca obiekt reprezentujacy transformacje
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Transform parseTransformation() throws IOException, ParsingException
	{
		// Ustawia rodzaj transformacji
		int type;
		if (symbol.getType() == Token.T_MOVE)
			type = Transform.MOVE;
		else if (symbol.getType() == Token.T_ROTATE)
			type = Transform.ROTATE;
		else if (symbol.getType() == Token.T_SCALE)
			type = Transform.SCALE;
		else if (symbol.getType() == Token.T_SHEARX)
			type = Transform.SHEARX;
		else if (symbol.getType() == Token.T_TRANSFORM)
			type = Transform.TRANSFORM;
		else
			type = Transform.SHEARY;
		
		// Pobiera nastepny token
		symbol = lekser.getToken();
		
		parseSimpleToken(Token.T_LEFT_BRACKET);
		
		// Tworzy tablice na parametry dla kazdej transformacji
		int n;
		if (type == Transform.MOVE)
			n = 2;
		else if (type == Transform.ROTATE)
			n = 1;
		else if (type == Transform.SCALE)
			n = 2;
		else if (type == Transform.SHEARX)
			n = 1;
		else if (type == Transform.SHEARY)
			n = 1;
		else
			n = 6;
		int[] params = new int[n];
		
		// Parsuje liste parametrow
		for (int i = 0; i < n; i++)
		{
			params[i] = parseNumber();
			if (i != n - 1)
			{
				parseSimpleToken(Token.T_COMMA);
			}
		}
		
		// Parsuje reszte
		parseSimpleToken(Token.T_RIGHT_BRACKET);
		return new Transform(type, params);
	}
	
	/**
	 * Parsuje figure prosta: rectangle(...)
	 * Zwraca obiekt reprezentujacy figure
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Figure parseFigure() throws IOException, ParsingException
	{
		// Ustawia typ figury
		int type;
		if (symbol.getType() == Token.T_CIRCLE)
			type = Figure.CIRCLE;
		else if (symbol.getType() == Token.T_RECTANGLE)
			type = Figure.RECTANGLE;
		else if (symbol.getType() == Token.T_POLYGON)
			type = Figure.POLYGON;
		else
			type = Figure.TRIANGLE;
		
		// Pobiera nastepny token
		symbol = lekser.getToken();
		
		parseSimpleToken(Token.T_LEFT_BRACKET);
		
		// Parsuje liste parametrow
		// Wielokat parsowany jest osobno, poniewaz posiada nieustalona z gory liczbe parametrow
		int[] params;
		if (type == Figure.POLYGON)
		{
			ArrayList<Integer> list = new ArrayList<>();
			while(symbol.getType() == Token.T_NUMBER)
			{
				list.add(parseNumber());
				parseSimpleToken(Token.T_COMMA);
			}
			
			// Liczba parametrow musi byc liczba parzysta (wspolrzedna x i y dla kazdego punktu)
			if (list.size()%2 == 1)
				throw new ParsingException();
			params = new int[list.size()];
			for (int i = 0; i < list.size(); i++)
				params[i] = list.get(i).intValue();
		}
		else
		{
			// Parsuje Liste parametrow dla pozostalych figur
			int n;
			if (type == Figure.CIRCLE)
				n = 3;
			else if (type == Figure.TRIANGLE)
				n = 6;
			else if (type == Figure.RECTANGLE)
				n = 4;
			else
				throw new ParsingException();
			params = new int[n];
			for (int i = 0; i < n; i++)
			{
				params[i] = parseNumber();
				parseSimpleToken(Token.T_COMMA);
			}
		}
		
		// Na koniec parsuje jeszcze kolor figury
		Color2 color = parseColor2();
		
		parseSimpleToken(Token.T_RIGHT_BRACKET);
		return new Figure(type, params, color);
	}
	
	/**
	 * Parsuje proste tokeny, takie jak: 'color', ',', ':', ';'
	 * @param token
	 * @throws IOException
	 * @throws ParsingException
	 */
	private void parseSimpleToken(int token) throws IOException, ParsingException
	{
		if (symbol.getType() != token)
		{

		}
		symbol = lekser.getToken();
	}
	
	/**
	 * Parsuje zmienna
	 * Zwraca obiekt reprezentujacy zmienna
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Variable parseVariable() throws IOException, ParsingException
	{
		if (symbol.getType() != Token.T_VARIABLE)
		{
			throw new ParsingException();
		}
		StringToken st = (StringToken) symbol;
		Variable var = new Variable(st.getValue());
		symbol = lekser.getToken();
		return var;
	}
	
	/**
	 * Parsuje liczbe calkowita
	 * Zwraca zparsowana liczbe
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private int parseNumber() throws IOException, ParsingException
	{
		if (symbol.getType() != Token.T_NUMBER)
		{
			throw new ParsingException();
		}
		NumberToken nt = (NumberToken) symbol;
		int result = nt.getValue();
		symbol = lekser.getToken();
		return result;
	}
	
	/**
	 * Parsuje obiekt graficzny, ktory jest w jednej z postaci:
	 *  - $var                                         : odwolanie do zmiennej
	 *  - rotate(...) * move(...) * $var               : odwolanie do zmiennej polaczonej z transformacja geometryczna
	 *  - rectangle(...)                               : utworzenie figury prostej
	 *  - rotate(...) * move(...) * rectangle(...)     : utworzenie figury prostej polaczonej z transformacja geometryczna
	 *  
	 *  Zwraca obiekt klasy ObjectElement
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private ObjectElement parseObjectElement() throws IOException, ParsingException
	{
		// Parsuje liste transformacji geometrycznych
		ArrayList<Transform> trans = new ArrayList<>();
		while(symbol.getType() == Token.T_ROTATE || symbol.getType() == Token.T_MOVE || symbol.getType() == Token.T_SCALE || symbol.getType() == Token.T_SHEARX || symbol.getType() == Token.T_SHEARY || symbol.getType() == Token.T_TRANSFORM)
		{
			trans.add(parseTransformation());
			parseSimpleToken(Token.T_MULTIPLY);
		}
		
		// Parsuje figure prosta
		if (symbol.getType() == Token.T_CIRCLE || symbol.getType() == Token.T_TRIANGLE || symbol.getType() == Token.T_RECTANGLE || symbol.getType() == Token.T_POLYGON)
		{
			Figure fg = parseFigure();
			if (trans.isEmpty())
				return fg;
			else
				return new TranFigList(trans, fg);
		}
		else if (symbol.getType() == Token.T_VARIABLE)     // Parsuje zmienna
		{
			Variable var2 = parseVariable();
			if (trans.isEmpty())
				return var2;
			else
				return new TranVarList(trans, var2);
		}
		else                                              // Zadna z mozliwosci nie pasuje, a wiec blad
		{
			throw new ParsingException();
		}
	}
	
	/**
	 * Parsuje punkt startowy animacji
	 * Zwraca obiekt reprezentujacy startowy punkt czasowy
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private FirstTimePoint parseFirstTimePoint() throws IOException, ParsingException
	{
		int time = parseNumber();
		parseSimpleToken(Token.T_COLON);
		ObjectElement elem = parseObjectElement();
		return new FirstTimePoint(time, elem);
	}
	
	/**
	 * Parsuje dalszy punkt animacji
	 * Zwraca obiekt reprezentujacy dalsze punkty czasowe
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private RestTimePoint parseRestTimePoint() throws IOException, ParsingException
	{
		// Parsuje czas
		int time = parseNumber();
		parseSimpleToken(Token.T_COLON);
		ArrayList<Transform> trans = new ArrayList<>();
		
		// Parsuje liste transformacji
		boolean flag = false;
		while(((symbol.getType() == Token.T_ROTATE || symbol.getType() == Token.T_MOVE || symbol.getType() == Token.T_SCALE || symbol.getType() == Token.T_SHEARX || symbol.getType() == Token.T_SHEARY || symbol.getType() == Token.T_TRANSFORM) && flag == false) || (flag == true && symbol.getType() == Token.T_MULTIPLY))
		{
			if (flag == true)
			{
				parseSimpleToken(Token.T_MULTIPLY);
			}
			trans.add(parseTransformation());
			flag = true;
		}
		return new RestTimePoint(time, trans);
	}
	
	/**
	 * Parsuje kolor
	 * Zwraca obiekt reprezentujacy kolor
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Color2 parseColor2() throws IOException, ParsingException
	{
		parseSimpleToken(Token.T_COLOR);
		parseSimpleToken(Token.T_LEFT_BRACKET);
		int rgb[] = new int[3];
		rgb[0] = parseNumber();
		parseSimpleToken(Token.T_COMMA);
		rgb[1] = parseNumber();
		parseSimpleToken(Token.T_COMMA);
		rgb[2] = parseNumber();
		parseSimpleToken(Token.T_RIGHT_BRACKET);
		return new Color2(rgb);
	}
}
