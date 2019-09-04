package org.midas.metainfo;

import java.io.Serializable;

public class ParameterInfo implements Comparable,Serializable
{
	String  name;
	Class   paramClass;
	boolean array;
	
	public ParameterInfo(String name, Class paramClass, boolean array) 
	{
		this.name = name;		
		this.array = array;
		this.paramClass = paramClass;
	}

	public boolean isArray() 
	{
		return array;
	}

	public String getName() 
	{
		return name;
	}

	public Class getParamClass() 
	{
		return paramClass;
	}
	
	public String toString()
	{
		String aux="";
		
		aux+= ("- "+name);
		
		if (array)
		{
			aux+="[]";
		}
		
		aux+= (" ("+paramClass.toString().replaceAll("class ","")+")\n");		
						
		return aux;
	}

	public int compareTo(Object param) 
	{
		String paramName = ((ParameterInfo)param).getName();		
		return ((this.name).compareTo(paramName));
	}

	@Override
	public boolean equals(Object param) 
	{
		if (((ParameterInfo)param).getName().equals(this.name))
			return true;
		else
			return false;	
	}
}
