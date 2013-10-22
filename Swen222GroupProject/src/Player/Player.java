package Player;

import java.awt.Image;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import Game.Game;
import Game.Location;
import GameParts.*;

/**
 * The Class Player. Holds the properties and actions of a player for a game client. Player receives input
 * from the master and relays back to the master for the output. Handles movement and collision with the board.
 * Backpack is held in this class, and deals with item collection actions.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public abstract class Player {

	/** The shape. */
	private int shape;

	/** The back pack. */
	private BackPack backPack;

	/** The location. */
	private Location location;

	/** The img. */
	private Image img;

	/** The off set y. */
	private double offSetX = .1, offSetY = .1;

	/** The id. */
	public final int id;

	/** The show inventory. */
	private int showInventory = 0;

	/** The score. */
	private volatile int playerScore=0;

	/** The images. */
	private static List<Image> images;

	/**
	 * Constructor for new Player.
	 *
	 * @param location the location
	 * @param id the id
	 * @param images the images
	 */

	public Player(Location location, int id,List<Image> images) {
		backPack = new BackPack(this);
		Player.images = images;
		this.location = location;
		this.id = id;
	}

	/**
	 * Constructor for shape changed Player.
	 *
	 * @param p the p
	 * @param i the i
	 * @param id the id
	 */

	public Player(Player p, GameObject i, int id) {
		changeLocation(p.getLocation());
		setBackPack(p.getBackPack());
		setScore(p.getScore());
		//backPack.removeItem(i);
		this.id = id;
	}

	/**
	 * Gets the shape.
	 *
	 * @return the shape
	 */
	public int getShape(){
		return shape;
	}

	/**
	 * Sets the shape.
	 *
	 * @param shape the new shape
	 */
	public void setShape(int shape) {
		this.shape = shape;
	}

	/**
	 * Move up.
	 *
	 * @param game the game
	 */
	public void moveUp(Game game) {
//System.out.println("Moving up");
		Location l = getLocation();
		Location newLoc = game.getBoard().getLocation(l.getX(), l.getY() + 1,
				l.getHeight());
		if (newLoc == null)
			newLoc = new Location(l.getX(), l.getY() + 1, l.getHeight());
		if ((!checkCollision(newLoc, game, 3) || newLoc.getTileName().equals(
				toString()))
				&& !checkPositions(newLoc, 3, game)) {
			if (getOffSetX() < 0.9 && !checkOutSide(newLoc, game)) {
				setOffSetX(getOffSetX() + 0.2);
			} else if (!checkOutSide(newLoc, game)) {
				setOffSetX(0.1);
				changeLocation(newLoc);
			}
		} else if (getOffSetX() > 0.2) {
			setOffSetX(getOffSetX() - 0.2);
		}
	}

	/**
	 * Move down.
	 *
	 * @param game the game
	 */
	public void moveDown(Game game) {
		//System.out.println("Moving down");
		Location l = getLocation();
		Location newLoc = game.getBoard().getLocation(l.getX(), l.getY() - 1,
				l.getHeight());
		if (newLoc == null)
			newLoc = new Location(l.getX(), l.getY() - 1, l.getHeight());

		if ((!checkCollision(newLoc, game, 4) || newLoc.getTileName().equals(
				toString()))
				&& !checkPositions(newLoc, 4, game)) {
			if (getOffSetX() > 0.1) {
				setOffSetX(getOffSetX() - 0.2);
			} else if (!checkOutSide(newLoc, game)) {
				setOffSetX(0.9);
				changeLocation(newLoc);
			}
		} else if (getOffSetX() > 0.2) {
			setOffSetX(getOffSetX() - 0.2);
		}

	}

	/**
	 * Move left.
	 *
	 * @param game the game
	 */
	public void moveLeft(Game game) {
		//System.out.println("Moving Left");
		Location l = getLocation();
		Location newLoc = game.getBoard().getLocation(l.getX() - 1, l.getY(),
				l.getHeight());
		if (newLoc == null)
			newLoc = new Location(l.getX() - 1, l.getY(), l.getHeight());
		if ((!checkCollision(newLoc, game, 2)|| newLoc.getTileName().equals(
				toString()))
				&& !checkPositions(newLoc, 2, game)) {
			if (getOffSetY() > 0.1) {
				setOffSetY(getOffSetY() - 0.2);
			} else if (!checkOutSide(newLoc, game)) {
				setOffSetY(0.9);
				changeLocation(newLoc);
			}
		} else if (getOffSetY() > 0.2) {
			setOffSetY(getOffSetY() - 0.2);
		}
	}

	/**
	 * Move right.
	 *
	 * @param game the game
	 */
	public void moveRight(Game game) {
		//System.out.println("Moving right");
		Location l = getLocation();
		Location newLoc = game.getBoard().getLocation(l.getX() + 1, l.getY(),
				l.getHeight());
		if (newLoc == null)
			newLoc = new Location(l.getX() + 1, l.getY(), l.getHeight());

		if ((!checkCollision(newLoc, game, 1)|| newLoc.getTileName().equals(
				toString()))
				&& !checkPositions(newLoc, 1, game)) {
			if (getOffSetY() < 0.9 && !checkOutSide(newLoc, game)) {
				setOffSetY(getOffSetY() + 0.2);
			} else if (!checkOutSide(newLoc, game)) {
				setOffSetY(0.1);
				changeLocation(newLoc);
			}
		} else if (getOffSetY() > 0.2) {
			setOffSetY(getOffSetY() - 0.1);
		}

	}

	/**
	 * Jump level.
	 *
	 * @param game the game
	 */
	public void jumpLevel(Game game) {
		if (game.getBoard()
				.getLocation(getLocation().getX(), getLocation().getY(),
						getLocation().getHeight() - 1).getTileName()
				.equals("r")) {
			changeLocation(new Location(0, 0, 1));
		}
	}

	/**
	 * checks the player position against a location to determine a collision.
	 *
	 * @param newLoc the new loc
	 * @param game the game
	 * @param dir the dir
	 * @return true is a collision has a occured
	 */
	private boolean checkCollision(Location newLoc, Game game, int dir) {
		// System.out.println("X :" + getLocation().getX() + " Y :" +
		// getLocation().getY() );
		if (checkOutSide(newLoc, game))
			return false;
		int height = getLocation().getHeight();
		Location[][] loc = game.getBoard().get(height);
		if (loc[newLoc.getX()][newLoc.getY()] == null)
			return false;
		// Checks if the location we are moving to contains a collectable item
		else if (loc[newLoc.getX()][newLoc.getY()].getItem() != null) {
			//System.out.println("Hitting magic item");
			if (loc[newLoc.getX()][newLoc.getY()].getItem() instanceof GameObject){
				//player gets chest with backpack
				if(loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("%")){
					collectMagicItem(loc[newLoc.getX()][newLoc.getY()].getItem(), game);
				}
				//player cannot pickup items until they have a backPack
				else if(!backPack.hasPack()){return false;}
				//chest collision
				else if(loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("@")||loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("#")||
						loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("&")||loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("*")){

					Chest chest= (Chest)loc[newLoc.getX()][newLoc.getY()].getItem();
					game.getBoard().removeLocation(newLoc.getX(), newLoc.getY(), height);
					game.getBoard().addRemove(newLoc.getX(), newLoc.getY(), height);
					if(chest.getType().equals("@")){
					game.getBoard().addLocation(newLoc.getX(), newLoc.getY(),
							height, ">");
					game.getBoard().addMoved(newLoc.getX(), newLoc.getY(), height, ">");}
					else{
					game.getBoard().addLocation(newLoc.getX(), newLoc.getY(),
							height, "<");
					game.getBoard().addMoved(newLoc.getX(), newLoc.getY(), height, "<");}
					if(!chest.isEmpty()){
						backPack.addToPendingItems(chest.getPendingItems());

					return false;
					}

				}else if(loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("<")||loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals(">")){
					return true;
				}
				else{
				collectMagicItem(loc[newLoc.getX()][newLoc.getY()].getItem(), game);}// pickup
			}
			 if(!loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("@")&&!loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("#")
					 &&!loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("&")&&!loc[newLoc.getX()][newLoc.getY()].getItem().getType().equals("*")){																	// item
			game.getBoard().removeLocation(newLoc.getX(), newLoc.getY(), height);

			game.getBoard().addRemove(newLoc.getX(), newLoc.getY(), height);
			return false;
			}
		} else if (loc[newLoc.getX()][newLoc.getY()].isMoveable()) {
			//System.out.println("Moving some shit");
			// remove the location
			// add it to the 'removed locations'
			// doing this once because if its moveable its gone either way
			game.getBoard().removeLocation(newLoc.getX(), newLoc.getY(), height);
			game.getBoard().addRemove(newLoc.getX(), newLoc.getY(), height);

			// added in
			blockShift(newLoc, game, dir, height);

			// going up the Y = y-1
			// going down the y= y+1
			// right x = x+1
			// right x = x-1
			return false;
		}
		return true;
	}

	/**
	 * switch used to move the blocks in the correct direction.
	 *
	 * @param newLoc - the new location
	 * @param game - game to make the change on
	 * @param dir - direction to move
	 * @param height - the height level of the move
	 */
	private void blockShift(Location newLoc, Game game, int dir, int height) {
		String type= checkNextLoc(newLoc, game, dir, height);

		switch (dir) {
		case 1:// UP
			//System.out.println("Moving up");
			game.getBoard().addLocation(newLoc.getX() + 1, newLoc.getY(),
					height, type);
			game.getBoard().addMoved(newLoc.getX() + 1, newLoc.getY(), height, type);
			// add a copy of this Location TIle one step in the appropriate
			// direction
			break;
		case 2:// DOWN
			//System.out.println("Moving down");
			game.getBoard().addLocation(newLoc.getX() - 1, newLoc.getY(),
					height, type);
			game.getBoard().addMoved(newLoc.getX() - 1, newLoc.getY(), height, type);
			break;
		case 3:// RIGHT
			//System.out.println("Moving right");
			game.getBoard().addLocation(newLoc.getX(), newLoc.getY() + 1,
					height, type);
			game.getBoard().addMoved(newLoc.getX(), newLoc.getY() + 1, height, type);
			break;
		case 4:// LEFT
			//System.out.println("Moving left");
			game.getBoard().addLocation(newLoc.getX(), newLoc.getY() - 1,
					height, type);
			game.getBoard().addMoved(newLoc.getX(), newLoc.getY() - 1, height, type);
			break;

		default:
			break;
		}
	}

