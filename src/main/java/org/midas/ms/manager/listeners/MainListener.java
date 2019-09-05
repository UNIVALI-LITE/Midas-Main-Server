package org.midas.ms.manager.listeners;

import org.midas.ms.manager.Manager;

public class MainListener
{		
	public void start()
	{
		Manager.initialize(7100);
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
