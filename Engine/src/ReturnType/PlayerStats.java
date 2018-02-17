package ReturnType;

import Player.APlayer;
import Player.PlayerState;
import Player.PlayerType;

public class PlayerStats extends PlayerReturnType implements Comparable<PlayerStats> {
    private int buy;
    private int handsWons;
    private int numOfGames;
    private PlayerType type;

    public PlayerStats(PlayerType type, PlayerState state, int chips, int buy, int handsWons, int numOfGames)
    {
        this(type, state,chips,buy, handsWons,numOfGames,"anonymuos",0);
    }
    public PlayerStats(PlayerType type, PlayerState state, int chips, int buy, int handsWons, int numOfGames, String name, int id)
    {
        super(type,state,chips,name,id);
        this.handsWons = handsWons;
        this.numOfGames = numOfGames;
        this.buy = buy;
        this.type=PlayerType.COMPUTER;
    }
    public PlayerStats(APlayer player,int num_of_games)
    {
        super(player.GetType(),player.GetPlayerState(),player.GetMoney(),player.GetName(),player.getId());
        this.handsWons = player.GetNumOfWins();
        this.numOfGames =num_of_games;
        this.buy = player.GetNumOfBuys();
        this.type=player.GetType();
    }

    public PlayerType GetType()
    {
     return this.type;
    }

    public int GetID()
    {
        return super.getId();
    }
    public int getBuy() {
        return buy;
    }
    public  String getName(){return super.getName();}


    public int getHandsWons() {
        return handsWons;
    }

    public int getNumOfGames() {
        return numOfGames;
    }

    @Override
    public int compareTo(PlayerStats o) {
        if(this.getHandsWons()< o.getHandsWons()) return 1;
        else if(this.getHandsWons()== o.getHandsWons()) return 0;
        else return -1;
    }
}
