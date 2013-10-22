import java.util.Arrays;



public class InternalNode implements Node{

	private Key[] keys;
	private int[] children;
	private int keyPos;
	private int childPos;
	private int blockNum;

	public InternalNode(int childrenSize, int BlockNumber){
		keys = new Key[childrenSize + 1];
		children = new int[childrenSize + 1];
		keyPos = 1;
		childPos = 0;
		blockNum = BlockNumber;
	}

	public int getChild(int i){
		return children[i];
	}

	public Key getKey(int i) {
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


	public void insertKey(Key key,int i){
		if(key == null){
			throw new Error("Can not be null");
		}
		//Checks to make sure keys[i] is a valid key
		//Move all the other keys upward
		if(keys[i] != null){
			for(int j = keyPos; j > i ; j --){
				keys[j] = keys[j - 1];
			}
		}
		//Inserts key at i
		keys[i] = key;
		keyPos ++;
	}

	public void insertChild(int node,int i){
		//Makes sure child is valid
		//Moves all the other keys upward
		if(children[i] != -1){
			for(int j = childPos; j > i ; j --){
				children[j] = children[j - 1];
			}
		}
		//Inserts Child at i
		children[i] = node;
		childPos ++;
	}

	public void insertKC(Key key, int node,int i) {
		//Checking if it is the first child added
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

	public int getBlockNum() {
		return blockNum;
	}

	public byte[] toByte(){
		Bytes method = new Bytes();
		byte[] bytes = new byte[1024]; //block size

		int bytePos = 0;
		//Setting the first byte to 1
		bytes[bytePos++] = method.intToByte(1);
		
		byte[] tempReturn = method.intToBytes(getSize()); 
		//Size is 5 now, adding the size to the bytes
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		//Adding the block number to the bytes
		tempReturn = method.intToBytes(blockNum);
		addToBytes(bytes,tempReturn,bytePos);
		bytePos += tempReturn.length;
		//Adding child 0 to bytes
		tempReturn = method.intToBytes(children[0]);
		addToBytes(bytes, tempReturn, bytePos);
		bytePos += tempReturn.length;
		
		//Iterating over the length of keys, adding the key and child at that location to the array of bytes
		for(int i = 1 ; i < keyPos  ; i ++){
			tempReturn = keys[i].toByte(method);
			addToBytes(bytes, tempReturn, bytePos);
			bytePos += 4;
			tempReturn = method.intToBytes(children[i]);
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

	public static InternalNode fromByte(byte[] Byte, Bytes method){
		//Position 1, as we know what type it is
		int pos = 1;
		//Copies from 1 - 5 into tempbytes
		byte[] tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
		pos += 4;
		//determines the size from tempBytes
		int size = method.bytesToInt(tempBytes);
		tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
		pos += 4;
		//Determine the blocknumb
		int blockNum = method.bytesToInt(tempBytes); 
		tempBytes = Arrays.copyOfRange(Byte, pos,  pos + 4);
		pos += 4;
		
		InternalNode leaf = new InternalNode(size,blockNum);
		//Gettingt he reference to the first child
		int ref = method.bytesToInt(tempBytes);
		leaf.insertChild(ref, 0);
		//Iterating over the size of the node inserting the keys and children as it reads them
		for(int i = 1 ; i < size ; i ++) {
			tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
			pos += 4;
			Key key = Key.fromByte(tempBytes, method);
			tempBytes = Arrays.copyOfRange(Byte, pos, pos + 4);
			pos += 4;
			ref = method.bytesToInt(tempBytes);
			leaf.insertKey(key, i);
			leaf.insertChild(ref, i);
		}

		return leaf;
	}

}

