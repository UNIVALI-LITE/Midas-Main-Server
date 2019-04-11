package org.midas.ms.manager.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

import org.midas.ms.manager.execution.Logger;
import org.midas.ms.manager.manager.tomcat.TomcatWrapper;
import org.midas.ms.manager.tasks.StatisticsTask;
import org.midas.ms.manager.tasks.SynchronizerTask;

public class Manager 
{
	// Contagem de Threads
	private static AtomicLong activeThreads = new AtomicLong(0);
	
	// Variáveis de estado
	private static boolean connected;
	private static boolean stopping;
	
	// Variáveis que Manipulam o Tomcat
	private final static TomcatWrapper tomcat = new TomcatWrapper();    
		
	// Variável que guarda as Rotinas de Funcionamento
	private static List<FutureTask> tasks 	 = new ArrayList<FutureTask>(10);	
	private static ExecutorService tasksPool = Executors.newCachedThreadPool();
		
	/**
	 * Rotina de inicialização do MAS. Se utiliza do modelo de recursos para 
	 * recuperar a lista de serviços, o URL do servidor, e o nome do MAS 
	 * e do modelo de mensagens para efetivar o cadastramento do MAS no TS.
	 */
	public static void initialize()
	{		
		/* 1. Carrega o catálogo
		try
		{
			Catalog.loadCatalog();
		}
		catch(ResourceException e)
		{
			ManagerScreen.notifyUser(("Unable to load catalog: "+e.getMessage()));
			e.printStackTrace();
			return;
		}*/
		
		// 2. Sobe o serviço do tomcat						
		try 
		{			
			tomcat.start(new File("").getAbsolutePath()+"/tomcat");												
		} 
		catch (Exception e) 
		{
			ManagerScreen.notifyUser(("Unable to start tomcat, check that port 8082 is free"));
			e.printStackTrace();
			return;
		}
			
		// 3. Sobe as rotinas de funcionamento
								
		// Criando Tarefas
		tasks.add(new FutureTask(new SynchronizerTask()));		
		tasks.add(new FutureTask(new StatisticsTask()));
		
		// Sinalizando Estado
		stopping=false;
		connected = true;
		
		// Disparando Tarefas
		for (FutureTask task : tasks)
		{
			tasksPool.execute(task);
		}
						
		// 6. Atualizando Interface com o Usuário	
		Logger.addEntry("MAS Server is Online");
		ManagerScreen.userInterfaceEvent("Connected");
		ManagerScreen.userInterfaceEvent("Refresh Services");				
	}
	
	public static void shutdown(boolean intentional,boolean taskCause)
	{		
		stopping=true;
		connected=false;
		
		// 1. Avisando Finalização se Necessário
		if (!intentional)
		{
			if (taskCause)
				Logger.addEntry("Disconnected due to Severe Internal Error");
			else
				Logger.addEntry("Disconnected due to Unknown Severe Internal Error");
		}
		else
		{
			Logger.addEntry("MAS Server is Offline");
		}
		
		// 2. Atualizando Interface com o Usuário		
		ManagerScreen.userInterfaceEvent("Disconnected");
		
		// 3. Cancelando Tarefas Ativas e Desligando Pool de Execução
		for (FutureTask task : tasks)
		{
			if ( (!task.isCancelled()) && (!task.isDone()) )
				task.cancel(true);
		}
			
		try 
		{
			tomcat.stop();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	
	
	public static void exit()
	{
		System.exit(0);
	}

	public static boolean isStopping()
	{		
		return stopping;		
	}
	
	public static boolean isConnected()
	{
		return connected;
	}

	public static long getThreadCount()
	{
		return (activeThreads.longValue());
	}
	
	/**
	 * Method invoked by a ServiceWrapper requisition when it starts operating to increase 
	 * the total active thread count.
	 */
	public static void increaseThreadCount()
	{
		activeThreads.incrementAndGet();
	}

	/**
	 * Method invoked by a ServiceWrapper requisition when it starts operating to decrease 
	 * the total active thread count.
	 */
	public static void decreaseThreadCount()
	{
		activeThreads.decrementAndGet();
	}
}

