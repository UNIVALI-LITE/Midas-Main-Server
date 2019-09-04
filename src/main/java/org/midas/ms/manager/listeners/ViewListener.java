package org.midas.ms.manager.listeners;

import org.midas.ms.manager.ManagerScreen;

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