/**
 * Checks the next location after a block has been moved in a particular direction, if
 * that tile is null then the new Location is a movable block, otherwise the block reverts
 * to a normal block and can't be moved again.
 *
 * @param newLoc the new loc
 * @param game the game
 * @param dir the dir
 * @param height the height
 * @return the string
 */
	public String checkNextLoc(Location newLoc, Game game, int dir, int height){
		String type="";
		Location nextLoc;
		switch (dir) {
		case 1:// UP
			//System.out.println("Moving up");
			nextLoc = game.getBoard().getLocation(newLoc.getX() + 2, newLoc.getY(),height);
			if(nextLoc != null){
				type = "x";
			}
			else{
				type = "m";
			}
			break;
		case 2:// DOWN
			//System.out.println("Moving down");
			nextLoc = game.getBoard().getLocation(newLoc.getX() - 2, newLoc.getY(),height);
			if(nextLoc != null){
				type = "x";
			}
			else{
				type = "m";
			}

			break;
		case 3:// RIGHT
			//System.out.println("Moving right");
			nextLoc = game.getBoard().getLocation(newLoc.getX(), newLoc.getY() + 2,height);
			if(nextLoc != null){
				type = "x";
			}
			else{
				type = "m";
			}

			break;
		case 4:// LEFT
			//System.out.println("Moving left");
			nextLoc = game.getBoard().getLocation(newLoc.getX() , newLoc.getY()- 2,height);
			if(nextLoc != null){
				type = "x";
			}
			else{
				type = "m";
			}

			break;

		default:
			break;
		}
		return type;
	}
	// 1 - Right, 2 - Left, 3 - Up, 4 - Down
	/**
	 * check around the player for collisions.
	 *
	 * @param newLoc the new loc
	 * @param pressed the pressed
	 * @param game the game
	 * @return true, if successful
	 */
	private boolean checkPositions(Location newLoc, int pressed, Game game) {

		if (pressed == 1) {// up
			if (getOffSetX() > .3) {
				return checkCollision(new Location(newLoc.getX(),
						newLoc.getY() - 1, 1), game, pressed);
			}
		}
		if (pressed == 2) {// down
			if (getOffSetX() > .3) {
				return checkCollision(new Location(newLoc.getX(),
						newLoc.getY() + 1, 1), game, pressed);
			}
		}
		if (pressed == 3) {// right
			if (getOffSetY() > .3) {
				return checkCollision(
						new Location(newLoc.getX() + 1, newLoc.getY(), 1),
						game, pressed);
			}
		}
		if (pressed == 4) {// left
			if (getOffSetY() > .3) {
				return checkCollision(
						new Location(newLoc.getX() - 1, newLoc.getY(), 1),
						game, pressed);
			}

		}

		return false;
	}

	/**
	 * check if a location is off the board or not.
	 *
	 * @param newLoc the new loc
	 * @param game the game
	 * @return true if location is off board
	 */
	private boolean checkOutSide(Location newLoc, Game game) {
		if (newLoc.getX() < 0 || newLoc.getY() < 0
				|| newLoc.getX() > game.getBoard().getHeight() - 1
				|| newLoc.getY() > game.getBoard().getWidth() - 1)
			return true;
		return false;
	}

	/**
	 * Change location.
	 *
	 * @param l the l
	 */
	public void changeLocation(Location l) {
		location = l;
	}

	/**
	 * Sets the name for this player
	 *
	 * @param n the new name
	 */
	public void setName(String n) {
	}


	/**
	 * Sets the backpack for this player Used when changing shape.
	 *
	 * @param bp the new back pack
	 */
	public void setBackPack(BackPack bp) {
		this.backPack = bp;
	}

	/**
	 * Update back pack.
	 *
	 * @param code the code
	 */
	public void updateBackPack(String code){
		char invent[] = code.toCharArray();

		for(int i =0; i < invent.length; i++){
			if(invent[i] == '1'){
				if(i == 0){backPack.addItem( new ShapeItem("+"));}
				else if(i == 1){backPack.addItem( new ShapeItem("^"));}
				else if(i == 2){backPack.addItem( new ShapeItem("o"));}
				else if(i == 3){backPack.addItem( new Chest("%"));}
				else if(i == 4){backPack.addPending(new Coin());}
				else if(i == 5){backPack.addPending(new Coin());}
				else if(i == 6){backPack.addPending(new Coin());}
			}
			else if(invent[i] == '2'){
				if(i >= 4){backPack.addPending(new ShapeItem("+"));}
			}
			else if(invent[i] == '3'){
				if(i >= 4){backPack.addPending(new ShapeItem("^"));}
			}
			else if(invent[i] == '4'){
				if(i >= 4){backPack.addPending(new ShapeItem("0"));}
			}
			else if(invent[i] == '5'){
				if(i >= 4){backPack.addPending(new ShapeItem("%"));}
			}
		}


	}

	/**
	 * for loading in from a save.
	 *
	 * @param invent the invent
	 */
	public void updateBackPack(char[] invent){
		for(int i =0; i < invent.length; i++){
			if(invent[i] == '1'){
				if(i == 0){backPack.addItem( new ShapeItem("+"));}
				else if(i == 1){backPack.addItem( new ShapeItem("^"));}
				else if(i == 2){backPack.addItem( new ShapeItem("o"));}
				else if(i == 3){backPack.addItem( new Chest("%"));}
				else if(i == 4){backPack.addPending(new Coin());}
				else if(i == 5){backPack.addPending(new Coin());}
				else if(i == 6){backPack.addPending(new Coin());}
			}
			else if(invent[i] == '2'){
				if(i >= 4){backPack.addPending(new ShapeItem("+"));}
			}
			else if(invent[i] == '3'){
				if(i >= 4){backPack.addPending(new ShapeItem("^"));}
			}
			else if(invent[i] == '4'){
				if(i >= 4){backPack.addPending(new ShapeItem("0"));}
			}
			else if(invent[i] == '5'){
				if(i >= 4){backPack.addPending(new ShapeItem("%"));}
			}
		}
	}

	/**
	 * Gets the backpack for this player Used when changing shape.
	 *
	 * @return the back pack
	 */
	public BackPack getBackPack() {
		return backPack;
	}

	/**
	 * Method for collecting Magic items throughout the game.
	 *
	 * @param i the i
	 * @param game the game
	 */
	public void collectMagicItem(GameObject i, Game game) {
		backPack.addItem(i);
		if(i.getType().equals("Coin")){
			this.setScore(100);
			System.out.println("Player Score"+id+" "+getScore());
		}
		if(i.getType().equals("Key")){
			Key key = (Key) i;
			game.getBoard().removeLocation(key.getLocation().getX(), key.getLocation().getY(), 1);
			game.getBoard().addRemove(key.getLocation().getX(), key.getLocation().getY(), 1);

		}
	}

	/**
	 * Returns true or false depending on whether the given item exists witin
	 * the Player backpack.
	 *
	 * @param i the i
	 * @return true, if successful
	 */
	public boolean hasItem(GameObject i) {
		if(backPack == null){return false;}
		if (this.getBackPack().getInventory().contains(i)) {
			return true;
		}
		return false;
	}

	/**
	 * To output stream.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String toOutputStream() throws IOException {
		return location.getX() + " " + location.getY() + " " + getOffSetX()
				+ " " + getOffSetY() + " " + playerScore + " " + getShape() + " " + id + " "
				+ showInventory() +" "+backPackOutPut() + " " + "/";

	}

	/**
	 * Back pack out put.
	 *
	 * @return the string
	 */
	public String backPackOutPut(){
		char[] str = {'0','0','0','0','0','0','0'};
		if(backPack == null){return String.valueOf(str);}
		for(GameObject i :backPack.getInventory()){
			if(i.getType().equals("+")){		//sqr
				str[0] = '1';
			}
			else if(i.getType().equals("^")){//tri
				str[1] = '1';
			}
			else if(i.getType().equals("o")){//cir
				str[2] = '1';
			}
			else if(i.getType().equals("%")){//chest
				str[3] = '1';
			}
		}
		for(int i = 0; i < backPack.pendingSize(); i++){
			if(backPack.getPending().get(i) instanceof Coin){
			str[4+i] = '1'; }
			if(backPack.getPending().get(i).getType().equals("+")){str[4+i] = '2';}
			else if(backPack.getPending().get(i).getType().equals("^")){str[4+i] = '3';}
			else if(backPack.getPending().get(i).getType().equals("o")){str[4+i] = '4';}
			else if(backPack.getPending().get(i).getType().equals("%")){str[4+i] = '5';}
		}
		
		String s = String.valueOf(str);
		return s;
	}

	/**
	 * From input stream.
	 *
	 * @param input the input
	 * @return the player
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Player fromInputStream(InputStreamReader input)
			throws IOException {

		String value = "";
		char pos = ' ';

		while (pos == ' ') {
			pos = (char) input.read();
		}
		while (pos != '/') {
			value += pos;
			pos = (char) input.read();
		}

		String[] values = value.split("\\s");

		int rx = Integer.valueOf(values[0]);
		int ry = Integer.valueOf(values[1]);
		double offSetX = Double.valueOf(values[2]);
		double offSetY = Double.valueOf(values[3]);
		Integer score = Integer.valueOf(values[4]);
		int shape = Integer.valueOf(values[5]);
		int id = Integer.valueOf(values[6]);
		int inv = Integer.valueOf(values[7]);
		String inventory = values[8];
		Player player = null;
		if (shape == 1) {
			player = new CubePlayer(new Location(rx, ry, 1), id,images);
			player.updateBackPack(inventory);
			player.setShowInventory(inv);

		} else if (shape == 3) {
			player = new SpherePlayer(new Location(rx, ry, 1), id,images);
			player.updateBackPack(inventory);
			player.setShowInventory(inv);

		} else if (shape == 2) {
			player = new PyramidPlayer(new Location(rx, ry, 1), id,images);
			player.updateBackPack(inventory);
			player.setShowInventory(inv);
		}
		player.setScore(score);
		player.setOffSetX(offSetX);
		player.setOffSetY(offSetY);
		return player;
	}


	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * toggle the integer for the inventory of each player as an int for the
	 * byte array.
	 */
	public void toggleInventory() {
		if(backPack.hasPack()){
		if (showInventory == 1) {
			showInventory = 0;
		} else {
			showInventory = 1;
		}}
		else{
			System.out.println("havnt picked up backpack yet");
		}
	}

	/**
	 * Show inventory.
	 *
	 * @return inventory toggle status integer
	 */
	public int showInventory() {
		return showInventory;
	}


	/**
	 * Sets the show inventory.
	 *
	 * @param showInventory the new show inventory
	 */
	public void setShowInventory(int showInventory) {
		this.showInventory = showInventory;
	}
	public void setHasBackPack(int h) {
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public Image getImage() {
		return img;
	}

	/**
	 * Sets the image.
	 *
	 * @param img the new image
	 */
	public void setImage(Image img) {
		this.img = img;
	}

	/**
	 * Gets the off set x.
	 *
	 * @return the off set x
	 */
	public double getOffSetX() {
		return offSetX;
	}

	/**
	 * Gets the off set y.
	 *
	 * @return the off set y
	 */
	public double getOffSetY() {
		return offSetY;
	}

	/**
	 * Sets the off set x.
	 *
	 * @param offSetX the new off set x
	 */
	public void setOffSetX(double offSetX) {
		this.offSetX = offSetX;
	}

	/**
	 * Sets the off set y.
	 *
	 * @param offSetY the new off set y
	 */
	public void setOffSetY(double offSetY) {
		this.offSetY = offSetY;
	}

	/**Returns the score for this player
	 *
	 * @return score
	 * */
	public synchronized int getScore(){
		return this.playerScore;
	}
	public synchronized void setScore(int i){
		this.playerScore+=i;
	}
}
