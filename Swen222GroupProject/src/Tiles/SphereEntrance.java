package Tiles;
/**
 * representation of a  sphere shaped door for a sphere player to move through
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class SphereEntrance extends Tile{
	/**
	 * Constructor for Sphere Entrance, argument specifies which direction to
	 * load
	 *
	 * @param String
	 *            left or right depending on desired rotation
	 * */
	public SphereEntrance(String dir){
		super("c");
		if (dir.equals("s")) {
			setImage(loadImage("src/images/shpereEntryRightPURP.png"));
		}

		else {
			setImage(loadImage("src/images/shpereEntryLeftPURP.png"));
		}
	}
}
