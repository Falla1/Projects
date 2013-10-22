import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeafNode implements Node{

	private int leafRight;

	private List<KeyValue> values;
	private int maxSize;
	private Comparator compare = new CompareKey();
	private int blockNum;
	
	public LeafNode(int keySize, int BlockNumber){
		values = new ArrayList<KeyValue>();
		this.compare = compare;
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
	
	public Key getKey(int i){
		return values.get(i).getKey();
	}

	public ValueString getValue(int i) {
		return values.get(i).getValueFull();
	}
	
	public int getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	public void addKV(Key k , ValueString v){
		//Adds the value to Leaf, if there is already a key associated with the value
		//Adds the value to the key instead
		for(KeyValue key : values){
			if(key.getKey().getValue() == k.getValue()){
				key.addValueString(v);
			}
		}
		//Sorts the information
		values.add(new KeyValue(k,v));
		Collections.sort(values, compare);
	}

	public void remove(int i) {
		values.remove(i);
	}
	
	public byte[] toByte(){
		Bytes method = new Bytes();
		byte[] bytes = new byte[1024]; //block size
		
		int bytePos = 0;
		//Adding the type of node it is
		bytes[bytePos++] = method.intToByte(0);
		
		byte[] tempReturn = method.intToBytes(getSize());
		//Size is 5 now, added the size
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		
		//Adding the block number
		tempReturn = method.intToBytes(blockNum);
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		//Adding the reference to the right of the leaf
		tempReturn = method.intToBytes(leafRight);
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;

		//Loops through the values and adds them to the array of bytes
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
	
	public static LeafNode fromByte (byte[] bytes, Bytes method){
		
		int pos = 1;
		//Copying 1 - 5 of the bytes array into tempbytes
		byte[] tempBytes = Arrays.copyOfRange(bytes, pos, pos + 4);
		pos += 4;
		//Getting the size of the leafnode
		int size = method.bytesToInt(tempBytes); 
		tempBytes = Arrays.copyOfRange(bytes, pos,  pos + 4);
		pos += 4;
		//getting the blocknumber
		int blockNum = method.bytesToInt(tempBytes); 
		tempBytes = Arrays.copyOfRange(bytes, pos,  pos + 4);
		pos += 4;
		
		LeafNode leaf = new LeafNode(size,blockNum); 
		
		//Setting the node that is to the leaf of this one
		leaf.setRight(method.bytesToInt(tempBytes));
		
		tempBytes = Arrays.copyOfRange(bytes, pos, pos + 64);
		//Loops through 0 - size and adds the correct information into the node
		for(int i = 0 ; i < size ; i ++) {
			KeyValue value = KeyValue.fromByte(tempBytes, method);
			pos += 64;
			tempBytes = Arrays.copyOfRange(bytes, pos, pos + 64);
			leaf.addKV(value.getKey(),value.getValue());
		}
		return leaf;
	}
}

class CompareKey implements Comparator{

	@Override
	public int compare(Object arg0, Object arg1) {
		Key k1 = ((KeyValue) arg0).getKey();
		Key k2 = ((KeyValue) arg1).getKey();
		int val =Integer.compare((Integer)k1.getValue() , (Integer)k2.getValue());
		return val;
	}

}