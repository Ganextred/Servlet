package org.example.luxuryhotel.framework.exaptions;

public class ConvertException extends RuntimeException{
    public ConvertException(String massage){
        super(massage);
    };
    public ConvertException(){
        super();
    };
}
