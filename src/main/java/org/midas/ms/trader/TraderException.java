package org.midas.ms.trader;

public class TraderException extends Exception 
{
	public TraderException(String arg0, Throwable arg1){super(arg0, arg1);}
	public TraderException(Throwable arg0){super(arg0);}
	public TraderException(){}
	public TraderException(String message){super(message);}
}
