package DataStorage;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import Game.Location;

import java.util.*;
import java.io.*;
/**
 * Xml class that reads in the maps for the board to use 
 * this is the gameworld that the players will be playing on
 * @author @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class XMLReader {
	// containers for the data
	private ArrayList<String> ground;
	private ArrayList<String> top;
	private int height, width,tlevel,glevel;
	private String content ; //content found between the tags in the parser
	private Location playerOne,playerTwo;
	private Location entrance;
	private Location keyDoor;
	private int p1x,p1y,p2x,p2y;//need these for making the locations
	private int keyDoorX,keyDoorY;
	private int enterX,enterY;


	public XMLReader() {
		ground = new ArrayList<String>();
		top = new ArrayList<String>();
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
		File directory = new File("src//DataStorage//Maps");
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
					if (qName.equalsIgnoreCase("Ground")) {
						glevel = Integer.parseInt(attributes.getValue("level"));
					}
					else if	(qName.equalsIgnoreCase("Top")) {
						tlevel = Integer.parseInt(attributes.getValue("level"));
					}
				}

				// qname is the tag
				// when the tag is found add the content matching to the list
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					if(qName.equalsIgnoreCase("gRow")){
						//for each row add it to the arraylist
						ground.add(content);
					}else if(qName.equalsIgnoreCase("tRow")){
						top.add(content);
					}else if(qName.equalsIgnoreCase("width")){
						width = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("height")){
						height = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("p1x")){
						p1x = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("p1y")){
						p1y = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("p2x")){
						p2x = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("p2y")){
						p2y = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("kX")){
						keyDoorX = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("kY")){
						keyDoorY = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("entX")){
						enterX = Integer.parseInt(content);
					}else if(qName.equalsIgnoreCase("entY")){
						enterY = Integer.parseInt(content);
					}

				}

				// gets the text out from the tag
				public void characters(char ch[], int start, int length)
						throws SAXException {

					content = String.copyValueOf(ch, start, length).trim();

				}

			};
			//start the parser here and use the above methods to get the data
			saxParser.parse(loader, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		playerOne = new Location(p1x,p1y,1);
		playerTwo = new Location(p2x,p2y,1);
		keyDoor = new Location(keyDoorX,keyDoorY,1);
		entrance = new Location(enterX,enterY,1);
	}

	/**
	 * @return the ground
	 */
	public ArrayList<String> getGround() {
		return ground;
	}

	/**
	 * @return the top
	 */
	public ArrayList<String> getTop() {
		return top;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the level
	 */
	public int getTLevel() {
		return tlevel;
	}
	/**
	 * @return the level
	 */
	public int getGLevel() {
		return glevel;
	}

	/**
	 * @return the playerOne
	 */
	public Location getPlayerOne() {
		return playerOne;
	}

	/**
	 * @return the playerTwo
	 */
	public Location getPlayerTwo() {
		return playerTwo;
	}

	/**
	 * @return the entrance
	 */
	public Location getEntrance() {
		return entrance;
	}

	public Location getKeyDoor(){
		return keyDoor;
	}

}
