package Tiles;
/**
 * representation of a  pyramid shaped door for a pyramid player to move through
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class TriangleEntrance extends Tile {


	/**
	 * Constructor for Triangle Entrance, argument specifies which direction to
	 * load
	 *
	 * @param String
	 *            left or right depending on desired rotation
	 * */

	public TriangleEntrance(String dir) {
		super("p");
		if (dir.equals("p")) {
			setImage(loadImage("src/images/triEntryLeftPURP.png"));
		}

		else {
			setImage(loadImage("src/images/triEntryRightPURP.png"));
		}
	}

}
