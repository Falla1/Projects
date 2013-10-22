package UserInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ClientServer.Slave;
import Game.Board;
import Game.Game;
import Game.Location;
import Player.Player;
/**
 * Game Canvas that displays the game board, background, players and map. Centers on game client's player in the board and map.
 * Sets up the field of view for map discovery.
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class GuiCanvas extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3157929404119071974L;
	private ArrayList<Point> stars = new ArrayList<Point>();
	Game game;

	private boolean dayTime = true;

	private float time = 0.0f;
	private int uid;
	private boolean showMap = false;
	private Image back;
	private double yLine = 0.6;

	public GuiCanvas(final Game game, Slave slave, int uid) {
		this.game = game;
		this.uid = uid;
		makeStars();
		this.repaint();
		//Loading the background mask
		back = loadImage("src/images/backgroundMASK.png");
	}

	public boolean isShowMap() {
		return showMap;
	}

	public void setShowMap(boolean showMap) {
		this.showMap = showMap;
	}

	@Override
	public synchronized void paint(Graphics g) {
		//Update the time for the colour change
		updateTime();
		Graphics2D g2 = (Graphics2D) g;

		//If either player == null, return
		if (game.getPlayer(0) == null || game.getPlayer(1) == null)
			return;

		//Setting the background colour
		g2.setColor(graduateBackGroundBLUE());
		g2.fillRect(0, 0, 820, 820);
		g2.setColor(graduateBackGroundRED());
		g2.fillRect(0, 0, 820, 820);

		//Paint the stars if its night time
		if(dayTime == false){
			this.paintStars(g2);
		}

		//Drawing the mask on the back
		g2.drawImage(back, 0,0, null, null);

		//Getting the height of the board
		int height = game.getBoard().getBoardHeight();

		Player player1 = game.getPlayer(0);
		Player player2 = game.getPlayer(1);

		Location tempPlay1Loc;
		Location tempPlay2Loc;

		//Determining the players 'real' location taking into account for the offset of the player
		//This helps reduce the clipping
		if (player1.getOffSetX() > yLine) {
			tempPlay1Loc = new Location(player1.getLocation().getX(), player1
					.getLocation().getY() + 1, player1.getLocation()
					.getHeight());
		} else {
			tempPlay1Loc = player1.getLocation();
		}

		if (player2.getOffSetX() > yLine) {
			tempPlay2Loc = new Location(player2.getLocation().getX(), player2
					.getLocation().getY() + 1, player2.getLocation()
					.getHeight());
		} else {
			tempPlay2Loc = player2.getLocation();
		}

		//Field of view and Centering
		
		//arrays for the x,y coordinates of neighboring locations 
		int[] xVals = new int[7];
		int[] yVals = new int[7];
		Board b = game.getBoard();
		//board width and height
		int h = b.getHeight();
		int w = b.getWidth();
		
		//Get the player for this interface, and location
		Player player = game.getPlayer(uid);
		Location playerMapLoc;
		//player x,y location
		if (player.getOffSetX() > yLine) {
			playerMapLoc = new Location(player.getLocation().getX(), player
					.getLocation().getY() + 1, player.getLocation().getHeight());
		} else {
			playerMapLoc = player.getLocation();
		}
		//veiw range
		int range = 9;
		//gets the neighbor coordinates of the locations around the player, if on the board
		xVals = playerMapLoc.getXNeighbours(w,range);	//x coordinates of neighbor locations
		yVals = playerMapLoc.getYNeighbours(h,range);	//y coordinates of neighbor locations
		
		//drawing x,y positions for the canvas
		int pCanvasX = (int) (((player.getLocation().getY()+ player.getOffSetX()) * 20 / 1) + (player.getLocation().getX() + player
				.getOffSetY()) * 20 / 1);
		int pCanvasY = (int) (((player.getLocation().getX() + player.getOffSetY()) * 18 / 2) - (player.getLocation().getY() + player
				.getOffSetX()) * 18 / 2);
		
		// x,y to center canvas to player on the canvas
		int yCenter = 410 - pCanvasY;
		int xCenter = 410 - pCanvasX;
		//drawing x,y positions for the map
		int pMapX = (int) ((player.getLocation().getX() + player.getOffSetY()) * 21) - 2;
		int pMapY = (int) ((((game.getBoard().getHeight() - 1)-player.getLocation().getY()) - player.getOffSetX()) * 21) + 2;
		
		// x,y to center canvas to player on the map
		int mapXCenter = 410 -pMapX;
		int mapYCenter = 410 -pMapY;

		for (int k = 0; k < height; k++) {
			//Gets the array of arrays of tile on the height level
			Location[][] loc = game.getBoard().get(k);
			for (int l = 0; l < xVals.length; l++) {
				
				//uses neighbor coordinates of player and sets locations to visible for line of sight.
				for (int m = 0; m <yVals.length; m++) {
					Location look = loc[xVals[l]][yVals[m]];
					if (look != null) {
						look.setVisible(player);
					}
				}
			}

			for (int j = game.getBoard().getHeight() - 1; j >= 0; j--) {
				for (int i = 0; i < game.getBoard().getWidth(); i++) {

					Location l = loc[i][j];
					//Determining the real X and Y locations of each location
					int xi = (int) (((j) * 20 / 1) + (i) * 20 / 1);
					int yi = (int) (((i) * 18 / 2) - (j) * 18 / 2);

					//Makes sure the player is centered on the map
					int xm = (int) (i * 21)+ mapXCenter;
					int ym = (int) (((game.getBoard().getHeight() - 1)-j) * 21)+mapYCenter;

					//Keeps the tiles nice and aligned along the edges
					if (k == 0) {
						yi += 25;
					}
					//If player can see the tile, then draw that tiles image
					if (l != null) {
						if(player.getScore() >= 1000){l.setToVisible();}
						if (l.isVisible() ) {
							Image img = l.getImage();
							if(!showMap){
								g2.drawImage(img, xi +xCenter, yi + yCenter, null, null);}
						}
					}

					//Check if player 1's 'real' location is on this tile
					if (tempPlay1Loc.equals(new Location(i, j, k))) {
						int tempJ = j;
						//Doing more changes to the players 'real' location to reduce clipping
						if (player1.getOffSetX() > yLine)
							tempJ--;
						//Getting the players x and y, taking into account for the offset
						int x = (int) (((tempJ + player1.getOffSetX()) * 20 / 1) + (i + player1
								.getOffSetY()) * 20 / 1);
						int y = (int) (((i + player1.getOffSetY()) * 18 / 2) - (tempJ + player1
								.getOffSetX()) * 18 / 2);
						//If player wants to see the map, show the player on the map
						//Else just draw the players image
						if(!showMap){
							g2.drawImage(player1.getImage(), x+ xCenter, y + yCenter, null, null);}
						if (showMap) {
							int x1 = (int) ((i+ player1.getOffSetY()) * 21) - 2;
							int y1 = (int) ((((game.getBoard().getHeight() - 1)-tempJ) - player1.getOffSetX()) * 21) + 2;
							g2.setColor(Color.YELLOW);
							g2.fillRect(x1+ mapXCenter, y1+ mapYCenter, 20, 20);
						}
					}

					//Check if player 1's 'real' location is on this tile
					if (tempPlay2Loc.equals(new Location(i, j, k))) {
						int tempJ = j;
						//Doing more changes to the players 'real' location to reduce clipping
						if (player2.getOffSetX() > yLine)
							tempJ--;
						//Getting the players x and y, taking into account for the offset
						int x = (int) (((tempJ + player2.getOffSetX()) * 20 / 1) + (i + player2
								.getOffSetY()) * 20 / 1);
						int y = (int) (((i + player2.getOffSetY()) * 18 / 2) - (tempJ + player2
								.getOffSetX()) * 18 / 2);
						//If player wants to see the map, show the player on the map
						//Else just draw the players image
						if(!showMap){
							g2.drawImage(player2.getImage(), x+ xCenter, y + yCenter, null, null);}
						if (showMap) {
							int x1 = (int) ((i+ player2.getOffSetY()) * 21) - 2;
							int y1 = (int) ((((game.getBoard().getHeight() - 1)-tempJ) - player2.getOffSetX()) * 21) + 2;
							g2.setColor(Color.YELLOW);
							g2.fillRect(x1+ mapXCenter, y1+ mapYCenter, 20, 20);
						}
					}
					if(l !=null){
						if (showMap) {
							//Draws the map on the players screen taking into account for the different game objects
							if (l.isVisible()) {
								String tileName2 = l.getTileName();
								if (k == 0) {
									g2.setColor(new Color(95, 158, 160));
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("^")
										|| tileName2.equals("+")
										|| tileName2.equals("o")) {// ShapeItem
									g2.setColor(Color.YELLOW);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("@")
										|| tileName2.equals("&")
										|| tileName2.equals("*")
										|| tileName2.equals("#")) {// Chest
									g2.setColor(Color.ORANGE);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("b")
										|| tileName2.equals("x")
										|| tileName2.equals("r")
										|| tileName2.equals("m")) {// if
									// location
									// item is a
									// tile
									g2.setColor(Color.MAGENTA);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("c")) {// Shpere
									// Entry
									g2.setColor(Color.MAGENTA);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("s")) {// Square
									// Entry
									g2.setColor(Color.MAGENTA);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("p")) {// Pyramid
									// Entry
									g2.setColor(Color.MAGENTA);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("$")) {// coin
									g2.setColor(Color.YELLOW);
									g2.fillRect(xm, ym, 20, 20);
								} else if (tileName2.equals("k")) {// key
									g2.setColor(Color.GREEN);
									g2.fillRect(xm, ym, 20, 20);
								}
								else{			
									g2.setColor(new Color(95, 158, 160));
									g2.fillRect(xm, ym, 20, 20);
								}

							}
						}}



				}
			}
		}
	}



	private void updateTime() {

		if(time>0.996f){
			dayTime=false;
		}
		if(dayTime){
			time+=0.001f;
		}
		else if(!dayTime){
			time-=0.001f;
		}
		if(time<0.002){
			dayTime=true;
		}
	}


	private Color graduateBackGroundRED() {
		Color c = new Color(0.8f,0.3f,0.3f, 1-time);
		return c;
	}

	private Color graduateBackGroundBLUE() {
		Color c = new Color(0.57f,0.8f,0.9f, time);
		return c;
	}


	private Image loadImage(String filename) { //TODO UPDATE loadImage methods to allow for .jar
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	public void makeStars(){
		//Creates a random number generator
		//And creates stars according to the numbers
		Random generator = new Random();
		for(int i=0;i<200;i++){
			int x =  generator.nextInt(829);
			int y = generator.nextInt(829);
			Point p = new Point(x,y);
			stars.add(p);
		}
	}


	/**
	 * @param g2
	 */
	private void paintStars(Graphics2D g2) {
		//Paints the stars on the object
		g2.setColor(Color.WHITE);
		for (Point p : stars) {
			g2.fillOval(p.x, p.y, 2, 2);
		}
	}


}
