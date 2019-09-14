package com.image.process;

import com.google.gson.annotations.SerializedName;

public class JsonResItem{

	@SerializedName("sendFlag")
	private String sendFlag;

	@SerializedName("phone")
	private String phone;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("designation")
	private String designation;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	public void setSendFlag(String sendFlag){
		this.sendFlag = sendFlag;
	}

	public String getSendFlag(){
		return sendFlag;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDesignation(String designation){
		this.designation = designation;
	}

	public String getDesignation(){
		return designation;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"JsonResItem{" + 
			"sendFlag = '" + sendFlag + '\'' + 
			",phone = '" + phone + '\'' + 
			",image_url = '" + imageUrl + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",designation = '" + designation + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}