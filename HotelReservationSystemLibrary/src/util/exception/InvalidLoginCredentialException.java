package util.exception;

import javax.xml.ws.WebFault;


public class InvalidLoginCredentialException extends Exception 
{
    public InvalidLoginCredentialException() 
    {
    }

    
    
    public InvalidLoginCredentialException(String msg) 
    {
        super(msg);
    }
}