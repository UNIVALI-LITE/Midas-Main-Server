package org.midas.metainfo;

import java.util.Set;
import java.util.TreeSet;

public class MSInfo 
{
	private Set<ContainerInfo> containers = new TreeSet<ContainerInfo>();
	
	public void addContainer(ContainerInfo container)
	{
		containers.remove(container);		
		containers.add(container);
	}
	
	public void removeContainer(ContainerInfo container)
	{	
		containers.remove(container);			
	}
	
	public Set<ContainerInfo> getContainers()
	{
		return containers;
	}
	
	public ContainerInfo getContainerByName(String containerName) throws MetaInfoException
	{
		// TODO: Otimizar algoritmo
		for (ContainerInfo container : containers) 
		{		
			if (container.getName().equals(containerName))
			{
				return container;
			}
		}
		
		throw new MetaInfoException("Invalid container name - "+containerName);
	}	
}
