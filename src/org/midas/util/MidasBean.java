/**
 * Created on 25/11/2004
 * @author Aluizio Haendchen Filho
 */
package org.midas.util;

import java.io.Serializable;
import java.lang.reflect.Field;

public abstract class MidasBean implements Serializable 
{		
	/**
	 * recebe uma String com o nome do campo e retorna um objeto com o conteudo
	 * do atributo
	 * @param fieldName nome do atributo
	 * @return um objeto com o conteudo do atributo
	 */
	public Object getField(String fieldName) 
	{
		try
		{
			Class myClass = this.getClass();
			Field myField = myClass.getField(fieldName);
					
			if (!myField.isAccessible())
			{
				myField.setAccessible(true);
			}
			
			return(myField.get(this));
		}
		catch (NoSuchFieldException e)
		{
			return null;
		} 
		catch (IllegalArgumentException e) 
		{
			return null;
		} 
		catch (IllegalAccessException e) 
		{
			return null;
		}		
	}
	/**
	 * 
	 * @param fieldName nome do atributo
	 * @param value Referência genérica a qual será atribuída o atributo recuperado 
	 * @return boolean > true caso a recuperação seja bem-sucedida / false caso contrário
	 */
	public boolean setField(String fieldName,Object value)
	{
		try
		{
			Class myClass = this.getClass();
			Field myField = myClass.getField(fieldName);
					
			if (!myField.isAccessible())
			{
				myField.setAccessible(true);
			}
			
			myField.set(this,value);
			
			return true;
		}
		catch (NoSuchFieldException e)
		{
			return false;
		} 
		catch (IllegalArgumentException e) 
		{
			return false;
		} 
		catch (IllegalAccessException e) 
		{
			return false;
		}		
	}
	
	@Override
	public String toString()
	{
		String aux = ""+this.getClass().toString().substring(6);
		
		try
		{
			Class   myClass   = this.getClass();
			Field[] myFields  = myClass.getFields();
			
			for (Field field : myFields)
			{
				aux += "Field - Name: "+field.getName();
				aux += " | Value: "+field.get(this).toString()+"\n";
			}
		}	
		catch (IllegalArgumentException e) 
		{
			
		} 
		catch (IllegalAccessException e) 
		{
			
		}	
		
		return aux;		
	}
}