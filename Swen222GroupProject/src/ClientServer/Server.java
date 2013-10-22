package ClientServer;

import Game.ServerMain;
/**
 * 
 * @author shawmarc 300252702 , watkinjame 300077392, rimmermich 301018584, minnssam 301003381
 *
 */
public class Server extends Thread{

	public Server(){
	}


	@Override
	public void run() {
		new ServerMain(new String[] {"-server","2"});
	}
}
