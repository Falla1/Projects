package GameParts;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Game.Location;

/**
 * chests contains one magical item only that the player can pick up.
 *
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Chest extends GameObject implements Containers{

	/** The location. */
	private Location location;

	/** Theimage when container has items. */
	private Image imgFill;

	/** The image when container is empty */
	private Image imgEmpty;

	/** The inventory. */
	private List<GameObject> inventory;

	/** If the chest has been opened. */
	private boolean opened;

	/** The type. */
	private String type;

	/** The open location. */
	private Location openLocation;

	/**
	 * Instantiates a new chest.
	 *
	 * @param dir the dir
	 */
	public Chest(String dir) {
		if (dir.equals("@")) {
			imgFill = loadImage("src/images/chestRight.png");
			imgEmpty = loadImage("src/images/chestRightCLOSED.png");
			inventory = new ArrayList<GameObject>(Arrays.asList(new Coin(), new Coin(), new Coin())); //new Coin();

		} else if (dir.equals("#")) {
			imgFill = loadImage("src/images/chestLeft.png");
			imgEmpty = loadImage("src/images/chestLeftCLOSED.png");
			inventory = new ArrayList<GameObject>(Arrays.asList(new Coin(), new ShapeItem("^"), new Coin()));
		}
		else if (dir.equals("&")) {
			imgFill = loadImage("src/images/chestLeft.png");
			imgEmpty = loadImage("src/images/chestLeftCLOSED.png");
			inventory = new ArrayList<GameObject>(Arrays.asList(new ShapeItem("o"), new Coin(), new Coin()));
		}
		else if (dir.equals("*")) {
			imgFill = loadImage("src/images/chestLeft.png");
			imgEmpty = loadImage("src/images/chestLeftCLOSED.png");
			inventory = new ArrayList<GameObject>(Arrays.asList(new Coin(), new Coin(), new ShapeItem("+")));
		}
		else if (dir.equals("%")) {
			imgFill = loadImage("src/images/backPackBLUE.png");
			imgEmpty = loadImage("src/images/backPackBLUE.png");
			inventory = new ArrayList<GameObject>();
		}
		opened = false;
		type = dir;
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
	 * Gets the open location.
	 *
	 * @return the open location
	 */
	public Location getOpenLocation() {
		return openLocation;
	}
	/**
	 * Sets the open location.
	 *
	 * @param l the new open location
	 */
	public void setOpenLocation(Location l) {
		openLocation = l;
	}

	/**
	 * Image of chest changes when it has no items.
	 *
	 * @return Image of chest state.
	 */
	public Image getImage() {
		if (isEmpty()) {
			return imgEmpty;
		} else {
			return imgFill;
		}
	}

	/**
	 * Add an item to this chest.
	 *
	 * @param i the item to add
	 */
	public void addToInventory(GameObject i){
		opened=false;
		inventory.add(i);
	}

	/**
	 * Rmove an item from this chest.
	 *
	 * @param i the item to remove
	 */
	public List<GameObject> getPendingItems(){
			return inventory;
	}
	
	public GameObject takeItem(int i){
		GameObject item= inventory.get(i);
		inventory.remove(i);
		return item;
	}

	/**
	 * Returns whether this chest has been opened.
	 *
	 * @return true, if opened
	 */
	public boolean opened(){
		return opened;
	}

	/**
	 * Sets this Chest opened.
	 */
	public void openChest(){
		opened = true;
	}
	public boolean isEmpty(){
		return inventory.isEmpty();
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

}
