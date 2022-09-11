import java.awt.Color;
import java.awt.Graphics;

public class Freezer extends Tower {
	//tower that freezes/stuns balloons for around a second
	//The exact same as the parent tower class except for the attack method where it calls the freeze method on the closest/current balloon to it
	private int freezeFramesPS;

	public Freezer(int towerID, int x, int y, int fps, double range, int damage, int price, Block correspondingBlock, int freezeFramesPS) {
		super(towerID, x, y, fps, range, damage, price, correspondingBlock);
		this.freezeFramesPS = freezeFramesPS;
	}
	
	public void draw(Graphics g) {
		g.drawImage(Screen.tileset_store[towerID], this.x + Screen.store.itemIn, this.y + Screen.store.itemIn, Screen.store.button[0].width - (Screen.store.itemIn*2), Screen.store.button[0].height - (Screen.store.itemIn*2), null);
		if(drawArc)
			g.drawArc((int) (this.correspondingBlock.getCenterX() - this.range), (int) (this.correspondingBlock.getCenterY() - this.range), (int) (2*this.range), (int) (2*this.range), 0, 360);
	
		this.frames++;
		if(frames > fps) {
			if(attack()) {
				frames = 0;
				Color tempColor = g.getColor();
				g.setColor(Color.RED);
				g.drawRect(this.correspondingBlock.x, this.correspondingBlock.y, 52, 52);
				g.setColor(tempColor);
				if(targetBalloon != null)
					g.drawLine((int) this.correspondingBlock.getCenterX(), (int) this.correspondingBlock.getCenterY(), targetBalloonDrawX + 26, targetBalloonDrawY + 26);
			}
		}
	}
	
	public boolean attack() {
		int iterator = 0;
		double minimumTargetBalloonDistance = Integer.MAX_VALUE;
		
		if (targetBalloon != null && !targetBalloon.isAlive()) {
			targetBalloon = null;
		}

		if (targetBalloon != null
				&& pythagoreanDistanceToBalloon(targetBalloon.getX() + 26, targetBalloon.getY() + 26) < this.range) {
			this.targetBalloonDrawX = targetBalloon.getX();
			this.targetBalloonDrawY = targetBalloon.getY();
			targetBalloon.subtractHealth(this.damage);
			targetBalloon.freeze(this.freezeFramesPS);
			if (!targetBalloon.isAlive()) {
				targetBalloon = null;
			}
			return true;
		} else if (targetBalloon == null
				|| pythagoreanDistanceToBalloon(targetBalloon.getX(), targetBalloon.getY()) > this.range) {
			targetBalloon = null;
			while (iterator < Balloons.balloons.size()) {
				if (!Balloons.balloons.get(iterator).isFrozen() && Balloons.balloons.get(iterator).isAlive()
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
				this.targetBalloonDrawX = targetBalloon.getX();
				this.targetBalloonDrawY = targetBalloon.getY();
				targetBalloon.subtractHealth(this.damage);
				targetBalloon.freeze(this.freezeFramesPS);
				if (!targetBalloon.isAlive()) {
					targetBalloon = null;
				}
				return true;
			} 
		}

		return false;
	}
	
	public int getFreezeFramesPS() {
		return freezeFramesPS;
	}

	public void setFreezeFramesPS(int freezeFramesPS) {
		this.freezeFramesPS = freezeFramesPS;
	}
}
