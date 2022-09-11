
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class KeyHandler implements MouseMotionListener, MouseListener{
	
	//Handles all the mouse presses and mouse moves

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//checks to see if a tower has been selected in the store, or an existing tower has been selected on a block
		Screen.store.click(e.getButton());
		for(int i = 0; i < Screen.room.block.length; i++) {
			for(int j = 0; j < Screen.room.block[0].length; j++) {
				if(Screen.room.block[i][j].contains(Screen.mse))
					Screen.room.block[i][j].click(e.getButton());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//sets the screen's mouse coordinates relative to the frame
		Screen.mse = new Point((e.getX()) + ((Frame.size.width - Screen.myWidth)/2), (e.getY() + ((Frame.size.height - (Screen.myHeight)) - (Frame.size.width - Screen.myWidth)/2)));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//sets the screen's mouse coordinates relative to the frame
		Screen.mse = new Point((e.getX()) - ((Frame.size.width - Screen.myWidth)/2), (e.getY() - ((Frame.size.height - (Screen.myHeight)) - (Frame.size.width - Screen.myWidth)/2)));
	}

	
	
}
