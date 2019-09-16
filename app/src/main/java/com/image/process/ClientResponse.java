package com.image.process;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ClientResponse{

	@SerializedName("client")
	private List<ClientItem> client;

	@SerializedName("error")
	private boolean error;

	public void setClient(List<ClientItem> client){
		this.client = client;
	}

	public List<ClientItem> getClient(){
		return client;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"ClientResponse{" + 
			"client = '" + client + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}