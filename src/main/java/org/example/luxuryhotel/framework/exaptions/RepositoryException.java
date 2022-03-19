package org.example.luxuryhotel.framework.exaptions;

public class RepositoryException extends RuntimeException{
    public RepositoryException(){
        super();
    }
    public RepositoryException(String s){
        super(s);
    }
    public RepositoryException(Throwable e){
        super(e);
    }
}
