package csci201_groupProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;




public class ServerThread extends Thread {
	
	private Socket socket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	private boolean done;
	private volatile boolean activeThread;
	private int sum;
	private int balance;
	private int bet;
	private boolean betting;
	private boolean playing;
	//private SET CARDS:
	
	
	public ServerThread(Socket s, int b) {
		socket = s;
		betting = false;
		playing = false;
		activeThread = true;
		sum = 0;
		
		
		try {
			printWriter = new PrintWriter(socket.getOutputStream());	
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		this.start();
	}
	
	/*public void addCard(Card c) {
		cards.add(c);
		sum += c.getValue();
	}*/
	
	public boolean getDone() {
		return done;
	}
	
	
	
	
	public void deactivate() {
		activeThread = false;
		//this.
	}
	
	
	//send message
	public void sendMessage(String message) {
		printWriter.println(message);
		printWriter.flush();	
	}
	
	
	public void inputWaiter() {
		printWriter.println("I");
		printWriter.flush();
	}
	
	public void startBetting() {
		betting = true;
	}
	
	public boolean getBetting() {
		return betting;
	}
	
	public void startPlaying() {
		playing = true;
	}
	
	public boolean getPlaying() {
		return playing;
	}
	
	
	public String cardString() {
		return "";
	}
	
	
	public int getBet() {
		return bet;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void loss() {
		reset();
	}
	
	public void win() {
		balance += 2 * bet;
		reset();
	}
	
	public void reset() {
		//cleare the cards used
		bet = 0;
	}
	
	public int getSum() {
		return sum;
	}
	
	
	public void run() {
		
		
		
		while(betting) {
			
			
			try {
				String betString = bufferedReader.readLine();
				
				int bet = Integer.parseInt(betString);
				
				
				if(bet <= balance) {
					betting = false;
					balance -= bet;
					
				} else {
					sendMessage("Error placing bet, please enter a valid bet");
				}
				
				
			} catch (Exception e) {
				
				sendMessage("Error placing bet, please enter a valid bet");
				
			}	
		}
		
		
		while(playing) {
			
			//check for blackjack or bust
			if(sum > 21) {
				sendMessage("Player Bust");
				//done = true;
				bet = 0;
				playing = false;
				break;
			} else if(sum == 21) {
				sendMessage("Blackjack");
				balance += 2 * bet;
				bet = 0;
				//done = true;
				playing = false;
				break;
			}
		
	
			
			boolean move = false;
			while(!move) {
				try {
					String betString = bufferedReader.readLine();
					
					if(betString == "H") {
						move = true;
						//get a card, new function?
					} else if(betString == "S") {
						move = true;
						playing = false;
					}

					
				} catch (Exception e) {
					sendMessage("Error with move, try again");
				}
			}	
		}
	}
}
