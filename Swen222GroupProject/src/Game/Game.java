package Game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import Player.*;
import UserInterface.ChatMessage;

/**
 * The game world class
 * controls the state the current game is in and
 * which map or room each player is on
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class Game {

	/** The players. */
	private List<Player> players = Collections.synchronizedList(new ArrayList<Player>());

	/** The board. */
	private Board board;

	/** The map. */
	public int map;

	/** The room num. */
	private int roomNum;

	/** The time sent remove. */
	private int timeSentRemove = 0;

	/** The time sent move. */
	private int timeSentMove = 0;

	/** The time sent chat. */
	private int timeSentChat = 0;

	/** The message. */
	private ChatMessage message;



	/** The Constant READY. */
	public static final int READY = 0;

	/** The Constant WAITING. */
	public static final int WAITING = 1;

	/** The Constant PLAYING. */
	public static final int PLAYING = 2;

	/** The state. */
	private int state;

	/** The started. */
	private boolean started = false;

	/** The images. */
	private List<Image> images;

	/**has won the game*/
	private boolean gameOver;

	/**
	 * Instantiates a new game.
	 *
	 * @param board the board
	 * @param map the map
	 */
	public Game(Board board, int map, int roomNum){
		state = 0;
		this.map = map;
		this.roomNum = roomNum;
		this.board = board;
		images = new ArrayList<Image>();
		images.add(loadImage("src/images/squareYELLOW.png"));
		images.add(loadImage("src/images/pyramidYELLOW.png"));
		images.add(loadImage("src/images/sphereYELLOW.png"));
		//Gets the players from the board and adds them to the game
		addPlayer(1, board.getPlayer1(),0);
		addPlayer(2, board.getPlayer2(),1);

	}


	/**
	 * Instantiates a new game.
	 *
	 * @param b the b
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	public Game(Board b, Player p1, Player p2, int map, int roomNum){
		this.map = map;
		this.roomNum = roomNum;
		this.board = b;
		images = new ArrayList<Image>();
		images.add(loadImage("src/images/squareYELLOW.png"));
		images.add(loadImage("src/images/pyramidYELLOW.png"));
		images.add(loadImage("src/images/sphereYELLOW.png"));
		addPlayer(p1,p2);
	}

	/**
	 * Adds the player.
	 *
	 * @param shape the shape
	 * @param loc the loc
	 * @param id the id
	 */
	public void addPlayer(int shape, Location loc, int id) {
		switch (shape) {
		case 1:
			players.add(new SpherePlayer(loc,id,images));
			break;
		case 2:
			players.add(new CubePlayer(loc,id,images));
			break;
		case 3:
			players.add(new PyramidPlayer(loc,id,images));
			break;

		default:
			break;
		}

	}

	/**
	 * Adds the player.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	public void addPlayer(Player p1, Player p2) {
		players.add(p1);
		players.add(p2);

	}

	/**
	 * Load image.
	 *
	 * @param filename the filename
	 * @return the image
	 */
	protected Image loadImage(String filename) { //TODO CHANGE THIS
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public synchronized List<Player> getPlayers() {
		List<Player> playersCopy = new ArrayList<Player>(players);
		return playersCopy;
	}

	/**
	 * Change room.
	 *
	 * @param room the room
	 */
	public synchronized void changeRoom(int room){
		String name = board.getMapName();
		//Gets the current room and goes to the opposite room
		//Changing the players location to the rooms entrance
		if(room == 2){
			String value = name.substring(0, 4);
			value += "R2.xml";
			roomNum = 2;
			board = new Board(value);
			getPlayer(0).changeLocation(board.getEntrance());
			getPlayer(1).changeLocation(board.getEntrance());

		}
		else if(room == 1){
			String value = name.substring(0, 4);
			value += "R1.xml";
			roomNum = 1;
			board = new Board(value);
			getPlayer(0).changeLocation(board.getEntrance());
			getPlayer(1).changeLocation(board.getEntrance());
		}
	}


	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public Board getBoard(){
		return board;
	}

	/**
	 * Checks if is started.
	 *
	 * @return true, if is started
	 */
	public boolean isStarted(){
		return started;
	}

	/**
	 * Get Player.
	 *
	 * @param id the id
	 * @return the player
	 */
	public synchronized Player getPlayer(int id){
		for(Player p : players){
			if(p.getId() == id)
				return p;
		}
		return null;
	}
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public int getState(){
		return state;
	}

	/**
	 * Change state.
	 *
	 * @param i the state to change to
	 */
	public void changeState(int i){
		state = i;
	}

	/**
	 * To byte array.
	 *
	 * @return the Current game as a string
	 */
	public synchronized String toByteArray(){
		//If no values are removed add a N
		//Else add all the removed tiles and increment times sent
		String s = "N";
		if(!board.getRemoved().isEmpty()){
			s = "R";
			if(board.getRemoved().size() > 9){
				s += "2" + board.getRemoved().size();
			}
			else{
				s += "1" + board.getRemoved().size();
			}


			for(Location l : board.getRemoved()){
				s += l.toStringRemove();}
			timeSentRemove ++;
		}


		//If no values are moved add a N
		//Else add all the moved tiles and increment times sent
		if(!board.getMoved().isEmpty()){
			s += "M" + board.getMoved().size();

			for(Location l : board.getMoved())
				s += l.toString();
			timeSentMove ++;
		}
		else if(board.getMoved().isEmpty()){
			s += "N ";
		}

		//If there is a message, add it to the string and increment times sent
		if(message != null){
			s += "C";

			s += message.getMessage();
			s += "/";
			timeSentChat ++;
		}
		else{
			s += "N ";
		}


		//Adding the players data to the output
		for(Player p : players){
			try {
				s += p.toOutputStream() +  " ";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Clear the lists if they have been sent 10 times
		if(timeSentRemove > 10){
			timeSentRemove = 0;
			board.clearRemoved();
		}

		if(timeSentMove > 10){
			timeSentMove = 0;
			board.clearMoved();
		}

		if(timeSentChat > 10){
			timeSentChat = 0;
			message = null;
		}

		//Add the map number and the room number
		s+= map;
		s+= roomNum;


		return s;
	}

	/**
	 * From byte array.
	 *
	 * @param input the input stream
	 *
	 *	 */
	public synchronized void fromByteArray(InputStreamReader input) {

		char[] readType = null;

		//Getting the first character with no spaces
		try {
			readType = Character.toChars(input.read());
			while(readType[0] == ' '){
				readType = Character.toChars(input.read());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//If character is N, skip over removing locations
		if(readType[0] == 'R'){
			try{
				String tempValue = "";
				//Gets the amount of digits the value is
				int amount = input.read() - '0';
				//Iterates over the length of the digit
				for(int j = 0 ; j < amount; j ++){
					tempValue += input.read() - '0';
				}

				//Get number of locations to remove
				int numToRemove = Integer.valueOf(tempValue);

				//Iterate over getting the x,y,height and then remove that location from the board
				for(int i = 0 ; i < numToRemove ; i ++ ) {
					tempValue = "";
					//Gets the amount of digits the value is
					amount = input.read() - '0';
					//Iterates over the length of the digit
					for(int j = 0 ; j < amount; j ++){
						tempValue += input.read() - '0';
					}
					int x = Integer.valueOf(tempValue);
					tempValue = "";
					amount = input.read() - '0';
					for(int j = 0 ; j < amount; j ++){
						tempValue += input.read() - '0';
					}
					int y = Integer.valueOf(tempValue);
					int h = input.read() - '0';

					board.removeLocation(x,y,h);
					board.addRemove(x,y,h);

				}
			} catch (IOException e){
				throw new Error(" did not flush correctly 1");
			}
		}

		//Getting the first character with no spaces
		try{
			readType = Character.toChars(input.read());
			while(readType[0] == ' '){
				readType = Character.toChars(input.read());
			}
		} catch (IOException e){
			throw new Error("Failed to parse the correct number");
		}

		//If character is N, skip over removing locations
		if(readType[0] == 'M'){
			try{
				//Gets the number of move objects to do
				int numToMove = input.read() - '0';
				for(int i = 0 ; i < numToMove ; i ++ ) {
					String tempValue = "";
					//Gets the amount of digits in the int
					int amount = input.read() - '0';
					//Iterates the length of the int
					for(int j = 0 ; j < amount; j ++){
						tempValue += input.read() - '0';
					}
					int x = Integer.valueOf(tempValue);
					tempValue = "";
					amount = input.read() - '0';
					for(int j = 0 ; j < amount; j ++){
						tempValue += input.read() - '0';
					}
					int y = Integer.valueOf(tempValue);
					int h = input.read() - '0';
					char typeChar = (char) input.read();
					String type =""+typeChar;
					board.getRemoved();
					board.addLocation(x,y,h,type);//change this to include te ADDED character
				}

			} catch (IOException e){
				throw new Error("did not flush correctly 2");
			}
		}


		try{
			readType = Character.toChars(input.read());
			while(readType[0] == ' '){
				readType = Character.toChars(input.read());
			}
		} catch (IOException e){
			throw new Error("Failed to parse the correct number");
		}

		//If chat message has been sent update game,s message
		if(readType[0] == 'C'){
			String cMessage = "";
			try {
				readType = Character.toChars(input.read());
				while(readType[0] != '/'){
					cMessage += readType[0];
					readType = Character.toChars(input.read());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			message = new ChatMessage(cMessage);
		}

		players.clear();

		//Clear players and create new ones from the input
		for(int i = 0 ; i < 2 ; i ++){
			try {
				players.add(Player.fromInputStream(input));
			} catch (IOException e) {
				throw new Error("Server has crashed");
			}
		}

		try {
			char pos = ' ';

			while (pos == ' ') {
				pos = (char) input.read();
			}
			//Check if the map has changed, if it has update the map
			int value = (int) pos - '0';
			if (value != map) {
				changeMap();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			char pos = ' ';

			while (pos == ' ') {
				pos = (char) input.read();
			}
			int value = (int) pos - '0';
			//Check if room has been changed, Change room if it has been
			if(value != roomNum){
				if(roomNum == 1){
					roomNum = 2;
					changeRoom(2);
				}else{
					roomNum = 1;
					changeRoom(1);
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change player.
	 *
	 * @param id the player id
	 * @param shape the shape to change to
	 */
	public void changePlayer(int id, int shape) {
		Player player = null;

		if (shape == 1) {
			player = new CubePlayer(players.get(id),null, id);
		} else if (shape == 3) {
			player = new SpherePlayer(players.get(id),null, id);
		} else if (shape == 2) {
			player = new PyramidPlayer(players.get(id),null, id);
		}
		players.set(id, player);
	}
	/**
	 * Change map.
	 */
	public synchronized void changeMap() {
		//Increment the map number and set the players location to that set in the XML file
		String nextLevel = "Map" + (map++ + 1) + "R1.xml";
		if(map >= 4){
			gameOver = true;
		}
		else{
		board = new Board(nextLevel);
		roomNum = 1;
		getPlayer(0).changeLocation(board.getPlayer1());
		getPlayer(1).changeLocation(board.getPlayer2());
		}
	}
	public boolean isGameOver() {
		return gameOver;
	}


	/**
	 * Gets the chat message.
	 *
	 * @return the chat message
	 */
	public ChatMessage getMessage() {
		return message;
	}


	/**
	 * Sets the message.
	 *
	 * @param message the new chat message
	 */
	public void setMessage(ChatMessage message) {
		this.message = message;
	}


	/**
	 * Update chat.
	 *
	 * @param input the input
	 */
	public void updateChat(InputStreamReader input) {

		String cMessage = "";
		char[] readType;
		try {
			readType = Character.toChars(input.read());
			//Gets the message that is sent
			while(readType[0] != '/'){
				cMessage += readType[0];
				readType = Character.toChars(input.read());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Update the message
		message = new ChatMessage(cMessage);
	}


	public int getRoomNum() {
		return roomNum;
	}

}
