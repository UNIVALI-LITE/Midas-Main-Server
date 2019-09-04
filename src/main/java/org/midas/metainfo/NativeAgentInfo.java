/**
 * 
 */
package org.midas.metainfo;

/**
 * @author Administrador
 *
 */
public class NativeAgentInfo extends AgentInfo
{
	private String className;
	private String packageName;
	
	public NativeAgentInfo(String name, String type, String protocol, String className, String packageName, OrganizationInfo organization)
	{
		super(name, type, protocol, organization);
		
		this.className=className;
		this.packageName=packageName;
	}

	public String getClassName()
	{
		return className;
	}

	public String getPackageName()
	{
		return packageName;
	}

	@Override
	public String toString()
	{
		String aux="";
		
		aux+= ("Agent: "+getName()+"\n\n");
		aux+= ("Protocol: \n - ");
		aux+= (getProtocol()+"\n\n");
		aux+= ("Package: \n - ");
		aux+= (packageName+"\n\n");
		aux+= ("Class: \n - ");
		aux+= (className+".class");
					
		return aux;
	}
}
