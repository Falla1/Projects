import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KeyValue {

	private Key key;
	private List<ValueString> value;
	
	public KeyValue(Key key, ValueString value){
		this.value = new ArrayList<ValueString>();
		this.key = key;
		this.value.add(value);
	}

	public ValueString getValue() {
		return value.get(0);
	}
	
	public ValueString getValueFull(){
		String str = "";
		for(ValueString s : value){
			str += s.getValue();
		}
		return new ValueString(str);
	}
	
	public Key getKey() {
		return key;
	}

	public byte[] toByte(Bytes method) {
		//Gets the key's byte information
		byte[] returnByte = key.toByte(method);
		int length = returnByte.length;
		
		int size = value.size();
		//Determines the size of the array of bytes
		byte[] tempByte = new byte[size * 60 + 1];
		//Sets the size
		tempByte[0] = method.intToByte(size);
		//Loops over adding every key value to the tempbyte
		for(int i = 0 ; i < size ;  i ++ ){
			byte[] t = value.get(i).toByte(method);
			for(int j = 0 ; j < 60 ; j ++){
				tempByte[(i * 60) + 1 + j] = t[j];
			}
		}
		//Creates a larger array to hold all the information
		returnByte = Arrays.copyOf(returnByte,returnByte.length + tempByte.length);
		//Puts the information from tempByte into returnByte
		//Would have been better to do returnByte to tempByte
		for(int i = 0 ; i < tempByte.length; i ++){
			returnByte[i + length] = tempByte[i];
		}
		return returnByte;
	}
	
	public static KeyValue fromByte(byte[] bytes, Bytes method) {
		//Creates a new key and value array 
		byte[] key = Arrays.copyOf(bytes, 4);
		byte[] value = Arrays.copyOfRange(bytes, 5, 65);
		//Gets the size
		int size = method.byteToInt(bytes[4]) - '0';
		//Creates the key and value
		Key keys = Key.fromByte(key, method);
		ValueString v = ValueString.fromByte(value,method);
		KeyValue keyValue = new KeyValue(keys,v);
		//Loops over if there are any additional key-values in the array
		for(int i = 1 ; i < size ; i ++){
			value = Arrays.copyOfRange(bytes, (i*60)+5, (i*60) + 65);
			ValueString newV = ValueString.fromByte(value, method);
			keyValue.addValueString(newV);
		}
		
		return keyValue;
	}

	public void addValueString(ValueString v) {
		value.add(v);
	}
}
