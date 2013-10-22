package UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ClientServer.Slave;
import DataStorage.LoadSave;
import DataStorage.XMLWriter;
import Game.Board;
import Game.Game;
import Game.Location;

/**
 * In Game Menu. Creates a new game, exits, and calls the loading and saving of a game. 
 *@author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class GameMenu extends JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3373077521428687280L;
	private JMenuBar menuBar;
	private Game game;
	private Slave slave;

	public GameMenu(Game g, Slave slace){
		this.slave = slace;
		game = g;
		create();
	}

	public void create(){
		menuBar = new JMenuBar();

		// Build the first menu.
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic(KeyEvent.VK_A);
		gameMenu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(gameMenu);

		JMenuItem newGame = new JMenuItem("New Game");
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		newGame.getAccessibleContext().setAccessibleDescription(
				"creates a new game");

		JMenu load = new JMenu("Load");

		JMenuItem cont = new JMenuItem("Continue");
		cont.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.ALT_MASK));
		cont.getAccessibleContext().setAccessibleDescription(
				"Continues previous game");
	

		load.add(cont);
		JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		save.getAccessibleContext().setAccessibleDescription(
				"Saves current game");

		gameMenu.add(newGame);
		gameMenu.add(load);
		gameMenu.add(save);


		JMenuItem exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9,ActionEvent.ALT_MASK));

		gameMenu.add(exit);
		newGame.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent ev) {
				String nextLevel = "Map1R1.xml";
				Board b = new Board(nextLevel);
				Game g = new Game(b,1,1);
				slave.newGame(g);
			}
		});
		cont.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent ev) {
				LoadSave l = new LoadSave();
				Board b = new Board(l.getMapName());
				Game g = new Game(b,l.getP1(),l.getP2(),l.getMapId(),l.getRoom());
				for(Location location : l.getRemovedLocations()){
					b.removeLocation(location.getX(), location.getY(), 1);
					b.addRemove(location.getX(), location.getY(), 1);
				}
				slave.newGame(g);
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				new XMLWriter(game.getPlayer(0),game.getPlayer(1),game.getBoard().getSaveRemoved(),game.getBoard().getMapName(),game.map,game.getRoomNum());
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				System.exit(0);
			}
		});


	}



	public JMenuBar getMenu(){
		return menuBar;
	}
}
