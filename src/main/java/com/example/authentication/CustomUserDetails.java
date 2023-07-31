package com.example.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
// これを継承することで、UserDetails作成の際に記述量を減らせる
import org.springframework.security.core.userdetails.User;


public class CustomUserDetails extends User {

	// userid,password,authoritiesを引数に取るコンストラクタを用意
	public CustomUserDetails(String userid, String password, Collection<? extends GrantedAuthority> authorities) {
		super(userid, password, authorities);
	}



}
