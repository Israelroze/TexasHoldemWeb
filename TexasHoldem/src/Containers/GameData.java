package Containers;

import GameTable.GameTableServlet;

import java.util.List;

public class GameData {

    final private int dataVerion;
    final private List<UserData> userData;
    final private List<String> winners;
    final private int  num_of_games;
    final private int current_game_number;
    final private int number_of_players;
    final private HandData table_data;
    final private GameStatusData game_status;


    public GameData(int dataVerion, List<UserData> userData, List<String> winners,HandData table_data,GameStatusData game_status, int num_of_games, int current_game_number, int number_of_players) {
        this.dataVerion = dataVerion;
        this.userData = userData;
        this.num_of_games = num_of_games;
        this.current_game_number = current_game_number;
        this.number_of_players = number_of_players;
        this.table_data = table_data;
        this.game_status=game_status;
        this.winners=winners;
    }


}
