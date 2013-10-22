package Tests;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;
import Game.*;
import Player.*;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class GameTests {
	private List<Image> images;
	public GameTests(){
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
	@Test
	public void testGameLoadConstructor() {
		Board b = new Board("Map1R1.xml");
		Player p1 = new CubePlayer(new Location(0, 3, 1), 0, images);
		Player p2 = new SpherePlayer(new Location(1, 5, 1), 1, images);
		Player p3 = new PyramidPlayer(new Location(1,6,1),1, images);
		Game g = new Game(b, p1, p2,1,1);
		assert(g.getPlayers().contains(p1));
		assert(g.getPlayers().contains(p2));
		assert(!g.getPlayers().contains(p3));

	}
	@Test
	public void testGameConstructor() {
		Board b = new Board("Map1R1.xml");
		new CubePlayer(new Location(0, 3, 1), 0, images);
		new SpherePlayer(new Location(1, 5, 1), 1, images);
		Player p3 = new PyramidPlayer(new Location(1,6,1),1, images);
		Game g = new Game(b,1,1);
		
		
		g.addPlayer(3, p3.getLocation(), 2);
		g.addPlayer(0, p3.getLocation(), 2);//testing edges of switch

	}
@Test 
public void testChangeRoom(){
	Board b = new Board("Map1R1.xml");
	Game g = new Game(b,1,1);
	g.changeRoom(2);
	g.changeRoom(1);
	g.changeRoom(4);//edge test for changeRoom
}

@Test
public  void testGameGetters(){
	Board b = new Board("Map1R1.xml");
	Game g = new Game(b,1,1);
	assert(g.getBoard()==b);
	
	g.getPlayer(4);
	g.isStarted();
	g.getState();
	g.changeState(0);
	
}

@Test
public void testToByteArray(){
	Board b = new Board("Map1R1.xml");
	Game g = new Game(b,1,1);
	g.toByteArray();

}
@Test
public void testChangePlayer(){
	Board b = new Board("Map1R1.xml");
	Game g = new Game(b,1,1);

	g.addPlayer(2, new Location(0,0,1), 0);
	g.changePlayer(0, 1);
	g.changePlayer(0, 2);
	g.changePlayer(0, 3);
	g.changePlayer(0, 4);
}

}
