package org.midas.ms.trader.webservices;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.midas.metainfo.ContainerInfo;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.OrganizationInfo;
import org.midas.metainfo.ParameterInfo;
import org.midas.metainfo.ServiceInfo;

public class Generator
{
	public static void GenerateWSDL(ContainerInfo container)
	{
		Set<OrganizationInfo> organizations = container.getOrganizations();
		
		for (OrganizationInfo org : organizations)
		{
			try
			{
				createAxisJws(org);
			}
			catch (IOException e)
			{
				// TODO Notificar a falha no LOGGER
				e.printStackTrace();
			}
		}
	}
	
	private static void createAxisJws(OrganizationInfo organization) throws IOException
	{
		// Criando o Arquivo
		String path = new File("").getAbsolutePath();
		File   file = new File(path+"/tomcat/webapps/axis/"+organization.getName()+".jws");		
		file.createNewFile();
		
		// Obtendo um writer sobre o arquivo
		FileWriter raf = new FileWriter(file);
						
		// Insere os imports básicos
		raf.write("import java.util.ArrayList;\n");
		raf.write("import java.util.HashMap;\n");
		raf.write("import java.util.List;\n");
		raf.write("import java.util.Map;\n\n");
				
		// Insere o import do Adapter
		raf.write("import org.midas.ms.trader.webservices.Adapter;\n\n");
				
		// Insere a declarção da classe
		raf.write("public class "+organization.getName()+"\n{\n");
					
		// Insere os métodos para os serviços da organização
		Set<EntityInfo> entities = organization.getEntities();
		
		for (EntityInfo entity : entities)
		{
			Set<ServiceInfo> services = entity.getServices();
			
			for (ServiceInfo service : services)
			{
				if (service.getScope().equals("web"))
				{
					// Insere um método com o nome do serviço
					raf.write("\tpublic List "+service.getName()+"(");
					
					// Insere parâmetros necessários
					Set<ParameterInfo> parametersSet = service.getParameters();
					ArrayList<ParameterInfo> parameters = new ArrayList(parametersSet);
					
					if (parameters.size()>0)
					{
						raf.write(parameters.get(0).getParamClass().toString().substring(6)+" "+parameters.get(0).getName());
						
						for (int i=1 ; i<parameters.size() ; i++)
						{
							raf.write(", "+parameters.get(i).getParamClass().toString().substring(6)+" "+parameters.get(i).getName());			
						}
					}
						
					// Fechando os parâmetros e a declaração do método
					raf.write(") throws Exception \n\t{\n");
					
					// insere parte que cria os objetos de entrada e saída
					raf.write("\t\tMap  in = new HashMap();\n");
					raf.write("\t\tList out = new ArrayList(10);\n\n");
						
					// Joga os parâmetros no HashMap
					for (ParameterInfo param : parameters)
					{
						raf.write("\t\tin.put(\""+param.getName()+"\","+param.getName()+");\n");			
					}
					
					// Insere uma linha em branco
					raf.write("\n");
					
					// Insere chamada ao Adapter
					raf.write("\t\tAdapter.provide(\""+service.getEntity().getOrganization().getName()+"\",\""+service.getName()+"\",in,out);\n\n");
					
					// Insere retorno do método
					raf.write("\t\treturn out;\n");
					
					// Insere o fechamento do método
					raf.write("\t}\n");		
				}					
			}
		}
		
		// Insere o fechamento da classe
		raf.write("}");
		
		raf.close();
	}
}
