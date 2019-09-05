/**
 * 
 * @author Aluizio Haendchen Filho
 *
 * Esta classe cria uma abstracao Service para armazenar dados de servicos
 */

package org.midas.metainfo;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class ServiceInfo implements Comparable,Serializable
{
	private String name;
	private String path;
	private String scope;
	private String description;
	
	private EntityInfo entity;
	private Set<ParameterInfo> parameters = new TreeSet<ParameterInfo>();

	public ServiceInfo(String name,String path,String scope,String description,EntityInfo entity)
	{
		this.name 		 = new String(name);
		this.path		 = new String(path);
		this.scope		 = new String(scope);
		this.description = new String(description);
		this.entity 	 = entity;
	}
	

	public String getName()
	{
		return (name);
	}
				

	public String getPath()
	{
		return (path);
	}
	

	public String getDescription()
	{
		return (description);
	}
	
	public String getScope()
	{
		return scope;
	}

	public EntityInfo getEntity()
	{
		return (entity);
	}
	
	public Set getParameters() 
	{
		return parameters;
	}
	
	public void addParameter(ParameterInfo parameter)
	{
		parameters.add(parameter);
	}
	
	public String toString()
	{
		String aux="";
		
		aux+= ("Service: "+name+"\n\n");
		
		aux+= ("Path: \n - ");
		aux+= (path+"\n\n");
		
		aux+= ("Scope: \n - ");
		aux+= (scope+"\n\n");
			
		aux+= ("Parameters: \n");
		
		for (ParameterInfo parameter : parameters)
		{
			aux+= parameter.toString();
		}
		
		aux+= ("\nDescription: \n    ");
		aux+= (description);
		
		return aux;
	}
	
	public int compareTo(Object serv) 
	{
		String servName = ((ServiceInfo)serv).getName();		
		return ((this.name).compareTo(servName));
	}

	@Override
	public boolean equals(Object serv) 
	{
		if (((ServiceInfo)serv).getName().equals(this.name))
			return true;
		else
			return false;	
	}
}