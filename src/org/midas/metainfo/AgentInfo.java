/**
 * 
 */
package org.midas.metainfo;

/**
 * @author Administrador
 *
 */
public abstract class AgentInfo extends EntityInfo
{
	public AgentInfo(String name, String type, String protocol,OrganizationInfo organization)
	{
		super(name,type,protocol,organization);
	}	
}
