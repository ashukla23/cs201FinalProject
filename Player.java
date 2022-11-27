import java.util.ArrayList;
import java.util.List;
import javax.websocket.*;



public class Player
{
	
	// MEMBERS
	private Session _session;
	private int _id = 0;
	private String _username;
	private int _balance = 0;
	private int _bet = 0;
	private List<Card> _hand;
	private int _handValue = 0;
	private String _winResult;
	
	
	
	
	// CONSTRUCTOR
	public Player(Session session, int id) {
		_session = session;
		_id = id;
		_hand = new ArrayList<Card>();
	}
	public Player(Session session, int id, String username, int balance)
	{
		_session = session;
		_id = id;
		_username = username;
		_balance = balance;
		_bet = 0;
		_hand = new ArrayList<Card>();
		_handValue = 0;
		_winResult = "";
	}
	
	public void setBalance(int balance) {
		_balance = balance;
	}
	
	// called by server, set player's bet for the round
	public void setBet(int bet)
	{
		_bet = bet;
	}
	
	public Session getSession() {
		return _session;
	}
	
	
	
	// called by server, player has requested to hit, add card to player's hand 
	public void receiveCard(Card card)
	{
		_hand.add(card);
		_handValue = getHandValue();
	}
	
	public void setUsername(String name) {
		_username = name;
	}
	
	
	// called by server upon round completion
	// ties mean dealer wins
	public void finishRound(int dealerScore)
	{
		if(_handValue > 21) {
			System.out.println("balance: "+_balance);
			_balance -= _bet;
			_winResult = "L";
			System.out.println("balance: "+_balance);
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
		return "" + _winResult + " " + _balance;
	}
	
	
	
	
	// get value of hand
	public int getHandValue()
	{
		// get initial total
		int total = 0;
		int aces = 0;
		for(Card card : _hand) {
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


}