package com.example.demo.dto;

import javax.validation.constraints.NotEmpty;

public class KeywordForm {
	@NotEmpty(message = "キーワードを入力してください。")
	private String keyword;
	private String searchType;

	public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchType() {
        return searchType;
    }

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

    
    
}