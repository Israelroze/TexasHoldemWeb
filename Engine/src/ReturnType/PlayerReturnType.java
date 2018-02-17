package ReturnType;

import Player.PlayerState;
import Player.PlayerType;

public class PlayerReturnType {
    private PlayerType type;
    private PlayerState state;
    private int chips;
    private String name;
    private int id;
    public PlayerReturnType(PlayerType type, PlayerState state, int chips ,String name, int id)
    {
        this.type = type;
        this.state = state;
        this.chips = chips;
        this.name = name;
        this.id = id;
    }

    public PlayerReturnType(PlayerType type, PlayerState state, int chips) {
        this(type,state,chips,"anonymous", 0);
    }


    @Override
    public String toString() {
        return this.type.toString();
    }


    public String getName() {
        return name;
    }

    public PlayerType GetType() {
        return type;
    }

    public int getChips() {
        return chips;
    }

    public int getId() { return id; }



    public String getState() {
        switch (this.state) {
            case BIG:
                return "B";
            case NONE:
                return " ";
            case SMALL:
                return "S";
            case DEALER:
                return "D";
        }
        return "U";
    }
}
