package Move;

public class Move {
    private MoveType type;
    private int value;


    public Move(MoveType type,int value)
    {
        this.type=type;
        this.value=value;
    }

    public  MoveType GetMoveType(){ return this.type; }

    public int GetValue(){return this.value;}

}
