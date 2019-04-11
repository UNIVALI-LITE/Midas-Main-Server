package org.midas.ms.trader.webservices;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.midas.ms.catalog.Catalog;

public class Adapter
{
	public static void provide(String organization, String service, Map in, List out) throws Exception
	{
		try 
		{
			URL url = new URL("http://localhost:7100/masserver/trader?type=provide");
			HttpURLConnection uc  = (HttpURLConnection)url.openConnection();
				
			uc.setRequestProperty("Content-Type", "application/octet-stream");
			
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
									         
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(uc.getOutputStream()));		    
			oos.writeUTF(organization);
			oos.writeUTF(service);
			oos.writeObject(in);	
			
			oos.flush();
			oos.close();
			
			ObjectInputStream ois =	new ObjectInputStream(new BufferedInputStream(uc.getInputStream()));
			
			String message = ois.readUTF();
			
			if (message.equals("success"))
			{
				out.addAll((List)ois.readObject());
			}
			else if (message.equals("error"))
			{		
				throw new Exception ((String)ois.readObject());
			}
			else
			{
				throw new Exception("Unable to proccess provide request due to internal error...");
			}
				
			ois.close();
			
			uc.disconnect();			
		} 
		catch (MalformedURLException e) 
		{		
			e.printStackTrace();
			throw new Exception("Invalid Server URL",e);
		}
		catch (IOException e) 
		{			
			e.printStackTrace();
			throw new Exception("Error on data transfer to server",e);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			throw new Exception("Error on data transfer from server, wrong return type",e); 			
		} 				
	}
	
	public static void require(String organization, String service, Map in, List out) throws Exception
	{
	    // Captura o Endereço onde se Encontra o WebService
		String url = "temp"; // TODO Recuperar do Catalog
		
		// Criando e configurando o serviço
	    Call call = (Call) new Service().createCall();
	    call.setTargetEndpointAddress(url);
	    
	    // Marcando o método a ser chamado 
	    call.setOperationName(service);		      
	    
	    // Adaptando parâmetros
	    Object[] param = in.values().toArray();
	    
	    // Capturando Retorno
	    out.add(call.invoke(param)); 			
	}

}
