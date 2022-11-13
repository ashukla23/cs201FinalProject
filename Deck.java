import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck
{
	// MEMBERS
	public String[] _number = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
	public String[] _suit = new String[] { "diamond", "spade", "heart", "club" };
	private List<Card> _cardList = new ArrayList<>();


	// CONSTRUCTOR: new deck created each round, fully shuffled and 52 cards
	public Deck() {
		// put all 52 cards in deck array
		for (int i = 0; i < 13; i++) {
			for (int m = 0; m < 4; m++) {
				// assign values to card based on number/name
				int value;
				if (_number[i].equals("J") || _number[i].equals("Q") || _number[i].equals("K")) {
					value = 10;
				}
				else if (_number[i].equals("A")) {
					value = 11;
				} 
				else {
					value = Integer.parseInt(_number[i]);
				}
				// add to array
				Card newcard = new Card(_number[i], _suit[m], value);
				_cardList.add(newcard);
			}
		}
		// shuffle cards
		Collections.shuffle(_cardList);
	}
	
	
	// called when hitting, and to deal first two cards around (remove from the
	// arraylist[])
	public Card getCard() {
		Card getcard = _cardList.get(0);
		_cardList.remove(0);
		return getcard;
	}
}
