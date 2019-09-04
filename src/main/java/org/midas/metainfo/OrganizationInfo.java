/**
 * Created on 31/03/2005
 * @author Aluizio Haendchen Filho
 */
package org.midas.metainfo;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Documentar Classe
 */
public class OrganizationInfo implements Comparable,Serializable
{
	private String  name;
	private String  packageName;
	
	private ContainerInfo  container;
	private Set<EntityInfo> entities = new TreeSet<EntityInfo>();
		
	public OrganizationInfo(String name,String packageName,ContainerInfo container)
	{
		this.container	 = container;
		this.name 		 = name;
		this.packageName = packageName;					
	}
	
	public String getName() 
	{
		return name;
	}
	
	public ContainerInfo getContainer() 
	{
		return container;
	}
	
	public String getPackageName() 
	{
		return packageName;
	}
				
	public Set<EntityInfo> getEntities() 
	{
		return entities;
	}
	
	public void addEntity(EntityInfo entity)
	{
		entities.add(entity);
	}
	
	public EntityInfo getEntityByName(String entityName) throws MetaInfoException
	{
		// TODO: Otimizar Algoritmo
		for (EntityInfo entity : entities) 
		{
			if (entity.getName().equals(entityName))
			{
				return entity;
			}
		}
		
		throw new MetaInfoException("Invalid entity name - "+entityName+" - for organization - "+name+" -");		
	}
	
	public String toString()
	{
		String aux="";
		
		aux+= ("Organization: "+name+"\n\n");
		
		aux+= ("Package: \n - ");
		aux+= (packageName);
						
		return aux;
	}

	public int compareTo(Object org) 
	{
		String orgName = ((OrganizationInfo)org).getName();		
		return ((this.name).compareTo(orgName));
	}

	@Override
	public boolean equals(Object org) 
	{
		if (((OrganizationInfo)org).getName().equals(this.name))
			return true;
		else
			return false;	
	}	
}
