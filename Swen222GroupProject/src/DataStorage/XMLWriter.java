package DataStorage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import Game.Location;
import Player.Player;

/**
 * writes a players details to xml for reloading into a game
 *this is an xml output class for representing the saved state of a working game
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class XMLWriter {
	private Player one;
	private Player two;
	private List<Location> removed;
	private String map;
	private int mapNum;
	private int roomId;

	/**
	 * empty constructor is for testing
	 */
	public XMLWriter() {

	}

	/**
	 * takes two players and then saves their details
	 *
	 * @param p1
	 * @param p2
	 */
	public XMLWriter(Player p1, Player p2,Set<Location> remove, String map, int mapNum, int roomId) {
		one = p1;
		two = p2;
		this.map = map;
		this.mapNum = mapNum;
		this.roomId = roomId;
		removed = new ArrayList<Location>();
		removed.addAll(remove);
		saveToXML();
	}


	/**
	 * saves player facts to an xml file saves the shape, id, locations and
	 * inventory of a player format as follows
	 *
	 * Start Document
	 *
	 * element name element content element closing bracket
	 *
	 * element name element content element closing bracket
	 *
	 * end document
	 */
	public void saveToXML() {

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		File directory = new File("src//DataStorage//Saves");

		try {
			XMLStreamWriter writer = factory
					.createXMLStreamWriter(new FileWriter(new File(directory,
							"//SaveTest.xml")));


			writer.writeStartDocument();// *
			// ####in the document

			writer.writeStartElement("saveGame");// start the element
			// player 1
			writer.writeStartElement("PlayerOne");// p

			writer.writeStartElement("PID-1");
			writer.writeCharacters(String.valueOf(one.id));
			writer.writeEndElement();

			writer.writeStartElement("PlayerOneShape");
			writer.writeCharacters(String.valueOf(one.getShape()));
			writer.writeEndElement();

			writer.writeStartElement("locX1");
			writer.writeCharacters(String.valueOf(one.getLocation().getX()));
			writer.writeEndElement();

			writer.writeStartElement("locY1");
			writer.writeCharacters(String.valueOf(one.getLocation().getY()));
			writer.writeEndElement();

			writer.writeStartElement("PlayerOneInventory");
			writer.writeCharacters(one.backPackOutPut());
			writer.writeEndElement();
			writer.writeEndElement();

			// player 2
			writer.writeStartElement("PlayerTwo");// p

			writer.writeStartElement("PID-2");
			writer.writeCharacters(String.valueOf(two.id));
			writer.writeEndElement();

			writer.writeStartElement("PlayerTwoShape");
			writer.writeCharacters(String.valueOf(two.getShape()));
			writer.writeEndElement();

			writer.writeStartElement("locX2");
			writer.writeCharacters(String.valueOf(two.getLocation().getX()));
			writer.writeEndElement();

			writer.writeStartElement("locY2");
			writer.writeCharacters(String.valueOf(two.getLocation().getY()));
			writer.writeEndElement();

			writer.writeStartElement("PlayerTwoInventory");
			writer.writeCharacters(two.backPackOutPut());
			writer.writeEndElement();
			writer.writeEndElement();

			writer.writeStartElement("playerOneScore");
			writer.writeCharacters(String.valueOf(one.getScore()));
			writer.writeEndElement();
			writer.writeStartElement("playerTwoScore");
			writer.writeCharacters(String.valueOf(two.getScore()));
			writer.writeEndElement();

			writer.writeStartElement("MapName");
			writer.writeCharacters(map);
			writer.writeEndElement();

			writer.writeStartElement("MapId");
			writer.writeCharacters(String.valueOf(mapNum));
			writer.writeEndElement();

			writer.writeStartElement("RoomId");
			writer.writeCharacters(String.valueOf(roomId));
			writer.writeEndElement();



			writer.writeStartElement("Locations");

			for(Location l: removed){
				writer.writeStartElement("locationXY");
				String x = String.valueOf(l.getX());
				String y = String.valueOf(l.getY());
				writer.writeCharacters(x+ " " + y);
				writer.writeEndElement();
			}

			writer.writeEndElement();// close start
			writer.writeEndElement();

			// ###document end
			writer.writeEndDocument();

			writer.flush();
			writer.close();

		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * tester code to write the save to console for testing
	 *
	 * @return
	 */
	public boolean writeToConsole() {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			XMLStreamWriter writer = factory.createXMLStreamWriter(System.out);
			writer.writeStartDocument();// *
			// ####in the document
			writer.writeStartElement("saveGame");// start the element
			// player 1
			writer.writeStartElement("PlayerOne");// p

			writer.writeStartElement("PID-1");
			writer.writeCharacters(String.valueOf(one.id));
			writer.writeEndElement();

			writer.writeStartElement("PlayerOneShape");
			writer.writeCharacters(String.valueOf(one.getShape()));
			writer.writeEndElement();
			writer.writeEndElement();

			// player 2
			writer.writeStartElement("PlayerTwo");// p

			writer.writeStartElement("PID-2");
			writer.writeCharacters(String.valueOf(two.id));
			writer.writeEndElement();

			writer.writeStartElement("PlayerTwoShape");
			writer.writeCharacters(String.valueOf(two.getShape()));
			writer.writeEndElement();

			writer.writeEndElement();

			writer.writeEndElement();// close start
			// ###document end
			writer.writeEndDocument();
			writer.flush();
			writer.close();

		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * this is only used for testing
	 *
	 * @param one
	 * @param two
	 */
	public void setPlayers(Player one, Player two) {
		this.one = one;
		this.two = two;
	}
	/**
	 * tester for the set players
	 * @param num
	 */
	public Player getPlayer(int num){
		switch(num){
		case 1: return this.one;
		case 2: return this.two;
		}
		return null;
	}

}
