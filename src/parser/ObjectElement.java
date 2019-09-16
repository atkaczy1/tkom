/**
 * 
 */
package parser;

/**
 * @author Artur
 * Interfejs implementowany przez wszystkie rodzaje obiektow graficznych
 * 
 * ObjectElement reprezentuje wystapienia elementow w programie takich jak
 *  - $var                                         : odwolanie do zmiennej
 *  - rotate(...) * move(...) * $var               : odwolanie do zmiennej polaczonej z transformacja geometryczna
 *  - rectangle(...)                               : utworzenie figury prostej
 *  - rotate(...) * move(...) * rectangle(...)     : utworzenie figury prostej polaczonej z transformacja geometryczna
 */
public interface ObjectElement {

}
