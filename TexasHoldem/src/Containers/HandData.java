package Containers;

import java.util.List;

public class HandData {
    final private  int pot;
    final private List<String> communityCards;

    public HandData(int pot, List<String> communityCards) {
        this.pot = pot;
        this.communityCards = communityCards;
    }
}
