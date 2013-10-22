package Player;

import java.awt.Image;
import java.util.List;
import Game.Location;
import GameParts.*;

/**
 * The Class PyramidPlayer. Represents a player on the board in the shape of a Pyramid.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class PyramidPlayer extends Player {

	/** The img file nme. */
	final String imgFileNme = "src/images/pyramidYELLOW.png";

	/**
	 * Constructor for new PyramidPlayer.
	 *
	 * @param location the location
	 * @param id the id
	 * @param images the images
	 */
	public PyramidPlayer(Location location, int id,List<Image> images) {
		super(location,id,images);
		setImage(images.get(1));
		setName("Pyramid");
		this.getBackPack().addItem( new ShapeItem("^"));
		setShape(2);
	}

	/**
	 * Constructor for changing a players shape.
	 *
	 * @param p the p
	 * @param i the i
	 * @param id the id
	 */
	public PyramidPlayer(Player p, GameObject i,int id) {
		super(p,i,id);
		setName("Pyramid");
		setShape(2);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "p";
	}


}
