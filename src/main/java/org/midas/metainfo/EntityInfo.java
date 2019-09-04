
package org.midas.metainfo;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class EntityInfo implements Comparable,Serializable
{
	private String name;
	private String type;
	private String protocol;
	
	private OrganizationInfo organization;
	private Set<ServiceInfo> services = new TreeSet<ServiceInfo>();
	
	public EntityInfo (String name,String type,String protocol,OrganizationInfo organization)
	{
		this.name = name;
		this.type = type;
		this.protocol = protocol;
		this.organization = organization;
	}
		
	public String getName() 
	{
		return name;
	}
	
	public String getType() 
	{
		return type;
	}
	
	public String getProtocol()
	{
		return protocol;
	}
	
	public OrganizationInfo getOrganization() 
	{
		return organization;
	}
		
	public Set<ServiceInfo> getServices()
	{
		return services;
	}
	
	public void addService(ServiceInfo service)
	{
		services.add(service);
	}
		
	public ServiceInfo getServiceByName(String serviceName) throws MetaInfoException
	{
		// TODO: Otimizar algoritmo
		for (ServiceInfo service : services) 
		{		
			if (service.getName().equals(serviceName))
			{
				return service;
			}
		}
		
		throw new MetaInfoException("Invalid service name - "+serviceName+" for entity - "+name+" - in organization - "+organization.getName()+ " -");		
	}
	
	public int compareTo(Object ent) 
	{
		String entName = ((EntityInfo)ent).getName();		
		return ((this.name).compareTo(entName));
	}

	public boolean equals(EntityInfo entity) 
	{
		if (entity.getName().equals(name))
			return true;
		else
			return false;
	}		
	
	@Override
	public boolean equals(Object ent) 
	{
		if (((EntityInfo)ent).getName().equals(this.name))
			return true;
		else
			return false;	
	}
}
