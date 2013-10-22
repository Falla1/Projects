
public class Value {

	private int value;
	
	public Value(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	
	public byte[] toByte(Bytes bytes){
		return bytes.intToBytes(value);
	}

	public static Value fromByte(byte[] value, Bytes method) {
		return new Value(method.bytesToInt(value));
	}
}
