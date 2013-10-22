package GameParts;

import java.awt.Image;

import Game.Location;

/**
 * The Class Coin. Represent a game coin, increases score.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Coin extends GameObject {

	/** The location. */
	private Location location;

	/** The img. */
	private Image img;

	/** The value. */
	private int value=0;

	/** The type. */
	private String type = "Coin";

	/**
	 * Instantiates a new coin.
	 */
	public Coin() {
		img = loadImage("src/images/coinGold.png");
		setValue(100);
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
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
		this.location = l;
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getType()
	 */
	@Override
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
