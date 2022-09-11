
import java.io.*;
import java.util.*;

public class Save {
	//loads the maps in from the textfiles
	//assigns each block its groundID and airID
	public void loadSave(File loadPath) {
		try {
			Scanner loadScanner = new Scanner(loadPath);
			
			while(loadScanner.hasNext()) {
				for(int y = 0; y < Screen.room.block.length; y++) {
					for(int x = 0; x < Screen.room.block[0].length; x++) {
						//first section is entirely groundIDs
						Screen.room.block[y][x].groundID = loadScanner.nextInt();
					}
				}
				
				for(int y = 0; y < Screen.room.block.length; y++) {
					for(int x = 0; x < Screen.room.block[0].length; x++) {
						//second section is entirely airIDs
						Screen.room.block[y][x].airID = loadScanner.nextInt();
					}
				}
			}
			
			loadScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
