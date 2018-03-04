package Exceptions;

public class FatalGameErrorException extends Exception {

    private String message;
    public FatalGameErrorException(String message) {
        this.message=message;
    }

    public String getMessage(){return this.message;}
}
