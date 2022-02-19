package org.example.luxuryhotel.framework.exaptions;

public class ControllerNotExist extends RuntimeException{
    public ControllerNotExist(){
        super();
    }
    public ControllerNotExist(String s){
        super(s);
    }
}

