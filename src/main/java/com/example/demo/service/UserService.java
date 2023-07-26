package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
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

	/**
	 * CSVデータに変換する
	 * @param userList
	 * @return
	 */
	public String convertToCSV(List<User> userList) {
		StringBuilder csvData = new StringBuilder();
		csvData.append("id,名前,住所,電話番号"); // ヘッダー行を追加

		for (User user : userList) {
			csvData.append("\n");
			csvData.append(user.getId()).append(",");
			csvData.append(user.getName()).append(",");
			csvData.append(user.getAddress()).append(",");
			csvData.append(user.getPhone()).append(",");
		}

		return csvData.toString();
	}

	
	/**
	 * 一括登録する
	 * @param userRequests
	 */
	public void bulkCreate(List<UserRequest> userRequests) {
		Date now = new Date();
		//TODO 一括INSERTに変更する
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
	 * CSVを元にデータを編集する
	 * @param records
	 */
	public void parseAndSaveUsers(Iterable<CSVRecord> records, List<String> errorMessages) {
		for (CSVRecord record : records) {
			try {
				Long id = Long.parseLong(record.get(0));
				Optional<User> optionalUser = userRepository.findById(id);

				if (optionalUser.isPresent()) {
					String name = record.get(1);
					String address = record.get(2);
					String phone = record.get(3);

					Date now = new Date();

					User user = optionalUser.get();
					user.setName(name);
					user.setAddress(address);
					user.setPhone(phone);
					user.setUpdateDate(now);
					userRepository.save(user);
				} else {
					errorMessages.add("該当ID（" + id + "）は存在しませんでした。無効な行：" + record);
				}
			} catch (NumberFormatException e) {
				errorMessages.add("CSVファイルのIDフィールドが数値に変換できません。無効な行：" + record);
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
		userRepository.delete(user);
	}

}