package cs201_final_project;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

// for space
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;



@ServerEndpoint (value="/ws")//don't change this
public class ServerClass extends Thread {

	private static int connections;
	volatile private boolean inGame;
	private boolean gettingBets;
	private static Vector<Player> players = new Vector<Player>();
	private ArrayList<Player> waitingPlayers = new ArrayList<Player>();
	private static String[] allCards = new String[]{"A-C", "A-D", "A-H", "A-S", "2-C", "2-D", "2-H", "2-S", "3-C", "3-D", "3-H", "3-S", "4-C", "4-D", "4-H", "4-S",
            "5-C", "5-D", "5-H", "5-S", "6-C", "6-D", "6-H", "6-S", "7-C", "7-D", "7-H", "7-S", "8-C", "8-D", "8-H", "8-S",
            "9-C", "9-D", "9-H", "9-S", "10-C", "10-D", "10-H", "10-S", "J-C", "J-D", "J-H", "J-S", "K-C", "K-D", "K-H", "K-S",
            "Q-C", "Q-D", "Q-H", "Q-S"};
	private static Vector<String> deck = new Vector<String>(Arrays.asList(allCards));

	private static ArrayList<String> dealerHand = new ArrayList<String>();
	private int currentPlayerIndex = 0;
	private static int numBets = 0;

	private static boolean started = false;



	//Synchronized list of client threads
	/*List<ServerThread> players = Collections.synchronizedList(new ArrayList<ServerThread>());
	List<ServerThread> waitingPlayers = Collections.synchronizedList(new ArrayList<ServerThread>());*/



	//for web Sockets connections/we don't have to use serverthreads



	@OnOpen
	public void open(Session session) throws IOException {
		//System.out.println("Connection made");
		Player temp = new Player(session);

		players.add(temp);

		System.out.println("New player added, player vector size is now " + String.valueOf(players.size()));






		// send the frontend its reference ID to store.

		temp.sendMessage(String.valueOf(players.size()-1));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("After message send");

		if(started == false) {
			this.start();
			started = true;
			System.out.println("Started");
			connections++;
		} else {

			if(connections < 4) {
				connections++;
			}
		}


	}

	@OnMessage
	public void message(String message, Session session) {
		System.out.println("Received message");
		System.out.println(message);
		try {

			int index = Integer.parseInt(message.substring(0, 1));


			String command = message.substring(1, 2);

			System.out.println("Command is " + command);

			Player temp = players.get(index);

			if(command.equals("L")) {

			}
			else if(command.equals("B")) {
				System.out.println("Received Bet of: " + message.substring(2));
				numBets++;
				int balance = Integer.parseInt(message.substring(3));
				temp.setBet(balance);
				// set up the player's starting hand
				String card = deck.remove(0);
				System.out.println(card);
				System.out.println("2");
				temp.addToHand(card);
				System.out.println("3");
				card = deck.remove(0);
				temp.addToHand(card);



				// send the player's starting hand to the frontend to display (done above)

//				session.send();

				// set up the dealer's starting hand. (Done on open)

				// send the dealer's starting hand to the frontend to display

				for (int i=0; i < dealerHand.size(); i++) {
					temp.sendMessage("D " + dealerHand.get(i));
				}

			} else if(command.equals("U")) {
				int bet = Integer.parseInt(message.substring(3));
				temp.setBalance(bet);

			} else if(command.equals("R")) {
				// sets username upon player join
				String username = message.substring(3);

			} else if(command.equals("H")) {
				temp.hit(deck.remove(0));
				// write hit function in player class

			} else if(command.equals("S")) {
				temp.stay();

				// write stay function in player class

			}/* else if(command.equals("C")) {

			}*/


		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}


	}

	@OnClose
	public void close(Session session) throws IOException {
		//System.out.println("Disconnecting");

		//mySessions.remove(session);

		System.out.println("Session closed");

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


	@Override
	public void run() {
		System.out.println("Running!");
		connections = 0;
		inGame = true;

		dealerHand.add(deck.remove(0));
		dealerHand.add(deck.remove(0));

		while (numBets != players.size()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Waiting... numbets is " + String.valueOf(numBets) + " while players length is " + String.valueOf(players.size()));
		}

		int activeplayer = 0;

		Player active = players.get(activeplayer);
		try {
			active.activate();
			System.out.println("Marking player " + String.valueOf(activeplayer) + "Active");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(true) {
			if (!active.isActive()) {
				if (activeplayer + 1 >= players.size()) {
					activeplayer = 0;

				}
				else {
					active.clearHand();
					activeplayer++;
					System.out.println("Hand cleared");
				}
				active = players.get(activeplayer);
				try {
					active.activate();
					if (active.getHand().isEmpty()) {
						String card = deck.remove(0);
						System.out.println(card);
						System.out.println("2");
						active.addToHand(card);
						System.out.println("3");
						card = deck.remove(0);
						active.addToHand(card);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}




//		while(inGame) {
//			//betting
//			gettingBets = true;
//
//
//			players.addAll(waitingPlayers);
//			waitingPlayers.removeAllElements();
//
//			for(int i = 0; i < players.size(); i++) {
//				players.get(i).bet();
//			}
//
//			int betsMade = 0;
//			while(betsMade < players.size()) {
//				betsMade = 0;
//				for(int i = 0; i < players.size(); i++) {
//					if(players.get(i).betting()) {
//						betsMade++;
//					}
//				}
//			}






			//use dealer



			//loop through all the players until they are done playing


//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}

//			System.out.println("Current player: " + activeplayer);
//			System.out.println("Player count: " + players.size());





//	udnidnede		try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}euiniuen


//		byubhbu	try {
//				currentPlayer.activate();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			while (currentPlayer.isActive()) {
//				if (!currentPlayer.isActive()) {
//					try {
//						currentPlayer.activate();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}bhibib


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



			//check player win/loss and reset the player threads
//			for(int i = 0; i < players.size(); i++) {
//				if(players.get(i).getSum() >= dealerSum && players.get(i).getSum() <= 21) {
//					players.get(i).win();
//				} else {
//					players.get(i).loss();
//				}
//			}


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
