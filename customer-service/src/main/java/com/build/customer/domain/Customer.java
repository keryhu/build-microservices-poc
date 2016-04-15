package com.build.customer.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class Customer {

	private Integer id;
	private String name;
	private String email;
	private String internalData;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getInternalData() {
		return internalData;
	}
	
	public void setInternalData(String internalData) {
		this.internalData = internalData;
	}

}
