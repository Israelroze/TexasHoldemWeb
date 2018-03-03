package Containers;

import java.util.List;

public class UserData {
    private String name;
    private int num_of_wins;
    private int money;
    private String type;
    final private List<String> cards;
    final private String role;


    public UserData(String name,int num_of_wins, int money, String type,List<String> cards, String role) {
        this.name = name;
        this.num_of_wins = num_of_wins;
        this.money = money;
        this.type = type;
        this.cards = cards;
        this.role = role;
    }
}
