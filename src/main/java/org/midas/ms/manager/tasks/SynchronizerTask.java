package org.midas.ms.manager.tasks;

import java.util.Set;
import java.util.concurrent.Callable;

import org.midas.metainfo.ContainerInfo;
import org.midas.ms.catalog.Catalog;
import org.midas.ms.manager.Manager;
import org.midas.ms.manager.ManagerScreen;
import org.midas.ms.trader.Trader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class SynchronizerTask implements Callable
{	
	private static Logger LOG = LoggerFactory.getLogger(SynchronizerTask.class);
	
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
						LOG.warn("Container '"+container.getName()+"' is offline and has been removed from MAS");
						ManagerScreen.userInterfaceEvent("Refresh Services");
					}
					else
					{
						LOG.debug("Container '"+container.getName()+"' is online");
					}
					
					Thread.sleep(10000);
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
