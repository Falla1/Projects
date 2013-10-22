package Tests;

import org.junit.Test;

import Game.Location;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class LocationTests {
	
	
@Test
public void testLocation(){
	Location l = new Location(0,0,1);
	Location locTwo = new Location(0,1,1);
	
	l.setTileName("!");
	assert(l.getTileName().equals("!"));
	assert(locTwo.getTileName()==".");
}

}
