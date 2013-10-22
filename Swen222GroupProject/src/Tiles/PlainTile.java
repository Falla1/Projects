package Tiles;
/**
 * represents the standard tiles exisitn ont he game board
 *@author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class PlainTile extends Tile {
	private String type="";

	public PlainTile(String type) {
		super(type);
		this.type = type;
		if(this.type.equals("b")){
			setImage(loadImage("src/images/tileBLUE.png"));
		}
		else if (this.type.equals("m")){
			setImage(loadImage("src/images/tilePURPMVBLECRKED.png"));
			super.setMoveable(true);
		}
		else if (this.type.equals("x")){
			setImage(loadImage("src/images/tilePURP.png"));
		}
		else if (this.type.equals("r")){
			setImage(loadImage("src/images/tileRED.png"));
		}
		else if (this.type.equals("!")){
			setImage(loadImage("src/images/tileYELLOW.png"));
		}
	}

}

