import java.awt.Color;
import java.awt.Graphics;

public class Bomber extends Tower {
	//splash damage tower that throws a bomb that damages every balloon in its bombRadius
	//The exact same as the parent class except for the attack method
	
	public Bomber(int towerID, int x, int y, int fps, double range, int damage, int price, Block correspondingBlock) {
		super(towerID, x, y, fps, range, damage, price, correspondingBlock);
	}
	
	public void draw(Graphics g) {
		g.drawImage(Screen.tileset_store[towerID], this.x + Screen.store.itemIn, this.y + Screen.store.itemIn, Screen.store.button[0].width - (Screen.store.itemIn*2), Screen.store.button[0].height - (Screen.store.itemIn*2), null);
		if(drawArc)
			g.drawArc((int) (this.correspondingBlock.getCenterX() - this.range), (int) (this.correspondingBlock.getCenterY() - this.range), (int) (2*this.range), (int) (2*this.range), 0, 360);
		//g.drawLine((int) this.correspondingBlock.getCenterX(), (int) this.correspondingBlock.getCenterY(), (int) this.correspondingBlock.getCenterX(), (int) (this.correspondingBlock.getCenterY() + range));

		this.frames++;

		if(frames > fps) {
			if(attack()) {
				frames = 0;
				Color tempColor = g.getColor();
				g.setColor(Color.RED);
				g.drawRect(this.correspondingBlock.x, this.correspondingBlock.y, 52, 52);
				g.setColor(tempColor);
			}
		}
	}
	
	public boolean attack() {
		// System.out.println("Tower attack method called");
		int iterator = 0;
		double minimumTargetBalloonDistance = Integer.MAX_VALUE;
		
		if (targetBalloon != null && !targetBalloon.isAlive()) {
			targetBalloon = null;
		}

		if (targetBalloon != null
				&& pythagoreanDistanceToBalloon(targetBalloon.getX() + 26, targetBalloon.getY() + 26) < this.range) {
			this.targetBalloonDrawX = targetBalloon.getX();
			this.targetBalloonDrawY = targetBalloon.getY();
			
			//When the bomber locks on a balloon it will throw a bomb in the balloon's direction. A new bomb is created and sent to the balloon's coordinates
			//Although the bomb is supposed to be a weapon, it is added to the towerList and is treated like a tower that just attacks a little differently
			
			Towers.towerList.add(new Bomb(6, targetBalloonDrawX, targetBalloonDrawY, 50, 1.25, damage, 0));
			
			if (!targetBalloon.isAlive()) {
				targetBalloon = null;
				//return false;
			}
			
			return true;
		} else if (targetBalloon == null
				||  pythagoreanDistanceToBalloon(targetBalloon.getX(), targetBalloon.getY()) > this.range) {
			
			targetBalloon = null;
			//System.out.println("Bomber: Target balloon was equal to null, or target balloon was out of range");
			while (iterator < Balloons.balloons.size()) {
				if (Balloons.balloons.get(iterator).isAlive()
						&& pythagoreanDistanceToBalloon(Balloons.balloons.get(iterator).getX() + 26,
								Balloons.balloons.get(iterator).getY() + 26) < this.range
						&& pythagoreanDistanceToBalloon(Balloons.balloons.get(iterator).getX() + 26,
								Balloons.balloons.get(iterator).getY() + 26) < minimumTargetBalloonDistance) {

					targetBalloon = Balloons.balloons.get(iterator);
					minimumTargetBalloonDistance = pythagoreanDistanceToBalloon(
							Balloons.balloons.get(iterator).getX() + 26, Balloons.balloons.get(iterator).getY() + 26);
				}
				iterator++;
			}
			if (targetBalloon != null) {
				//System.out.println("Bomber found another target balloon");
				this.targetBalloonDrawX = targetBalloon.getX();
				this.targetBalloonDrawY = targetBalloon.getY();
				Towers.towerList.add(new Bomb(6, targetBalloonDrawX, targetBalloonDrawY, 50, 2, damage, 0));
				if (!targetBalloon.isAlive()) {
					targetBalloon = null;
					//return false;
				}
				return true;
			} 
		}

		return false;
	}
	
	//need to create instance variable called bomb
	//need to release bomb during attack
	//need to then call bomb's explode method when want it to explode
	//when it explodes need to draw a different image
	//when it explodes it has to get all balloons with pythagorean distance less than the bombRadius
	
	
	
}
