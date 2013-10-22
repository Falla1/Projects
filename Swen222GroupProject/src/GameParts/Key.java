package GameParts;

import java.awt.Image;

import Game.Location;

/**
 * The Class Key. Opens invisible door when collected.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Key extends GameObject{

	/** The remove. */
	private Location remove;

	/** The img. */
	private Image img;

	/**
	 * Instantiates a new key.
	 */
	public Key (){
		img = loadImage("src/images/keyGREEN.png");
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getLocation()
	 */
	public final Location getLocation() {
		return remove;
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#setLocation(Game.Location)
	 */
	@Override
	public final void setLocation(final Location l) {
		remove = l;
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getType()
	 */
	@Override
	public final String getType() {
		return "Key";
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getImage()
	 */
	@Override
	public final Image getImage() {
		return img;
	}
}
