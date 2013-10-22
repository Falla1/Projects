package UserInterface;

import javax.swing.UIManager.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import ClientServer.Slave;
import Game.Game;
import GameParts.BackPack;
import GameParts.GameObject;
import Player.*;
/**
 * User interface for a Game Client. Holds the game menu, and displays the game and its interactive parts.
 * Holds the GuiCanvas which is the main canvas for the interface.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class Gui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9206885299615246205L;
	/**
	 * 
	 */

	private GuiCanvas gamePanel;
	private GameMenu menu;
	private Game game;
	private GodDialogPanel clockScore;
	private PackCanvas inventory;
	private int uid = 0;
	private ChatPanel chatP;
	private JTextArea inventoryArea;
	private WinScreen winScreen;

	public Gui(String title, Game g, int uid, Slave slave, KeyListener... keys) {

		super(title);
		setNimbus();
		game = g;
		this.uid = uid;
		this.setLocation(400, 100);
		this.setResizable(false);

		//Creating the canvas's to go onto the Gui
		gamePanel = new GuiCanvas(g, slave, uid);
		Player player = game.getPlayer(uid);
		player.getBackPack();
		inventory = new PackCanvas(slave);
		inventoryArea = new JTextArea(1, 20);
		chatP = new ChatPanel("Player " + (uid + 1), this,slave,game);
		clockScore = new GodDialogPanel(slave); 
		winScreen= new WinScreen(g,uid);

		//Creating a layerpane for the canvas's
		JLayeredPane jl = new JLayeredPane();

		jl.setPreferredSize(new Dimension(820, 820));

		for (KeyListener k : keys) {
			this.addKeyListener(k);
		}

		setLayout(new BorderLayout());
		//Creating a new menu
		menu = new GameMenu(game, slave);

		gamePanel.repaint();
		inventory.repaint();
		clockScore.repaint();
		winScreen.repaint();

		// board layer
		gamePanel.setSize(jl.getPreferredSize());
		gamePanel.setLocation(0, 0);
		setMouseListenerForJL(jl);

		// inventory layer
		inventory.setSize(inventory.getPreferredSize());
		inventory.setLocation(500, 50);
		inventoryArea.setSize(220, 100);
		inventoryArea.setLocation(500, 130);
		
		clockScore.setLocation(50, 0);
		clockScore.setSize(clockScore.getPreferredSize());
		
		winScreen.setSize(jl.getPreferredSize());
		winScreen.setLocation(0, 0);
		winScreen.setVisible(false);

		// Layered panel, adding at layers
		jl.add(gamePanel, new Integer(0));
		jl.add(inventory, new Integer(2));
		//jl.add(inventoryArea, new Integer(1));
		
		jl.add(clockScore, new Integer(4));
		jl.add(winScreen, new Integer(3));

		this.add(jl, BorderLayout.CENTER);
		this.add(chatP, BorderLayout.SOUTH);
		
		super.repaint();
		setUp();
		this.setVisible(true);
		requestFocusInWindow();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		super.pack();
	}

	@Override
	public void repaint(){
		//Repainting the Gui
		//And then checking for any new messages
		super.repaint();
		chatP.checkMessage();
		
	}


	private void setMouseListenerForJL(JLayeredPane jl) {

		jl.addMouseListener(new MouseListener() {
			//Adds a requestFocus event when the mouse is click on the JLayeredPane
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
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
		});
	}

	public void setNimbus() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					//Searchs for Nimbus on the system, then sets the lookandfeel
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			//Nimbus is unavaible, can ignore and it will return to default
		}
	}
/**
 * Updates the day counter in GUI and cycles the dialog for player hints
 * Days last for 4500 ticks
 * 
 * @param i Integer counter from slave 
 * @param j 
 * */
	public void updateClockScore(int i, int j) {
		//System.out.println(j); 
		if (clockScore == null) {
			return;
		}
		if(clockScore.hasDiags() && i%400==0  ){
			clockScore.updateDialog(); 
		}
		//clock recieves a tick from the slave 
		//every 4500 ticks the day display is updated in the GUI
		if(i%4500==0 && !(i<4500)){
			clockScore.updateDay();
		}
		if (j>0) {
			clockScore.updatePlayerScore(j);
		}
		clockScore.repaint();
	}

	public void checkInventory() {
		if (inventory == null) {
			return;
		}
		if (gamePanel == null) {
			return;
		}
		Player player = game.getPlayer(uid);
		BackPack backPack = player.getBackPack();
		if(backPack.pendingSize()>0){
			inventory.addPendingItems(backPack.getPending());
			player.toggleInventory();
		}
		Set<GameObject> inv = player.getBackPack().getInventory();
		//If inventory is not empty, then set it from players backpack
		if (!inv.isEmpty()) {
			inventory.setInventory(inv);
			inventory.repaint();
		}
		
		//If players requested to view inventory, turn visible
		if (player.showInventory() == 1) {
			inventory.setVisible(true);
			inventoryArea.setVisible(true);
			gamePanel.setShowMap(true);

		} else {
			inventory.setVisible(false);
			inventoryArea.setVisible(false);
			gamePanel.setShowMap(false);
		}
	}
	public void toggleWin() {
		winScreen.setVisible(true);
		clockScore.endGame();
		inventory.setVisible(false);
		inventoryArea.setVisible(false);
		gamePanel.setShowMap(false);

	}

	public void setUp() {
		this.add(menu.getMenu(), BorderLayout.NORTH);
		menu.setVisible(true);
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(820, 820);
	}



}
