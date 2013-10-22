import java.util.Arrays;


public class KeyValueString {

	private KeyString key;
	private Value value;
	
	public KeyValueString(KeyString key, Value value){
		this.key = key;
		this.value = value;
	}

	public Value getValue() {
		return value;
	}
	
	public KeyString getKey() {
		return key;
	}
	
	public byte[] toByte(Bytes method) {
		byte[] returnByte = key.toByte(method);
		int length = returnByte.length;
		byte[] tempByte = value.toByte(method);
		
		returnByte = Arrays.copyOf(returnByte,returnByte.length + tempByte.length);
		
		for(int i = 0 ; i < tempByte.length; i ++){
			returnByte[i + length] = tempByte[i];
		}
		
		return returnByte;
	}
	
	public static KeyValueString fromByte(byte[] bytes, Bytes method) {
		byte[] key = Arrays.copyOf(bytes, 60);
		byte[] value = Arrays.copyOfRange(bytes, 60,64);
		KeyString keys = KeyString.fromByte(key, method);
		Value v = Value.fromByte(value,method);
		return new KeyValueString(keys,v);
	}
}
