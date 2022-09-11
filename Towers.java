import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public class Towers {
	//there are 5 types of towers
	public static ArrayList<Tower> towerList;  //contains all initialized towers
	public static String[] names = {"Poltroon", "Ninja", "Sniper", "Freezer", "Bomber"};   //names/types of the towers; in each array the index of each of the types of towers corresponds to the same index in tileset_store
	public static int[] fps = {20, 35, 60, 60, 60};   //frames needed for each tower to attack
	public static double[] ranges = {2, 2.75, 4, 2.25, 2.25};   //ranges of all the towers
	public static int[] damages = {75, 250, 500, 50, 800};   //the damage per attack of all the towers
	public static int[] prices = {250, 500, 1250, 1600, 2200};   //the prices of all the towers
	
	public static int iterator;
	
	public Towers() {
		define();
		iterator = 0;
	}
	
	public void define() {
		towerList = new ArrayList<Tower>();   //towerList initialized
	}
	
	public void draw(Graphics g) {
		while(iterator < towerList.size()) {
			if(towerList.get(iterator)!=null) {
				//draws each tower, from which each tower's attack method is called periodically
				towerList.get(iterator).draw(g);
			}
			iterator++;
		}
		iterator = 0;
		
	}
}
