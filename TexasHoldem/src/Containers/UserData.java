package Containers;

import java.util.List;

public class UserData {
    private String name;
    private int bid;
    private int num_of_wins;
    private int money;
    private String type;
    final private List<String> cards;


    public UserData(String name, int bid, int num_of_wins, int money, String type,List<String> cards) {
        this.name = name;
        this.bid = bid;
        this.num_of_wins = num_of_wins;
        this.money = money;
        this.type = type;
        this.cards = cards;
    }
}
