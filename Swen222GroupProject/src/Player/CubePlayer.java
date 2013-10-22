package Player;

import java.awt.Image;
import java.util.List;

import Game.Location;
import GameParts.*;

/**
 * The Class CubePlayer. Represents a player on the board in the shape of a Cube.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class CubePlayer extends Player {

	/** The img file nme. */
	final String imgFileNme = "src/images/squareYELLOW.png";
	/**
	 * Constructor for new CubePlayer.
	 *
	 * @param location the location
	 * @param id the id
	 * @param images the images
	 */
	public CubePlayer(Location location, int id,List<Image> images) {
		super(location,id,images);
		setImage(images.get(0));
		setName("Cube");
		this.getBackPack().addItem( new ShapeItem("+"));
		setShape(1);
	}

	/**
	 * Constructor for changing a players shape.
	 *
	 * @param p the player
	 * @param i the item
	 * @param id the uid
	 */
	public CubePlayer(Player p, GameObject i, int id) {
		super(p,i, id);
		setName("Cube");
		setShape(1);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "s";
	}

}
