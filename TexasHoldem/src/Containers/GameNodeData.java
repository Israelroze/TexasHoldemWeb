package Containers;

public class GameNodeData {
    final private String game_name;
    final private String uploader;
    final private int num_of_hands;
    final private int buy;
    final private int total_players;
    final private int registered_players;
    final private int big;
    final private int small;
    final private boolean is_game_started;


    public GameNodeData(String game_name,String user_name, int buy , int big , int small, int num_of_hands, int t_players, int r_players,boolean is_game_started)
    {
        this.game_name=game_name;
        this.uploader=user_name;
        this.buy=buy;
        this.small=small;
        this.big=big;
        this.num_of_hands=num_of_hands;
        this.total_players=t_players;
        this.registered_players=r_players;
        this.is_game_started=is_game_started;
    }
}
