import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	public String[] number = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
	public String[] suit = new String[] { "diamond", "spade", "heart", "club" };
	private List<Card> deck = new ArrayList<>();

	// called when hitting, and to deal first two cards around (remove from the
	// arraylist[])
	Card getcard() {
		Card getcard = deck.get(0);
		deck.remove(0);
		return getcard;

	}

	// constructor, put all the 52 cards in the deck array
	public Deck() {
		int count = 0;
		for (int i = 0; i < 13; i++) {
			for (int m = 0; m < 4; m++) {
				Card newcard = new Card(number[i], suit[m]);
				deck.add(newcard);
			}
		}
	}

	// shuffle the whole list,resets to full random deck
	void shuffle() {
		Collections.shuffle(deck);
	}

	// if jqk, then counts as 10;
	// if A, then 1
	// if other number, then just the number
	public void initialize_wholedeck_value() {
		for (Card card : deck) {
			String number = card.getnumber();
			if (number.equals("J") || number.equals("J") || number.equals("K")) {
				card.setValue(10);
			}
			else if (number.equals("A")) {
				card.setValue(1);
			} 
			else {
				int othernumber = Integer.parseInt(number);
				card.setValue(othernumber);
			}
		}
	}
	
	public void Reset() {
		Deck deck=new Deck();
		deck.initialize_wholedeck_value();
		deck.shuffle();		
	}
}
