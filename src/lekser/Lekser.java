/**
 * 
 */
package lekser;
import java.io.*;
/**
 * @author Artur
 * Klasa leksera
 */
public class Lekser {
	
	// Zrodlo danych
	private PushbackReader source;
	
	public Lekser(PushbackReader source)
	{
		this.source = source;
	}
	
	/**
	 * Wczytuje komentarz
	 * @throws IOException
	 */
	private void readComment()  throws IOException
	{
		while(true)
		{
			while(source.read() != '*');
			while(true)
			{
				int next = source.read();
				if (next == '/') return;
				else if (next == '*') continue;
				else break;
			}
		}
	}
	
	/**
	 * Wczytuje zmienna
	 * @return
	 * @throws IOException
	 */
	private Token readVariable()  throws IOException
	{
		String s = "";
		int first = source.read();
		if (Character.isAlphabetic(first)) s += (char) first;
		else return new BasicToken(Token.T_ERROR);
		int next = source.read();
		while(Character.isAlphabetic(next) || Character.isDigit(next) || next == '_')
		{
			s += (char) next;
			next = source.read();
		}
		source.unread(next);
		return new StringToken(Token.T_VARIABLE, s);
	}
	
	/**
	 * Wczytuje slowo kluczowe object
	 * @return
	 * @throws IOException
	 */
	private Token readObject()  throws IOException
	{
		char[] next = new char[6];
		int count = source.read(next, 0, 6);
		if (count != 6) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("bject "))return new BasicToken(Token.T_OBJECT);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe rotate
	 * @return
	 * @throws IOException
	 */
	private Token readRotate()  throws IOException
	{
		char[] next = new char[4];
		int count = source.read(next, 0, 4);
		if (count != 4) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("tate"))return new BasicToken(Token.T_ROTATE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe polygon
	 * @return
	 * @throws IOException
	 */
	private Token readPolygon()  throws IOException
	{
		char[] next = new char[6];
		int count = source.read(next, 0, 6);
		if (count != 6) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("olygon"))return new BasicToken(Token.T_POLYGON);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe move
	 * @return
	 * @throws IOException
	 */
	private Token readMove()  throws IOException
	{
		char[] next = new char[3];
		int count = source.read(next, 0, 3);
		if (count != 3) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("ove"))return new BasicToken(Token.T_MOVE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje liczbe calkowita
	 * @param ch - wczytany pierwszy znak liczby
	 * @return
	 * @throws IOException
	 */
	private Token readNumber(int ch)  throws IOException
	{
		String s = "";
		s += (char) ch;
		int next = source.read();
		while(Character.isDigit(next))
		{
			s += (char) next;
			next = source.read();
		}
		source.unread(next);
		return new NumberToken(Token.T_NUMBER, Integer.parseInt(s));
	}
	
	/**
	 * Wczytuje liczbe ujemna
	 * Od readNumber rozni ja to, ze nie wczytano jeszcze zadnej cyfry, tylko znak minus
	 * @return
	 * @throws IOException
	 */
	private Token readNegative()  throws IOException
	{
		String s = "";
		int next = source.read();
		while(Character.isDigit(next))
		{
			s += (char) next;
			next = source.read();
		}
		source.unread(next);
		return new NumberToken(Token.T_NUMBER, -(Integer.parseInt(s)));
	}
	
	/**
	 * Wczytuje slowo kluczowe color
	 * @return
	 * @throws IOException
	 */
	private Token readColor()  throws IOException
	{
		char[] next = new char[3];
		int count = source.read(next, 0, 3);
		if (count != 3) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("lor"))return new BasicToken(Token.T_COLOR);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe circle
	 * @return
	 * @throws IOException
	 */
	private Token readCircle()  throws IOException
	{
		char[] next = new char[4];
		int count = source.read(next, 0, 4);
		if (count != 4) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("rcle"))return new BasicToken(Token.T_CIRCLE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe transform
	 * @return
	 * @throws IOException
	 */
	private Token readTransform()  throws IOException
	{
		char[] next = new char[6];
		int count = source.read(next, 0, 6);
		if (count != 6) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("nsform"))return new BasicToken(Token.T_TRANSFORM);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe triangle
	 * @return
	 * @throws IOException
	 */
	private Token readTriangle()  throws IOException
	{
		char[] next = new char[5];
		int count = source.read(next, 0, 5);
		if (count != 5) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("angle"))return new BasicToken(Token.T_TRIANGLE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe rectangle
	 * @return
	 * @throws IOException
	 */
	private Token readRectangle() throws IOException
	{
		char[] next = new char[7];
		int count = source.read(next, 0, 7);
		if (count != 7) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("ctangle"))return new BasicToken(Token.T_RECTANGLE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe show
	 * @return
	 * @throws IOException
	 */
	private Token readShow()  throws IOException
	{
		char[] next = new char[1];
		int count = source.read(next, 0, 1);
		if (count != 1) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("w"))return new BasicToken(Token.T_SHOW);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowa kluczowe shearx i sheary
	 * @return
	 * @throws IOException
	 */
	private Token readShear()  throws IOException
	{
		char[] next = new char[2];
		int count = source.read(next, 0, 2);
		if (count != 2) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("ar"))
		{
			int next2 = source.read();
			if (next2 == 'x')return new BasicToken(Token.T_SHEARX);
			else if (next2 == 'y') return new BasicToken(Token.T_SHEARY);
			else return new BasicToken(Token.T_ERROR);
		}
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe scale
	 * @return
	 * @throws IOException
	 */
	private Token readScale()  throws IOException
	{
		char[] next = new char[3];
		int count = source.read(next, 0, 3);
		if (count != 3) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("ale"))return new BasicToken(Token.T_SCALE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe animation
	 * @return
	 * @throws IOException
	 */
	private Token readAnimation()  throws IOException
	{
		char[] next = new char[3];
		int count = source.read(next, 0, 3);
		if (count != 3) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals("on "))return new BasicToken(Token.T_ANIMATION);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Wczytuje slowo kluczowe animate
	 * @return
	 * @throws IOException
	 */
	private Token readAnimate()  throws IOException
	{
		char[] next = new char[1];
		int count = source.read(next, 0, 1);
		if (count != 1) return new BasicToken(Token.T_ERROR);
		if (new String(next).equals(" "))return new BasicToken(Token.T_ANIMATE);
		else return new BasicToken(Token.T_ERROR);
	}
	
	/**
	 * Zwraca nastepny token
	 * @return
	 * @throws IOException
	 */
	public Token getToken() throws IOException
	{
		int ch = source.read();
		
		// Pomija biale znaki
		while(Character.isWhitespace(ch))
		{
			ch = source.read();
		}
		
		// Koniec pliku
		if (ch == -1) return new BasicToken(Token.T_EOF);
		
		switch((char) ch)
		{
		case '/': {
			if (source.read() != '*') return new BasicToken(Token.T_ERROR);
			readComment();
			return new BasicToken(Token.T_COMMENT);
		}
		case ';': return new BasicToken(Token.T_SEMICOLON);
		case ':': return new BasicToken(Token.T_COLON);
		case '=': return new BasicToken(Token.T_EQUALS);
		case '(': return new BasicToken(Token.T_LEFT_BRACKET);
		case ')': return new BasicToken(Token.T_RIGHT_BRACKET);
		case '{': return new BasicToken(Token.T_LEFT_BUCKLE);
		case '}': return new BasicToken(Token.T_RIGHT_BUCKLE);
		case ',': return new BasicToken(Token.T_COMMA);
		case '$': return readVariable();
		case '*': return new BasicToken(Token.T_MULTIPLY);
		case 'o': return readObject();
		case 'r': {
			int next = source.read();
			if (next == 'o') 
				return readRotate();
			else if (next == 'e') 
				return readRectangle();
			else 
				return new BasicToken(Token.T_ERROR);
		}
		case '-': return readNegative();
		case 'a': {
			char[] next = new char[5];
			int count = source.read(next, 0, 5);
			if (count != 5) 
				return new BasicToken(Token.T_ERROR);
			if (new String(next).equals("nimat"))
			{
				int next2 = source.read();
				if (next2 == 'i') 
					return readAnimation();
				else if (next2 == 'e') 
					return readAnimate();
				else 
					return new BasicToken(Token.T_ERROR);
			}
			else 
				return new BasicToken(Token.T_ERROR);
		}
		case 's': {
			int next = source.read();
			if (next == 'c') 
				return readScale();
			else if (next == 'h')
			{
				int next2 = source.read();
				if (next2 == 'o') 
					return readShow();
				else if (next2 == 'e') 
					return readShear();
				else 
					return new BasicToken(Token.T_ERROR);
			}
			else return new BasicToken(Token.T_ERROR);
		}
		case 't': {
			int next = source.read();
			if (next != 'r') return new BasicToken(Token.T_ERROR);
			next = source.read();
			if (next == 'a') return readTransform();
			else if (next == 'i') return readTriangle();
			else return new BasicToken(Token.T_ERROR);
		}
		case 'p': return readPolygon();
		case 'm': return readMove();
		case 'c': {
			int next = source.read();
			if (next == 'o') return readColor();
			else if (next == 'i') return readCircle();
			else return new BasicToken(Token.T_ERROR);
		}
		case 'l': {
			int next = source.read();
			if (next == 'o') 
			{
				int next2 = source.read();
				if (next2 == 'o')
				{
					int next3 = source.read();
					if (next3 == 'p') 
						return new BasicToken(Token.T_LOOP);
					else 
						return new BasicToken(Token.T_ERROR);
				}
				else
				{
					return new BasicToken(Token.T_ERROR);
				}
			}
			else
			{
				return new BasicToken(Token.T_ERROR);
			}
		}
		case 'y': {
			int next = source.read();
			if (next == 'e') 
			{
				int next2 = source.read();
				if (next2 == 's') 
					return new BasicToken(Token.T_YES);
				else 
					return new BasicToken(Token.T_ERROR);
			}
			else
			{
				return new BasicToken(Token.T_ERROR);
			}
		}
		case 'n': {
			int next = source.read();
			if (next == 'o') 
			{
				return new BasicToken(Token.T_NO);
			}
			else
			{
				return new BasicToken(Token.T_ERROR);
			}
		}
		default: {
			if (Character.isDigit(ch)) return readNumber(ch);
			else return new BasicToken(Token.T_ERROR);
		}
		}
		// return new BasicToken(Token.T_ERROR);
	}
}
