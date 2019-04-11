package org.midas.ms.manager.manager.listeners;

import org.midas.ms.manager.manager.Manager;

public class MainListener
{		
	public void start()
	{
		Manager.initialize();
	}
	
	public void stop()
	{
		Manager.shutdown(true,false);
	}
	
	public void exit()
	{
		Manager.exit();
	}
}
