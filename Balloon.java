import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Balloon {
	
	private int radius;
	private int x;   //of top left corner
	private int y;   //of top left corner
	
	private int airID;   //corresponds to the order it is listed in the tileset_air image array
	
	private int blockID;   //the blockID of the block the balloon is currently "in"
	private Block correspondingBlock;   //the corresponding block of the balloon
	private Block destinationBlock;   //the block that the balloon is travelling to
	private ArrayList<Integer> haveBeenThere = new ArrayList<Integer>();  //an array of blocks that the balloon has already been to so it doesn't go there again
	
	private int originalHealth;   //same as health when first initialized, used to draw healthy, damaged, and critically damaged balloons respectively
	
	private int health;
	private int speed;
	private int reward;
	
	private int originalSpeed;   //used for resetting the balloon to original speed after frozen
	private int freezeFrames = 0;   //the amount of frames the balloon is currently on with regards to unfreezing
	private int freezeFramesPS;    //the amount of frames the balloon needs to reach in order to unfreeze and return to normal speed
	
	private boolean frozen = false;
	
	private boolean alive;
	
	public Balloon(int airID, int blockID) {
		super();
		define(airID, blockID);
		
	}
	
	public void define(int airID, int blockID) {
		this.airID = airID;
		if(airID == 0) {
			//red balloon
			this.health = 200;
			this.originalHealth = 200;
			this.speed = 3;
			this.originalSpeed = speed;
			this.reward = 50;
		}
		else if(airID == 1) {
			//green balloon
			this.health = 1200;
			this.originalHealth = 1200;
			this.speed = 6;
			this.originalSpeed = speed;
			this.reward = 125;
		}
		else {
			//blue balloon
			this.health = 2400;
			this.originalHealth = 2400;
			this.speed = 9;
			this.originalSpeed = speed;
			this.reward = 250;
		}
		
		this.radius = 13;
		this.blockID = blockID;
		this.correspondingBlock = Screen.room.findBlock(this.blockID);   //initializes corresponding block appropriately
		this.x = correspondingBlock.x;
		this.y = correspondingBlock.y;
		this.haveBeenThere.add(this.blockID);
		this.alive = true;
	}
	
	public void subtractHealth(int healthLost) {
		this.health-=healthLost;
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		this.checkStatus();   //sets the alive boolean variable before drawing the balloon
		if(this.alive && this.frozen && this.freezeFrames < this.freezeFramesPS) {
			//balloon while frozen
			g.drawImage(Screen.tileset_air[airID + 9], x, y, 52, 52, null);  //will draw a semi-transparent balloon to make it look frozen
			this.speed = 0;   //sets the speed to zero as its frozen
			freezeFrames++;   //starts incrementing the frames that go toward the balloon's freeze time
			return;
		}
		
		this.speed = this.originalSpeed;
		this.frozen = false;
		//System.out.println("Unfrozen with speed " + this.speed);
		
		if (this.alive && this.health > this.originalHealth * 2 / 3) {
			g.drawImage(Screen.tileset_air[airID], x, y, 52, 52, null);  //draws a "healthy" balloon
		} else if (this.alive && this.health > this.originalHealth * 1 / 3) {
			g.drawImage(Screen.tileset_air[airID + 3], x, y, 52, 52, null);   //draws a mildly damaged balloon
		} else if (this.alive) {
			g.drawImage(Screen.tileset_air[airID + 6], x, y, 52, 52, null);   //draws a critically damaged balloon
		}
		
		move();
		
	}

	public void move() {
		// TODO Auto-generated method stub
		
		if(Screen.room.findBlock(this.blockID + 1) != null && 
				Screen.room.findBlock(this.blockID + 1).groundID == Value.groundRoad && 
				this.haveBeenThere.indexOf(Screen.room.findBlock(this.blockID + 1).id) == -1) {
			
			//check Right
			//checks if a block to the right exists (to not get an error when checking for its ID)
			//then checks if said block is a road block
			//then checks if the balloon has not already been there
			//if all conditions are met, the balloon will have its x-position incremented, moving it to the right by its speed
			//same algorithm is implemented for every direction
			
			destinationBlock = Screen.room.findBlock(this.blockID + 1);
			this.x += speed;
			if(this.x > destinationBlock.x) {
				this.correspondingBlock = destinationBlock;
				this.x = correspondingBlock.x;  //Makes sure the balloon doesn't overshoot a block when it changes direction
				this.y = correspondingBlock.y;
				this.blockID = correspondingBlock.id;
				this.haveBeenThere.add(this.blockID);
			}
			
		}
		else if(Screen.room.findBlock(this.blockID - 1) != null && 
				Screen.room.findBlock(this.blockID - 1).groundID == Value.groundRoad && 
				this.haveBeenThere.indexOf(Screen.room.findBlock(this.blockID - 1).id) == -1) {
			
			destinationBlock = Screen.room.findBlock(this.blockID - 1);
			this.x -= speed;
			if(this.x < destinationBlock.x) {
				this.correspondingBlock = destinationBlock;
				this.x = correspondingBlock.x;
				this.y = correspondingBlock.y;
				this.blockID = correspondingBlock.id;
				this.haveBeenThere.add(this.blockID);
			}
		}
		else if(Screen.room.findBlock(this.blockID + Screen.room.block[0].length) != null && 
				Screen.room.findBlock(this.blockID + Screen.room.block[0].length).groundID == Value.groundRoad && 
				this.haveBeenThere.indexOf(Screen.room.findBlock(this.blockID + Screen.room.block[0].length).id) == -1) {
			
			destinationBlock = Screen.room.findBlock(this.blockID + Screen.room.block[0].length);
			this.y += speed;
			if(this.y > destinationBlock.y) {
				this.correspondingBlock = destinationBlock;
				this.x = correspondingBlock.x;
				this.y = correspondingBlock.y;
				this.blockID = correspondingBlock.id;
				this.haveBeenThere.add(this.blockID);
			}
		}
		else if(Screen.room.findBlock(this.blockID - Screen.room.block[0].length) != null && 
				Screen.room.findBlock(this.blockID - Screen.room.block[0].length).groundID==Value.groundRoad && 
				this.haveBeenThere.indexOf(Screen.room.findBlock(this.blockID - Screen.room.block[0].length).id) == -1) {
			
			destinationBlock = Screen.room.findBlock(this.blockID - Screen.room.block[0].length);
			this.y -= speed;
			if(this.y < destinationBlock.y) {
				this.correspondingBlock = destinationBlock;
				this.x = correspondingBlock.x;
				this.y = correspondingBlock.y;
				this.blockID = correspondingBlock.id;
				this.haveBeenThere.add(this.blockID);
			}
		}
	}
	
	public void freeze(int freezeFramesPS) {
		//when freezer attacks, it calls this freeze method. The clock starts ticking and frozen is set to true.
		this.freezeFrames = 0;
		this.freezeFramesPS = freezeFramesPS;
		this.frozen = true;
	}
	
	public void updateHaveBeenThere(int newBlockID) {
		haveBeenThere.add(newBlockID);
	}
	
	public void checkStatus() {
		//a method that is constantly called to check whether or not the balloon is alive
		if(this.health<=0) {
			this.setAlive(false);
		}
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getAirID() {
		return airID;
	}

	public void setAirID(int airID) {
		this.airID = airID;
	}

	public int getBlockID() {
		return blockID;
	}

	public void setBlockID(int blockID) {
		this.blockID = blockID;
	}

	public Block getCorrespondingBlock() {
		return correspondingBlock;
	}

	public void setCorrespondingBlock(Block correspondingBlock) {
		this.correspondingBlock = correspondingBlock;
	}

	public Block getDestinationBlock() {
		return destinationBlock;
	}

	public void setDestinationBlock(Block destinationBlock) {
		this.destinationBlock = destinationBlock;
	}

	public ArrayList<Integer> getHaveBeenThere() {
		return haveBeenThere;
	}

	public void setHaveBeenThere(ArrayList<Integer> haveBeenThere) {
		this.haveBeenThere = haveBeenThere;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public void setAlive(boolean isAlive) {
		this.alive = isAlive;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getOriginalHealth() {
		return originalHealth;
	}

	public void setOriginalHealth(int originalHealth) {
		this.originalHealth = originalHealth;
	}

	public int getOriginalSpeed() {
		return originalSpeed;
	}

	public void setOriginalSpeed(int originalSpeed) {
		this.originalSpeed = originalSpeed;
	}

	public int getFreezeFrames() {
		return freezeFrames;
	}

	public void setFreezeFrames(int freezeFrames) {
		this.freezeFrames = freezeFrames;
	}

	public int getFreezeFramesPS() {
		return freezeFramesPS;
	}

	public void setFreezeFramesPS(int freezeFramesPS) {
		this.freezeFramesPS = freezeFramesPS;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	

}
