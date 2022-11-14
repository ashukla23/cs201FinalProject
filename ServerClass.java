package csci201_groupProject;

import java.awt.image.PixelInterleavedSampleModel;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.LookAndFeel;


public class ServerClass {
	
	public static void main(String[] args) {
		
		//game thread and connecting thread
		
		//value for number of connected clients
		int connections = 0;
		
		//port, intialise the server
		int port = 3456;
		ServerSocket server = null;
		Socket socket;
		
		
		//Synchronised list of client threads
		List<ServerThread> players = Collections.synchronizedList(new ArrayList<ServerThread>());
		
		
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
				} catch (Exception e) {
				}
				
				
			} else {
				
				System.out.println("There are currently " + connections + " Player(s)");
				
				
				
				//Play game + look for new connections
				
				
				//look for new connections
				
				//make a new thread for this? Will be blocked by next loop
				try {
					socket = server.accept();
					
					//create client thread, add it to the list
					connections++;
					
					ServerThread tempServerThread = new ServerThread(socket, port);
					tempServerThread.sendMessage("Welcome!");
					tempServerThread.sendMessage("You are player " + connections);
					
					
					players.add(tempServerThread);	
					
				} catch (Exception e) {
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
						if(players.get(i).getBetting()) {
							betsMade++;
						}
					}
				}
				
				
				
				
				
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
				
				
				int playersDone = 0;
				while(playersDone < players.size()) {
					playersDone = 0;
					for(int i = 0; i < players.size(); i++) {
						if(!players.get(i).getPlaying()) {
							playersDone++;
						}
					}
				}	
			
			
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
		}
	}
}
