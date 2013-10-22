package GameParts;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Game.Location;
import Player.Player;

/**
 * BackPack is the players inventory it contains the set of shapes the player
 * has.
 *
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class BackPack extends GameObject implements Containers {

	/** The location. */
	private Location location;

	/** The Pending items to be added from chest. */
	private List<GameObject> pending; 
	
	/** The inventory. */
	private Set<GameObject> inventory; 

	/** The img. */
	private Image img;

	private boolean hasPack = false;
	/**
	 * Instantiates a new back pack.
	 *
	 * @param player the player
	 */
	public BackPack(Player player) {
		inventory = new HashSet<GameObject>();
		pending =  new ArrayList<GameObject>();
		img = loadImage("src/images/backPackBLUE.png");
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
	 * Gets the inventory.
	 *
	 * @return the inventory
	 */
	public Set<GameObject> getInventory() {
		return inventory;
	}


	/**
	 *Adds an item to the backpack
	 *
	 * @param item to add in the set
	 */
	public void addItem(GameObject item) {
		if (item != null) {
			//Adds item if doesn't contain already
			for (Item i : this.getInventory()) {
				if (i.getType().equals(item.getType())) {
					return;
				}
			}
			if(item.getType().equals("%")){hasPack  = true;}
			inventory.add(item);
		}
	}

	/**
	 * Remove an item after it has been used by the player.
	 *
	 * @param item to remove in the set
	 */
	public void removeItem(GameObject item) {
		if (item != null) {
			inventory.remove(item);
		}
	}
	/**
	 * Adds a single item to pending items
	 * @param i item from chest to aid to wait list
	 */
	public void addPending(GameObject i){
		pending.add(i);
	}
	
	/**
	 * Replaces existing pending item list with new items from chest.
	 * Items are added to backPack from this list, upon player selection.
	 * @param p list of items from chest
	 */
	public void addToPendingItems(List<GameObject> p) {
		pending.clear();
		pending.addAll(p);
	}
	/**
	 * Takes a single item from inventory
	 * @param i index of item
	 */
	public void takeItem(int i){
		GameObject item= pending.get(i);
		pending.remove(i);
		inventory.add(item);
	}
	/**
	 * clears list of items to waiting to be added from chest
	 */
	public void clearPending(){pending.clear();}

	/**
	 * get the list of items to waiting to be added from chest
	 * @return
	 */
	public List<GameObject> getPending(){
		return pending;
	}
	/**
	 * Size of the pending item list
	 * @return size of pending
	 */
	public int pendingSize(){return pending.size();}

	/* (non-Javadoc)
	 * @see GameParts.Item#getType()
	 */
	@Override
	public String getType() {
		return "BackPack";
	}

	/* (non-Javadoc)
	 * @see GameParts.Item#getImage()
	 */
	@Override
	public Image getImage() {
		return img;
	}

	public boolean hasPack() {
		return hasPack;
	}

}
