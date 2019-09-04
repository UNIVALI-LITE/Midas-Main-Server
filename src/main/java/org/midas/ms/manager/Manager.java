package org.midas.ms.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.midas.ms.MasServer;
import org.midas.ms.manager.tasks.SynchronizerTask;
import org.midas.ms.trader.Trader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class Manager 
{
	private static Logger LOG = LoggerFactory.getLogger(MasServer.class);
	
	// Contagem de Threads
	private static AtomicLong activeThreads = new AtomicLong(0);
	
	// Controle de Estado
	private static boolean connected;
	private static boolean stopping;
			
	// Controle de Tarefas
	private static List<FutureTask> tasks 	 = new ArrayList<FutureTask>(10);	
	private static ExecutorService tasksPool = Executors.newCachedThreadPool();
		
	/**
	 * Rotina de inicialização do MAS. 
	 */
	@SuppressWarnings("unchecked")
	public static void initialize(int port)
	{		
		//
		// 1. Inicia o servidor Jetty
		//
		try
		{			
			Server server = new Server(port);
			
			ServletHandler handler = new ServletHandler();
	        server.setHandler(handler);
	        handler.addServletWithMapping(Trader.class, "/masserver/trader");
	        
			server.start();						
		}
		catch (Exception e) 
		{
			throw new IllegalStateException("Unable to start Jetty, check that port "+port+" is free.",e);
		}
			
		//
		// 2. Inicia as tarefas do servidor
		//
		
		// Criando Tarefas
		tasks.add(new FutureTask(new SynchronizerTask()));		
		
		// Sinalizando Estado
		stopping = false;
		connected = true;
		
		// Disparando Tarefas
		for (FutureTask task : tasks)
		{
			tasksPool.execute(task);
		}
						
		// 6. Atualizando Interface com o Usu�rio	
		LOG.info("MAS server is online...");
		ManagerScreen.userInterfaceEvent("Connected");
		ManagerScreen.userInterfaceEvent("Refresh Services");				
	}
	
	public static void shutdown(boolean intentional,boolean taskCause)
	{		
		stopping=true;
		connected=false;
		
		// 1. Avisando Finaliza��o se Necess�rio
		if (!intentional)
		{
			if (taskCause)
				LOG.error("Disconnected due to Severe Internal Error");
			else
				LOG.error("Disconnected due to Unknown Severe Internal Error");
		}
		else
		{
			LOG.info("MAS Server is Offline");
		}
		
		// 2. Atualizando Interface com o Usu�rio		
		ManagerScreen.userInterfaceEvent("Disconnected");
		
		// 3. Cancelando Tarefas Ativas e Desligando Pool de Execu��o
		for (FutureTask task : tasks)
		{
			if ( (!task.isCancelled()) && (!task.isDone()) )
				task.cancel(true);
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

