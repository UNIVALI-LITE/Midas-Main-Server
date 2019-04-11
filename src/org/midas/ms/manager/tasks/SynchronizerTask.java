package org.midas.ms.manager.tasks;

import java.util.Set;
import java.util.concurrent.Callable;

import org.midas.metainfo.ContainerInfo;
import org.midas.ms.catalog.Catalog;
import org.midas.ms.manager.execution.Logger;
import org.midas.ms.manager.manager.Manager;
import org.midas.ms.manager.manager.ManagerScreen;
import org.midas.ms.trader.Trader;

public class SynchronizerTask implements Callable
{	
	public Object call() 
	{
		try 
		{
			while(true)
			{
				Set<ContainerInfo> containers = Catalog.getMS().getContainers();
				
				for (ContainerInfo container : containers)
				{
					if (!Trader.ping(container))
					{
						Catalog.removeContainer(container);
						Logger.addEntry("Container '"+container.getName()+"' is out-of-synchronization.");
						ManagerScreen.userInterfaceEvent("Refresh Services");
					}
					
					Thread.sleep(1000);
				}
			}									
		} 
		catch (InterruptedException e) 
		{
			if(!Manager.isStopping() && Manager.isConnected())
			{
				call();
			}			
		}				
		
		return null;
	}	
}
