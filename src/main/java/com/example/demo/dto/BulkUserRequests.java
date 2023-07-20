package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

public class BulkUserRequests {
    
    /**
     * ユーザー情報リスト
     */
    private List<@Valid UserRequest> userRequests;

    /**
     * コンストラクタ
     */
    public BulkUserRequests() {
        // userRequests を新しい空のリストで初期化する
        this.userRequests = new ArrayList<>();
    }

    /**
     * ユーザー情報リストを取得します。
     * 
     * @return ユーザー情報リスト
     */
    public List<@Valid UserRequest> getUserRequests() {
        return userRequests;
    }

    /**
     * ユーザー情報リストを設定します。
     * 
     * @param userRequests ユーザー情報リスト
     */
    public void setUserRequests(List<@Valid UserRequest> userRequests) {
        this.userRequests = userRequests;
    }
}
