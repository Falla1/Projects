import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
  Implements a B+ tree in which the keys are integers and the
  values are Strings (with maximum length 60 characters)
 */

public class BPlusTreeIntToString60 {

	private int Header = 5;
	private int Key = 4;
	private int BlockSize = 1024;
	private int InternalNodeSize = (BlockSize - Header - 4) / (Key + 4);
	private int blockNum = 1;

	private int Value = 120;
	private int LeafNodeSize = (BlockSize - Header - 4)/(Key + Value);
	
	private int rootNum = 0;
	
	private BlockFile bf;
	private Node root;

	private Comparator compareKeyValue = new CompareKeyValue();

	public BPlusTreeIntToString60(BlockFile bf) {
		this.bf = bf;
	}
	
	public BPlusTreeIntToString60(BlockFile bf,boolean value) {
		this.bf = bf;
		try {
			//Reading the root into the file
			root = InternalNode.fromByte(bf.read(0), new Bytes());
		} catch (IOException e) {
		}
	}

	/**
	 * Returns the String associated with the given key,
	 * or null if the key is not in the B+ tree.
	 */
	public String find(int key){
		if(root.getSize() == 0)
			return null;
		else
			try {
				return find(key,root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	private String find(int intKey, Node node) throws IOException{
		Key key = new Key(intKey);
		if(node instanceof LeafNode){
			LeafNode lNode = (LeafNode) node;
			//iterates through the key-values in the leaf node
			//if value equal, return the string
			for(int i = 0 ; i < node.getSize(); i++){
				if(intKey == (Integer)lNode.getKey(i).getValue()){
					return (String) lNode.getValue(i).getValue();
				}
			}
		}
		if(node instanceof InternalNode){
			//Loops through 1 - n number of keys
			InternalNode iNode = (InternalNode) node;
			for(int i = 1 ; i < iNode.getSize(); i ++){
				//if key is smaller than nodes key at i
				if((Integer)key.getValue() < (Integer)iNode.getKey(i).getValue()){
					//read in the child on left of key
					byte[] b = bf.read(iNode.getChild(i - 1));
					//Create the correct node type and recurseivly call find
					if(getType(b) == 0){
						LeafNode child = LeafNode.fromByte(bf.read(iNode.getChild(i - 1)), new Bytes());
						return find(intKey,child);
					}
					else {
						InternalNode child = InternalNode.fromByte(bf.read(iNode.getChild(i - 1)), new Bytes());
						return find(intKey,child);
					}
				}
			}
			//Gets the last child in the node as key is larger than any key in the node
			byte[] b = bf.read(iNode.getChild(iNode.getTrueSize()));
			if(getType(b) == 0){
				LeafNode child = LeafNode.fromByte(bf.read(iNode.getChild(iNode.getTrueSize())), new Bytes());
				return find(intKey,child);
			}
			else {
				InternalNode child = InternalNode.fromByte(bf.read(iNode.getChild(iNode.getTrueSize())), new Bytes());
				return find(intKey,child);
			}
		}
		return null;
	}

	
	/**
	 * Stores the value associated with the key in the B+ tree.
	 * If the key is already present, replaces the associated value.
	 * If the key is not present, adds the key with the associated value
	 * @param key 
	 * @param value
	 * @return whether pair was successfully added.
	 * @throws IOException 
	 */
	public boolean put(int tKey, String tValue) throws IOException{
		Key key = new Key(tKey);
		ValueString value = new ValueString(tValue);
		if(root == null){
			//First time adding something
			//Create node and write it twice, to 0 and 1
			LeafNode tempNode = new LeafNode(LeafNodeSize,blockNum ++);
			tempNode.addKV(key, value);
			root = tempNode;
			bf.write(tempNode.toByte());
			bf.write(tempNode.toByte());
			rootNum = 0;
		}
		else{
			ReturnValue val = add(key,value,root);
			if(val != null){
				//Creating a new node at the top of the tree
				InternalNode intNode = new InternalNode(InternalNodeSize,blockNum ++);
				intNode.insertKC(null,root.getBlockNum(),0);
				intNode.insertKC(val.getKey(), val.getValue().getBlockNum(),1);
				root = intNode;
				bf.write(intNode.toByte());
				bf.write(intNode.toByte(),0);
				rootNum = blockNum - 1;
			}
		}
		return true;
	}

	private ReturnValue add(Key key, ValueString value, Node node) throws IOException{
		if(node instanceof LeafNode){
			if(node.getSize() < LeafNodeSize){ 
				//If room in leaf node, add key to leaf
				LeafNode leafNode = (LeafNode) node;
				leafNode.addKV(key,value);
				bf.write(leafNode.toByte(), leafNode.getBlockNum());
				return null;
			}
			else{
				//If no room in leaf, have to split the leaf
				return splitLeaf(key,value,(LeafNode) node);
			}
		}
		if(node instanceof InternalNode){
			InternalNode nodeI = (InternalNode) node;
			int nodeSize = nodeI.getSize();
			//Loops through 1 - n number of keys
			for(int i = 1 ; i < nodeSize ; i ++){
					if((Integer)key.getValue() < (Integer)nodeI.getKey(i).getValue()){
						//Read the child on the left of the key
						byte[] b = bf.read(nodeI.getChild(i - 1));
						ReturnValue returnTmp = null;
						//Need to check if its leaf node or internal node
						if(getType(b) == 0){
							LeafNode child = LeafNode.fromByte(b, new Bytes());
							returnTmp = add(key,value,child);
						}
						else {
							InternalNode child = InternalNode.fromByte(b, new Bytes());
							returnTmp = add(key,value,child);
						}				
						
						//If there was a split occured, deal with promote
						if(returnTmp == null)
							return null;
						else
							return dealWithPromote(returnTmp.getKey(),returnTmp.getValue(),(InternalNode)node);
					}
				
			}
			
			byte[] b = bf.read(nodeI.getChild(nodeI.getTrueSize()));
			ReturnValue returnTmp = null;
			if(getType(b) == 0){
				LeafNode child = LeafNode.fromByte(b, new Bytes());
				returnTmp = add(key,value,child);
			}
			else {
				InternalNode child = InternalNode.fromByte(b, new Bytes());
				returnTmp = add(key,value,child);
			}
			
			//If there was a split occured, deal with promote
			if(returnTmp == null)
				return null;
			else
				return dealWithPromote(returnTmp.getKey(),returnTmp.getValue(),(InternalNode)node);
		}
		return null;
	}
	
	private int getType(byte[] bytes){
		return bytes[0];
	}

	private ReturnValue dealWithPromote(Key key, Node node2, InternalNode node) throws IOException{
		//Checking input is valid
		if(key == null && node2 == null)
			return null;
		//If Key is greater than largest key in node
		//Add key to the end of key list, and add node2 as the child
		if((Integer)key.getValue() > (Integer)node.getKey(node.getTrueSize()).getValue()){
			node.insertKC(key,node2.getBlockNum(),node.getTrueSize() + 1);
			//Rewrites the node in the file
			bf.write(node.toByte(), node.getBlockNum());
			if(rootNum == node.getBlockNum()){
				//If node is root, write to first block
				bf.write(node.toByte(),0);
			}
		}
		else {
			int nodeSize = node.getSize();
			//Determines and inserts where the key child is meant to be in the node
			for(int i = 1 ; i < nodeSize  ; i ++){
				if((Integer)key.getValue() < (Integer)node.getKey(i).getValue()){
					node.insertKC(key,node2.getBlockNum(),i);
					bf.write(node.toByte(), node.getBlockNum());
					if(rootNum == node.getBlockNum()){
						bf.write(node.toByte(),0);
					}
					break;
				}
			}
		}
		//Checking if need to create a new internal node
		if(node.getSize() < InternalNodeSize){
			return null;
		}
		
		
		InternalNode sibling = new InternalNode(InternalNodeSize,blockNum ++);

		//getting the mid of the node
		int mid = (node.getSize() / 2) + 1;

		int j;
		int i = 0;
		int size = node.getSize();
		
		//Inserting the mid - size of the children and keys from node to sibling
		for(j = mid; j < size ; j++ ){
			sibling.insertChild(node.getChild(j),i ++);
		}
		
		for(j = mid; j < size ; j ++){
			node.removeChild(j);
		}

		i = 1;
		for(j = mid + 1; j < size ; j++ ){
			sibling.insertKey(node.getKey(j),i++);
		}
		
		for(j = mid + 1; j < size ; j ++){
			node.removeKey(j);
		}

		Key promoteKey = node.getKey(mid);
		node.removeKey(mid);
		
		//Updates the old block in the file
		//Writes the new block
		bf.write(sibling.toByte());
		bf.write(node.toByte(), node.getBlockNum());
		if(rootNum == node.getBlockNum()){
			bf.write(node.toByte(),0);
		}
		
		return new ReturnValue(promoteKey,sibling);
	}

	private ReturnValue splitLeaf(Key key, ValueString value, LeafNode node) throws IOException{
		node.addKV(key, value);
		//Creating a new leaf node
		LeafNode sibling = new LeafNode(LeafNodeSize,blockNum ++);
		
		//Gets the middle of the leafnode
		int mid = (node.getSize() + 1) / 2;
		int size = node.getSize();

		//Splits the value of the leaf node into two
		for(int i = mid; i < size ; i ++){
			sibling.addKV(node.getKey(mid), node.getValue(mid));
			node.remove(mid);
		}
		
		//Sets the right values of the leafnode
		sibling.setRight(node.getRight());
		node.setRight(sibling.getBlockNum());
		
		//Rewrites the old node, and writes the new one
		bf.write(sibling.toByte());
		bf.write(node.toByte(), node.getBlockNum());
		if(rootNum == node.getBlockNum()){
			bf.write(node.toByte(),0);
		}
		
		return new ReturnValue(sibling.getKey(0),sibling);
	}



}
