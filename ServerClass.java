package csci201_groupProject;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.websocket.*; // for space
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint (value="/ws")//don't change this
public class ServerClass extends Thread {
	
	private int connections;
	private int port;
	private ServerSocket server;
	private Socket socket;
	private boolean inGame;
	private static Vector<Session> mySessions = new Vector<Session>();
	
	//Synchronised list of client threads
	List<ServerThread> players = Collections.synchronizedList(new ArrayList<ServerThread>());
	List<ServerThread> waitingPlayers = Collections.synchronizedList(new ArrayList<ServerThread>());
	
	
	public ServerClass() {
		//game thread and connecting thread
		
		//value for number of connected clients
				
		connections = 0;
				
		//port, intialise the server
		port = 3456;
		server = null;
		inGame = false;
				
			
				
		/*		
		try {
			server = new ServerSocket(port);
					
		}catch (Exception e) {
		}
				
				
		//create a deck for the game
					
				
				
		while(true) {
					
			if(connections < 1) {
					System.out.println("Waiting for at least one connection");
						
					try {
						socket = server.accept();
							
						//create client thread, add it to the LIST
						connections++;
							
						ServerThread tempServerThread = new ServerThread(socket, port);
						tempServerThread.sendMessage("Welcome!");
						tempServerThread.sendMessage("You are player " + connections);
							
							
						players.add(tempServerThread);		
						
						this.start();
						
					} catch (Exception e) {
					}
						
						
			} else {
						
				System.out.println("There are currently " + connections + " Player(s)");
						
						
						
				//Play game + look for new connections
				
				while(!waitingPlayers.isEmpty()) {
					Thread.yield();
				}
						
									
						
						
				//prompt for start
				for(int i = 0; i < players.size(); i++) {
					players.get(i).sendMessage("There are currently " + connections + " Player(s)");
					players.get(i).sendMessage("Please place your bet");
					players.get(i).inputWaiter();
					players.get(i).startBetting();
				}
						
						
				int betsMade = 0;
				while(betsMade < players.size()) {
					betsMade = 0;
					for(int i = 0; i < players.size(); i++) {
						if(!players.get(i).getBetting()) {
							betsMade++;
						}
					}
				}
				
				inGame = true;
						
						
						
						
						
				//play the game,
				//Potentially acquire a lock here to prevent new connections
						
						
				//get the cards
						
				//first the dealer
				int dealerSum = 0;
				//let the dealer take two cards
						
						
						
				//deal two cards to each player
				for(int i = 0; i < players.size(); i++) {
					players.get(i).sendMessage(players.get(i).cardString());
				}
						
						
				//loop through all the players until they are done playing
				int playersDone = 0;
				while(playersDone < players.size()) {
					playersDone = 0;
							
					for(int i = 0; i < players.size(); i++) {
						//if the player is not playing add to sum
						if(!players.get(i).getPlaying()) {
							playersDone++;
									
						} else {
							//if playing and wants card
							if(players.get(i).wantsCard()) {
								//give them a card
							}
						}
					}
				}
				
				inGame = false;
					
				//check player win/loss and reset the player threads
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).getSum() >= dealerSum && players.get(i).getSum() <= 21) {
						players.get(i).sendMessage("You won " + players.get(i).getBet());
						players.get(i).win();
					} else {
						players.get(i).sendMessage("You lost " + players.get(i).getBet());
						players.get(i).loss();
					}
							
					players.get(i).sendMessage("Your balance is " + players.get(i).getBalance());
				}
						
								
			}			
		}*/
	}
	

	//for web Sockets connections/we don't have to use serverthreads

	@OnOpen
	public void open(Session session) {
		System.out.println("Connection made");
		mySessions.add(session);
	}
	
	@OnMessage
	public void message(String message, Session session) {
		System.out.println(message);
		try {
			session.getBasicRemote().sendText("You sent a message");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting");
		mySessions.remove(session);
	}

	
	//public static void main(String[] args) {
		
		//ServerClass serverClass = new ServerClass();
		
	//}
	
	
	/*public void run() {
		//look for new connections
		
		while(connections < 4) {
			//accept new connections
			
			if(!inGame) {
				players.addAll(waitingPlayers);
				waitingPlayers.clear();
			}
			
			
			try {
				socket = server.accept();
			
				//create client thread, add it to the list
				connections++;
			
				ServerThread tempServerThread = new ServerThread(socket, port);
				tempServerThread.sendMessage("Welcome!");
				tempServerThread.sendMessage("You are player " + connections);
			
				if(!inGame) {
					players.add(tempServerThread);	
				} else {
					waitingPlayers.add(tempServerThread);
				}
			
			} catch (Exception e) {
			}
		}
	}*/
}
