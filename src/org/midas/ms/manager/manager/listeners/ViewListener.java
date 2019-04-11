package org.midas.ms.manager.manager.listeners;

import org.midas.ms.manager.manager.ManagerScreen;

public class ViewListener
{		
	public void updateTree()
	{
		ManagerScreen.userInterfaceEvent("Refresh Services");
	}
	
	public void displayDetails()
	{
		ManagerScreen.userInterfaceEvent("Refresh Details");
	}
}
