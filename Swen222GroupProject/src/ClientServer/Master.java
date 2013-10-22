// Credits to David James Pearce for the base of this class

package ClientServer;

import java.io.*;
import java.net.*;
import Game.Game;
import Game.Location;
import GameParts.GameObject;
import Player.Player;


/**
 * A master connection receives events from a slave connection via a socket.
 * These events are registered with the board. The master connection is also
 * responsible for transmitting information to the slave about the current board
 * state.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public final class Master extends Thread {
	private Game game;
	private final int broadcastClock;
	private final int uid;
	private final Socket socket;

	public Master(Socket socket, int uid, int broadcastClock, Game game) {
		this.game = game;
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.uid = uid;
	}

	public void run() {
		try {
			//Creating the buffered input/output streams and writers
			BufferedInputStream inputBuff = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream outputBuff = new BufferedOutputStream(socket.getOutputStream());
			OutputStreamWriter output = new OutputStreamWriter(outputBuff, "US-ASCII");
			InputStreamReader input = new InputStreamReader(inputBuff, "US-ASCII");

			//Writing the uid of the master to the slave
			output.write(uid);

			boolean exit=false;
			while(!exit) {
				try {
	
					//If slave parsed an event through
					if(input.ready()) {
						// read event from slave, and react accordingly

						/*char[] readType = new char[10]; 
						input.read(readType);
						for(int i = 0; i < readType.length; i++){
							System.out.println(readType[i]);
						}*/
					
						int dir = input.read() - '0';
						
						switch(dir) {
						case 1:
							game.getPlayer(uid).moveUp(game);
							break;
						case 2:
							game.getPlayer(uid).moveDown(game);
							break;
						case 3:
							game.getPlayer(uid).moveRight(game);
							break;
						case 4:
							game.getPlayer(uid).moveLeft(game);
							break;
						case 5:
							int action = input.read() -'0';

							if(action == 1){
								for(GameObject i: game.getPlayer(uid).getBackPack().getPending()){
								game.getPlayer(uid).collectMagicItem(i, game);
								}
								game.getPlayer(uid).getBackPack().clearPending();
							}
							else{
							game.getPlayer(uid).toggleInventory();}
							break;
						case 6:
							//Change of shape occured
							int id = input.read();
							int shape = input.read();
							game.getPlayer(id).setShape(shape);
							game.changePlayer(id,shape);
							break;
						case 7:
							//New game started
							game.fromByteArray(input);
							break;
						case 8:
							//Change of room
							int room = input.read();
							game.changeRoom(room);
						case 9:
							input.read();
							game.updateChat(input);

						}
						//Check if the players are standing on the correct tiles to finish or enter new room
						if(!game.isGameOver()){checkFinish();}
						checkRoom();
					}

					// Now, broadcast the state of the board to client
					String state = game.toByteArray();

					output.write(state);
					output.flush();
					Thread.sleep(broadcastClock * 2);
				} catch(InterruptedException e) {
				}
			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");
		}
	}

	private void checkFinish() {
		//Retrieve players and check what type of tile they are on
		Player p1 = game.getPlayer(0);
		Player p2 = game.getPlayer(1);
		Location belowplayer1 = game.getBoard().getLocation(p1.getLocation().getX(),p1.getLocation().getY(),0);
		Location belowplayer2 =  game.getBoard().getLocation(p2.getLocation().getX(),p2.getLocation().getY(),0);
		if ((p1 != null && p2 != null)
				&& belowplayer1.getTileName().equals("!")
				&& belowplayer2.getTileName().equals("!")) {

			game.changeMap();

			return;
		}

	}

	private void checkRoom() {
		//Retrieve players and check what type of tile they are on

		Player p1 = game.getPlayer(0);
		Player p2 = game.getPlayer(1);
		Location belowplayer1 = game.getBoard().getLocation(p1.getLocation().getX(),p1.getLocation().getY(),0);
		Location belowplayer2 =  game.getBoard().getLocation(p2.getLocation().getX(),p2.getLocation().getY(),0);

		if ((p1 != null && p2 != null)
				&& belowplayer1.getTileName().equals("r")
				&& belowplayer2.getTileName().equals("r")) {
			if (game.getBoard().getMapName().contains("R1")) {
				game.changeRoom(2);
			} else {
				game.changeRoom(1);
			}
			return;
		}

	}
}
