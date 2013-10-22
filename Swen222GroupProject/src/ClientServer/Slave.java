// Credits to David James Pearce for the base of this class

package ClientServer;

import java.awt.event.*;
import java.io.*;
import java.net.*;

import UserInterface.ChatMessage;
import UserInterface.Gui;
import Game.Board;
import Game.Game;


/**
 * A slave connection receives information about the current state of the board
 * and relays that into the local copy of the board. The slave connection also
 * notifies the master connection of key presses by the player.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public final class Slave extends Thread implements KeyListener, MouseListener {
	private final Socket socket;
	private Game game;
	private OutputStreamWriter output;
	private InputStreamReader input;
	private int uid;
	private int tick=0;//updates the day counter appropriately

	/**
	 * Construct a slave connection from a socket. A slave connection does no
	 * local computation, other than to display the current state of the board;
	 * instead, board logic is controlled entirely by the server, and the slave
	 * display is only refreshed when data is received from the master
	 * connection.
	 *
	 * @param socket
	 * @param dumbTerminal
	 */
	public Slave(Socket socket) {
		this.socket = socket;

		try {
			//Creating and setting the input and output methods
			BufferedInputStream inputBuff = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream outputBuff = new BufferedOutputStream(socket.getOutputStream());
			output = new OutputStreamWriter(outputBuff, "US-ASCII");
			input = new InputStreamReader(inputBuff, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new Error("The encoding type is not supported");
		} catch (IOException e) {
			throw new Error("Could not find the input or output stream");
		}
	}

	public void run() {
		try {
			//Setting up the game and slaves id
			this.uid = input.read();
			System.out.println("Shapes CLIENT UID: " + uid);
			game = new Game(new Board("Map1R1.xml"),1,1);

			boolean exit=false;

			Gui display = new Gui("Shapes (client@" + socket.getInetAddress() +" "+uid+")",game,uid,this,this);
			while(!exit) {
				//Update the slaves game from the master
				tick++;
				game.fromByteArray(input);
				display.repaint();
				display.updateClockScore(tick,game.getPlayer(uid).getScore());
				if(game.isGameOver()){display.toggleWin();}
				display.checkInventory();
				Thread.yield();
			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("I/O Error: " + e.getMessage());  
			e.printStackTrace(System.err);
		}
	}


	public void keyPressed(KeyEvent e) {

		try {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
				output.write("3");
			} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
				output.write("4");
			} else if(code == KeyEvent.VK_UP) {
				output.write("1");
			} else if(code == KeyEvent.VK_DOWN) {
				output.write("2");
			}else if(code == KeyEvent.VK_I){
				output.write("5");
			}
			output.write(uid);
			output.flush();
		} catch(IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server.  So, we just ignore it.
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}


	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}


	public void updateChat( ChatMessage c) {

		try {
			output.write("9");
			output.write(uid);
			output.write(c.getMessage());
			output.write("/");
			output.flush();
		} catch (IOException e) {
			throw new Error("Something went wrong with the output stream");
		}
	}

	public void updateShape(int i) {

		try {
			output.write("6");
			output.write(uid);
			output.write(i);
			output.flush();
		} catch (IOException e) {
			throw new Error("Something went wrong with the output stream");
		}
	}

	public void newGame(Game g) {
		try{
			output.write("7"); //TODO IS THIS WORKING ?
			output.write(g.toByteArray());
			output.flush();
		} catch (Exception e){
			throw new Error("Something went wrong with the output stream");
		}
	}



	public void addAllPending() {
		try {
			output.write("5");
			output.write("1");
			output.flush();
		} catch (IOException e) {
			throw new Error("Something went wrong with the output stream");
		}
		
	}


}
