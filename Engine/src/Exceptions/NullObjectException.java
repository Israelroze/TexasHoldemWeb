package Exceptions;

public class NullObjectException extends Exception {
    String object_name;

    public NullObjectException(){
        this.object_name="";
    };
    public NullObjectException(String object_name){
        this.object_name=object_name;
    }
    public String GetObjectName(){return this.object_name;}
}
