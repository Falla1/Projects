
public class KeyInternal {

	private Node node;
	private Key key;
	private int keyVal;

	public KeyInternal(Node node, Key key){
		this.node = node;
		this.key = key;
		if(key != null)
			keyVal = (Integer)key.getValue();
		else
			keyVal = -1;
	}

	public int getKeyValue(){
		return keyVal;
	}

	public Node getNode() {
		return node;
	}

	public Key getKey() {
		return key;
	}



}
