package GameParts;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



/**
 * The Class GameObject. This just contains the image drawing method for all other item types to use
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public abstract class GameObject implements Item {

	/**
	 * Loads an image for drawing.
	 *
	 * @param filename file to load
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

}
