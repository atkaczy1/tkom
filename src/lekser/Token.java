/**
 * 
 */
package lekser;

/**
 * @author Artur
 * Interfejs tokenu
 * Jest implementowany przez 3 klasy
 * Rozne klasy implementujace sa potrzebne, poniewaz niektore tokeny przechowuja dodatkowe dane
 */
public interface Token {
	
	// Typy tokenow
	final static int T_ANIMATION = 0;
	final static int T_OBJECT = 1;
	final static int T_SHOW = 2;
	final static int T_ANIMATE = 3;
	final static int T_VARIABLE = 4;
	final static int T_SEMICOLON = 5;
	final static int T_COLON = 6;
	final static int T_COMMA = 7;
	final static int T_MULTIPLY = 8;
	final static int T_EQUALS = 9;
	final static int T_NUMBER = 10;
	final static int T_TRIANGLE = 11;
	final static int T_RECTANGLE = 12;
	final static int T_POLYGON = 13;
	final static int T_SHEARX = 14;
	final static int T_CIRCLE = 15;
	final static int T_COLOR = 16;
	final static int T_LEFT_BRACKET = 17;
	final static int T_RIGHT_BRACKET = 18;
	final static int T_TRANSFORM = 19;
	final static int T_SHEARY = 20;
	final static int T_MOVE = 21;
	final static int T_ROTATE = 22;
	final static int T_SCALE = 23;
	final static int T_LEFT_BUCKLE = 24;
	final static int T_RIGHT_BUCKLE = 25;
	final static int T_LOOP = 26;
	final static int T_YES = 27;
	final static int T_NO = 28;
	final static int T_EOF = 29;
	final static int T_ERROR = 30;
	final static int T_COMMENT = 31;
	
	int getType();
}
