//package csci201_groupProject;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.websocket.*; // for space
import javax.websocket.server.ServerEndpoint;

import org.apache.jasper.tagplugins.jstl.core.Remove;


@ServerEndpoint (value="/ws")//don't change this
public class ServerClass extends Thread {
	
	private static Deck myDeck = new Deck();
	private int connections = 0;
	private int dealerScore = 0;
	//private Card card1 = myDeck.getCard();//my dealer cards
	//private Card card2 = myDeck.getCard();
	volatile private boolean inGame;
	private boolean gettingBets;
	private static Vector<Player> players = new Vector<Player>();
	private static Vector<Player> waitingPlayers = new Vector<Player>();
	private static List<Card> dealer_hand = new ArrayList<Card>();
	
	
	//for web Sockets connections/we don't have to use serverthreads
	public ServerClass() {
		System.out.println("Created Server");
		if(dealer_hand.size() == 0) {//if I have no cards
			for(int i = 0; i< 2; i++) {
				dealer_hand.add(myDeck.getCard());//creates hand for the dealer
			}
			dealerScore = getHandValue();
			if(dealerScore < 10) {
				dealer_hand.add(myDeck.getCard());
				dealerScore = getHandValue();
			}
		}
		
	}
	
	private int getHandValue()
	{
		// get initial total
		int total = 0;
		int aces = 0;
		for(Card card : dealer_hand) {
			total += card.getValue();
			if(card.getNumber().equals("A")) {
				aces++;
			}
		}
		
		// if we've bust, try to reduce ace values from 11 to 1
		while(total > 21) {
			if(aces > 0) {
				total -= 10;
				aces--;
			}
			else {
				break;
			}
		}
		
		// return
		return total;
	}
	
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection made");
		System.out.println(connections);
		Player temp = new Player(session, connections);
		if(connections<4) {
			connections++;
			players.add(temp);
			System.out.println(players.size());
			System.out.println("Created Player");
		}
		else {
			waitingPlayers.add(temp);
		}
		
		try {
			session.getBasicRemote().sendText(""+(connections-1));
			
			for(Card card: dealer_hand) {
				session.getBasicRemote().sendText("D "+card.getNumber()+"-"+card.getSuit());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//session.sendText(connections);
	}
		
	@OnMessage
	public void message(String message, Session session) {
		System.out.println(message);
		try {
			int index = Integer.parseInt(message.substring(0, 1));
			Player temp = players.get(index);
			String commandLine = message.substring(1);
			String command = commandLine.substring(0, 1);
			if(command.equals("L")) {//getting balance
				int balance = Integer.parseInt(commandLine.substring(2));
				temp.setBalance(balance);
			} else if(command.equals("B")) {//getting bet
				int bet = Integer.parseInt(commandLine.substring(2));
				temp.setBet(bet);
			} else if(command.equals("U")) {//getting username
				String username = message.substring(2);
				temp.setUsername(username);
				
			} else if(command.equals("R")) {
				//idk the point of this
				
			} else if(command.equals("H")) {
				Card newCard = myDeck.getCard();
				session.getBasicRemote().sendText("P "+newCard.getNumber()+"-"+newCard.getSuit());
				temp.receiveCard(newCard);//gives the player a card from the deck
				if(temp.getHandValue() > 21) {
					System.out.println("dealer score: "+dealerScore);
					temp.finishRound(dealerScore);
					session.getBasicRemote().sendText(temp.sendInfo());
				}
				
			} else if(command.equals("S")) {
				temp.finishRound(dealerScore);
				session.getBasicRemote().sendText(temp.sendInfo());
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
		
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting");
		
		//mySessions.remove(session);
		
		//Remove from players
		for(int i = 0; i < players.size(); i++) {
			if(session == players.get(i).getSession()) {
				players.remove(i);
			}
		}
		
		connections--;
	}
	
	@OnError
	public void error(Throwable error) {
		//System.out.println("Error!");
	
	}
	
	
	public void run() {
		/*connections = 0;
		inGame = true;
		
		
		
		while(inGame) {
			//betting
			gettingBets = true;
			
			
			players.addAll(waitingPlayers);
			waitingPlayers.removeAllElements();
			
			for(int i = 0; i < players.size(); i++) {
				players.get(i).bet();
			}
			
			int betsMade = 0;
			while(betsMade < players.size()) {
				betsMade = 0;
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).betting()) {
						betsMade++;
					}
				}
			}
			
			gettingBets = false;
			
			
			
			
			//use dealer
			
			
			
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
			
				
			//check player win/loss and reset the player threads
			for(int i = 0; i < players.size(); i++) {
				if(players.get(i).getSum() >= dealerSum && players.get(i).getSum() <= 21) {
					players.get(i).win();
				} else {
					players.get(i).loss();
				}
			}
			
			
		}*/
	}

	
}








/*
public class ServerClass extends Thread {
	
	private int connections;
	private int port;
	private ServerSocket server;
	private Socket socket;
	private boolean inGame;
	
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
		}
	}
	
	public static void main(String[] args) {
		
		ServerClass serverClass = new ServerClass();
		
	}
	
	
	public void run() {
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
	}
}*/


