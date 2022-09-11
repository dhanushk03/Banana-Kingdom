import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import javax.swing.ImageIcon;

public class Block extends Rectangle{
	public int id;   //individual ID
	public int groundID;   //grass block or road block
	public int airID;   //does it have a soil layer on the top that prevents a tower from being placed on it?
	public boolean hasTower = false;   //does it have a tower on top of it right now?
	public boolean showingTowerArc = false;   //is the tower on top of it showing its range?
	public Tower blockTower;   //the tower that is on the block (if any)
	
	public Block(int x, int y, int width, int height, int id, int groundID, int airID) {
		setBounds(x, y, width, height);   //setting the rectangular bounds of the block
		this.id = id;
		this.groundID = groundID;
		this.airID = airID;
	}
	
	public void draw(Graphics g) {
		g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);   //draws the appropriate image
		
		
		if(airID != Value.airAir) {
			g.drawImage(Screen.tileset_obstacles[airID], x, y, width, height, null);   //draws any air image that goes on top of the ground image
		}
	
		if(Store.holdsItem && groundID == 0 && airID == -1 && !hasTower) {
			//if a tower is clicked and the block is available it is circled in white
			g.setColor(new Color(255,255,255));
			g.drawRect(x, y, width, height);
		}
	}
	
	public void click(int mouseButton) {
		//If its clicked and has a tower it will display/remove the range arc
		if(mouseButton==1) {
			if(blockTower!=null) {
				if(!showingTowerArc) {
					blockTower.drawArc = true;
					showingTowerArc = true;
				}
				else {
					blockTower.drawArc = false;
					showingTowerArc = false;
				}
			}
			
		}
	}
}
 