package UserInterface;

import java.io.Serializable;
/**
 * The class is responsible for a single chat message which is used to communicate between clients
 * The ChatPanel class uses chat messages to send messages between the clients
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class ChatMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2211515900387802685L;
	private String message="";

	public ChatMessage(String s){
		this.setMessage(s);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	public void append(String s){
		this.message+="\n"+s;
	}

}
