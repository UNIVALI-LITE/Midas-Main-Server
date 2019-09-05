package org.midas.ms.catalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.midas.metainfo.ContainerInfo;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.MSInfo;
import org.midas.metainfo.OrganizationInfo;
import org.midas.metainfo.ServiceInfo;

public class Catalog 
{
	private static MSInfo                    msInfo   = new MSInfo();
	private static Map<String,ContainerInfo> services = new HashMap<String,ContainerInfo>();
		
	public static MSInfo getMS()
	{
		return msInfo;
	}
	
	public static Map<String,ContainerInfo> getServices()
	{
		return services;
	}
	
	public static boolean hasService(String organization,String service)
	{
		if(services.containsKey(organization+"."+service))
			return true;
		else
			return false;
	}
	
	public static void removeContainer(ContainerInfo container)
	{		 
		msInfo.removeContainer(container);				
	}
	
	public static void addContainer(ContainerInfo container) throws CatalogException
	{		
		// Adiciona container ao catálogo
		msInfo.addContainer(container);		
		
		// Compõe o índice para os serviços registrados
		Set<OrganizationInfo> organizations = container.getOrganizations();
		
		for (OrganizationInfo org : organizations)
		{
			Set<EntityInfo> entities = org.getEntities();
			
			for (EntityInfo ent : entities)
			{
				Set<ServiceInfo> servs = ent.getServices();
			
				for (ServiceInfo ser : servs)
				{
					if (!ser.getScope().equals("local"))
					{
						services.put(org.getName()+"."+ser.getName(),container);
					}
						
				}
			}			
		}
	}
		
	public static ContainerInfo getContainerByService(String organizationName,String serviceName) throws CatalogException
	{
		if (services.containsKey(organizationName+"."+serviceName))
		{
			return (services.get(organizationName+"."+serviceName));
		}
		else
		{
			throw new CatalogException("Invalid service - " + serviceName + " - for organization - " + organizationName + " -");
		}
	}	
}
