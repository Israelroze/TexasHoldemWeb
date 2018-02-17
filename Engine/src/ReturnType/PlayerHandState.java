package ReturnType;

import Card.Card;
import Card.CardNumber;
import Card.CardSuit;
import Player.PlayerState;
import Player.PlayerType;

import java.util.LinkedList;
import java.util.List;

import static Player.PlayerType.*;

public class  PlayerHandState extends PlayerReturnType {

    private int chips;
    private int bet;
    private List<Card> cards;

    public PlayerHandState(PlayerType type, PlayerState state, int chips, int bet, List<Card> cards)
    {
        this(type,state,chips,bet,cards,"anonymous",0);

    }


    public PlayerHandState(PlayerType type, PlayerState state, int chips, int bet, List<Card> cards, String name, int id) {
        super(type, state, chips,name,id);
        this.bet = bet;
        this.cards= cards;
        if (type == COMPUTER ){
            // If the player is computer , we don't know his cards.
            cards= new LinkedList<Card>();
            cards.add(new Card(CardNumber.UNKNOWN_NUMBER, CardSuit.UnknownSuit));
            cards.add(new Card(CardNumber.UNKNOWN_NUMBER, CardSuit.UnknownSuit));
        }
    }

    public int getBet() {
        return bet;
    }

    public List<String> getCard() {
        List<String> res = new LinkedList<>();
        res.add(cards.get(0).toString());
        res.add(cards.get(1).toString());
        return res;
    }

    public boolean IsHuman(){
        return GetType() == HUMAN;
    }
    public boolean IsComputer(){
        return GetType() == COMPUTER;
    }

}
