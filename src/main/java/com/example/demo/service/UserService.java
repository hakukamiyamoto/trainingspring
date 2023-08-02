package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserSearchRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserMapper;
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

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * ユーザー情報 全検索
	 * @return 検索結果
	 */
	public List<User> searchAll() {
		return userMapper.searchAll();
	}

	/**
	 * ユーザー情報 主キー検索
	 * @return 検索結果
	 */
	public User findById(Long id) {
		return userMapper.findById(id);
	}

	/**
	  * ユーザー情報 新規登録
	  * @param user ユーザー情報
	  */
	public void create(UserRequest userRequest) {

		User existingUser = userRepository.findByUsername(userRequest.getUsername());
		if (existingUser != null) {
			throw new IllegalArgumentException("User ID already exists");
		}

		Date now = new Date();
		User user = new User();
		user.setName(userRequest.getName());
		user.setUsername(userRequest.getUsername());
		user.setAddress(userRequest.getAddress());
		user.setPhone(userRequest.getPhone());
		// パスワードをハッシュ化
		String hashedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
		user.setPassword(hashedPassword);
		user.setCreateDate(now);
		user.setUpdateDate(now);
		userMapper.insert(user);
	}

	/**
	 * 一括登録する
	 * @param userRequests ユーザーリクエストのリスト
	 */
	@Transactional
	public void bulkCreate(List<UserRequest> userRequests) {
		Date now = new Date();
		List<User> users = new ArrayList<>();

		// 一括登録対象のユーザーIDをチェックし、既存のユーザーIDと重複していないか確認
		for (UserRequest userRequest : userRequests) {
			// 一括登録対象のユーザーIDが既存のユーザーIDと重複していないか確認
			if (userMapper.findByUsername(userRequest.getUsername()) != null) {
				throw new RuntimeException("ユーザーIDが既に存在します: " + userRequest.getUsername());
			}
			// 一括登録対象のユーザーIDがリスト内の他のユーザーIDと重複していないか確認
			if (users.stream().anyMatch(u -> u.getUsername().equals(userRequest.getUsername()))) {
				throw new RuntimeException("同一のユーザーIDがリスト内に存在します: " + userRequest.getUsername());
			}

			User user = new User();
			user.setName(userRequest.getName());
			user.setUsername(userRequest.getUsername());
			user.setAddress(userRequest.getAddress());
			user.setPhone(userRequest.getPhone());
			// パスワードをハッシュ化
			String hashedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
			user.setPassword(hashedPassword);
			user.setCreateDate(now);
			user.setUpdateDate(now);
			users.add(user);
		}

		userMapper.bulkInsert(users);
	}

	/**
	 * ユーザー情報 更新
	 * @param user ユーザー情報
	 */
	public void update(UserUpdateRequest userUpdateRequest) {
		User user = findById(userUpdateRequest.getId());
		user.setUsername(userUpdateRequest.getUsername());
		user.setAddress(userUpdateRequest.getAddress());
		user.setName(userUpdateRequest.getName());
		user.setPhone(userUpdateRequest.getPhone());
		user.setPassword(userUpdateRequest.getPassword());
		user.setUpdateDate(new Date());
		userMapper.insert(user);
	}

	/**
	 * 指定されたキーワードで住所を前方一致で検索します。
	 * @param keyword キーワード
	 * @return 検索結果のユーザー情報リスト
	 */
	public List<User> searchByAddressStartingWith(String keyword) {
		return userMapper.findByAddressStartingWith(keyword);
	}

	/**
	 * 指定されたキーワードで住所を末尾一致で検索します。
	 * @param keyword キーワード
	 * @return 検索結果のユーザー情報リスト
	 */
	public List<User> searchByAddressEndingWith(String keyword) {
		return userMapper.findByAddressEndingWith(keyword);
	}

	/**
	 * 指定されたキーワードを住所に含むユーザー情報を検索します。
	 * @param keyword キーワード
	 * @return 検索結果のユーザー情報リスト
	 */
	public List<User> searchByAddressContaining(String keyword) {
		return userMapper.findByAddressContaining(keyword);
	}
	
	/**
     * ユーザー情報 Mapper
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * ユーザー情報検索
　　　* @param userSearchRequest リクエストデータ
     * @return 検索結果
     */
    public User search(UserSearchRequest userSearchRequest) {
        return userMapper.search(userSearchRequest);
    }

	/**
	 * CSVデータに変換する
	 * @param userList
	 * @return
	 */
	public String convertToCSV(List<User> userList) {
		StringBuilder csvData = new StringBuilder();
		csvData.append("id,ユーザーID,名前,住所,電話番号"); // ヘッダー行を追加

		for (User user : userList) {
			csvData.append("\n");
			csvData.append(user.getId()).append(",");
			csvData.append(user.getUsername()).append(",");
			csvData.append(user.getName()).append(",");
			csvData.append(user.getAddress()).append(",");
			csvData.append(user.getPhone()).append(",");
		}

		return csvData.toString();
	}

	/**
	 * CSVを元にデータを編集する
	 * @param records
	 */
	public void parseAndSaveUsers(Iterable<CSVRecord> records, List<String> errorMessages) {
	    int count = 0;
	    for (CSVRecord record : records) {
	        try {
	            count++;
	            Long id = Long.parseLong(record.get(0));
	            Optional<User> optionalUser = Optional.ofNullable(userMapper.findById(id));

	            if (optionalUser.isPresent()) {
	                String username = record.get(1);
	                String name = record.get(2);
	                String address = record.get(3);
	                String phone = record.get(4);

	                Date now = new Date();

	                User user = optionalUser.get();
	                user.setUsername(username);
	                user.setName(name);
	                user.setAddress(address);
	                user.setPhone(phone);
	                user.setUpdateDate(now);
	                userMapper.update(user);  
	            } else {
	                errorMessages.add("該当ID（" + id + "）は存在しませんでした。スキップされた行：" + record);
	            }
	        } catch (NumberFormatException e) {
	            errorMessages.add(count + "行目のIDが数値が無効でした。スキップされた行：" + record);
	        }
	    }
	}


	/**
	 * 複数のユーザー情報を削除
	 * @param deleteFlags 削除するユーザーIDのリスト
	 */
	public void deleteMultiple(List<Long> deleteFlags) {
		for (Long id : deleteFlags) {
			delete(id);
		}
	}

	/**
	  * ユーザー情報 物理削除
	  * @param id ユーザーID
	  */
	public void delete(Long id) {
		User user = findById(id);
		userMapper.delete(user);
	}

}