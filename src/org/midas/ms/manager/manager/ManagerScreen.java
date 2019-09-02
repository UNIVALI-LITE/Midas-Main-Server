package org.midas.ms.manager.manager;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.midas.metainfo.ContainerInfo;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.MSInfo;
import org.midas.metainfo.MetaInfoException;
import org.midas.metainfo.OrganizationInfo;
import org.midas.metainfo.ServiceInfo;
import org.midas.ms.catalog.Catalog;
import org.midas.ms.manager.manager.listeners.MainListener;
import org.midas.ms.manager.manager.listeners.ViewListener;

import thinlet.FrameLauncher;
import thinlet.Thinlet;

public class ManagerScreen
{
	// Vari�veis de Estado
	private static boolean hidden = true;
	
	// Vari�veis do Thinlet
	private static Object  mainScreen;
	private static Object  viewScreen;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static FrameLauncher frame;
	private static Thinlet 		 thinlet;
				
	// �cones
	private static Image agentIcon;
	private static Image folderIcon;
	private static Image serviceIcon;
	private static Image containerIcon;
	private static Image componentIcon;
	private static Image organizationIcon;
	private static Image serviceOkIcon;
	private static Image serviceErrorIcon;
		
	public static void show()
	{
		// 1. Montando Tela de Visualiza��o
		thinlet = new Thinlet();
				
		try 
		{			
			// 2. Renderizando Arquivos XUL
			mainScreen  = thinlet.parse("/org/midas/ms/manager/manager/screens/main.xul",new MainListener());
			viewScreen  = thinlet.parse("/org/midas/ms/manager/manager/screens/view.xul",new ViewListener());
						
			// 3. Relacionando as Telas
			thinlet.add(mainScreen);
			thinlet.add(thinlet.find(mainScreen,"viewTab"),viewScreen);
						
			// 4. Disparando Frame AWT			
			frame = new FrameLauncher("Multi-Agent System Server Manager",thinlet,500,500);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

			// 5. Acertando Estado da Tela
			hidden = false;
			
			// 5. Sincronizando com estado do AS
			if (Manager.isConnected())
			{
				// Verifica se os �cones j� foram carregados
				if (organizationIcon == null)
				{
					agentIcon = tk.getImage(new File("").getAbsolutePath()+"/images/agentIcon.gif");
					folderIcon = tk.getImage(new File("").getAbsolutePath()+"/images/folderIcon.gif");
					serviceIcon = tk.getImage(new File("").getAbsolutePath()+"/images/serviceIcon.gif");
					componentIcon = tk.getImage(new File("").getAbsolutePath()+"/images/componentIcon.gif");					
					containerIcon = tk.getImage(new File("").getAbsolutePath()+"/images/containerIcon.gif");
					organizationIcon = tk.getImage(new File("").getAbsolutePath()+"/images/organizationIcon.gif");

					serviceOkIcon = tk.getImage(new File("").getAbsolutePath()+"/images/serviceOk.gif");
					serviceErrorIcon = tk.getImage(new File("").getAbsolutePath()+"/images/serviceError.gif");
				}
				
				userInterfaceEvent("Connected");
				userInterfaceEvent("Refresh Services");			
			}									
		}
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,("Unable to start Manager - could not find all the XUL files"));
		}				
	}
	
	public static void userInterfaceEvent(String event)
	{
		if (!hidden)
		{
			if (event.equals("Refresh Services"))
			{
				refreshServices();
				refreshDetails();
			}		
			else if (event.equals("Refersh Logger"))
			{
				
			}
			else if (event.equals("Refresh Statistics"))
			{
				refreshStatistics();
			}
			else if (event.equals("Refresh Details"))
			{
				refreshDetails();
			}
			else if (event.equals("Connected"))
			{
				// Verifica se os �cones j� foram carregados
				if (organizationIcon == null)
				{
					agentIcon = tk.getImage(new File("").getAbsolutePath()+"/images/agentIcon.gif");
					folderIcon = tk.getImage(new File("").getAbsolutePath()+"/images/folderIcon.gif");
					serviceIcon = tk.getImage(new File("").getAbsolutePath()+"/images/serviceIcon.gif");
					componentIcon = tk.getImage(new File("").getAbsolutePath()+"/images/componentIcon.gif");					
					containerIcon = tk.getImage(new File("").getAbsolutePath()+"/images/containerIcon.gif");
					organizationIcon = tk.getImage(new File("").getAbsolutePath()+"/images/organizationIcon.gif");
					
					serviceOkIcon = tk.getImage(new File("").getAbsolutePath()+"/images/serviceOk.gif");
					serviceErrorIcon = tk.getImage(new File("").getAbsolutePath()+"/images/serviceError.gif");
				}
				
				refreshConnection();
			}
			else if (event.equals("Disconnected"))
			{
				refreshDetails();
				refreshServices();				
				refreshStatistics();
				refreshConnection();
			}
		}
	}
			
	public static void notifyUser(String message)
	{
		if (!hidden)
		{
			Object logList = thinlet.find(viewScreen,"logList");
			Object logEntry = Thinlet.create("item");
			
			thinlet.setString(logEntry,"text",new GregorianCalendar().getTime()+" | "+message);			
			thinlet.add(logList,logEntry);
		}
		else
		{
			System.out.println(message);
		}
	}
	
	public static void notifyUser(String message, boolean success)
	{
		if (!hidden)
		{
			Object logList = thinlet.find(viewScreen,"logList");
			Object logEntry = Thinlet.create("item");
			
			if (success)
			{
				thinlet.setIcon(logEntry,"icon",serviceOkIcon);
			}
			else
			{
				thinlet.setIcon(logEntry,"icon",serviceErrorIcon);
			}
			
			thinlet.setString(logEntry,"text",new GregorianCalendar().getTime()+" | "+message);			
			thinlet.add(logList,logEntry);
		}
		else
		{
			System.out.println(message);
		}	
	}
			
	private static void refreshDetails()
	{
		if (hidden)
		{
			return;
		}
		else if (!Manager.isConnected())
		{
			thinlet.setString(thinlet.find(viewScreen,"detailsTextArea"),"text","");			
		}
		else
		{
			// Vari�veis Thinlet 
			Object thinletDetails      = thinlet.find(viewScreen,"detailsTextArea");
			Object thinletServicesTree = thinlet.find(viewScreen,"servicesTree");
			
			// Vari�veis de Informa��o
			String            info="";
			ContainerInfo     container;			
						
			// Obtendo Strings com as Informa��es da Sele��o			
			String[] selection = null;
			
			try
			{
				selection = (thinlet.getString(thinlet.getSelectedItem(thinletServicesTree),"name")).split("-");
			}
			catch(IllegalArgumentException e){}
			
			// Limpando a �rea de Texto
			thinlet.removeAll(thinletDetails);
			
			// Preenchendo o Painel			
			if (selection!=null && !selection[0].equals("folder"))
			{				
				try 
				{										
					MSInfo msInfo = Catalog.getMS();
					container = msInfo.getContainerByName(selection[1]);
					
					if (selection[0].equals("container"))
					{
						info = container.toString();
					}					
					else if (selection[0].equals("organization"))
					{
						info = container.getOrganizationByName(selection[2]).toString();
					}
					else if (selection[0].equals("entity"))
					{
						info = container.getOrganizationByName(selection[2]).getEntityByName(selection[3]).toString();						
					}
					else if (selection[0].equals("service"))
					{
						info = container.getOrganizationByName(selection[2]).getEntityByName(selection[3]).getServiceByName(selection[4]).toString();						
					}										
				} 
				catch (MetaInfoException e)
				{
					e.printStackTrace();
					notifyUser("unable to display details due to severe error on the consistence of information about the container, recommended to re-register the container");					
					return;				
				}										
			}
			else
			{
				if(selection!=null)
					info = selection[1]+" folder";
			}
			
			thinlet.setString(thinletDetails,"text",info);
		}
	}
	
	private static void refreshServices()
	{
		if (hidden)
		{
			return;
		}
		else if (!Manager.isConnected())
		{
			thinlet.removeAll(thinlet.find(viewScreen,"servicesTree"));					
			thinlet.removeAll(thinlet.find(viewScreen,"containerList"));
		}
		else
		{			
			// Vari�veis Thinlet
			Object thinletContainerList = thinlet.find(viewScreen,"containerList");
			Object thinletStyleSelection = thinlet.find(viewScreen,"viewCheckBox");
			Object thinletServicesTree = thinlet.find(viewScreen,"servicesTree");			
			
			// Vari�veis Thinlet Auxiliares
			Object thinletItem;
			Object thinletOrgNode;
			Object thinletEnttNode;
			Object thinletServNode;
			Object thinletAgentsNode;
			Object thinletContainerNode;
			Object thinletComponentsNode;			
			
			// Vari�veis de Informa��o
			Set<ServiceInfo> services;
			Set<EntityInfo> entities;
			Set<ContainerInfo> containers;
			Set<OrganizationInfo> organizations;
									
			// Limpando as �rvores e Listas
			thinlet.removeAll(thinletContainerList);
			thinlet.removeAll(thinletServicesTree);
			
			// Preenchendo �rvore de Servi�os
			if ((thinlet.getBoolean(thinletStyleSelection,"selected")))
			{
				// Varrendo Containers
				containers = Catalog.getMS().getContainers();
				
				for (ContainerInfo containerInfo : containers)
				{		
					// Criando Item Thinlet
					thinletItem = Thinlet.create("item");
					thinlet.setIcon(thinletItem,"icon",containerIcon);
					thinlet.setString(thinletItem,"text",containerInfo.getName());
					
					// Adicionando Item a � Lista
					thinlet.add(thinletContainerList,thinletItem);
					
					// Criando N� Thinlet
					thinletContainerNode = Thinlet.create("node");
					thinlet.setIcon(thinletContainerNode,"icon",containerIcon);
					thinlet.setString(thinletContainerNode,"text",containerInfo.getName());
					thinlet.setString(thinletContainerNode,"name","container-"+containerInfo.getName());
					
					// Adicionando N� � �rvore
					thinlet.add(thinletServicesTree,thinletContainerNode);
					

					
					
					// Varrendo Organiza��es
					organizations = containerInfo.getOrganizations();
						
					for (OrganizationInfo orgInfo : organizations)
					{
						// Criando N� Thinlet
						thinletOrgNode = Thinlet.create("node");
						thinlet.setIcon(thinletOrgNode,"icon",organizationIcon);
						thinlet.setString(thinletOrgNode,"text",orgInfo.getName());
						thinlet.setString(thinletOrgNode,"name","organization-"+containerInfo.getName()+"-"+orgInfo.getName());
						
						// Adicionando N� ao Container
						thinlet.add(thinletContainerNode,thinletOrgNode);				
						
						// Criando N�s Separadores de Agentes/Componentes
						thinletAgentsNode = Thinlet.create("node");
						thinlet.setString(thinletAgentsNode,"text","/Agents");
						thinlet.setString(thinletAgentsNode,"name","folder-"+"agents");
						thinlet.setIcon(thinletAgentsNode,"icon",folderIcon);
						thinletComponentsNode = Thinlet.create("node");
						thinlet.setString(thinletComponentsNode,"text","/Components");
						thinlet.setString(thinletComponentsNode,"name","folder-"+"components");
						thinlet.setIcon(thinletComponentsNode,"icon",folderIcon);
						
						// Adicionando N�s Separadores � Organiza��o
						thinlet.add(thinletOrgNode,thinletAgentsNode);
						thinlet.add(thinletOrgNode,thinletComponentsNode);
						
						// Varrendo Entidades
						entities = orgInfo.getEntities();
							
						for (EntityInfo enttInfo : entities)
						{								
							// Criando N� Thinlet
							thinletEnttNode = Thinlet.create("node");
							thinlet.setString(thinletEnttNode,"text",enttInfo.getName());
							thinlet.setString(thinletEnttNode,"name","entity-"+containerInfo.getName()+"-"+orgInfo.getName()+"-"+enttInfo.getName());
								
							// Adicionando N� ao N� Pai
							if ((enttInfo.getType()).equals("agent"))
							{
								thinlet.setIcon(thinletEnttNode,"icon",agentIcon);
								thinlet.add(thinletAgentsNode,thinletEnttNode);
							}
							else
							{
								thinlet.setIcon(thinletEnttNode,"icon",componentIcon);
								thinlet.add(thinletComponentsNode,thinletEnttNode);
							}
													
							// Varrendo Servi�os
							services = enttInfo.getServices();
								
							for (ServiceInfo servInfo : services)
							{								
								// Criando N� Thinlet
								thinletServNode = Thinlet.create("node");							
								thinlet.setIcon(thinletServNode,"icon",serviceIcon);
								thinlet.setString(thinletServNode,"text",servInfo.getName());
								thinlet.setString(thinletServNode,"name","service-"+containerInfo.getName()+"-"+orgInfo.getName()+"-"+enttInfo.getName()+"-"+servInfo.getName());
									
								// Adicionando N� ao N� Pai
								thinlet.add(thinletEnttNode,thinletServNode);	
							}
						}						
					}
				}
			}
			else
			{
				/* Varrendo Servi�os
				Collection servs = Catalog.getServices().values();
				services = new TreeSet<>(servs);
				
				for (ServiceInfo servInfo : services)
				{					
					// Criando N� Thinlet
					thinletServNode = Thinlet.create("node");
					
					StringBuilder thinletText = new StringBuilder();
					
					thinletText.append(servInfo.getName());
					thinletText.append(" ( ");
					thinletText.append(servInfo.getEntity().getName());
					thinletText.append(" - ");
					thinletText.append(servInfo.getEntity().getOrganization().getName());
					thinletText.append(" - ");
					thinletText.append(servInfo.getEntity().getOrganization().getContainer().getName());				
					thinletText.append(" ) ");
					
					thinlet.setIcon(thinletServNode,"icon",serviceIcon);
					thinlet.setString(thinletServNode,"text",thinletText.toString());
					thinlet.setString(thinletServNode,"name","service-"+servInfo.getEntity().getOrganization().getName()+"-"+servInfo.getName());
										
					thinlet.add(thinletServicesTree,thinletServNode);
				}*/
			}	
		}
	}

	private static void refreshStatistics()
	{
		if (hidden)
		{
			return;
		}
		else if (!Manager.isConnected())
		{
			// Vari�veis Thinlet
			Object thinletThreadField = thinlet.find(viewScreen,"threadCount");
			Object thinletMemoryField = thinlet.find(viewScreen,"memoryUsage");
			
			// Populando a Tela
			thinlet.setString(thinletThreadField,"text","");
			thinlet.setString(thinletMemoryField,"text","");
		}
		else
		{			
			// Vari�veis Thinlet
			Object thinletThreadField = thinlet.find(viewScreen,"threadCount");
			Object thinletMemoryField = thinlet.find(viewScreen,"memoryUsage");
			
			// Obtendo Informa��es
			Long memoryUsage = Runtime.getRuntime().totalMemory();
								
			// Populando a Tela
			thinlet.setString(thinletThreadField,"text",new Long(Manager.getThreadCount()).toString());
			thinlet.setString(thinletMemoryField,"text",memoryUsage.toString());
		}
	}
	
	public static void refreshConnection()
	{
		if (hidden)
		{
			return;
		}
		else if (Manager.isConnected())
		{			
			Object offlineLabel  = thinlet.find(viewScreen,"statusOffline");
			Object onlineLabel   = thinlet.find(viewScreen,"statusOnline");
			Object startMenuItem = thinlet.find(mainScreen,"startMenuItem");
			Object stopMenuItem  = thinlet.find(mainScreen,"stopMenuItem");
			
			thinlet.setBoolean(offlineLabel,"visible",false);
			thinlet.setBoolean(onlineLabel,"visible",true);
			thinlet.setBoolean(startMenuItem,"enabled",false);
			thinlet.setBoolean(stopMenuItem,"enabled",true);								
		}
		else
		{
			Object offlineLabel = thinlet.find(viewScreen,"statusOffline");
			Object onlineLabel  = thinlet.find(viewScreen,"statusOnline");
			Object startMenuItem = thinlet.find(mainScreen,"startMenuItem");
			Object stopMenuItem  = thinlet.find(mainScreen,"stopMenuItem");
			
			thinlet.setBoolean(onlineLabel,"visible",false);
			thinlet.setBoolean(offlineLabel,"visible",true);
			thinlet.setBoolean(stopMenuItem,"enabled",false);
			thinlet.setBoolean(startMenuItem,"enabled",true);
		}
	}
}