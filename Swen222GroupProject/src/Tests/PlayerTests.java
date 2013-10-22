package Tests;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import Game.Board;
import Game.Game;
import Game.Location;
import Player.*;
import GameParts.*;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class PlayerTests {
	
	private List<Image> images;
	public PlayerTests(){
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

	/**
	 * Test for Cube constructor
	 * */
	@Test
	public void testCubePlayerConstrutor() {
		Player cp = new CubePlayer(new Location(0, 0, 1),0,images);
		assert(cp.getImage()!=null);
		assert (cp instanceof CubePlayer);
		ShapeItem s = new ShapeItem("Pyramid");
		cp.collectMagicItem(s,null);
		assert (cp.hasItem(s));
	}
	/**
	 * Test for Sphere constructor
	 * */
	@Test
	public void testSpherePlayerConstrutor() {
		Player sp = new SpherePlayer(new Location(0, 0, 1),0,images);
		assert (sp instanceof SpherePlayer);
		ShapeItem s = new ShapeItem("Pyramid");
		sp.collectMagicItem(s,null);
		assert (sp.hasItem(s));

	}
/**
 * Test for Pyramid constructor
 * */
	@Test
	public void testPyramidPlayerConstrutor() {
		Player pp = new PyramidPlayer(new Location(0, 0, 1),0,images);
		assert (pp instanceof PyramidPlayer);
		ShapeItem s = new ShapeItem("Cube");
		pp.collectMagicItem(s,null);
		assert (pp.hasItem(s));
	}
/**
 * Tst for changing Shape function*/
	@Test
	public void testChangeShapeCube() {
		Player pp = new PyramidPlayer(new Location(0, 0, 1),0,images);
		ShapeItem s = new ShapeItem("Cube");
		pp.collectMagicItem(s,null);
		assert (pp.hasItem(s));
		Player cp = new CubePlayer(pp,s,0);
		pp = cp;
		assert (pp.getBackPack() == cp.getBackPack());
		assert (pp instanceof CubePlayer);

	}
	/**
	 * Tst for changing Shape function for Sphere*/
		@Test
		public void testChangeShapeSphere() {
			Player sp = new SpherePlayer(new Location(0, 0, 1),2,images);
			ShapeItem s = new ShapeItem("o");
			Player cp = new SpherePlayer(sp,s,0);
			sp = cp;
			assert (sp.getBackPack() == cp.getBackPack());
			assert (sp instanceof SpherePlayer);
			assert(sp.toString().equals("c"));

		}
		/**
		 * Tst for changing Shape function for Sphere*/
			@Test
			public void testChangeShapePyra() {
				Player pp = new PyramidPlayer(new Location(0, 0, 1),2,images);
				ShapeItem s = new ShapeItem("o");
				Player cp = new PyramidPlayer(pp,s,0);
				pp = cp;
				assert (pp.getBackPack() == cp.getBackPack());
				assert (pp instanceof PyramidPlayer);
				assert(pp.toString().equals("p"));

			}


	/**
	 * Test for uniqueness of items in Player BackPack
	 */
	@Test
	public void testBackPackUniqueItemsOnly() {
		Player cp = new CubePlayer(new Location(0, 0, 1),0,images);
		ShapeItem s = new ShapeItem("o");
		cp.collectMagicItem(s,null);
		ShapeItem x = new ShapeItem("o");
		cp.collectMagicItem(x,null);
		assert (cp.getBackPack().getInventory().size() == 2);
		ShapeItem c = new ShapeItem("Cube");
		cp.collectMagicItem(c,null);
		assert (cp.getBackPack().getInventory().size() == 3);
	}
	@Test
	public void testBackPackUniqueItemsOnlySphere() {
		Player sp = new SpherePlayer(new Location(0, 0, 1),0,images);
		ShapeItem s = new ShapeItem("o");
		sp.collectMagicItem(s,null);
		ShapeItem x = new ShapeItem("o");
		sp.collectMagicItem(x,null);
		assert (sp.getBackPack().getInventory().size() == 1);
		ShapeItem c = new ShapeItem("+");
		sp.collectMagicItem(c,null);
		assert (sp.getBackPack().getInventory().size() == 2);
	}
	/**Tests player moving functions
	 * */
	@Test
	public void testPlayerMovement(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		p.moveDown(g);
		p.moveLeft(g);
		p.moveDown(g);
		p.moveUp(g);
		p.moveRight(g);

	}
	@Test
	public void testMovingUp(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
		p.moveUp(g);
	}
	@Test
	public void testMovingToBlockedPos(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		for (int i = 0; i < 11; i++) {
			p.moveRight(g);
		}
		for (int i = 0; i < 6; i++) {
			p.moveUp(g);
		}
		p.moveUp(g);
	}
	@Test
	public void testMovingToMoveablePos(){
		Board b = new Board("Map_6.xml");
		Location[][] larr = b.get(1);
		Location mover=null;
		for (int i = 0; i < larr.length; i++) {
			for (int j = 0; j < larr[i].length; j++) {
				if (larr[i][j]!=null) {
					if (larr[i][j].isMoveable()) {
						mover = larr[i][j];
					}
				}
			}
		}
		System.out.println(mover.getX()+""+mover.getY());
		
		
		Game g = new Game(b,1, 1);
		Location l = new Location(24,17,1);
		Player p = new CubePlayer(l,1,images);
//		for (int i = 0; i < 16; i++) {
//			p.moveRight(g);
//		}
//		for (int i = 0; i < 6; i++) {
//			p.moveUp(g);
//		}
		System.out.println(p.getLocation().getX()+""+p.getLocation().getY());
		p.moveRight(g);
		p.moveRight(g);
		p.moveRight(g);
		p.moveRight(g);
		p.moveRight(g);
		System.out.println(p.getLocation().getX()+""+p.getLocation().getY());
	}
	@Test
	public void testMovingToMagicItem(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		BackPack bp = new BackPack(p);
		p.setBackPack(bp);
		for (int i = 0; i < 61; i++) {
			p.moveRight(g);
		}
		
	}
	@Test
	public void testAddCoinToBackPack(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		BackPack bp = new BackPack(p);
		p.setBackPack(bp);
		Coin c = new Coin();
		p.collectMagicItem(c, g);
	}
	@Test
	public void testAddKeyToBackPack(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		BackPack bp = new BackPack(p);
		p.setBackPack(bp);
		Key k = new Key();
		k.setLocation(new Location(1,1,1));
		p.collectMagicItem(k, g);
	}
	@Test
	public void testAddWithoutBackPack(){
		Board b = new Board("Map_6.xml");
		new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
//		BackPack bp = new BackPack(p);
//		p.setBackPack(bp);
		Key k = new Key();

		assert(!p.hasItem(k));
		p.setBackPack(null);
		assert(!p.hasItem(k));
		
		p.backPackOutPut();
	}
	@Test
	public void testChestToBackPack(){
		Board b = new Board("Map_6.xml");
		Game g = new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		BackPack backPack = new BackPack(p);
		p.setBackPack(backPack);
		Chest chest = new Chest("#");
		chest.setLocation(new Location(1,1,1));
		backPack.addToPendingItems(chest.getPendingItems());
		assert(backPack.pendingSize()>0);
		for(GameObject i: p.getBackPack().getPending()){	
		p.collectMagicItem(i, g);
		}
		p.getBackPack().clearPending();
		assert(backPack.pendingSize()==0);
	}
	
	
	@Test
	public void testToggleInventory(){
		Board b = new Board("Map_6.xml");
		new Game(b,1, 1);
		Location l = new Location(0,0,1);
		Player p = new CubePlayer(l,1,images);
		p.toggleInventory();
		BackPack bp = new BackPack(p);
		p.setBackPack(bp);
		new Key();
		p.setShowInventory(0);
		p.toggleInventory();
		p.setShowInventory(1);
		p.toggleInventory();
		p.setHasBackPack(1);

	}
	@Test
	public void testToByteArray(){
		Player p = new CubePlayer(new Location (0,0,1), 1,images);
	try {
		p.toOutputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	@Test
	public void testFromByteArray(){
	String s = "0 0 0.2 0.2 2 1 0 1 11 /";
	String s2 = "0 0 0.2 0.2 2 2 0 1 11 /";
	String s3 = "0 0 0.2 0.2 2 3 0 1 111 /";
	String s4 = "0 0 0.2 0.2 2 3 0 1 1111 /";
	String s5 = "0 0 0.2 0.2 2 3 0 1 00001 /";
		
	byte[] ch = stringToBytesASCII(s);
		InputStreamReader input = new InputStreamReader(new ByteArrayInputStream(ch));
		try {
			Player p = Player.fromInputStream(input);
			p.toggleInventory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ch = stringToBytesASCII(s2);
	 	input = new InputStreamReader(new ByteArrayInputStream(ch));
		try {
			Player p = Player.fromInputStream(input);
			p.toggleInventory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ch = stringToBytesASCII(s3);
	 	input = new InputStreamReader(new ByteArrayInputStream(ch));
		try {
			Player p = Player.fromInputStream(input);
			p.toggleInventory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ch = stringToBytesASCII(s4);
	 	input = new InputStreamReader(new ByteArrayInputStream(ch));
		try {
			Player p = Player.fromInputStream(input);
			p.toggleInventory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ch = stringToBytesASCII(s5);
	 	input = new InputStreamReader(new ByteArrayInputStream(ch));
		try {
			Player p = Player.fromInputStream(input);
			p.toggleInventory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testUpdateBackPack(){
		Board b = new Board("Map_6.xml");
		new Game(b,1, 1);
		Location l = new Location(0,0,1);
	
		
		Player p = new CubePlayer(l,1,images);
		Player p1 = new CubePlayer(l,1,images);
		Player p2 = new CubePlayer(l,1,images);
		Player p3 = new CubePlayer(l,1,images);
		Player p4 = new CubePlayer(l,1,images);

		Player p5 = new CubePlayer(l,1,images);
		
		char[] invent = {'0','1'};
		char[] invent1 = {'1'};
		char[] invent2 = {'0','0','1'};
		char[] invent3 = {'0','0','0','1'};	
		char[] invent4 = {'0','0','0','0','1'};
		char[] invent5 = {'0','0','0','0','0','1'};
		
		p.updateBackPack(invent);
		p1.updateBackPack(invent2);
		p2.updateBackPack(invent3);
		p3.updateBackPack(invent4);
		p4.updateBackPack(invent5);
		p5.updateBackPack(invent1);
				
	}
	public static byte[] stringToBytesASCII(String str) {
		
		 char[] buffer = str.toCharArray();
		
		 byte[] b = new byte[buffer.length];
		
		 for (int i = 0; i < b.length; i++) {
		
		  b[i] = (byte) buffer[i];
		
		 }
		
		 return b;
		
		}

}
