package org.midas.ms.manager.tasks;

import java.util.concurrent.Callable;

import org.midas.ms.manager.Manager;
import org.midas.ms.manager.ManagerScreen;

@SuppressWarnings("rawtypes")
public class StatisticsTask implements Callable 
{
	public Object call() throws Exception 
	{
		try 
		{
			while(true)
			{
				ManagerScreen.userInterfaceEvent("Refresh Statistics");
				Thread.sleep(500);
			}									
		} 
		catch (InterruptedException e) 
		{
			if(!Manager.isStopping())
			{
				Manager.shutdown(false,true);
			}
		}				
		
		return null;		
	}
}
