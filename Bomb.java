import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Bomb extends Tower{
	
	//basically the weapon that bomber throws that causes splash damage to all balloons inside its radius
	
	public Bomb(int towerID, int x, int y, int fps, double range, int damage, int price) {
		super(towerID, x, y, fps, range, damage, price, null);
	}
	
	public void draw(Graphics g) {
		g.drawImage(Screen.tileset_store[towerID], this.x, this.y, Screen.store.button[0].width, Screen.store.button[0].height, null);
		g.drawArc((int) (this.x+26 - this.range), (int) (this.y+26 - this.range), (int) (2*this.range), (int) (2*this.range), 0, 360);
		
		this.frames++;
		if(this.frames > this.fps) {
			if(attack()) {
				frames = 0;
				g.drawImage(new ImageIcon("res/bombExploded.png").getImage(), this.x, this.y, Screen.store.button[0].width, Screen.store.button[0].height, null);   //draws an exploded bomb when it explodes
				Towers.towerList.remove(Towers.towerList.indexOf(this));
				Towers.iterator--;
			}
		}
	}
	
	public boolean attack() {
		
		int iterator = 0;
		while (iterator < Balloons.balloons.size()) {
			if (Balloons.balloons.get(iterator) != null && Balloons.balloons.get(iterator).isAlive()
					&& pythagoreanDistanceToBalloon(Balloons.balloons.get(iterator).getX() + 26,
							Balloons.balloons.get(iterator).getY() + 26) < this.range) {
				
				//attacks differently than other towers in that it damages ALL balloons within its range (not just one)
				Balloons.balloons.get(iterator).subtractHealth(this.damage);
			}
			iterator++;
		}
		return true;
	}
	
	public double pythagoreanDistanceToBalloon(int balloonX, int balloonY) {
		int xDist = (int) this.x + 26 - balloonX;
		int yDist = (int) this.y + 26 - balloonY;
		return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
	}
	

}
