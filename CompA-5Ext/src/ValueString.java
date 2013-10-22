import java.util.Arrays;


public class ValueString {

	private String value;
	
	public ValueString(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

	public static ValueString fromByte(byte[] value, Bytes method) {
		String val = "";
		for(int i = 0 ; i < value.length ; i ++){
			val += (char)value[i];
		}
		return new ValueString(val);
	}

	public byte[] toByte(Bytes bytes){
		byte[] Bytes = new byte[60];
		char[] keyArray = value.toCharArray();
		for(int i = 0 ; i < 60 ; i ++){
			if(i < value.length()){
				int charVal = Integer.valueOf(keyArray[i]);
				Bytes[i] = bytes.intToByte(charVal);
			}
		}
		
		return Bytes;
	}
	
}
