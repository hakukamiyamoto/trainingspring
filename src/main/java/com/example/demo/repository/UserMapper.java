package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.UserSearchRequest;
import com.example.demo.entity.User;

/**
 * ユーザー情報 Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * ユーザー情報を全て検索します。
     * @return ユーザー情報のリスト
     */
    List<User> searchAll();
    
    
    /**
	  * 論理削除されていないユーザーを取得
	  * @return List<User>
	  */
    List<User> findWhereDeleteDateIsNull();
    
    /**
	  * 論理削除されているユーザーを取得
	  * @return List<User>
	  */
   List<User> findWhereDeleteDateIsNotNull();

    /**
     * IDによりユーザー情報を検索します。
     * @param id ユーザーID
     * @return ユーザー情報
     */
    User findById(Long id);

    /**
     * ユーザー名によりユーザー情報を検索します。
     * @param string ユーザー名
     * @return ユーザー情報
     */
    User findByUsername(String string);
    
    /**
     * ユーザー名により名前を表示します。
     * @param name ユーザー名
     * @return ユーザー情報
     */
    String showName(String username);

    /**
     * 住所で前方一致検索します。
     * @param address 住所
     * @return ユーザー情報のリスト
     */
    List<User> findByAddressStartingWith(String address);

    /**
     * 住所で後方一致検索します。
     * @param address 住所
     * @return ユーザー情報のリスト
     */
    List<User> findByAddressEndingWith(String address);

    /**
     * 住所が指定したキーワードを含むユーザー情報を検索します。
     * @param address 住所
     * @return ユーザー情報のリスト
     */
    List<User> findByAddressContaining(String address);
    
    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByNameContaining(String name);
    

    /**
     * ユーザー情報を新規に登録します。
     * @param user ユーザー情報
     */
    void insert(User user);
    
    /**
     * ユーザー情報を新規に一括登録します。
     * @param users ユーザー情報のリスト
     */
    void bulkInsert(List<User> users);
    
    /**
     * ユーザー情報検索
     * @param user 検索用リクエストデータ
     * @return ユーザー情報
     */
    User search(UserSearchRequest user);

    /**
     * ユーザー情報を更新します。
     * @param user ユーザー情報
     */
    void update(User user);

    /**
     * ユーザー情報を削除します。
     * @param user ユーザー
     */
    void delete(User user);

}
