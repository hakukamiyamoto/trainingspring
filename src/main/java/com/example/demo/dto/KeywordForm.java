package com.example.demo.dto;

@ValidKeyword
public class KeywordForm {

	private String addressKeyword;
	private String nameKeyword;
	private String addressSearchType;
	private String nameSearchType;

	public String getAddressKeyword() {
		return addressKeyword;
	}

	public void setAddressKeyword(String addressKeyword) {
		this.addressKeyword = addressKeyword;
	}

	public String getNameKeyword() {
		return nameKeyword;
	}

	public void setNameKeyword(String nameKeyword) {
		this.nameKeyword = nameKeyword;
	}

	public String getAddressSearchType() {
		return addressSearchType;
	}

	public void setAddressSearchType(String adressSearchType) {
		this.addressSearchType = adressSearchType;
	}

	public String getNameSearchType() {
		return nameSearchType;
	}

	public void setNameSearchType(String nameSearchType) {
		this.nameSearchType = nameSearchType;
	}

}