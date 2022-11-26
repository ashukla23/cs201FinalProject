package csci201_finalProject;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.websocket.*; // for space
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

import org.apache.jasper.tagplugins.jstl.core.Remove;



@ServerEndpoint (value="/ws")//don't change this
public class ServerClass extends Thread {

	private int connections;
	volatile private boolean inGame;
	private boolean gettingBets;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> waitingPlayers = new ArrayList<Player>();
	private String[] allCards = new String[]{"A-C", "A-D", "A-H", "A-S", "2-C", "2-D", "2-H", "2-S", "3-C", "3-D", "3-H", "3-S", "4-C", "4-D", "4-H", "4-S",
            "5-C", "5-D", "5-H", "5-S", "6-C", "6-D", "6-H", "6-S", "7-C", "7-D", "7-H", "7-S", "8-C", "8-D", "8-H", "8-S",
            "9-C", "9-D", "9-H", "9-S", "10-C", "10-D", "10-H", "10-S", "J-C", "J-D", "J-H", "J-S", "K-C", "K-D", "K-H", "K-S",
            "Q-C", "Q-D", "Q-H", "Q-S"};
	private ArrayList<String> deck = Arrays.asList(allCards);

	private ArrayList<String> dealerHand = new ArrayList<String>();
	private int currentPlayerIndex = 0;



	//Synchronised list of client threads
	/*List<ServerThread> players = Collections.synchronizedList(new ArrayList<ServerThread>());
	List<ServerThread> waitingPlayers = Collections.synchronizedList(new ArrayList<ServerThread>());*/



	//for web Sockets connections/we don't have to use serverthreads



	@OnOpen
	public void open(Session session) {
		//System.out.println("Connection made");
		Player temp = new Player(session);

		players.add(temp);




		if(!this.isAlive()) {
			this.start();
			players.add(temp);
		} else {

			if(connections < 3) {
				connections++;

				if(gettingBets) {
					players.add(temp);
				} else {

					waitingPlayers.add(temp);

				}
			}
		}

		// send the frontend its reference ID to store.

		session.getBasicRemote().sendText(connections);

		// set up the player's starting hand
		String card = deck.remove();
		temp.addToHand(card);
		session.send("P " + card);
		card = deck.remove();
		temp.addToHand(deck.remove());
		session.send("P" + card);


		// send the player's starting hand to the frontend to display (done above)

//		session.send();

		// set up the dealer's starting hand. (Done on open)

		// send the dealer's starting hand to the frontend to display

		for (int i=0; i < dealerHand.size(); i++) {
			session.send("D " + dealerHand.get(i));
		}


	}

	@OnMessage
	public void message(String message, Session session) {
		//System.out.println(message);
		try {

			int index = Integer.parseInt(message.substring(0, 1));
			Player temp = players.get(index);

			String command = message.substring(1, 2);

			if(command.equals("L")) {

			} else if(command.equals("B")) {
				int balance = Integer.parseInt(message.substring(2));
				temp.setBalance(balance);

			} else if(command.equals("U")) {
				int bet = Integer.parseInt(message.substring(2));
				temp.setBalance(bet);

			} else if(command.equals("R")) {
				// sets username upon player join
				String username = message.substring(2);

			} else if(command.equals("H")) {
				temp.hit(deck.remove(0));
				// write hit function in player class

			} else if(command.equals("S")) {
				temp.stay();
				temp.toggleActive();
				currentPlayerIndex++;
				// write stay function in player class

			}/* else if(command.equals("C")) {

			}*/


		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	@OnClose
	public void close(Session session) {
		//System.out.println("Disconnecting");

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
		connections = 0;
		inGame = true;

		dealerHand.add(deck.remove(0));
		dealerHand.add(deck.remove(0));



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

			Player currentPlayer = players.get(currentPlayerIndex);
			currentPlayer.toggleActive();
			while (currentPlayer.isActive()) {
				currentPlayer = players.get(currentPlayerIndex);
				if (!currentPlayer.isActive()) {
					currentPlayer.activate();
				}
			}

			while(playersDone < players.size()) {
				playersDone = 0;

//				for(int i = 0; i < players.size(); i++) {
//					// set up a round where a turn is activated for the player. Then it becomes deactivated.
//					//if the player is not playing add to sum
//					if(!players.get(i).getPlaying()) {
//						playersDone++;
//
//					} else {
//						//if playing and wants card
//						if(players.get(i).wantsCard()) {
//							//give them a card
//						}
//					}
//				}
			}


			//check player win/loss and reset the player threads
			for(int i = 0; i < players.size(); i++) {
				if(players.get(i).getSum() >= dealerSum && players.get(i).getSum() <= 21) {
					players.get(i).win();
				} else {
					players.get(i).loss();
				}
			}


		}
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
