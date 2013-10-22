package Game;

import java.awt.Image;

import GameParts.*;
import Player.Player;
import Tiles.PlainTile;
import Tiles.SphereEntrance;
import Tiles.SquareEntrance;
import Tiles.Tile;
import Tiles.TriangleEntrance;

/**
 * The Class Location. Is an x y coordinate system for the game
 * everything that exists in the gameworld is on a location
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Location {

	/** The x. */
	private int x;

	/** The y. */
	private int y;

	/** The hieght. */
	private int hieght;

	/** The tile name. */
	private String tileName;

	/** The item. */
	private GameObject item;

	/** The tile. */
	private Tile tile;

	/** boolean for Visibility. */
	private boolean visible;


	/**
	 * Instantiates a new location.
	 *
	 * @param x the x
	 * @param y the y
	 * @param hieght the hieght
	 */
	public Location(int x, int y, int hieght){
		this.x = x;
		this.y = y;
		this.hieght = hieght;
	}


	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return hieght;
	}

	/**
	 * Gets the tile name.
	 *
	 * @return the tile name
	 */
	public String getTileName(){
		return tileName == null ? "." : tileName;
	}

	/**
	 * Sets the tile name.
	 *
	 * @param name the new tile name
	 */
	public void setTileName(String name){
		tileName = name;
		setObject(tileName);
	}

	/**
	 * Creates and stores the appropriate object for this location
	 * if there is one to be stored.
	 *
	 * @param tileName2 the new object
	 */
	private void setObject(String tileName2) {
		if(tileName2.equals("^") || tileName2.equals("+") || tileName2.equals("o" )){//ShapeItem
			item = new ShapeItem(tileName2);
		}
		else if(tileName2.equals("@") || tileName2.equals("&") || tileName2.equals("*")
				|| tileName2.equals("#")||tileName2.equals("%")){//Chest
			item = new Chest(tileName2);
		}
		else if(tileName2.equals("<") || tileName2.equals(">")) {item = new EmptyChest(tileName2);}

		else if( tileName2.equals("b") || tileName2.equals("x") || tileName2.equals("r") 
				|| tileName2.equals("m") || tileName2.equals("!")   ){//if location item is a tile
			tile = new PlainTile(tileName2);
		}
		else if( tileName2.equals("c")) {//Shpere Entry
			tile = new SphereEntrance(tileName2);
		}
		else if( tileName2.equals("s") ){//Square Entry
			tile = new SquareEntrance(tileName2);
		}
		else if( tileName2.equals("p") ){//Pyramid Entry
			tile = new TriangleEntrance(tileName2);
		}
		else if(tileName2.equals("$")){//Coin
			item = new Coin();
		}
		else if (tileName2.equals("k")){//Key
			item = new Key();
		}
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public GameObject getItem(){
		return item;
	}

	/**
	 * Sets the visable.
	 *
	 * @param p cretaes the field of view for the given player based on location
	 */
	public void setVisible(Player p){
		double distance = distanceTo(p.getLocation());
		if(distance < 5 && !visible){
			setToVisible();
		}


	}

	/**
	 * Distance to.
	 *
	 * @param other the other location
	 * @return the double
	 */
	public double distanceTo(Location other){
		return Math.hypot(this.x-other.getX(), this.y-other.getY());
	}

	/**
	 * Returns the image of the tile or item in the location.
	 *
	 * @return the image
	 */
	public Image getImage(){
		if(item != null){
			return item.getImage();
		}
		else if(tile != null){
			return tile.getImage();
		}
		else{//error
			return null;
		}
	}



	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible(){
		return visible;
	}

	/**
	 * Sets the to visible.
	 */
	public void setToVisible(){
		visible = true;
	}

	/**
	 * Method to determine if this location contains a collectable item or an openable chest.
	 *
	 * @return true, if is collectable
	 */

	public boolean isCollectable(){
		if(this.item!=null){
		}
		return false;
	}

	/**
	 * Checks if is moveable.
	 *
	 * @return true, if is moveable
	 */
	public boolean isMoveable(){
		if(tile != null){
			return tile.isMoveable();
		}return false;
	}


	/**
	 * Gets the x neighbours.
	 *
	 * @param w the w
	 * @param size the size
	 * @return the x neighbours
	 */
	public int[] getXNeighbours(int w, int size){
		int[] xVals = new int[size];

		for (int i = 0; i < size; i++) {
			int mod = i - (size/2);
			if(x+mod < 0 || x+mod >= w){
				xVals[i] = x;
			}
			else{
				xVals[i] = x+mod;
			}
		}
		return xVals;
	}

	/**
	 * Gets the y neighbours.
	 *
	 * @param h the h
	 * @param size the size
	 * @return the y neighbours
	 */
	public int[] getYNeighbours(int h, int size){
		int[] yVals = new int[size];

		for (int i = 0; i < size; i++) {
			int mod = i - (size/2);
			if(y+mod < 0 || y+mod >= h){
				yVals[i] = y;
			}
			else{
				yVals[i] = y+mod;
			}
		}
		return yVals;
	}


	/**
	 * To string remove.
	 *
	 * @return the string
	 */
	public String toStringRemove(){
		String s = "";
		//If x is int length 2, then put 2 infront then x
		//Else 1 infront then x
		if(x > 9){
			s += "2" + x;
		}
		else {
			s += "1" + x;
		}
		if(y > 9){
			s += "2" + y;
		}
		else {
			s += "1" + y;
		}
		s += String.valueOf(hieght);
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hieght;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (hieght != other.hieght)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	//Utility methods
	@Override
	public String toString(){
		String s = toStringRemove();
		s+= this.getTileName();
		return s;
	}


	public void setItem(GameObject i) {
		// TODO Auto-generated method stub
		tileName = i.getType();
		item= i;
	}



}
