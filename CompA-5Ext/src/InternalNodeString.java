import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class InternalNodeString implements Node{

	private KeyString[] keys;
	private int[] children;
	private int keyPos;
	private int childPos;
	private int blockNum;
	
	public InternalNodeString(int childrenSize, int BlockNumber){
		keys = new KeyString[childrenSize + 1];
		children = new int[childrenSize + 1];
		keyPos = 1;
		childPos = 0;
		blockNum = BlockNumber;
	}

	public int getChild(int i){
		return children[i];
	}

	public KeyString getKey(int i) {
		if(i == 0){
			throw new Error("Should not have asked for key 0");
		}
		return keys[i];
	}

	public int getSize(){
		return keyPos;
	}
	
	public int getTrueSize(){
		return keyPos - 1;
	}

	
	public int getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	public void insertKey(KeyString key,int i){
		if(key == null){
			throw new Error("Can not be null");
		}
		
		if(keys[i] != null){
			for(int j = keyPos; j > i ; j --){
				keys[j] = keys[j - 1];
			}
		}
		keys[i] = key;
		keyPos ++;
	}
	
	public void insertChild(int node,int i){
		if(children[i] != -1){
			for(int j = childPos; j > i ; j --){
				children[j] = children[j - 1];
			}
		}
		children[i] = node;
		childPos ++;
	}
	
	public void insertKC(KeyString key, int node,int i) {
		if(key == null && children[0] == 0){
			children[0] = node;
			childPos ++;
			return;
		}
		
		if(key == null){
			throw new Error("Can not be null");
		}
		
		insertKey(key,i);
		insertChild(node,i);
	}

	public void removeKey(int i) {
		keys[i] = null;
		keyPos--;
	}
	
	public void removeChild(int i){
		children[i] = -1;
		childPos --;
	}

	public byte[] toByte(){
		Bytes method = new Bytes();
		byte[] bytes = new byte[1024]; //block size
		
		int bytePos = 0;
		
		bytes[bytePos++] = method.intToByte(1);
		
		byte[] tempReturn = method.intToBytes(getSize());
		//Size is 5 now
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		
		tempReturn = method.intToBytes(blockNum);
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		
		tempReturn = method.intToBytes(children[0]);//Need to make it a reference number
		addToBytes(bytes, tempReturn, bytePos);
		bytePos += tempReturn.length;
		
		for(int i = 1 ; i < keyPos ; i ++){
			tempReturn = keys[i].toByte(method);
			addToBytes(bytes, tempReturn, bytePos);
			bytePos += 60;
			tempReturn = method.intToBytes(children[i]);//Need to make it a reference number
			addToBytes(bytes, tempReturn, bytePos);
			bytePos += 4;
		}
		
		
		return bytes;
	}
	
	public void addToBytes(byte[] bytes, byte[] temp, int startPos){
		for(int i = 0 ; i < temp.length; i ++){
			bytes[startPos++] = temp[i];
		}
	}
	
	public static InternalNodeString fromByte(byte[] Byte, Bytes method){
		int pos = 1;

		byte[] tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
		pos += 4;

		int size = method.bytesToInt(tempBytes);
		tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
		pos += 4;
		
		int blockNum = method.bytesToInt(tempBytes); //TODO
		tempBytes = Arrays.copyOfRange(Byte, pos,  pos + 4);
		pos += 4;
		
		InternalNodeString leaf = new InternalNodeString(size,blockNum);
		
		int ref = method.bytesToInt(tempBytes);
		leaf.insertChild(ref, 0);
		
		for(int i = 1 ; i < size ; i ++) {
			tempBytes = Arrays.copyOfRange(Byte, pos, pos + 60);
			pos += 60;
			KeyString key = KeyString.fromByte(tempBytes, method);
			tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
			pos += 4;
			ref = method.bytesToInt(tempBytes);
			leaf.insertKey(key, i);
			leaf.insertChild(ref, i);
		}

		return leaf;
	}
}

