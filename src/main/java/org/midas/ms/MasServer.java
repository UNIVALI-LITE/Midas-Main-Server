package org.midas.ms;

import org.midas.ms.manager.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasServer 
{
	private static Logger LOG = LoggerFactory.getLogger(MasServer.class);

	public static void main(String[] args)
	{
		LOG.info(" __  __  ___  ____     _     ____            __  __     _     ____ ");
		LOG.info("|  \\/  ||_ _||  _ \\   / \\   / ___|          |  \\/  |   / \\   / ___| ");
		LOG.info("| |\\/| | | | | | | | / _ \\  \\___ \\   _____  | |\\/| |  / _ \\  \\___ \\ ");
		LOG.info("| |  | | | | | |_| |/ ___ \\  ___) | |_____| | |  | | / ___ \\  ___) |");
		LOG.info("|_|  |_||___||____//_/   \\_\\|____/          |_|  |_|/_/   \\_\\|____/ ");
		LOG.info("====================================================================");
		   
		int port=7100;
		
		LOG.info("Initializing MAS Server on port "+port);
		Manager.initialize(port);
	}	
}
