package cs201_final_project;

import java.io.IOException;
import java.util.ArrayList;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;



public class Player
{

	// MEMBERS
	private Session _session = null;
	private static int _id;
	private String _username = null;
	private static int _balance;
	private static int _bet;
	private static ArrayList<String> _hand;
	private static int _handValue;
	private String _winResult = null;
	private boolean playerActive = false;
	RemoteEndpoint.Async basicRemote = null;




	// CONSTRUCTOR
	public Player(Session session, int id, String username, int balance)
	{
		_session = session;
		_id = id;
		_username = username;
		_balance = balance;
		_bet = 0;
		_hand = new ArrayList<String>();
		_handValue = 0;
		_winResult = "";
		basicRemote = _session.getAsyncRemote();
	}

	// another constructor
	public Player(Session session)
	{
		_session = session;
		_id = -1;
		_username = "";
		_balance = 0;
		_bet = 0;
		_hand = new ArrayList<String>();
		_handValue = 0;
		_winResult = "";
		basicRemote = _session.getAsyncRemote();
	}



	// called by server, set player's bet for the round
	public void setBet(int bet)
	{
		_bet = bet;
	}

	public void setUsername(String username)
	{
		_username = username;
	}

	public void addToHand(String card) {

		System.out.println("4");
		_hand.add(card);
		System.out.println("5");
		sendMessage("P " + card);
		updateHandValue();
	}

	public void resetHand() {
		_hand.clear();
	}

	public static ArrayList<String> getHand() {
		return _hand;
	}


	// called by server, player has requested to hit, add card to player's hand
	public void receiveCard(String card)
	{
		_hand.add(card);
		updateHandValue();
	}




	// called by server upon round completion
	// ties mean dealer wins
	public void finishRound(int dealerScore)
	{
		if(_handValue > 21) {
			_balance -= _bet;
			_winResult = "L";
		}
		else if(dealerScore > 21) {
			_balance += _bet;
			_winResult = "W";
		}
		else if(_handValue > dealerScore) {
			_balance += _bet;
			_winResult = "W";
		}
		else {
			_balance -= _bet;
			_winResult = "L";
		}
		_hand.clear();
		_bet = 0;
	}



	// called by server, returns the win result and updated balance in string form
	// return format example (id=2, win, balance of 100): "2,W,100"
	// return format example (id=1, loss, balance of 50): "1,L,50"
	public String sendInfo()
	{
		return _id + "," + _winResult + "," + _balance;
	}




	// get value of hand
	private static void updateHandValue()
	{
		// get initial total
		int total = 0;
		int aces = 0;

		System.out.println("A");
		for(int i=0; i < _hand.size(); i++) {

			String card = _hand.get(i);
			String[] values = card.split("-");
			int value;
			System.out.println("B");

			System.out.println(card.charAt(0));

			if (card.charAt(0) == 'A') {
				value = 11;
				aces++;
				System.out.println("E");
			}
			else if (card.charAt(0) == 'K' || card.charAt(0) == 'Q' || card.charAt(0) == 'J') {
				value = 10;
				System.out.println("F");
			}
			else {
				value = Integer.parseInt(values[0]);
				System.out.println("G");
			}
			total += value;
		}

		// if we've bust, try to reduce ace values from 11 to 1
		while(total > 21) {
			System.out.println("D");
			if(aces > 0) {
				total -= 10;
				aces--;
			}
			else {
				break;
			}
		}

		// return
		System.out.println("C");
		_handValue = total;
	}

	public void setBalance(int balance) {
		_balance = balance;
	}

	static void snooze(int ms) {
	    try {
	        System.out.printf("Sleeping...");
	        Thread.sleep(ms);
	        } catch (InterruptedException e) {
	            System.out.printf("Exception caught");
	            }

	}

	public void sendMessage(String message) {


		boolean bWriteSucceeded = false;
		System.out.println("6");
	    if (_session.isOpen()) {
	    	System.out.println("7");
	        for (int i = 0; i < 10 && !bWriteSucceeded; i++) {
	            // Crap. Sometimes getting this:
				// The remote endpoint was in state [TEXT_FULL_WRITING] which is an invalid
				// state for called method
	        	System.out.println("9");
	        	System.out.println("trying to send message: " + message);
	        	try {
	        		basicRemote.sendText(message);
	        	}
	        	catch (IllegalStateException ise) {

	        	}
				System.out.println("sent message: " + message);
				bWriteSucceeded = true;
	        }
	    }
	    System.out.println("8");

	}

	public int getBalance() {
		return _balance;
	}

	public void hit(String card) throws IOException {
		updateHandValue();
		if (_handValue < 21) {
			sendMessage("P " + card);
			_hand.add(card);
		}
		else {
			sendMessage("N");
		}

	}

	public void stay() throws IOException {
		updateHandValue();
		if (_handValue <= 21) {
			sendMessage("W");
		}
		else {
			sendMessage("L");
		}
		markInactive();

	}

	public void activate() throws IOException {
		markActive();
		System.out.println("Sending message Player activate");
		sendMessage("A");
	}

	public void deactivate() throws IOException {
		markInactive();
		System.out.println("Sending message Player activate");
		sendMessage("E");
	}

	public boolean isActive() {
		return playerActive;
	}

	public void markActive() {
		playerActive = true;
		System.out.println("Player active status is: " + playerActive);
	}

	public void markInactive() {
		playerActive = false;
		System.out.println("Player active status is: " + playerActive);
	}

	public void startRound() {

	}

	public Session getSession() {
		// TODO Auto-generated method stub
		return _session;
	}

	public void clearHand() {
		_hand.clear();
		_handValue = 0;
	}


}
