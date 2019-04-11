package org.midas.ms.manager;

public class ManagerException extends Exception 
{
	public ManagerException(String arg0, Throwable arg1){super(arg0, arg1);}
	public ManagerException(Throwable arg0){super(arg0);}
	public ManagerException(){}
	public ManagerException(String message){super(message);}
}
