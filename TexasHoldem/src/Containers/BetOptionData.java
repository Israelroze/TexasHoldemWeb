package Containers;

public class BetOptionData {

    final private boolean bet;
    final private boolean raise;
    final private boolean check;
    final private boolean fold;
    final private boolean call;
    final private int minVal;
    final private int maxVal;



    public BetOptionData(boolean bet, boolean raise, boolean call, boolean fold, boolean check, int minVal, int maxVal) {
        this.bet = bet;
        this.raise = raise;
        this.call = call;
        this.fold = fold;
        this.check = check;
        this.minVal = minVal;
        this.maxVal = maxVal;
    }







}
