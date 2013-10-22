package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import DataStorage.XMLReader;
import GameParts.Key;

/**
 * The board is the set of locations and where they exist
 * it is made from loading up a map from an xml file
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Board {

	/** The height locations. */
	private List<Location[][]> heightLocations;

	/** The reader. */
	XMLReader reader;

	/** The locations that have been removed. */
	private List<Location> removed;

	/** The locations that have been moved. */
	private List<Location> moved;

	private Set<Location> saveRemoved = Collections.synchronizedSet(new HashSet<Location>());//used for the save game
	
	/** The map. */
	private Map map;

	/** Player1. */
	private Location player1;

	/** Player2. */
	private Location player2;

	/** The entrance. */
	private Location entrance;

	/**
	 * Instantiates a new board.
	 *
	 * @param mapName the map name
	 */
	public Board(String mapName){
		moved = new ArrayList<Location>();
		removed = new ArrayList<Location>();
		heightLocations = new ArrayList<Location[][]>();
		createBoard(mapName);
	}

	/**
	 * Gets the map name.
	 *
	 * @return the map name
	 */
	public String getMapName() {
		return map.toString();
	}

	/**
	 * Gets the board height.
	 *
	 * @return the board height
	 */
	public int getBoardHeight(){
		return heightLocations.size();
	}

	/**
	 * Gets the appropraite list of locations
	 * 0 is the base layer of tiles
	 * 1 is the game wordl tiles
	 *
	 * @param height the height
	 * @return the location[][]
	 */
	public Location[][] get(int height) {
		return heightLocations.get(height);
	}

	/**
	 * Creates the board.
	 *
	 * @param mapName map name must have no spaces before it
	 */
	private void createBoard(String mapName){
		//Reads in the data from the xml file
		reader = new XMLReader();
		reader.readXML(mapName);
		//Gets the width and height of the board
		int width = reader.getWidth();
		int height = reader.getHeight();

		//Creates a new map
		map = new Map(reader.getEntrance(),mapName);

		//Gets the player1 and 2 starting locations
		player1 = reader.getPlayerOne();
		player2 = reader.getPlayerTwo();
		entrance = reader.getEntrance();

		Location[][] l = new Location[width][height];
		int heightValue = reader.getGLevel();
		int heightValueTop = reader.getTLevel();
		String val = ".";

		//Reading in the ground level of the board
		for(int i = 0; i<width; i++){
			String s = reader.getGround().get(i);
			for(int j = 0 ; j < height ; j ++){
				val = String.valueOf(s.charAt(j));
				//if val.equals . , then leave the location null
				//else set its tile name
				if(!val.equals(".")){
					l[i][j] = new Location(i,j,heightValue);
					l[i][j].setTileName(val);
				}
			}
		}

		//Adding ground level to the list of levels
		heightLocations.add(l);

		l = new Location[width][height];
		for(int i = 0 ; i < width ; i ++){
			String s = reader.getTop().get(i);
			for(int j = 0 ; j < height ; j ++){
				val = String.valueOf(s.charAt(j));
				//if val.equals . , then leave the location null
				if(!val.equals(".")){
					//Create a new location
					l[i][j] = new Location(i,j,heightValueTop);
					l[i][j].setTileName(val);
					if(l[i][j].getItem() instanceof Key){
						//If key is located on tile, get the keys door to open
						Key key = (Key)(l[i][j].getItem());
						key.setLocation(reader.getKeyDoor());
					}

				}
			}
		}
		//Adding level 1 to the list of levels
		heightLocations.add(l);

	}
	/**
	 * Returns the height of the board.
	 *
	 * @return the height
	 */
	public int getHeight(){
		return heightLocations.get(0)[0].length;
	}

	/**
	 * Returns the width of the board.
	 *
	 * @return the width
	 */
	public int getWidth(){
		return heightLocations.get(0).length;
	}

	/**
	 * Gets the location.
	 *
	 * @param x the x
	 * @param y the y
	 * @param height the height
	 * @return the location
	 */
	public Location getLocation(int x, int y, int height) {
		if(x < 0 || y < 0
				|| x > getWidth() - 1|| y > getHeight() - 1)
			return null;
		return heightLocations.get(height)[x][y];
	}

	/**
	 * Adds a given location to the board
	 * Used for moving blocks that can be moved.
	 *
	 * @param x the x
	 * @param y the y
	 * @param height the height
	 * @param type the type
	 */
	public synchronized void addLocation(int x, int y,int height,String type){
		Location  newLoc = new Location (x,y,height);
		newLoc.setTileName(type);
		heightLocations.get(height)[x][y] = newLoc;
	}

	/**
	 * Gets the entrance.
	 *
	 * @return the entrance
	 */
	public Location getEntrance() {
		return entrance;
	}

	/**
	 * Removes a given location from the board.
	 *
	 * @param x the x
	 * @param y the y
	 * @param hieght the hieght
	 */
	public synchronized void removeLocation(int x, int y,int hieght){
		if(hieght > 2){
			throw new Error("Height can not be larger than 2");
		}
		if(heightLocations.get(hieght)[x][y] != null
				&& heightLocations.get(hieght)[x][y].getTileName().equals("@")){
			heightLocations.get(hieght)[x][y].setTileName("<");
			return;
		}
		heightLocations.get(hieght)[x][y]=null;
		saveRemoved.add(new Location(x,y,hieght));
	}

	/**
	 * Adds a location that have been removed to a list of locations for updating the client.
	 *
	 * @param x the x
	 * @param y the y
	 * @param hieght the hieght
	 */
	public void addRemove(int x, int y, int hieght){
		removed.add(new Location(x,y,hieght));
		
	}

	/**
	 * Adds a location that has been moved to a list of locations for updating the client.
	 *
	 * @param x the x
	 * @param y the y
	 * @param hieght the hieght
	 * @param type the type
	 */
	public void addMoved(int x, int y, int hieght, String type){
		Location add = new Location(x, y, hieght);
		add.setTileName(type);
		moved.add(add);
	}

	/**
	 * Returns a list of locations that have been removed.
	 *
	 * @return List Locations removed
	 */
	public List<Location> getRemoved(){
		return removed;
	}

	/**
	 * Returns a list of locations that have been moved.
	 *
	 * @return List Locations moved
	 */
	public List<Location> getMoved(){
		return moved;
	}

	/**
	 * Clears the list of removed locations.
	 */
	public void clearRemoved(){
		removed.clear();
	}

	/**
	 * Clears the list of moved locations.
	 */
	public void clearMoved(){
		moved.clear();
	}

	/**
	 * Gets Player1.
	 *
	 * @return the player1
	 */
	public Location getPlayer1() {
		return player1;
	}

	/**
	 * Gets Player2.
	 *
	 * @return the player2
	 */
	public Location getPlayer2() {
		return player2;
	}

	public Set<Location> getSaveRemoved() {
		for(Location l : saveRemoved){
			System.out.println(l.toString());
		}
		return this.saveRemoved;
	}


	
}

