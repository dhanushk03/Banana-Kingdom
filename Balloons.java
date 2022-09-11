import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Balloons {
	public static ArrayList<Balloon> balloons;
	public static int balloonsGenerated;
	public static int[] initialFPS = {60, 50, 45, 40, 35, 100};
	public static int[] balloonsPerOneSecFPSDecrease = {5, 7, 10, 12, 15, 100};
	
	public static int entryPoint;
	public static int exitPoint;
	
	public static int frames = 0;
	public static int fps;
	
	public static boolean isFirst;
	
	public Balloons() {
		balloons = new ArrayList<Balloon>();
		balloonsGenerated = 0;
		isFirst = true;
		
		define();
	}
	
	public Balloons(ArrayList<Balloon> balloonList) {
		balloons = balloonList;
	}
	
	public void define() {
		//randomly generates red, green, and blue balloons
		//the ratio of the red, green, and blue balloons goes from red-heavy to blue-heavy as the game progresses
		//adds generated balloons to a balloons ArrayList
		//when screen calls this class's draw method, each balloon in the arrayList is drawn
		if(isFirst)
			fps = 120;   //initial fps is higher to give the player some time to get ready
		else
			fps = initialFPS[Screen.level-1] - (balloonsGenerated / balloonsPerOneSecFPSDecrease[Screen.level-1]); //fps stands for the amount of frames that need to be reached before a new balloon is generated (applies to all places and corresponding actions where fps is used)
		Balloon balloon;
		int airID;
		double cap;
		double redCap = 0.6;
		double greenCap = 0.9;
		for(int i = 0; i < Screen.room.worldHeight; i++) {
			if(Screen.room.block[i][0].groundID==Value.groundRoad) {
				entryPoint = i; //finds the entry point to start generating the balloons
			}	
			if(Screen.room.block[i][Screen.room.block[0].length - 1].groundID == Value.groundRoad)
				exitPoint = i;  //finds the exit point to know where to remove the balloons and subtract from the kingdom's health if reached
		}
		if (frames >= fps) {
			isFirst = false;
			frames = 0;
			redCap = generateRedCap();  //the likelihood that a red balloon will be generated
			greenCap = redCap + 0.3;  //the likelihood that a green balloon will be generated (minus the redCap)
			cap = Math.random();   //random number generator
			if (cap <= redCap)
				airID = Value.airRed;
			else if (cap <= greenCap)
				airID = Value.airGreen;
			else
				airID = Value.airBlue;    //if not red or green, a blue balloon is automatically generated
			balloon = new Balloon(airID, Screen.room.block[entryPoint][0].id);
			balloonsGenerated++;
			balloons.add(balloon);   //the newly generated balloon is added to the balloons arrayList
		}
		frames++;
	}
	
	public double generateRedCap() {
		return 1.0 - (balloonsGenerated / 250.0);   //after 250 balloons generated, it is impossible for a red balloon to be generated
	}
	
	public void draw(Graphics g) {
		define();
		int iterator = 0;
		while(iterator < balloons.size()) {
			//the while loop will iterate through all the balloons remaining in the balloons arrayList
			if(balloons.get(iterator).isAlive() && balloons.get(iterator).getX() >= Screen.room.block[exitPoint][Screen.room.worldWidth-1].x) {
				Screen.kingdomHealth-=balloons.get(iterator).getHealth();   //if balloon reaches the exit point
				balloons.get(iterator).setAlive(false);    //its alive boolean variable is set to false
				balloons.remove(iterator);   //the balloon is then popped off of the arrayList so it will never be drawn again
			}
			else if(balloons.get(iterator).isAlive()) {
				balloons.get(iterator).draw(g);   //normal case scenario: draw an alive balloon
				iterator++;
			}
			else {
				Screen.coins += balloons.get(iterator).getReward();   //if it isn't at the end and it's not alive, then it's dead, so add its corresponding reward to our coinage
				balloons.remove(iterator);   //remove from the arrayList
			}
			
			
			
		}
	}
	
	
}
