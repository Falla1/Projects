
public class ReturnValueString {

	private KeyString key;
	private Node node;
	
	public ReturnValueString(KeyString key, Node node){
		this.key = key;
		this.node = node;
	}

	public Node getValue() {		
		return node;
	}
	
	public KeyString getKey(){
		return key;
	}
}
