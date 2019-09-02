package org.midas.ms;

import org.midas.ms.manager.manager.Manager;
import org.midas.ms.manager.manager.ManagerScreen;

public class MasServer 
{
	public static void initialize()
	{
		Manager.initialize();
	}
	
	public static void loadManager() 
	{
		ManagerScreen.show();
		MasServer.initialize();
	}
	
	public static void main(String[] args)
	{
		loadManager();
	}	
}
