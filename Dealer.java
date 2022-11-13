import java.util.ArrayList;
import java.util.List;

public class Dealer
{
	// MEMBERS
	private Server _server;
	private List<Card> _hand = new ArrayList<>();
	
	
	// CONSTRUCTOR: new dealer created each round
	public Dealer(Server serv)
	{
		_server = serv;
	}
	
	
	// get dealer's hand (for display)
	public List<Card> getHand()
	{
		return new ArrayList<Card>(_hand);		//copy to enforce immutability
	}
	
	
	// get value of dealer's hand
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
	
	
	
	// have dealer draw first card
	public void hitFirstCard()
	{
		_hand.add(_server.getDeck().getCard());
	}
	
	
	// have dealer draw until bust or reaches 17
	public void hitAll()
	{
		while(getHandValue() < 17) {
			_hand.add(_server.getDeck().getCard());
		}
	}

}
