package Player;

import API.Engine;
import Card.Card;

import Exceptions.NoSufficientMoneyException;
import Exceptions.PlayerDataMissingException;
import Generated.Player;

public class APlayer{
    //info

    private PlayerType type;
    private int id;
    private String name;

    // global stats
    private int money;
    private int num_of_buys;
    private int num_of_wins;

    //bid stats
    private Card[] cards;
    private PlayerState state;
    private boolean is_placed_bet;
    private boolean is_folded;
    private int stake;

    //for replay
    private String win_chance;


    //Ctors
    public APlayer(String name, PlayerType type, int ID)
    {
        this.type=type;
        this.name=name;
        this.id=ID;
        this.ClearBidStats();
        this.num_of_buys=0;
        this.num_of_wins=0;
        this.money=0;
        this.win_chance="0%";
        this.state=PlayerState.NONE;
    }

    public void InitPlayer(){
        System.out.println("INIITING Player:"+this.name);
        this.id=0;
        this.ClearBidStats();
        this.num_of_buys=0;
        this.num_of_wins=0;
        this.money=0;
        this.win_chance="0%";
        this.state=PlayerState.NONE;
    }

    //Ctors
    public APlayer(String name, PlayerType type)
    {
        this.type=type;
        this.name=name;
        this.id=0;
        this.ClearBidStats();
        this.num_of_buys=0;
        this.num_of_wins=0;
        this.money=0;
        this.win_chance="0%";
        this.state=PlayerState.NONE;
    }

    public APlayer(Player player) throws PlayerDataMissingException {
        try{this.id=player.getId();}
        catch (NullPointerException e){throw new PlayerDataMissingException("ID");}
        try{this.name=player.getName();}
        catch (NullPointerException e){throw new PlayerDataMissingException("Name");}
        if(player.getType().equals("Human"))
        {
            this.type=PlayerType.HUMAN;
        }
        else
        {
            if(player.getType().equals("Computer"))
            {
                this.type=PlayerType.COMPUTER;
            }
            else
            {
                throw new PlayerDataMissingException("Type");
            }
        }
        this.num_of_buys=0;
        this.num_of_wins=0;
        this.ClearBidStats();
        this.state=PlayerState.NONE;
    }


    public void SetWinChance(String chance)
    {
        this.win_chance=chance;
    }

    public String GetWinChance()
    {
        return this.win_chance;
    }

    //Getters Setters
    public void IncWinner()
    {
        this.num_of_wins++;
    }

    public void DecWinner()
    {
        this.num_of_wins--;
    }

    public int getId() { return id; }

    public void setID(int id){this.id=id;}

    public boolean isPlacedBet() {return is_placed_bet; }

    public void setBetPlaceFlag(boolean state){this.is_placed_bet=state;}

    public void setStake(int stake){this.stake=stake;}

    public int getStake() { return stake; }

    public boolean isFolded() {
        return is_folded;
    }

    public void setFoldedFlag(boolean state){this.is_folded=state;}

    public Card[] GetCards() {
        return cards;
    }

    public int GetMoney() { return money; }

    public int GetNumOfBuys() { return num_of_buys; }
     
    public PlayerState GetPlayerState() {
        return state;
    }

    public String GetName() {
        return name;
    }

    public void AddMoney(int amount) {
        this.money=this.money+amount;
    }

    public void BuyMoney(int amount) {
        System.out.println("Adding money to:"+this.name+" the amount of:"+amount);
        AddMoney(amount);
        num_of_buys++;
    }

    public int GetNumOfWins() {
        return this.num_of_wins;
    }

    public PlayerType GetType() {
        return this.type;
    }

    public void DecMoney(int amount) throws NoSufficientMoneyException {
        if((this.money-amount)>=0) {
            this.money = this.money - amount;
        }
        else
        {
            throw new NoSufficientMoneyException(this.GetName());
        }
    }

    public boolean GetIsFoldedFlag() {
        return this.is_folded;
    }

    public void SetCards(Card[] cards) {
        this.cards=cards;
    }

    public void SetMoney(int money) {
        this.money=money;
    }

    public void SetPlayerState(PlayerState state) {
        this.state=state;
    }

    public void ClearBidStats() {
        this.stake=0;
        this.is_placed_bet=false;
        this.is_folded=false;
    }
}
