
public class Card
{
	private String number;
	private String suit;
	private int value;
	
	public Card(String number, String suit, int value) {
		this.number = number;
		this.suit = suit;
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	public String getNumber() {
		return number;
	}
	public String getSuit() {
		return suit;
	}

}
