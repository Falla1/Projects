
public class ReturnValue {

	private Key key;
	private Node node;
	
	public ReturnValue(Key key, Node node){
		this.key = key;
		this.node = node;
	}

	public Node getValue() {		
		return node;
	}
	
	public Key getKey(){
		return key;
	}
}
