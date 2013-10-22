package Tests;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.Test;

import Game.Location;
import GameParts.*;
import Player.CubePlayer;
import Player.Player;


/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class GamePartsTests {
	
/**
 * testing the getter and setters in the shape item
 */
	@Test
	public void shapeTypeTest() {
	 ShapeItem shape = new ShapeItem("+");
	 assertEquals(shape.getType(),"+");
	}

	@Test
	public void shapeLocationTest(){
		Location l =new Location(1,1,1);
		ShapeItem shape = new ShapeItem("+");
		shape.setLocation(l);
		assertEquals(shape.getLocation(),l);
	}

	@Test
	public void shapeImgTest(){
		ShapeItem shape = new ShapeItem("+");
		assertNotNull(shape.getImage());
	}

@Test
public void testChest(){
	new Chest ("#");
	Chest d = new Chest ("@");
	new Chest ("&");
	Chest g = new Chest ("*");
	
	new Chest ("p");
	
	new Chest("%");
	Image img = d.getImage();
	assert(img != null);
	
	assert(d.opened()==false);
	d.openChest();
	assert(d.opened() == true);
	assertFalse(d.isEmpty());
	int size  = d.getPendingItems().size();
	//adding
	d.addToInventory(new Coin());
	assert(size < d.getPendingItems().size());
	//removing
	GameObject coin2 =  d.takeItem(d.getPendingItems().size()-1);
	assert(coin2 instanceof Coin);
	assert(size == d.getPendingItems().size());
	
	//removeAll
	for (int i =0; i < d.getPendingItems().size(); i++){
		d.takeItem(i);
	}
	assert(0 == d.getPendingItems().size());
	//assert(d.isEmpty());
	
	//location
	Location l = new Location(0,0,1);
	g.setLocation(l);
	assert(g.getLocation()==l);
	assert(g.getType().equals("*"));
	
	g.setOpenLocation(l);
	assert(g.getOpenLocation()==l);
}
@Test 
public void testKey(){
	Key k = new Key();
	Location keyLoc  = new Location(0,0,1);
	k.setLocation(keyLoc);
	assert(k.getLocation()==keyLoc);
	assert(k.getType().equals("Key"));
	k.getImage();
	
	
}

@Test
public void testShapeItemCons(){
	ShapeItem sq = new ShapeItem("+");//square item
	assert(sq.getType().equalsIgnoreCase("+"));
	ShapeItem sp = new ShapeItem("o");//sphere item
	assert(!sp.getType().equalsIgnoreCase("+"));
	assert(sp.getType().equalsIgnoreCase("o"));
	
	ShapeItem py = new ShapeItem("^");//sphere item
	assert(py.getType().equals("^"));
}



@Test
public void testEmptyChest(){
	EmptyChest emptyCHest = new EmptyChest(">");
	EmptyChest emptyCHestTwo = new EmptyChest("<");
	emptyCHest.getLocation();
	
	Location newLoc = new Location(0,0,1);
	emptyCHestTwo.setLocation(newLoc);
	emptyCHestTwo.setOpenLocation(newLoc);
	
	Location opened = emptyCHestTwo.getOpenLocation();
	assert(newLoc==opened);
	
	Coin coin = new Coin();
	
	emptyCHestTwo.addToInventory(coin);
	assert(emptyCHestTwo.isEmpty()==false);
	emptyCHestTwo.removeFromInventory(coin);
	assert(emptyCHestTwo.isEmpty()==true);
	emptyCHestTwo.removeFromInventory(coin);
	
	assert(emptyCHest.opened()==false);
	emptyCHest.openChest();
	assert(emptyCHest.opened()==true);
	assert(emptyCHest.getType().equals(">"));
	
	emptyCHest.addToInventory(new Coin());
	emptyCHest.removeChestItem();
}


@Test 
public void testPlayerBackPack(){

	ArrayList<Image> images = new ArrayList<Image>();
	images.add(loadImage("src/images/squareYELLOW.png"));
	images.add(loadImage("src/images/pyramidYELLOW.png"));
	images.add(loadImage("src/images/sphereYELLOW.png"));
	
	Location l = new Location(0,0,1);
	Location bpL = new Location(0,0,1);
	Player cp = new CubePlayer(l,0,images);
	BackPack bp = new BackPack(cp);
	bp.setLocation(bpL);
	assert(bp.getLocation()==bpL);
	
	Coin c = new Coin();
	ShapeItem tri = new ShapeItem("^");
	bp.addItem(c);
	bp.addItem(tri);
	bp.addItem(c);
	bp.removeItem(c);
	
	Coin x = null;
	bp.addItem(x);
	bp.removeItem(x);
	
	bp.getImage();
	
	assert(bp.getType().equals("BackPack"));
	
	Chest bpChest = new Chest("%");
	bp.addItem(bpChest);
	
	assert(bp.hasPack()==true);
	
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


@Test
public void coinTest(){
	Coin c = new Coin();
	assert(c.getValue()==100);
	Location coinLoc = new Location(0,0,1);
	c.setLocation(coinLoc);
	assert(c.getLocation()==coinLoc);
	assert(c.getType()=="Coin");
	assertNotNull(c.getImage());
}
}
