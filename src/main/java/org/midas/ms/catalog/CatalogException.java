package org.midas.ms.catalog;

import org.midas.ms.MasServerException;


public class CatalogException extends MasServerException 
{
	public CatalogException(String arg0, Throwable arg1){super(arg0, arg1);}
	public CatalogException(Throwable arg0){super(arg0);}
	public CatalogException(){}
	public CatalogException(String message){super(message);}
}
