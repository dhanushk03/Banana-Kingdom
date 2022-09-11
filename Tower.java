import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Tower {
	
	//the blueprint for every single tower
	
	protected int towerID;  //the type of tower it is (corresponds to tileset_store)
	
	protected Block correspondingBlock;   //the block the tower is on
	
	protected int x;   //top left x
	protected int y;   //top left y
	
	protected int fps;   //attack fps (used in the same way as in other classes)
	protected int frames;   //frames contributing to attack
	
	protected double range;   //circular range in which the tower can attack
	protected int damage;   //the amount of damage the tower does with each attack
	
	protected Balloon targetBalloon = null;  //the balloon the tower is locked in on & attacking
	protected int targetBalloonDrawX = 0;  //used for drawing the line from the tower to the target balloon during an attack
	protected int targetBalloonDrawY = 0;
	
	protected int price;   //how much is subtracted from the coins variable when the tower is initialized
	
	public boolean drawArc = false;   //whether or not its arc is being drawn (as dictated by its block)
	
	public Tower(int towerID, int x, int y, int fps, double range, int damage, int price, Block correspondingBlock) {
		//System.out.println("Tower created");
		this.towerID = towerID;
		//System.out.println("Tower initialized");
		this.correspondingBlock = correspondingBlock;
		//System.out.println("Tower's corresponding block initialized");
		this.x = x;
		this.y = y;
		//System.out.println("Tower's x and y initialized");
		this.fps = fps;
		this.frames = this.fps + 1;
		//System.out.println("Tower's fps initialized");
		this.range = range*Screen.room.blockSize;
		//System.out.println("Tower's range initialized");
		this.damage = damage;
		//System.out.println("Tower's damage initialized");
		this.price = price;
		Screen.coins-=this.price;
	}
	
	public Tower() {
		this.x = Screen.room.block[0][0].x;
		this.y = Screen.room.block[0][0].y;
	}
	
	public void draw(Graphics g) {
		g.drawImage(Screen.tileset_store[towerID], this.x + Screen.store.itemIn, this.y + Screen.store.itemIn, Screen.store.button[0].width - (Screen.store.itemIn*2), Screen.store.button[0].height - (Screen.store.itemIn*2), null);
		if(drawArc)
			g.drawArc((int) (this.correspondingBlock.getCenterX() - this.range), (int) (this.correspondingBlock.getCenterY() - this.range), (int) (2*this.range), (int) (2*this.range), 0, 360);
		
		
		//Every time the draw method is called, a tower's frames are incremented
		this.frames++;
		//When the frames exceed the fps, the tower tries to attack
		if(frames > fps) {
			//Only if the attack is successful will the tower restart its attack clock
			if(attack()) {
				//the attack is successful, so the attack clock is restarted
				frames = 0;
				Color tempColor = g.getColor();   
				g.setColor(Color.RED);    //a red rectangle around the tower will indicate that the tower has fired
				g.drawRect(this.correspondingBlock.x, this.correspondingBlock.y, 52, 52);
				g.setColor(tempColor);
				if(targetBalloon != null) {
					//A line will be drawn to the balloon that the tower attacked
					g.drawLine((int) this.correspondingBlock.getCenterX(), (int) this.correspondingBlock.getCenterY(), targetBalloonDrawX + 26, targetBalloonDrawY + 26);
				}
			}
		}
	}
	
	public boolean attack() {
		
		//The tower's attack method
		int iterator = 0;
		double minimumTargetBalloonDistance = Integer.MAX_VALUE;
		

		if(targetBalloon != null && !targetBalloon.isAlive()) {
			//If its target balloon is dead, then set the target balloon to be null
			targetBalloon = null;
		}
		
		if(targetBalloon != null && pythagoreanDistanceToBalloon(targetBalloon.getX()+26, targetBalloon.getY()+26) < this.range) {
			//Current balloon is in range and ready to fire at
			this.targetBalloonDrawX = targetBalloon.getX();
			this.targetBalloonDrawY = targetBalloon.getY();
			//Tower attacks the target balloon by subtracting its damage from the balloon's health
			targetBalloon.subtractHealth(this.damage);
			if(!targetBalloon.isAlive()) {
				//if the target balloon is no longer alive, it is set to null
				targetBalloon = null;
				//return false;
			}
			return true;
		}
		else if(targetBalloon == null ||  pythagoreanDistanceToBalloon(targetBalloon.getX(), targetBalloon.getY()) > this.range) {
			//Target balloon was null or out of range, so a new one is being found by iterating through the balloons arrayList and picking the closest available one
			targetBalloon = null;
			while(iterator < Balloons.balloons.size()) {
				if(Balloons.balloons.get(iterator).isAlive() && 
						pythagoreanDistanceToBalloon(Balloons.balloons.get(iterator).getX()+26, Balloons.balloons.get(iterator).getY()+26) < this.range && 
						pythagoreanDistanceToBalloon(Balloons.balloons.get(iterator).getX()+26, Balloons.balloons.get(iterator).getY()+26) < minimumTargetBalloonDistance) {
					
					targetBalloon = Balloons.balloons.get(iterator);
					minimumTargetBalloonDistance = pythagoreanDistanceToBalloon(Balloons.balloons.get(iterator).getX()+26, Balloons.balloons.get(iterator).getY()+26);
				}
				iterator++;
			}
			iterator = 0;
			if (targetBalloon != null) {
				//Found a target balloon after it was null or out of range
				this.targetBalloonDrawX = targetBalloon.getX();
				this.targetBalloonDrawY = targetBalloon.getY();
				targetBalloon.subtractHealth(this.damage);
				if (!targetBalloon.isAlive()) {
					targetBalloon = null;
				}
				return true;
			}
		}
		
		
		return false;
		
	}
	
	public double pythagoreanDistanceToBalloon(int balloonX, int balloonY) {
		//calculates how many pixels away the center of the tower is from the center of the balloon it wants to target
		int xDist = (int) this.correspondingBlock.getCenterX() - balloonX;
		int yDist = (int) this.correspondingBlock.getCenterY() - balloonY;
		return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
	}

	public int getTowerID() {
		return towerID;
	}

	public void setTowerID(int towerID) {
		this.towerID = towerID;
	}

	public Block getCorrespondingBlock() {
		return correspondingBlock;
	}

	public void setCorrespondingBlock(Block correspondingBlock) {
		this.correspondingBlock = correspondingBlock;
	}

	public Balloon getTargetBalloon() {
		return targetBalloon;
	}

	public void setTargetBalloon(Balloon targetBalloon) {
		this.targetBalloon = targetBalloon;
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

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public int getFrames() {
		return frames;
	}

	public void setFrames(int frames) {
		this.frames = frames;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getTargetBalloonDrawX() {
		return targetBalloonDrawX;
	}

	public void setTargetBalloonDrawX(int targetBalloonDrawX) {
		this.targetBalloonDrawX = targetBalloonDrawX;
	}

	public int getTargetBalloonDrawY() {
		return targetBalloonDrawY;
	}

	public void setTargetBalloonDrawY(int targetBalloonDrawY) {
		this.targetBalloonDrawY = targetBalloonDrawY;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isDrawArc() {
		return drawArc;
	}

	public void setDrawArc(boolean drawArc) {
		this.drawArc = drawArc;
	}
	
	
	
	

}
