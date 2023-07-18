package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class BulkUserRequests {
	private List<UserRequest> userRequests;

	// コンストラクタ
	public BulkUserRequests() {
		// userRequests を新しい空のリストで初期化する
		this.userRequests = new ArrayList<>();
	}

	public List<UserRequest> getUserRequests() {
		return userRequests;
	}

	public void setUserRequests(List<UserRequest> userRequests) {
		this.userRequests = userRequests;

	}
}
