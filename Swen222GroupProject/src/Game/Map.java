package Game;

/**
 * The Class Map.Meta data class for the board to store maps
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Map {
	
	/** The exit. */
	private Location exit;
	
	/** The entrance. */
	private Location entrance;
	
	/** The map. */
	private String map; 

	/**
	 * Instantiates a new map.
	 *
	 * @param entrance the entrance
	 * @param mapName the map name
	 */
	public Map( Location entrance, String mapName) {
		this.entrance = entrance;
		map = mapName;
	}
	
	/**
	 * Gets the exit.
	 *
	 * @return the exit
	 */
	public Location getExit() {
		return exit;
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
	 * Sets the exit.
	 *
	 * @param exit the new exit
	 */
	public void setExit(Location exit) {
		this.exit = exit;
	}

	/**
	 * Sets the entrance.
	 *
	 * @param entrance the new entrance
	 */
	public void setEntrance(Location entrance) {
		this.entrance = entrance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return map;
	}
	

}
