package Tiles;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * a tile is any of the locations on the game world that the player interacts with
 * during game time there are several iterations of a tile
 * all interactable objects in the game are a tile existing on a location
 *@author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public abstract class Tile {

	private boolean moveable = false;
	private Image img;
	public Tile(String type){
	}
	/**
	 * Loads an image for drawing
	 * @param filenamefile to load
	 * @return Image to draw
	 */
	protected Image loadImage(String filename) { //TODO CHANGE THIS
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
	public void setImage(Image i){
		img = i;
	}

	public Image getImage() {
		return img;
	}

	/**
	 * Sets this Tiles moveability
	 * @param boolean the value to set moveable to
	 * */
	public void setMoveable(boolean b){
		this.moveable = b;
	}
	/**Returns whether this tile can be moved
	 *
	 * @return boolean moveable
	 * */
	public boolean isMoveable(){
		return moveable;
	}
}
