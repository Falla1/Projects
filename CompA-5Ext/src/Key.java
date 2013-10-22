
public class Key {

	private int key;
	
	public Key(int key){
		this.key = key;
	}

	public int getValue() {
		return key;
	}
	
	public byte[] toByte(Bytes bytes){
		return bytes.intToBytes(key);
	}
	
	public static Key fromByte(byte[] bytes, Bytes method){
		return new Key(method.bytesToInt(bytes));
	}
}
