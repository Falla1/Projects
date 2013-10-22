package GameParts;

import java.awt.Image;

import Game.Location;

/**
 * The Interface Item. ShapeItem and GameObjects implement this interface
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public interface Item {

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation();

	/**
	 * Sets the location.
	 *
	 * @param l the new location
	 */
	public void setLocation(Location l);

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType();

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public Image getImage();


}
