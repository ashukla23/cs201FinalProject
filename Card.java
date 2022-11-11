import java.util.HashMap;
import java.util.Map;

public class Card {
	private String number;
	private String suit;
	private static int value;

	public int getValue() {
		return value;
	}

	void setValue(int newValue) {
		Card.value = newValue;
	}

	public Card(String number, String suit) {
		this.number = number;
		this.suit = suit;

	}

	public String getnumber() {
		return number;
	}

}
