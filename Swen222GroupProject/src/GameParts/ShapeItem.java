package GameParts;

import java.awt.Image;
import Game.Location;

/**
 * The Class ShapeItem. The item pickup of a shape that gets added to the players inventory
 * that they can then change into
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class ShapeItem extends GameObject{

	/** The location. */
	Location location;

	/** The img. */
	private Image img;

	/** The type. */
	private String type;

	/** The img sp. */
	private String imgSp = "src/images/sphereYELLOWpickup.png";

	/** The img sq. */
	private String imgSq = "src/images/squareYELLOWpickup.png";

	/** The img pyra. */
	private String imgPyra = "src/images/pyramidYELLOWpickup.png";

	/**
	 * Instantiates a new shape item.
	 *
	 * @param type the type
	 */
	public ShapeItem(String type) {

		if(type.equalsIgnoreCase("+")){
			img = loadImage(imgSq);
		}
		else if(type.equalsIgnoreCase("o")){
			img = loadImage(imgSp);
		}
		else if(type.equalsIgnoreCase("^")){
			img = loadImage(imgPyra);
		}

		this.type = type;
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getLocation()
	 */
	@Override
	public Location getLocation() {

		return location;
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#setLocation(Game.Location)
	 */
	@Override
	public void setLocation(Location l) {
		location = l;

	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/* (non-Javadoc)
	 * @see GameParts.Item#getImage()
	 */
	@Override
	public Image getImage() {
		return img;
	}

}
