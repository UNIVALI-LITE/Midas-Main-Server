/**
 * 
 */
package org.midas.metainfo;

/**
 * @author Administrador
 *
 */
public class WebServiceComponentInfo extends ComponentInfo
{
	private String url;

	public WebServiceComponentInfo(String name, String type, String protocol, String url, OrganizationInfo organization)
	{
		super(name, type, protocol, organization);
		
		this.url = url;
	}

	public String getUrl()
	{
		return url;
	}
	
	@Override
	public String toString()
	{
		String aux="";
		
		aux+= ("Component: "+getName()+"\n\n");
		aux+= ("Protocol: \n - ");
		aux+= (getProtocol()+"\n\n");
		aux+= ("Url: \n - ");
		aux+= (url+"\n\n");
					
		return aux;
	}
}
