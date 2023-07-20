package com.example.demo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザー情報更新リクエストデータ
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateRequest extends UserRequest implements Serializable {

	/**
	 * ユーザーID
	 */
	@NotNull
	private Long id;
}