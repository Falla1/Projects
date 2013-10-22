package UserInterface;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import Game.Game;
/**
 * Final screen when the game is over. Shows player score.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class WinScreen extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4075770274648593817L;
	private Image back;

	public WinScreen(final Game game,int uid){
		back = loadImage("src/images/winScreen.png");
		this.setFocusable(false);
	}
	private Image loadImage(String filename) { //TODO UPDATE loadImage methods to allow for .jar
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
	
	@Override
	public synchronized void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(back, 0,0, null, null);
	}

}
