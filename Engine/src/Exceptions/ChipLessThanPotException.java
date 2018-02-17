package Exceptions;

public class ChipLessThanPotException extends Exception {
    private int chips;

    public ChipLessThanPotException(int chip)
    {
        this.chips=chip;
    }
    public int GetMaxChips(){
        return this.chips;
    }
}
