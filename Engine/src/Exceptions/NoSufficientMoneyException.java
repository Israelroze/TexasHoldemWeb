package Exceptions;

public class NoSufficientMoneyException extends Exception {
    private String player_name;

    public NoSufficientMoneyException(String name)
    {
        this.player_name=name;
    }
    public String GetPlauerName()
    {
        return this.player_name;
    }
}
