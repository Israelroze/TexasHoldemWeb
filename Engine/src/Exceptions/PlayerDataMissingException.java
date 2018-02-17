package Exceptions;

public class PlayerDataMissingException extends  Exception {
    private String data;

    public PlayerDataMissingException(String data)
    {
        this.data=data;
    }
    public  String GetMissingData(){ return this.data;}
}
