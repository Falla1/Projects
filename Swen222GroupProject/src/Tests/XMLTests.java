package Tests;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.imageio.ImageIO;

import org.junit.Test;

import DataStorage.LoadSave;
import DataStorage.XMLReader;
import DataStorage.XMLWriter;
import Game.Location;
import Player.CubePlayer;
import Player.Player;
import Player.SpherePlayer;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class XMLTests {

	private List<Image> images;

	public XMLTests() {
		images = new ArrayList<Image>();
		images.add(loadImage("src/images/squareYELLOW.png"));
		images.add(loadImage("src/images/pyramidYELLOW.png"));
		images.add(loadImage("src/images/sphereYELLOW.png"));
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

	private final String testMap = "Map_6.xml";



	/**
	 * tests ensuring some data being read into the fields
	 */
	@Test
	public void testReadArrays() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		assertNotNull(reader.getGround());
		assertNotNull(reader.getTop());

	}

	@Test
	public void testReadIntegers() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		assertNotNull(reader.getHeight());
		assertNotNull(reader.getWidth());
		assertNotNull(reader.getTLevel());
		assertNotNull(reader.getGLevel());
	}

	/*
	 * tests checking the correct field data values
	 */
	@Test
	public void testHeight() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		assertEquals(reader.getHeight(), 40);
	}

	@Test
	public void testWidth() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		assertEquals(reader.getWidth(), 40);
	}

	@Test
	public void testGroundLevel() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		assertEquals(reader.getGLevel(), 0);
	}

	@Test
	public void testTopLevel() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		assertEquals(reader.getTLevel(), 1);
	}

	/**
	 * tests checking the arraylist values are correct
	 */
	@Test
	public void testGroundMap() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		String s = reader.getGround().get(reader.getGround().size() - 1);
		assertEquals(s, "bbbbbbbbbbbbbbbbbbbrbbbbbbbbbbbbbbbbbbbb");
	}

	@Test
	public void testTopMap1() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		String s = reader.getTop().get(2);
		assertEquals(s, "..xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx...");
	}

	@Test
	public void testTopMap2() {
		XMLReader reader = new XMLReader();
		reader.readXML(testMap);
		String s = reader.getTop().get(5);
		assertEquals(s, "..x.................................xxxx");
	}

	// ################################################################
	// game save tests
	/**
	 * checks the save is succesful
	 */
	@Test
	public void testSave() {
		XMLWriter w = createSave();
		assertTrue(w.writeToConsole());
	}

	/**
	 * checking the player set is correct
	 */
	@Test
	public void testSave2() {
		XMLWriter w = createSave();
		w.setPlayers(new SpherePlayer(new Location(1, 1, 1), 1, images),
				new CubePlayer(new Location(1, 1, 1), 1, images));
		int shape = w.getPlayer(1).getShape();
		assertTrue(shape == 3);
		shape = w.getPlayer(2).getShape();
		assertFalse(shape == 3);
	}

	@Test
	public void testSave3() {
		XMLWriter w = createSave();
		assertNull(w.getPlayer(0));
	}

	public XMLWriter createSave() {
		XMLWriter writer = new XMLWriter();
		Player one = new CubePlayer(new Location(1, 1, 1), 1, images);
		Player two = new SpherePlayer(new Location(1, 1, 1), 1, images);
		writer.setPlayers(one, two);
		return writer;
	}

	// ###########Loading Tests###############

	/**
	 * cant check absolute values because they change every save
	 */
	@Test
	public void loadSaveTest1() {
		//check players arent null
		LoadSave loader = new LoadSave();
		assertNotNull(loader.getP1());
		assertNotNull(loader.getP2());
	}

	@Test
	public void loadSaveTest2() {
		//check inventories are filled
		LoadSave loader = new LoadSave();
		assertNotNull(loader.getPlayerOneInventory());
		assertNotNull(loader.getPlayerTwoInventory());
	}
	@Test
	public void loadSaveTest3() {
		//check inventories are filled
		LoadSave loader = new LoadSave();
		assertNotNull(loader.getMapName());
	}
	@Test
	public void loadSaveTest4() {
		//check inventories are filled
		LoadSave loader = new LoadSave();
		assertNotNull(loader.getMapId());
	}
	@Test
	public void loadSaveTest5() {
		//check inventories are filled
		LoadSave loader = new LoadSave();
		assertNotNull(loader.getRoom());
	}
	@Test
	public void loadSaveTest6() {
		//check inventories are filled
		LoadSave loader = new LoadSave();
		assertNotNull(loader.getRemovedLocations());
	}

}
