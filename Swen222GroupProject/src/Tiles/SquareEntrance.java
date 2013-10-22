package Tiles;
/**
 * representation of a  square shaped door for a square player to move through
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class SquareEntrance extends Tile{

	/**
	 * Constructor for Square Entrance, argument specifies which direction to
	 * load
	 *
	 * @param String
	 *            left or right depending on desired rotation
	 * */
	public SquareEntrance(String dir){
		super("s");
		if (dir.equals("left")) {
			setImage(loadImage("src/images/squareEntryLeftPURP.png"));
		}
		else {
			setImage(loadImage("src/images/squareEntryRightPURP.png"));
		}
	}
}
