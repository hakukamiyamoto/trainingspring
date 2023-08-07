package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * ユーザー情報 リクエストデータ
 */
@Data
public class Signin implements Serializable {
	/**
	 * id
	 */
	@NotEmpty(message = "ユーザーIDを入力してください")
	@Size(max = 100, message = "ユーザーIDは100桁以内で入力してください")
	private String username;
	/**
	 * パスワード
	 */
	@NotEmpty(message = "パスワードを入力してください")
	@Size(max = 255, message = "パスワードは255桁以内で入力してください")
	private String password;
}