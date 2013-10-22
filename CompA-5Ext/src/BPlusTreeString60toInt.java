import java.io.IOException;
import java.util.Comparator;


/**
  Implements a B+ tree in which the keys  are Strings (with
  maximum length 60 characters) and the values are integers 
 */

public class BPlusTreeString60toInt {


	private int Header = 5;
	private int Key = 60;
	private int BlockSize = 1024;
	private int InternalNodeSize = (BlockSize - Header - 4) / (Key + 4);
	private int blockNum = 1;
	private int Value = 4;
	private int LeafNodeSize = (BlockSize - Header - 4)/(Key + Value);

	private BlockFile bf;
	private Node root;

	private Comparator compareKeyValue = new CompareKeyValue();
	private int rootNum = 0;


	public BPlusTreeString60toInt(BlockFile bf) {
		this.bf = bf;
	}

	public BPlusTreeString60toInt(BlockFile bf, boolean val) {
		this.bf = bf;
		try {
			root = InternalNodeString.fromByte(bf.read(0), new Bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Returns the String associated with the given key,
	 * or null if the key is not in the B+ tree.
	 */
	public Integer find(String key){
		if(root.getSize() == 0)//Should be checking fi empty
			return null;
		else
			try {
				return find(key,root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	private Integer find(String intKey, Node node) throws IOException{
		KeyString key = new KeyString(intKey);
		if(node instanceof LeafNodeString){
			LeafNodeString lNode = (LeafNodeString) node;
			for(int i = 0 ; i < node.getSize(); i++){
				if(intKey.equals((String)lNode.getKey(i).getValue().trim())){
					return (Integer) lNode.getValue(i).getValue();
				}
			}
		}
		if(node instanceof InternalNodeString){
			InternalNodeString iNode = (InternalNodeString) node;
			for(int i = 1 ; i < iNode.getSize(); i ++){
				if(((String)key.getValue()).compareTo(((String)iNode.getKey(i).getValue())) < 0){
					byte[] b = bf.read(iNode.getChild(i - 1));
					if(getType(b) == 0){
						LeafNodeString child = LeafNodeString.fromByte(bf.read(iNode.getChild(i - 1)), new Bytes());
						return find(intKey,child);
					}
					else {
						InternalNodeString child = InternalNodeString.fromByte(bf.read(iNode.getChild(i - 1)), new Bytes());
						return find(intKey,child);
					}
				}
			}
			byte[] b = bf.read(iNode.getChild(iNode.getTrueSize()));
			if(getType(b) == 0){
				LeafNodeString child = LeafNodeString.fromByte(bf.read(iNode.getChild(iNode.getTrueSize())), new Bytes());
				return find(intKey,child);
			}
			else {
				InternalNodeString child = InternalNodeString.fromByte(bf.read(iNode.getChild(iNode.getTrueSize())), new Bytes());
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
	public boolean put(String tKey, int iP) throws IOException{
		
		KeyString key = new KeyString(tKey);
		Value value = new Value(iP);
		if(root == null){
			LeafNodeString tempNode = new LeafNodeString(LeafNodeSize,blockNum++);
			tempNode.addKV(key, value);
			root = tempNode;
			bf.write(tempNode.toByte());
			bf.write(tempNode.toByte());
			rootNum = 0;
		}
		else{
			ReturnValueString val = add(key,value,root);
			if(val != null){
				InternalNodeString intNode = new InternalNodeString(InternalNodeSize,blockNum++);
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

	private ReturnValueString add(KeyString key, Value value, Node node) throws IOException{
		if(node instanceof LeafNodeString){
			if(node.getSize() < LeafNodeSize){
				LeafNodeString leafNode = (LeafNodeString) node;
				leafNode.addKV(key,value);
				bf.write(leafNode.toByte(), leafNode.getBlockNum());
				return null;
				//Insert key and value into leaf in correct place
			}
			else{
				return splitLeaf(key,value,(LeafNodeString) node);
			}
		}
		if(node instanceof InternalNodeString){

			InternalNodeString nodeI = (InternalNodeString) node;
			int nodeSize = nodeI.getSize();
			for(int i = 1 ; i < nodeSize ; i ++){

				if(((String)key.getValue()).compareTo((String)nodeI.getKey(i).getValue()) < 0){
					byte[] b = bf.read(nodeI.getChild(i - 1));
					ReturnValueString returnTmp = null;
					if(getType(b) == 0){
						LeafNodeString child = LeafNodeString.fromByte(bf.read(nodeI.getChild(i - 1)), new Bytes());
						returnTmp = add(key,value,child);
					}
					else {
						InternalNodeString child = InternalNodeString.fromByte(bf.read(nodeI.getChild(i - 1)), new Bytes());
						returnTmp = add(key,value,child);
					}				

					if(returnTmp == null)
						return null;
					else
						return dealWithPromote(returnTmp.getKey(),returnTmp.getValue(),(InternalNodeString)node);
				}

			}

			byte[] b = bf.read(nodeI.getChild(nodeI.getTrueSize()));
			ReturnValueString returnTmp = null;
			if(getType(b) == 0){
				LeafNodeString child = LeafNodeString.fromByte(bf.read(nodeI.getChild(nodeI.getTrueSize())), new Bytes());
				returnTmp = add(key,value,child);
			}
			else {
				InternalNodeString child = InternalNodeString.fromByte(bf.read(nodeI.getChild(nodeI.getTrueSize())), new Bytes());
				returnTmp = add(key,value,child);
			}
			if(returnTmp == null)
				return null;
			else
				return dealWithPromote(returnTmp.getKey(),returnTmp.getValue(),(InternalNodeString)node);
		}
		return null;
	}

	private ReturnValueString dealWithPromote(KeyString key, Node node2, InternalNodeString node) throws IOException{
		if(key == null && node2 == null)
			return null;

		if(((String)key.getValue()).compareTo((String)node.getKey(node.getTrueSize()).getValue()) > 0){
			node.insertKC(key,node2.getBlockNum(),node.getTrueSize() + 1);
			bf.write(node.toByte(), node.getBlockNum());
			if(rootNum == node.getBlockNum()){
				bf.write(node.toByte(),0);
			}
		}
		else {
			int nodeSize = node.getSize();
			for(int i = 1 ; i < nodeSize  ; i ++){
				if(((String)key.getValue()).compareTo((String)node.getKey(i).getValue()) < 0){
					node.insertKC(key,node2.getBlockNum(),i);
					bf.write(node.toByte(), node.getBlockNum());
					if(rootNum == node.getBlockNum()){
						bf.write(node.toByte(),0);
					}
					break;
				}
			}
		}
		if(node.getSize() < InternalNodeSize){
			return null;
		}
		InternalNodeString sibling = new InternalNodeString(InternalNodeSize,blockNum++);

		//adding two, as getSize() returns 1 less than actual size
		int mid = (node.getSize() / 2)  + 1;

		int j;
		int i = 0;
		int size = node.getSize();

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

		KeyString promoteKey = node.getKey(mid);
		node.removeKey(mid);

		bf.write(sibling.toByte());
		bf.write(node.toByte(), node.getBlockNum());
		if(rootNum == node.getBlockNum()){
			bf.write(node.toByte(),0);
		}

		return new ReturnValueString(promoteKey,sibling);
	}

	private ReturnValueString splitLeaf(KeyString key, Value value, LeafNodeString node) throws IOException{
		node.addKV(key, value);
		LeafNodeString sibling = new LeafNodeString(LeafNodeSize,blockNum++);

		int mid = (node.getSize() + 1) / 2;
		int size = node.getSize();
		int i = mid;

		for(; i < size ; i ++){
			sibling.addKV(node.getKey(mid), node.getValue(mid));
			node.remove(mid);
		}

		sibling.setRight(node.getRight());
		node.setRight(sibling.getBlockNum());

		bf.write(sibling.toByte());
		bf.write(node.toByte(), node.getBlockNum());
		if(rootNum == node.getBlockNum()){
			bf.write(node.toByte(),0);
		}
		return new ReturnValueString(sibling.getKey(0),sibling);
	}

	private int getType(byte[] bytes){
		return bytes[0];
	}


}
