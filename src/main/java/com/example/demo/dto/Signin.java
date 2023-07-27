package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
	@NotNull(message = "IDを入力してください")
	@Max(value = 99999, message = "IDは5桁以内で入力してください")
	private Long id;
	/**
	 * パスワード
	 */
	@NotEmpty(message = "パスワードを入力してください")
	@Size(max = 255, message = "パスワードは255桁以内で入力してください")
	private String password;
}