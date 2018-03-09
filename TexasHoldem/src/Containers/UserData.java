package Containers;

import javafx.beans.property.BooleanProperty;

import java.util.List;

public class UserData {
    private String name;
    private int num_of_wins;
    private int num_of_buys;
    private int money;
    private String type;
    final private List<String> cards;
    final private String role;
    final private boolean is_turn;


    public UserData(String name,int num_of_wins,int num_of_buys, int money, String type,List<String> cards, String role,boolean turn) {
        this.name = name;
        this.num_of_wins = num_of_wins;
        this.money = money;
        this.type = type;
        this.cards = cards;
        this.role = role;
        this.is_turn=turn;
        this.num_of_buys=num_of_buys;
    }
}
