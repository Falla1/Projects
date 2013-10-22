package ClientServer;
import Game.ServerMain;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class Client extends Thread{

	String ip;

	public Client(String ip){
		this.ip = ip;
	}

	@Override
	public void run() {
		new ServerMain(new String[] {"-connect", ip});
	}
}
