package Containers;

public class GameStatusData {
    final private boolean is_game_started;
    final private boolean is_game_over;
    final private boolean is_hand_started;
    final private boolean is_hand_over;
    final private boolean is_bid_over;
    final private boolean is_your_turn;

    public GameStatusData(boolean is_game_started, boolean is_game_over, boolean is_hand_started, boolean is_hand_over, boolean is_bid_over,boolean is_your_turn) {
        this.is_game_started = is_game_started;
        this.is_game_over = is_game_over;
        this.is_hand_started = is_hand_started;
        this.is_hand_over = is_hand_over;
        this.is_bid_over = is_bid_over;
        this.is_your_turn=is_your_turn;
    }
}
