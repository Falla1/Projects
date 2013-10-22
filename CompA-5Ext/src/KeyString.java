import java.util.Arrays;


public class KeyString {

	private String key;

	public KeyString(String key){
		this.key = key;
	}

	public String getValue() {
		return key;
	}

	public byte[] toByte(Bytes bytes){
		byte[] Bytes = new byte[60];
		char[] keyArray = key.toCharArray();
		for(int i = 0 ; i < 60 ; i ++){
			if(i < key.length()){
			Bytes[i] = bytes.intToByte(Integer.valueOf(keyArray[i]));
			}
		}
		

		return Bytes;
	}

	public static KeyString fromByte(byte[] bytes, Bytes method){
		String val = "";
		for(int i = 0 ; i < bytes.length ; i ++){
			val += (char)bytes[i];
		}
		return new KeyString(val);
	}
}
