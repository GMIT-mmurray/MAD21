package com.example.intentdemo3;

import java.io.Serializable;

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	
	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFullName() {
		return  firstName + " " + lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
