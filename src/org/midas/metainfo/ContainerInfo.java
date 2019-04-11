package org.midas.metainfo;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class ContainerInfo implements Comparable,Serializable
{
	private String name;
	private String type;
	private String path;
	
	private String containerPort;
	private String containerAddress;
	
	private String serverPort;
	private String serverAddress;
	
	private Set<DataSourceInfo>	  dataSources   = new TreeSet<DataSourceInfo>();	
	private Set<OrganizationInfo> organizations = new TreeSet<OrganizationInfo>();
			
	public ContainerInfo(String name, String type, String path, String containerAddress, String containerPort, String serverAddress, String serverPort)
	{
		this.name = name;
		this.type = type;
		this.path = path;
		this.containerPort = containerPort;
		this.containerAddress = containerAddress;
		this.serverPort = serverPort;
		this.serverAddress = serverAddress;
	}

	
	public String getContainerAddress()
	{
		return containerAddress;
	}


	public String getContainerPort()
	{
		return containerPort;
	}


	public String getServerAddress()
	{
		return serverAddress;
	}


	public String getServerPort()
	{
		return serverPort;
	}


	public String getName() 
	{
		return name;
	}
	
	public String getPath() 
	{
		return path;
	}
		
	public String getType() 
	{
		return type;
	}
		
	public Set<DataSourceInfo> getDataSources() 
	{
		return dataSources;
	}
	
	public void addDataSource(DataSourceInfo dataSource)
	{
		dataSources.add(dataSource);
	}
	
	public Set<OrganizationInfo> getOrganizations() 
	{
		return organizations;
	}
	
	public OrganizationInfo getOrganizationByName(String organizationName) throws MetaInfoException
	{
		// TODO: Otimizar Algoritmo
		for (OrganizationInfo org : organizations) 
		{		
			if (org.getName().equals(organizationName))
			{
				return org;
			}
		}
		
		throw new MetaInfoException("Invalid organization name");		
	}
	
	public void addOrganization(OrganizationInfo organization)
	{
		organizations.add(organization);
	}
	
	public String toString()
	{
		String aux="";
		
		if (type.equals("as"))
		{
			aux+= ("** Agent Server: "+name+" **\n\n");
		}
		else
		{
			aux+= ("** Enterprise Server: "+name+" **\n\n");
		}
		
		aux+= ("Directory: "+path+"\n\n");
		aux+= ("LocalURL: "+containerAddress+":"+containerPort+"\n\n");
		aux+= ("ServerURL: "+serverAddress+":"+serverPort+"\n");
						
		return aux;
	}
	
	public int compareTo(Object container) 
	{
		String containerName = ((ContainerInfo)container).getName();		
		return ((this.name).compareTo(containerName));
	}

	@Override
	public boolean equals(Object container) 
	{
		if (((ContainerInfo)container).getName().equals(this.name))
			return true;
		else
			return false;	
	}
}
