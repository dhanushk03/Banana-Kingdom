
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Frame extends JFrame {
	
	//Is technically the driver but really the driver is Screen
	public static String title = "Bloons Tower Defense Battles Alpha";
	public static Dimension size = new Dimension(1000, 650);   //width and height of the frame
	
	
	public Frame() {
		setTitle(title);
		setSize(size);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
	}
	
	public void initialize() {
		
		setLayout(new GridLayout(1,1,0,0));  //sets the grid layout of the map, grass/road/other blocks are loaded in later
		Screen screen = new Screen(this);  //basically a color panel that acts like a driver because it implements runnable
		add(screen);
		setVisible(true);
	}
	
	public static void main(String args[]) {
		Frame frame = new Frame();
	}
	
	
	
	
}
