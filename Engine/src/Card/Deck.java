package Card;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;

    private Card randomCard()
    {
        Random rnd = new Random();
        int i = rnd.nextInt(cards.size());
        return cards.remove(i);
    }
    private void CreateDeck()
    {
        cards=new LinkedList<Card>();
        for( CardSuit suit : CardSuit.values())
        {
            if(suit != CardSuit.UnknownSuit) {
                for (CardNumber number : CardNumber.values()) {
                    if (number != CardNumber.UNKNOWN_NUMBER)
                        cards.add(new Card(number, suit));
                }
            }
        }
    }

    public Deck()
    {
        CreateDeck();
    }

    public Card PopCard()
    {
        return randomCard();
    }

    public void RefreshDeck()
    {
        CreateDeck();
    }
}
