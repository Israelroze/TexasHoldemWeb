package Replay;
import Move.*;
import Card.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GameEvent {

    private EventTypes type;
    private Move move;
    private List<Card> cards;
    private int player_id;
    private int handpot;

    private int amount;

    private List<Integer> winners;
    private List<Integer> winners_money;
    public GameEvent(EventTypes type, Move move, int id)
    {
        this.type=type;
        this.move=move;
        this.player_id=id;
        this.cards=new LinkedList<>();
        this.winners=new LinkedList<>();
        this.winners_money=new LinkedList<>();
        this.amount=0;
    }

    public GameEvent(EventTypes type)
    {
        this.type=type;
        this.move=null;
        this.player_id=0;
        this.cards=new LinkedList<>();
        this.winners=new LinkedList<>();
        this.winners_money=new LinkedList<>();
        this.amount=0;
    }

    public GameEvent(EventTypes type,int amount)
    {
        this.type=type;
        this.move=null;
        this.player_id=0;
        this.cards=new LinkedList<>();
        this.winners=new LinkedList<>();
        this.winners_money=new LinkedList<>();
        this.amount=amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int GetPlayerId(){
        return this.player_id;
    }

    public Move GetMove(){
        return this.move;
    }

    public List<Card> GetCards(){
        return this.cards;
    }

    public EventTypes GetEventType(){
        return this.type;
    }

    public List<Integer> GetWinners()
    {
        return this.winners;
    }

    public int GetWinnerMoney(int index)
    {
        return this.winners_money.get(index);
    }

    public void AddEventCard(Card card){
        this.cards.add(card);
    }

    public void AddWinner(int index,int money)
    {
        this.winners.add(index);
        this.winners_money.add(money);
    }
}
