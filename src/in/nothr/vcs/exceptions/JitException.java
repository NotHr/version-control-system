package in.nothr.vcs.exceptions;

public class JitException extends RuntimeException{
    JitException(){
        super();
    }

    public JitException(String msg){
        super(msg);
    }
}