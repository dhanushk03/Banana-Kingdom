import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Room {
	
	//Will represent every single block on the ground
	public int worldHeight = 9;   //the amount of blocks making up the height
	public int worldWidth = 14;   //the amount of blocks making up the width
	public int blockSize = 52;    //the pixel by pixel size of each block
	
	public Block[][] block;  //the main variable -- a 2D array of blocks
	
	public Room() {
		define();
	}
	
	public void define() {
		//creates an array of blocks with dimensions of worldHeight * worldWidth (9 x 14)
		block = new Block[worldHeight][worldWidth];
		for(int y = 0; y < worldHeight; y++) {
			for(int x = 0; x < worldWidth; x++) {
				//constructs blocks 
				//determines x coordinate by adding the equivalent of an individual block size to the x coordinate every time
				//initial x coordinate is determined by halving the difference between the frame and the room width (worldWidth * blockSize)
				//determines y coordinate by adding the blocksize in increments to it for each new row
				block[y][x] = new Block((Screen.myWidth/2) - (worldWidth*blockSize/2) +  x*blockSize, y*blockSize + Screen.myHeight/15, blockSize, blockSize, y*worldWidth + x, Value.groundGrass, Value.airAir);
			}
		}
	}
	
	public Block findBlock(int blockID) {
		//algorithm to pick the block from the block array that the balloon is currently "in".
		//[0][0] has blockID 0
		//[0][13] has blockID 13
		//[2][5] has blockID 2*14 + 5, or 75; just reverse that to get the individual block
		if(0 <= blockID && blockID < worldHeight * worldWidth) {
			return Screen.room.block[blockID / worldWidth][blockID % worldWidth];
		}
		else
			return null;
	}
	
	public void draw(Graphics g) {
		//draws every block in the 2D block array
		for(int y = 0; y < block.length; y++) {
			for(int x = 0; x < block[0].length; x++) {
				block[y][x].draw(g);
			}
		}
	}

}
