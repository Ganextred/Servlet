package org.example.luxuryhotel.framework.exaptions;

public class NullParamException extends RuntimeException{
    public NullParamException(){
        super();
    }
    public NullParamException(String s){
        super(s);
    }
}
