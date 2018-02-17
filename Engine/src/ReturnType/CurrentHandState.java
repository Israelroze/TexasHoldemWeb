package ReturnType;

import Card.Card;

import java.util.List;
public class CurrentHandState {

    private List<PlayerHandState> playersState;
    private List<Card> communityCard;
    private int pot;
    private int currentPlayer;
    private int bigBlind;
    private int smallBlind;


    public CurrentHandState(List<PlayerHandState> playersState, List<Card> communityCards, int pot, int currentPlayer) {
        this(playersState,communityCards,pot,currentPlayer,0,0);
    }

    public CurrentHandState(List<PlayerHandState> playersState, List<Card> communityCards, int pot, int currentPlayer, int bigBlind, int smallBlind) {

        this.playersState = playersState;
        this.communityCard = communityCards;
        this.pot = pot;
        this.currentPlayer = currentPlayer;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;


    }

    public List<PlayerHandState> getPlayersState() {
        return playersState;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }


    public List<Card> getCommunityCard() {
        return communityCard;
    }

    public String getStringOfCommunityCard() {
        String res = "";
        for (int i=0; i< communityCard.size(); i++)
        {
            res += communityCard.get(i).toString();
            if (i != communityCard.size() -1) res += " | ";
        }
        return res;
    }

    public int getPot() {
        return pot;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
