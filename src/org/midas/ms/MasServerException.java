package org.midas.ms;

import org.midas.MidasException;

public class MasServerException extends MidasException 
{
	public MasServerException(String arg0, Throwable arg1){super(arg0, arg1);}
	public MasServerException(Throwable arg0){super(arg0);}
	public MasServerException(){}
	public MasServerException(String message){super(message);}
}
