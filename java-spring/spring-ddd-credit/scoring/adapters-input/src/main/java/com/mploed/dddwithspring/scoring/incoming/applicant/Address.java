package com.mploed.dddwithspring.scoring.incoming.applicant;

import java.io.Serializable;

public class Address implements Serializable {
	private String street;

	private String postCode;

	private String city;

	public String getStreet() {
		return street;
	}

	public String getPostCode() {
		return postCode;
	}

	public String getCity() {
		return city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Address{" +
				"street='" + street + '\'' +
				", postCode='" + postCode + '\'' +
				", city='" + city + '\'' +
				'}';
	}
}
