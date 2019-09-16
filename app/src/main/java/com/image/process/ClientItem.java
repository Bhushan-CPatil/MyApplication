package com.image.process;

import com.google.gson.annotations.SerializedName;

public class ClientItem{

	@SerializedName("Client_Name")
	private String clientName;

	public void setClientName(String clientName){
		this.clientName = clientName;
	}

	public String getClientName(){
		return clientName;
	}

	@Override
 	public String toString(){
		return 
			"ClientItem{" + 
			"client_Name = '" + clientName + '\'' + 
			"}";
		}
}