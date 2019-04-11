package org.midas.ms.manager.execution;

import org.midas.ms.manager.manager.ManagerScreen;

/**
 * The Logger class operates the logging of the MIDAS events and
 * ocurrences. It´s invoked by the {@link ServiceWrapper} objects,
 * to record their processing status and results.   
 */
public class Logger
{
	// TODO Desfazer macete c/ ManagerScreen....
	
	/**
	 * Adds a common entry to the log.
	 * 
	 *  @param entry  String with the message to log
	 */
	public static void addEntry(String entry)
	{
		ManagerScreen.notifyUser(entry);
	}

	/**
	 * Adds a iconified entry to the log
	 * 
	 * @param entry  String with the message to log
	 * @param success  If true displays a green icon along the log entry, if false displays a red one
	 */
	public static void addEntry(String entry, boolean success)
	{
		//ManagerScreen.notifyUser(entry,success);		
	}
}
