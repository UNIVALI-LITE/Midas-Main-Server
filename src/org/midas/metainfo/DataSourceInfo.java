package org.midas.metainfo;

import java.io.Serializable;

public class DataSourceInfo implements Serializable 
{
	private String name;
	private String driver;
	private String connection;
	
	public DataSourceInfo(String name, String driver, String connection) 
	{
		this.name = name;
		this.driver = driver;
		this.connection = connection;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public String getDriver() 
	{
		return driver;
	}

	public String getConnection() 
	{
		return connection;
	}	
}
