package UserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import ClientServer.Slave;
import Game.Game;
/**
 * The ChatPanel which holds and updates the messages which are sent between the two players.
 * Also is resonable for sending messages through to the game.
 *@author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class ChatPanel extends JPanel {
	private String nothing = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel label;

	// to hold the Username and later on the messages

	private JTextArea tA;

	// to hold the server address an the port number

	

	// to Logout and get the list of the users

	private JButton send;

	// for the chat room

	private JTextPane tP;
	private ChatMessage c;
	private String player;

	private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	private Game g;

	public ChatPanel(final String player, final Gui gui, final Slave slave,Game g) {
		super();

		this.player = player;
		setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		this.g = g;
		//Creating a label with the players id
		label = new JLabel(player);

		//Creating text area and textpane
		tA = new JTextArea(1, 20);
		tP = new JTextPane();
		tP.setPreferredSize(new Dimension(300, 50));
		tP.setEditable(false);
		tP.setText("");

		tA.setPreferredSize(new Dimension(300, 20));
		tA.addKeyListener(new KeyListener() {

			//allows a user to either use the enter key to send a chat or the send button
			@Override
			public void keyTyped(KeyEvent e) {


				char cha = e.getKeyChar();
				//Check if someone presses the enter key
				if (cha == KeyEvent.VK_ENTER) {
					if (tA.getText().length() > 0) {
						tA.getSelectionEnd();
						//Setting c to the correct value
						if (c == null) {
							c = new ChatMessage(tA.getText());
						}
						else{
							c.setMessage(tA.getText());
						}
						//Set the text area to null
						tA.setText(nothing);

						//Add in to the text pane what was typed + the old text
						tP.setText(player + ": " + c.getMessage()+ "\n" + tP.getText());
						//Add players name to the message
						c.setMessage(player + ": " + c.getMessage());
						//Tell slave that there has been a message
						slave.updateChat(c);
						//Give focus back to the gui
						gui.requestFocus();
					}
				}

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		send = new JButton("Send chat");
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tA.getText().length() > 0) {
					tA.getSelectionEnd();
					//Setting c to the correct value
					if (c == null) {
						c = new ChatMessage(tA.getText());
					}
					else{
						c.setMessage(tA.getText());
					}
					//Set the text area to null
					tA.setText(nothing);

					//Add in to the text pane what was typed + the old text
					tP.setText(player + ": " + c.getMessage()+ "\n" + tP.getText());
					//Add players name to the message
					c.setMessage(player + ": " + c.getMessage());
					//Tell slave that there has been a message
					slave.updateChat(c);
					//Give focus back to the gui
					gui.requestFocus();
				}

			}
		});

		//Adding the swing components to this
		add(label, BorderLayout.WEST);
		add(tA, BorderLayout.CENTER);
		add(send, BorderLayout.EAST);
		add(tP, BorderLayout.EAST);
		this.repaint();

	}

	public Dimension setPreferedSize() {
		return new Dimension(100, 300);
	}

	public void checkMessage(){
		//If chatMessage is empty or the previous message doesnt equal the new message
		//And its not a message the owner of the chat panel sent
		//Then add message to chatMessages and update the textPane
		if(g != null && g.getMessage() != null){
			if((chatMessages.isEmpty() || 
					!chatMessages.get(chatMessages.size() - 1).getMessage().equals(g.getMessage().getMessage()))
					&& !g.getMessage().getMessage().contains(player)){
				chatMessages.add(g.getMessage());
				tP.setText(g.getMessage().getMessage() + "\n" + tP.getText());
			}
		}
	}
}
