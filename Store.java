import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Store {
	public static int shopWidth = 6;  //how many store buttons there are
	public static int buttonSize = 52;   //how big each button is
	public static int cellSpace = 8;   //how much space there is between each button

	public static int itemIn = 4;   //how much the item is shrunk in within the store button
	public static int[] buttonID = {0, 1, 2, 3, 4, 5};   //corresponds to the tower type in each button; the last one is just for the trash, to let go of a selected tower
	public static int heldID = -2;   //will be used to access the buttonID array
	public static boolean holdsItem = false;   //if the user has selected a tower from the store
	public static boolean dropsItem = false;   //if the user has dropped a tower from the store onto the map
	public static int droppedX = 0;   //where the user dropped the tower
	public static int droppedY = 0;
	public static int droppedID = -2;   //what tower the user dropped
	public static boolean towerInitialized = false;   //was the tower able to be initialized
	
	public Rectangle[] button = new Rectangle[shopWidth];   //the store button array
	
	public Rectangle coins;   //the coins/bananas rectangle display
	public Rectangle kingdomHealth;   //the kingdom health display 
	public Rectangle balloonsGenerated;    //the number of balloons generated display
	
	public Store() {
		define();
	}
	
	public void click(int mouseButton) {
		if(mouseButton == 1) {
			//if a mouse is clicked, it determines if a store button was clicked, what store button it was, and brings the store icon to the mouse coordinates
			for(int i = 0; i < button.length; i++) {
				if(button[i].contains(Screen.mse)) {
					heldID = buttonID[i];
					if(heldID == Value.airTrashCan) {
						//If it's the trash that they selected, then no item is held
						holdsItem = false;
					}
					else {
						heldID = buttonID[i];
						holdsItem = true;
					}
				}
			}
			
			if(holdsItem && heldID != Value.airTrashCan && Screen.coins - Towers.prices[heldID] >= 0) {
				//if the user is already holding an item and they click again, then the drop item switch is activated
				dropsItem = true;
				droppedX = Screen.mse.x;
				droppedY = Screen.mse.y;
			}
		}
		
	}
	
	public void define() {
		//initializes all store buttons and statistical displays
		for(int i = 0; i < button.length; i++) {
			button[i] = new Rectangle((Screen.myWidth/2) - ((shopWidth*buttonSize)/2) - Screen.room.blockSize/2 + ((buttonSize+cellSpace)*i), Screen.myHeight - 82, buttonSize, buttonSize);
		}
		
		coins = new Rectangle(Screen.room.block[0][0].x, 1, Screen.room.worldWidth * Screen.room.blockSize/3,Screen.room.block[0][0].y-3);
		kingdomHealth = new Rectangle(Screen.room.block[0][0].x + Screen.room.worldWidth * Screen.room.blockSize/3, 1, Screen.room.worldWidth * Screen.room.blockSize/3, Screen.room.block[0][0].y-3);
		balloonsGenerated = new Rectangle(Screen.room.block[0][0].x + 2*Screen.room.worldWidth * Screen.room.blockSize/3 + 1, 1, Screen.room.worldWidth * Screen.room.blockSize/3, Screen.room.block[0][0].y-3);
		
	}
	
	public void draw(Graphics g) {
		for(int i = 0; i < button.length; i++) {
			if(button[i].contains(Screen.mse)) {
				//if mouse is hovered over a store button, it becomes transparent white
				g.setColor(new Color(255,255,255,150));
				g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
				g.drawImage(Screen.tileset_store[buttonID[i]], button[i].x + itemIn, button[i].y + itemIn, button[i].width - (itemIn*2),  button[i].height - (itemIn*2), null);
				//isTowerSelected = true;
				
			}
			else {
				//store button background is set to black and the appropriate image icon is drawn in the button (from the tileset_store array)
				g.setColor(new Color(0,0,0));
			    g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
			    g.drawImage(Screen.tileset_store[buttonID[i]], button[i].x + itemIn, button[i].y + itemIn, button[i].width - (itemIn*2),  button[i].height - (itemIn*2), null);
			    //isTowerSelected = false;
			}
			
			
		}
		
		if(holdsItem) {
			//just draws the image wherever the mouse goes
			g.drawImage(Screen.tileset_store[heldID], Screen.mse.x - ((button[0].width - (itemIn*2))/2) + itemIn, Screen.mse.y - ((button[0].width - (itemIn*2))/2) + itemIn, button[0].width - (itemIn*2),  button[0].height - (itemIn*2), null);
			if(0 <= heldID && heldID < 5)
				Screen.selectedTowerID = heldID;
		}
		if(dropsItem && Screen.coins - Towers.prices[heldID] >= 0) {
			//If a user drops an item and he can afford it
			for(int i = 0; i < Screen.room.block[0].length; i++) {
				for(int j = 0; j < Screen.room.block.length; j++) {
					if(Screen.room.block[j][i].contains(droppedX, droppedY) && Screen.room.block[j][i].groundID==Value.groundGrass && Screen.room.block[j][i].airID==-1 && !Screen.room.block[j][i].hasTower) {
						//The block where he dropped the item is found and checked if it is an available block
						Tower tempBlockTower;
						if(0 <= heldID && heldID < 3) {
							//initializes and adds the same block the user selected from the store to the arrayList in the towers class -- used for poltroon, ninja, and sniper
							tempBlockTower = new Tower(heldID, Screen.room.block[j][i].x, Screen.room.block[j][i].y, Towers.fps[heldID], Towers.ranges[heldID], Towers.damages[heldID], Towers.prices[heldID], Screen.room.block[j][i]);
							Towers.towerList.add(tempBlockTower);
							Screen.room.block[j][i].blockTower = tempBlockTower;
						}
						else if(heldID == 3) {
							//used for Freezer tower
							tempBlockTower = new Freezer(heldID, Screen.room.block[j][i].x, Screen.room.block[j][i].y, Towers.fps[heldID], Towers.ranges[heldID], Towers.damages[heldID], Towers.prices[heldID], Screen.room.block[j][i], 30);
							Towers.towerList.add(tempBlockTower);
							Screen.room.block[j][i].blockTower = tempBlockTower;
						}
						else if(heldID == 4) {
							//used for Bomber tower
							tempBlockTower = new Bomber(heldID, Screen.room.block[j][i].x, Screen.room.block[j][i].y, Towers.fps[heldID], Towers.ranges[heldID], Towers.damages[heldID], Towers.prices[heldID], Screen.room.block[j][i]);
							Towers.towerList.add(tempBlockTower);
							Screen.room.block[j][i].blockTower = tempBlockTower;
						}
						Screen.room.block[j][i].hasTower = true;
						towerInitialized = true;
						break;
					}
				}
				if(towerInitialized) {
					towerInitialized = false;
					holdsItem = false;
					break;
				}
			}
			
			dropsItem = false;
			
			//holds item and drops item are then set to false
		}
		
		//The displays for bananas (coins), kingdom health, and balloons generated are drawn
		g.setColor(new Color(235,195,52));
		g.fillRect(coins.x, coins.y, coins.width, coins.height);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 18));
		g.drawString("BANANAS: " + Screen.coins, coins.x + 50, coins.y + 25);
		
		g.setColor(Color.PINK);
		g.fillRect(kingdomHealth.x, kingdomHealth.y, kingdomHealth.width, kingdomHealth.height);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 18));
		g.drawString("Kingdom's Health: " + Screen.kingdomHealth, kingdomHealth.x + 20, kingdomHealth.y + 25);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(balloonsGenerated.x, balloonsGenerated.y, balloonsGenerated.width, balloonsGenerated.height);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 18));
		g.drawString("Balloons Generated: " + Balloons.balloonsGenerated, balloonsGenerated.x + 20, balloonsGenerated.y + 25);
		
		g.drawRect(coins.x-1, coins.y-1, coins.width+1, coins.height+1);
		g.drawRect(kingdomHealth.x-1, kingdomHealth.y-1, kingdomHealth.width+1, kingdomHealth.height+1);
		g.drawRect(balloonsGenerated.x-1, balloonsGenerated.y-1, balloonsGenerated.width+1, balloonsGenerated.height+1);
		
		
	}
}
