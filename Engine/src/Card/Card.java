package Card;

import java.util.LinkedList;
import java.util.List;

public class Card {
    private CardNumber number;
    private CardSuit suit;

    public static final List<Card> UnknownComputerCards =  new LinkedList<Card>();
    static{

        UnknownComputerCards.add(new Card(CardNumber.UNKNOWN_NUMBER,CardSuit.UnknownSuit));
        UnknownComputerCards.add(new Card(CardNumber.UNKNOWN_NUMBER,CardSuit.UnknownSuit));
    }

    public Card(CardNumber num,CardSuit suit) {

        this.number=num;
        this.suit=suit;
    }
    @Override
    public String toString() {

        return this.number.toString() + this.suit.toString();
    }

    public CardSuit GetSuit() {
        return this.suit;
    }

    public CardNumber GetNumber() {
        return this.number;
    }

    public void setNumber(CardNumber number) {
        this.number = number;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }




}
