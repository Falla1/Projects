import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeafNodeString implements Node{

	private int leafRight;

	private List<KeyValueString> values;
	private int maxSize;
	private Comparator compare = new CompareKeyValue();
	private int blockNum;
	
	public LeafNodeString(int keySize, int BlockNumber){
		values = new ArrayList<KeyValueString>();
		blockNum = BlockNumber;
		
	}
	
	public int getRight(){
		return leafRight;
	}
	
	public void setRight(int right){
		this.leafRight = right;
	}
	
	public int getSize(){
		return values.size();
	}
	
	public KeyString getKey(int i){
		return values.get(i).getKey();
	}

	public Value getValue(int i) {
		return values.get(i).getValue();
	}
	
	public int getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	public void addKV(KeyString k , Value v){
		values.add(new KeyValueString(k,v));
		Collections.sort(values, compare);
	}

	public void remove(int i) {
		values.remove(i);
	}

	public byte[] toByte(){
		Bytes method = new Bytes();
		byte[] bytes = new byte[1024]; //block size
		
		int bytePos = 0;
		
		bytes[bytePos++] = method.intToByte(0);
		
		byte[] tempReturn = method.intToBytes(getSize());
		//Size is 5 now
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		
		
		tempReturn = method.intToBytes(blockNum);
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		
		
		//TODO Need to add pointer to the next node in here
		tempReturn = method.intToBytes(leafRight);
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;

		for(int i = 0 ; i < getSize() ; i ++){
			tempReturn = values.get(i).toByte(method);
			addToBytes(bytes, tempReturn, bytePos);
			bytePos += 64;
		}
		
		
		return bytes;
	}

	
	public void addToBytes(byte[] bytes, byte[] temp, int startPos){
		for(int i = 0 ; i < temp.length; i ++){
			bytes[startPos++] = temp[i];
		}
	}
	
	public static LeafNodeString fromByte (byte[] bytes, Bytes method){
		//TODO FINISH THIS
		int pos = 1;
		
		byte[] tempBytes = Arrays.copyOfRange(bytes, pos, pos + 4);
		pos += 4;
		
		int size = method.bytesToInt(tempBytes);
		tempBytes = Arrays.copyOfRange(bytes, pos,  pos + 4);
		pos += 4;
		
		int blockNum = method.bytesToInt(tempBytes); //TODO
		tempBytes = Arrays.copyOfRange(bytes, pos,  pos + 4);
		pos += 4;
		
		LeafNodeString leaf = new LeafNodeString(size,blockNum);

		
		leaf.setRight(method.bytesToInt(tempBytes));
		
		tempBytes = Arrays.copyOfRange(bytes, pos, pos + 64);
		
		for(int i = 0 ; i < size ; i ++) {
			KeyValueString value = KeyValueString.fromByte(tempBytes, method);
			pos += 64;
			tempBytes = Arrays.copyOfRange(bytes, pos, pos + 64);
			leaf.addKV(value.getKey(),value.getValue());
		}
		return leaf;
	}
}

class CompareKeyValue implements Comparator{

	@Override
	public int compare(Object arg0, Object arg1) {
		KeyString k1 = ((KeyValueString) arg0).getKey();
		KeyString k2 = ((KeyValueString) arg1).getKey();

		int val = ((String)k1.getValue()).compareTo((String)k2.getValue());
		return val;
	}

}
