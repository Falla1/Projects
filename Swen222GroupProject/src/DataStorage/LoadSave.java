package DataStorage;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import Game.Location;
import Player.CubePlayer;
import Player.Player;
import Player.PyramidPlayer;
import Player.SpherePlayer;

import java.awt.Image;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * reads the save game xml file and creates new players as they were when the game finished
 * previous, it also reads in the correct map and room the game was saved in
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class LoadSave {

	String content; // content found between the tags in the parser
	private int id1, id2, shape1, shape2, locX1, locX2, locY1, locY2,mapId;
	private Player p1, p2;
	private char[] p1Inv;// player inventory
	private char[] p2Inv;// player inventory
	private String p1I, p2I,mapName;// inventory read values
	private List<Image> images;
	private HashSet<Location> locationsToBeRemoved;//locations that need to be removed off the board for consitency
	private String XY;
	private int roomId;
	private int playerOneScore,playerTwoScore;


	public LoadSave() {
		images = new ArrayList<Image>();
		images.add(loadImage("src/images/squareYELLOW.png"));
		images.add(loadImage("src/images/pyramidYELLOW.png"));
		images.add(loadImage("src/images/sphereYELLOW.png"));

		p1Inv = new char[4];
		p2Inv = new char[4];
		locationsToBeRemoved = new HashSet<Location>();

		readXML("SaveTest.xml");
		createPlayers();

	}

	protected Image loadImage(String filename) {
		File fname = new File(filename);
		try {
			Image img = ImageIO.read(fname);
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	/**
	 * goes through the xml file creating a string arraylist of all the elements
	 * it finds returns true when complete
	 *
	 * @param xml
	 *            the xml to parse
	 * @return
	 */
	public void readXML(String xml) {
		File directory = new File("src//DataStorage//Saves");
		File loader = new File(directory + "//" + xml);

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				// overidden the handler methods
				// qname is the tag
				// attribute is used for the map level
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
								throws SAXException {

				}

				// qname is the tag
				// when the tag is found add the content matching to the list
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					if (qName.equalsIgnoreCase("PID-1")) {
						id1 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("PlayerOneShape")) {
						shape1 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("locX1")) {
						locX1 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("locY1")) {
						locY1 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("PlayerOneInventory")) {
						p1I = content;
					}
					if (qName.equalsIgnoreCase("RoomId")) {
						roomId = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("PID-2")) {
						id2 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("PlayerTwoShape")) {
						shape2 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("locX2")) {
						locX2 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("locY2")) {
						locY2 = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("PlayerTwoInventory")) {
						p2I = content;
					}
					if (qName.equalsIgnoreCase("playerOneScore")) {
						playerOneScore = Integer.parseInt(content);
					}
					if (qName.equalsIgnoreCase("playerTwoScore")) {
						playerTwoScore = Integer.parseInt(content);
					}

					if(qName.equalsIgnoreCase("locationXY")){
						XY = content;
						String x = "";
						String y = "";
						boolean xVal = true;
						for(int i = 0 ; i < XY.length(); i++){
							if(XY.charAt(i) != ' ' && xVal == true){
								x += XY.charAt(i);
							}
							else {
								if(XY.charAt(i) == ' ')
									i++;
								y += XY.charAt(i);
								xVal = false;
							}
						}
						locationsToBeRemoved.add(new Location(Integer.valueOf(x),Integer.valueOf(y),1));
					}
					if(qName.equalsIgnoreCase("MapName")){
						mapName = content;
					}
					if(qName.equalsIgnoreCase("MapId")){
						mapId = Integer.parseInt(content);;
					}
				}

				// gets the text out from the tag
				public void characters(char ch[], int start, int length)
						throws SAXException {

					content = String.copyValueOf(ch, start, length).trim();

				}

			};

			saxParser.parse(loader, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * sets up the two inventories for the players
	 */
	private void loadInventories() {
		p1Inv = p1I.toCharArray();
		p2Inv = p2I.toCharArray();
	}

	/**
	 * sets up the players and sets their inventories to the saved versions
	 * with their shape and player uid
	 */
	private void createPlayers() {
		loadInventories();
		p1 = makePlayer(shape1,id1,locX1,locY1);
		p1.updateBackPack(p1Inv);
		p1.setScore(playerOneScore);
		p2 = makePlayer(shape2,id2,locX2,locY2);
		p2.updateBackPack(p2Inv);
		p2.setScore(playerTwoScore);
	}

	public char[] getPlayerOneInventory() {
		return p1Inv;
	}

	public char[] getPlayerTwoInventory() {
		return p2Inv;
	}

	public Player getP1() {
		return p1;
	}

	public Player getP2() {
		return p2;
	}

	/**
	 * creates the player based on their shape
	 *
	 * @param shape
	 * @return
	 */
	private Player makePlayer(int shape, int id, int x, int y) {
		switch (shape) {
		case 1:
			return new CubePlayer(new Location(x, y, 1), id, images);
		case 2:
			return new PyramidPlayer(new Location(x, y, 1), id, images);
		case 3:
			return new SpherePlayer(new Location(x, y, 1), id, images);
		default:
			break;
		}
		return null;
	}

	public HashSet<Location> getRemovedLocations(){
		return this.locationsToBeRemoved;
	}

	public int getMapId() {
		return mapId;
	}

	public String getMapName() {
		return mapName;
	}

	public int getRoom() {
		return roomId;
	}

}
