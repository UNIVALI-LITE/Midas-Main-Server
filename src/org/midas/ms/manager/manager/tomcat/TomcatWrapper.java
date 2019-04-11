package org.midas.ms.manager.manager.tomcat;

public class TomcatWrapper 
{
	TomcatStartup tomcat;
	
	public void start(String path) throws Exception
	{
		tomcat = new TomcatStartup();
		tomcat.setPath(path);
		tomcat.startTomcat();			
	}
	
	public void stop() throws Exception
	{		
		tomcat.stopTomcat();			
	}
}
