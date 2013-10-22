package Player;

import java.awt.Image;
import java.util.List;

import Game.Location;
import GameParts.*;
/**
 * The Class SpherePlayer. Represents a player on the board in the shape of a Sphere.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class SpherePlayer extends Player {

	/** The img file nme. */
	final String imgFileNme = "src/images/sphereYELLOW.png";

	/**
	 * Constructor for new SpherePlayer.
	 *
	 * @param location the location
	 * @param id the id
	 * @param images the images
	 */

	public SpherePlayer(Location location, int id,List<Image> images) {
		super(location,id,images);
		setImage(images.get(2));
		setName("Sphere");
		this.getBackPack().addItem( new ShapeItem("o"));
		setShape(3);
	}
	
	/**
	 * Constructor for changing a players shape.
	 *
	 * @param p the p
	 * @param i the i
	 * @param id the id
	 */
	public SpherePlayer(Player p, GameObject i,int id) {
		super(p,i,id);
		setName("Sphere");
		setShape(3);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "c";
	}


}
