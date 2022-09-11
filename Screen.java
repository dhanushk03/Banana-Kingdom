
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Screen extends JPanel implements Runnable{
	public Thread thread = new Thread(this);   //used for the run method and thread.sleep()
	public boolean stopThread = false;   //stop thread when game is over
	
	public static Image[] tileset_ground = new Image[40];   //Image array of all images that belong on the ground (blocks)
	public static Image[] tileset_air = new Image[40];   //Image array of all images that belong on the air (balloons)
	public static Image[] tileset_store = new Image[40];   //Image array of all images that go in the store (towers)
	public static Image[] tileset_obstacles = new Image[40];   //Image array of all images that are obstacles (brown soil block / golden end block)
	
	public static int myWidth, myHeight;   //screen dimensions
	
	public static boolean isFirst = true;   //if a level is loading for the first time
	public static boolean haveLost = false;   //if the user has lost
	public static boolean haveWon = false;   //if the user has won
	
	public static Point mse = new Point(0, 0);   //wherever the mouse is (assigned in keyHandler)
	public static int kingdomHealth;   //how much health kingdom has remaining
	public static int level = 1;   //level to be loaded; default is 1 but player will choose
	
	public static int coins;   //coins for purchasing towers
	public static int selectedTowerID;   //the type of tower that the user has selected from the store
	
	public static Room room;   //Room object
	public static Save save;   //Save object
	public static Balloons balloons;   //Balloons object
	public static Towers towers;   //Towers object
	public static Store store;   //Store object
	
	public Screen(Frame frame) {
		frame.addMouseListener(new KeyHandler());  //Add the mouse stuff
		frame.addMouseMotionListener(new KeyHandler());
		thread.start();   //start thread aka call the run method
		
		Integer[] levels = {1,2,3,4,5};
		Integer input = (Integer)JOptionPane.showInputDialog(frame,"What level do you want to play/start from?", "Level Select",
				JOptionPane.QUESTION_MESSAGE,null,levels,levels[0]);   //level select JOptionPane
		level = input;
		//setBackground(Color.PINK);
	}
	
	public void define() {
		//JOptionPane()
		room = new Room();   //all objects are instantiated here. Define is only called at the START of every level (aka if isFirst == true)
		save = new Save();
		balloons = new Balloons();
		towers = new Towers();
		store = new Store();
		
		for(int i = 0; i < tileset_ground.length; i++) {
			//tileset_ground is an array for images of all blocks that go on the ground; the first image is of the grass; the second is of the rocky road 
			tileset_ground[i] = new ImageIcon("res/tileset_ground.png").getImage();
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i = 0; i < tileset_air.length; i++) {
			//tileset_air is an array for images of all blocks that go in the air (balloons)
			tileset_air[i] = new ImageIcon("res/tileset_air.png").getImage();
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		for(int i = 0; i < tileset_obstacles.length; i++) {
			//tileset_obstacles is an array for images of all blocks that nullify a grass block from having towers being placed on it, and the golden block at the end
			tileset_obstacles[i] = new ImageIcon("res/tileset_obstacles.png").getImage();
			tileset_obstacles[i] = createImage(new FilteredImageSource(tileset_obstacles[i].getSource(), new CropImageFilter(0, 26*i, 26, 26)));
		}
		
		//manual loading of all the towers because I couldn't bother to do it all in a 26 x 6 million pixel png file
		tileset_store[0] = new ImageIcon("res/poltroon.png").getImage();
		tileset_store[1] = new ImageIcon("res/ninja.png").getImage();
		tileset_store[2] = new ImageIcon("res/sniper.png").getImage();
		tileset_store[3] = new ImageIcon("res/freezer.png").getImage();
		tileset_store[4] = new ImageIcon("res/bomber.png").getImage();
		tileset_store[5] = new ImageIcon("res/trash.png").getImage();
		tileset_store[6] = new ImageIcon("res/bomb.png").getImage();
		
		tileset_air[9] = new ImageIcon("res/frozenRedBalloon.png").getImage();
		tileset_air[10] = new ImageIcon("res/frozenGreenBalloon.png").getImage();
		tileset_air[11] = new ImageIcon("res/frozenBlueBalloon.png").getImage();
		
		
		kingdomHealth = 15000;   //kingdomHealth, selectedTowerID, and coins initialized
		selectedTowerID = -1;
		coins = 1000;
		//displayIntro();
		createLevel();   //level is created based on level variable
	}
	
	public void createLevel() {
		//creates levels that ascend in difficulty
		if(level == 1)
			//easy
			save.loadSave(new File("save/mission1.txt"));
		else if(level == 2)
			//medium
			save.loadSave(new File("save/mission2.txt"));
		else if(level == 3)
			//hard
			save.loadSave(new File("save/mission3.txt"));
		else if(level == 4)
			//advanced
			save.loadSave(new File("save/mission4.txt"));
		else if(level == 5)
			//expert
			save.loadSave(new File("save/mission5.txt"));
		else if(level > 5)
			haveWon = true;
			
	}
	
	public void paintComponent(Graphics g) {
		
		if(isFirst) {
			//Calls the define method to load in everything and then the switch is turned off so the game can actually begin
			myWidth = getWidth();
			myHeight = getHeight();
			define();
			isFirst = false;
		}
		else {
			//Sets background to blue
			g.setColor(new Color(56,143,186));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0,0,0));
			
			//drawing the borders around the room
			g.setFont(new Font("TimesRoman", Font.BOLD, 25));
			g.drawString("LEVEL " + level, (int) (getWidth()*1/20)-35, (int) (getHeight()*1/15));
			g.drawLine(room.block[0][0].x-1, room.block[0][0].y-1, room.block[0][0].x-1, room.block[room.worldHeight-1][0].y + room.blockSize + 1);
			g.drawLine(room.block[room.worldHeight-1][0].x-1, room.block[room.worldHeight-1][0].y+room.blockSize+1, room.block[room.worldHeight-1][room.worldWidth-1].x+room.blockSize+1, room.block[room.worldHeight-1][room.worldWidth-1].y+room.blockSize + 1);
			g.drawLine(room.block[room.worldHeight-1][room.worldWidth-1].x+room.blockSize+1, room.block[room.worldHeight-1][room.worldWidth-1].y+room.blockSize + 1, room.block[0][room.worldWidth-1].x + room.blockSize + 1, room.block[0][room.worldWidth-1].y);
			g.drawLine(room.block[0][room.worldWidth-1].x + room.blockSize + 1, room.block[0][room.worldWidth-1].y - 1, room.block[0][0].x-1, room.block[0][0].y-1);
			
			room.draw(g); //Drawing the room --> thereby draws all the blocks in the room
			store.draw(g); //Drawing the store, aka the buttons and the displays
			balloons.draw(g); //Drawing all the balloons and moving them at the same time
			towers.draw(g); //Drawing all the towers and checking for attacks in each of them at the same time
			
		}
		
		checkIfGameOver();   //constantly checks if the game is over before going into the lost and won if statements
		
		if(haveLost) {
			//Displays lost screen
			g.setColor(Color.RED);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 50));
			g.drawString("YOU LOST, GET BETTER", getWidth()/2 - 300, getHeight()/2 - 50);
			g.drawString("Balloons Survived: " + Balloons.balloonsGenerated, getWidth()/2 - 300, getHeight()/2 + 50);
			stopThread = true;   //stop thread is activated to true so the repaint method is no longer called
		}
		else if(haveWon) {
			//Displays won screen (only after beating level 5)
			System.out.println(level);
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 50));
			g.drawString("YOU WON!! MONKE IS SAFE!!", getWidth()/2 - 350, getHeight()/2);
			stopThread = true;
		}
		
		if (!haveLost && !haveWon && selectedTowerID != -1) {
			//if a tower is selected from the store its details and stats are portrayed to the right
			double secondsPerAttack = Towers.fps[selectedTowerID] * 0.025;
			g.drawImage(tileset_store[selectedTowerID], (int) (getWidth()*1/20) - 20, (int) (getHeight() * 4/10), 80, 80, null);
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 18));
			g.drawString("Details:", (int) (getWidth()*8.75/10), getHeight()*2/10);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
			g.drawString("Name: " + '\n' + Towers.names[selectedTowerID], (int) (getWidth()*8.75/10), getHeight()*3/10);
			g.drawString("Price: " + '\n' + Towers.prices[selectedTowerID] + " bananas", (int) (getWidth()*8.75/10), getHeight()*4/10);
			g.drawString("Range: " + '\n' + Towers.ranges[selectedTowerID] + " blocks", (int) (getWidth()*8.75/10), getHeight()*5/10);
			g.drawString("Damage: " + '\n' + Towers.damages[selectedTowerID] + " HP", (int) (getWidth()*8.75/10), getHeight()*6/10);
			g.drawString("Sec/Attack: " + '\n' + secondsPerAttack + " sec", (int) (getWidth()*8.75/10), getHeight()*7/10);
		}
		
	}
	
	public void checkIfGameOver() {
		if(Balloons.fps <= 5) {
			//Ends the game, calls it a victory if its only taking 5 frames to generate a new balloon; causes some variability in where the game ends for each level
			level++;  //level is incremented by 1 and isFirst is set to true to reload the new level
			isFirst = true;
		}
		if(!isFirst && Screen.kingdomHealth<=0) {
			//if kingdomHealth is less than 0 then haveLost is set to true
			haveLost = true;
			
		}
	}
	
	public void run() {
		while(!stopThread) {
			repaint();
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
