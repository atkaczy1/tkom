/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Artur
 * Klasa do wyswietlania wyniku interpretowania danych wejsciowych
 */
public class View {
	private JFrame frame;
	private ShowPanel showPanel;
	
	// Figury do wyswietlenia 
	private ArrayList<Shape> shapes;
	private ArrayList<Color> colors;
	private ArrayList<AffineTransform> transforms;
	
	public View()
	{
		shapes = new ArrayList<>();
		transforms = new ArrayList<>();
		colors = new ArrayList<>();
		showPanel = new ShowPanel();
		frame = new JFrame("Interpreter scen graficznych");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(showPanel);
		frame.setVisible(true);
	}
	
	/**
	 * Ustawia nowe dane do wyswietlenia i odswieza widok
	 * @param s
	 * @param t
	 * @param c
	 */
	public void show(ArrayList<Shape> s, ArrayList<AffineTransform> t, ArrayList<Color> c)
	{
		showPanel.setVisible(false);
		shapes = s;
		transforms = t;
		colors = c;
		showPanel.setVisible(true);
	}
	
	/**
	 * Panel w ktorym wyswietlane sa figury
	 * @author Artur
	 *
	 */
	class ShowPanel extends JPanel
	{
		public void paintComponent(Graphics g) 
		{
			Graphics2D g2d = (Graphics2D) g;
			
			// Czyszczenie widoku
			g2d.setPaint(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(0,0,800,600));
			
			// Wyswietlanie wszystkich figur
			for (int i = 0; i < shapes.size(); i++)
			{
				g2d.setPaint(colors.get(i));
				g2d.setTransform(transforms.get(i));
				g2d.fill(shapes.get(i));
			}
		}
	}
}
