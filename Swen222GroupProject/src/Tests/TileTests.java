package Tests;

import org.junit.Test;

import Tiles.*;

/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class TileTests {

@Test
public void testTriEntrace(){
	new TriangleEntrance("p");
	TriangleEntrance teL = new TriangleEntrance("l");
	teL.getImage();
	teL.isMoveable();
	teL.setMoveable(false);
}
@Test
public void sphereTriEntrace(){
	new SphereEntrance("s");
	new SphereEntrance("l");
}
@Test
public void squareTriEntrace(){
	new SquareEntrance("left");
	try {
		new SquareEntrance("l");
	} catch (Exception e) {
		//ok we expect this to fail

	}
}
@Test public void testplainTile(){
	new PlainTile("b");
	new PlainTile("m");
	new PlainTile("x");
	new PlainTile("r");
	new PlainTile("!");
	
	try {
		new PlainTile("t");
	
	} catch (Exception e) {
		// TODO: handle exception
	}

}
}
