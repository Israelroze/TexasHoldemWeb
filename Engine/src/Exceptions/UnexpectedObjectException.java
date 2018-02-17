package Exceptions;

public class UnexpectedObjectException extends Exception {
    String object_name;

    public UnexpectedObjectException(){
        this.object_name="";
    };
    public UnexpectedObjectException(String object_name){
        this.object_name=object_name;
    }
    public String GetObjectName(){return this.object_name;}
}
