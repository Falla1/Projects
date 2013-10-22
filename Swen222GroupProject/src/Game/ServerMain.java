// Credits to David James Pearce for the base of this class

package Game;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import ClientServer.ClockThread;
import ClientServer.Master;
import ClientServer.Slave;

/**
 * The Class ServerMain.Runs the game itself threaded for multiplayer
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 */
public class ServerMain {

	/** The Constant DEFAULT_CLK_PERIOD. */
	private static final int DEFAULT_CLK_PERIOD = 20;

	/** The Constant DEFAULT_BROADCAST_CLK_PERIOD. */
	private static final int DEFAULT_BROADCAST_CLK_PERIOD = 5;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public  ServerMain(String[] args) {
		// ======================================================
		// ======== First, parse command-line arguments ========
		// ======================================================
		String filename = "Map1R1.xml";
		boolean server = false;
		int nclients = 2;
		String url = null;
		int gameClock = DEFAULT_CLK_PERIOD;
		int broadcastClock = DEFAULT_BROADCAST_CLK_PERIOD;
		int port = 32768; // default

		for (int i = 0; i != args.length; ++i) {
			if (args[i].startsWith("-")) {
				String arg = args[i];
				/*if(arg.equals("-help")) {
					usage();
					System.exit(0);
				} else*/
				if(arg.equals("-server")) {
					server = true;
					nclients = Integer.parseInt(args[++i]);
				} else if(arg.equals("-connect")) {
					url = args[++i];
				} else if(arg.equals("-clock")) {
					gameClock = Integer.parseInt(args[++i]);
				} else if(arg.equals("-port")) {
					port = Integer.parseInt(args[++i]);
				}
			} else {
				filename = args[i];
			}
		}

		// Sanity checks
		if(url != null && server) {
			System.out.println("Cannot be a server and connect to another server!");
			System.exit(1);
		} else if(url != null && gameClock != DEFAULT_CLK_PERIOD) {
			System.out.println("Cannot overide clock period when connecting to server.");
			System.exit(1);
		} else if(url == null && filename == null) {
			System.out.println("Board file must be provided for single user, or server mode.");
			System.exit(1);
		}

		try {
			if(server) {
				// Run in Server mode
				Game board = new Game(new Board(filename),1,1);
				runServer(port,nclients,gameClock,broadcastClock, board);
			} else if(url != null) {
				// Run in client mode
				runClient(url,port);
			}

		} catch(IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
			ioe.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
	/**
 * Run client.
 *
 * @param addr the addr
 * @param port the port
 * @throws IOException Signals that an I/O exception has occurred.
 */
private static void runClient(String addr, int port) throws IOException {
		Socket s = new Socket(addr,port);
		System.out.println("AUDIOMETRY CLIENT CONNECTED TO " + addr + ":" + port);
		new Slave(s).run();
	}

	/**
	 * Run server.
	 *
	 * @param port the port
	 * @param nclients the nclients
	 * @param gameClock the game clock
	 * @param broadcastClock the broadcast clock
	 * @param game the game
	 */
	private static void runServer(int port, int nclients, int gameClock, int broadcastClock, Game game) {

		ClockThread clk = new ClockThread(gameClock,game,null);
		int uid = 0;
		// Listen for connections
		System.out.println("AUDIOMETRY SERVER LISTENING ON PORT " + port);
		System.out.println("AUDIOMETRY SERVER AWAITING " + nclients + " CLIENTS");
		try {
			Master connection = null;
			// Now, we await connections.
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				// 	Wait for a socket
				Socket s = ss.accept();
				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());

				connection = new Master(s,uid,broadcastClock,game);
				connection.start();
				if(nclients == 0) {
					System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
					multiUserGame(clk,game,connection);
					System.out.println("ALL CLIENTS DISCONNECTED --- GAME OVER");
					return; // done
				}
				uid++;
			}
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		}
	}

	/**
	 * The following method controls a multi-user game. When a given game is
	 * over, it will simply restart the game with whatever players are
	 * remaining. However, if all players have disconnected then it will stop.
	 *
	 * @param clk the clk
	 * @param game the game
	 * @param connections the connections
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void multiUserGame(ClockThread clk, Game game,
			Master... connections) throws IOException {
		// save initial state of board, so we can reset it.

		clk.start(); // start the clock ticking!!!

		// loop forever
		while(atleastOneConnection(connections)) {
			game.changeState(Game.READY);

			game.changeState(Game.PLAYING);
			// now, wait for the game to finish
			while(game.getState() == Game.PLAYING) {
				Thread.yield();
			}
			// If we get here, then we're in game over mode
			pause(3000);
			// Reset board state
			game.changeState(Game.WAITING);
		}
	}

	/**
	 * Check whether or not there is at least one connection alive.
	 *
	 * @param connections the connections
	 * @return true, if successful
	 */
	private static boolean atleastOneConnection(Master... connections) {
		for (Master m : connections) {
			if (m.isAlive()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Pause.
	 *
	 * @param delay the delay
	 */
	private static void pause(int delay) {
		try {
			Thread.sleep(delay);
		} catch(InterruptedException e){
		}
	}

	// The following two bits of code are a bit sneaky, but they help make the
	// problems more visible.
	static {
		System.setProperty("sun.awt.exception.handler", "Game.ServerMain");
	}

	/**
	 * Handle.
	 *
	 * @param ex the ex
	 */
	public void handle(Throwable ex) {
		try {
			ex.printStackTrace();
			System.exit(1); }
		catch(Throwable t) {}
	}
}