package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

/**
 * ユーザー情報 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
	/**
	 * ユーザー情報 Repository
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * ユーザー情報 全検索
	 * @return 検索結果
	 */
	public List<User> searchAll() {
		return userRepository.findAll();
	}

	/**
	 * ユーザー情報 主キー検索
	 * @return 検索結果
	 */
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

	/**
	  * ユーザー情報 新規登録
	  * @param user ユーザー情報
	  */
	public void create(UserRequest userRequest) {
		Date now = new Date();
		User user = new User();
		user.setName(userRequest.getName());
		user.setAddress(userRequest.getAddress());
		user.setPhone(userRequest.getPhone());
		user.setCreateDate(now);
		user.setUpdateDate(now);
		userRepository.save(user);
	}

	/**
	 * ユーザー情報 更新
	 * @param user ユーザー情報
	 */
	public void update(UserUpdateRequest userUpdateRequest) {
		User user = findById(userUpdateRequest.getId());
		user.setAddress(userUpdateRequest.getAddress());
		user.setName(userUpdateRequest.getName());
		user.setPhone(userUpdateRequest.getPhone());
		user.setUpdateDate(new Date());
		userRepository.save(user);
	}
	
	/**
	 * 指定されたキーワードで住所を前方一致で検索します。
	 * @param keyword キーワード
	 * @return 検索結果のユーザー情報リスト
	 */
	public List<User> searchByAddressStartingWith(String keyword) {
		return userRepository.findByAddressStartingWith(keyword);
	}

	/**
	 * 指定されたキーワードで住所を末尾一致で検索します。
	 * @param keyword キーワード
	 * @return 検索結果のユーザー情報リスト
	 */
	public List<User> searchByAddressEndingWith(String keyword) {
		return userRepository.findByAddressEndingWith(keyword);
	}

	/**
	 * 指定されたキーワードを住所に含むユーザー情報を検索します。
	 * @param keyword キーワード
	 * @return 検索結果のユーザー情報リスト
	 */
	public List<User> searchByAddressContaining(String keyword) {
		return userRepository.findByAddressContaining(keyword);
	}

	
	
	public void bulkCreate(List<UserRequest> userRequests) {
	    Date now = new Date();

	    for (UserRequest userRequest : userRequests) {
	        User user = new User();
	        user.setName(userRequest.getName());
	        user.setAddress(userRequest.getAddress());
	        user.setPhone(userRequest.getPhone());
	        user.setCreateDate(now);
	        user.setUpdateDate(now);
	        userRepository.save(user);
	    }
	}
	 /**
	   * ユーザー情報 物理削除
	   * @param id ユーザーID
	   */
	  public void delete(Long id) {
	    User user = findById(id);
	    userRepository.delete(user);
	  }
	
}