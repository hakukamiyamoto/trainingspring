package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.BulkUserRequests;
import com.example.demo.dto.KeywordForm;
import com.example.demo.dto.Signin;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserSearchRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

/**
 * ユーザー情報 Controller
 */
@Controller
public class UserController {

	/**
	 * ユーザー情報 Service
	 */
	@Autowired
	private UserService userService;

	@ControllerAdvice
	public class GlobalControllerAdvice {
		@ModelAttribute("user")
		public User globalUser() {
			return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		}
	}

	/**
	 * ユーザー情報一覧画面を表示
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@GetMapping(value = "/user/list")
	public String displayList(Model model, Principal principal) {
		String loggedInUserName = principal.getName();
		model.addAttribute("username", loggedInUserName);
		List<User> userlist = userService.searchAll();
		model.addAttribute("userlist", userlist);
		return "user/list";
	}

	/**
	   * ユーザー新規登録画面を表示
	   * @param model Model
	   * @return ユーザー新規登録画面
	   */
	@GetMapping(value = "/user/add")
	public String displayAdd(Model model) {
		model.addAttribute("userRequest", new UserRequest());
		return "user/add";
	}

	/**
	 * ユーザー新規登録
	 * @param userRequest リクエストデータ
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@PostMapping("/user/create")
	public String create(@Validated @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {

		if (result.hasErrors()) {
			// 入力チェックエラーの場合
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "user/add";
		}
		// ユーザー情報の登録
		userService.create(userRequest);
		return "redirect:/user/list";
	}

	/**
	 * ユーザー一括登録画面を表示
	 * @param model Model
	 * @return ユーザー一括登録画面
	 */
	@GetMapping("/user/bulkadd")
	public String showCreateForm(Model model) {
		BulkUserRequests bulkUserRequests = new BulkUserRequests();
		for (int i = 0; i < 1; i++) {
			bulkUserRequests.getUserRequests().add(new UserRequest());
		}
		model.addAttribute("bulkUserRequests", bulkUserRequests);
		return "user/bulkadd";
	}

	/**
	 * ユーザー一括登録
	 * @param bulkUserRequests ユーザー情報のリクエスト
	 * @param result           入力チェック結果のバインディング結果
	 * @param model            モデル
	 * @return ユーザー登録画面
	 */
	@PostMapping("/user/bulkcreate")
	public String bulkCreate(@Validated @ModelAttribute BulkUserRequests bulkUserRequests, BindingResult result,
			Model model) {
		List<UserRequest> userRequests = bulkUserRequests.getUserRequests();

		// 入力チェックエラーの場合
		if (result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "user/bulkadd";
		}

		try {
			// ユーザーの一括登録処理
			userService.bulkCreate(userRequests);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "user/bulkadd";
		}

		return "redirect:/user/list";
	}

	/**
	 * ユーザー情報詳細画面を表示
	 * @param id 表示するユーザーID
	 * @param model Model
	 * @return ユーザー情報詳細画面
	 */
	@GetMapping("/user/{id}")
	public String displayView(@PathVariable Long id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("userData", user);
		return "user/view";
	}

	/**
	 * ユーザー編集画面を表示
	 * @param id 表示するユーザーID
	 * @param model Model
	 * @return ユーザー編集画面
	 */
	@GetMapping("/user/{id}/edit")
	public String displayEdit(@PathVariable Long id, Model model) {
		User user = userService.findById(id);
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
		userUpdateRequest.setId(user.getId());
		userUpdateRequest.setUsername(user.getUsername());
		userUpdateRequest.setName(user.getName());
		userUpdateRequest.setPhone(user.getPhone());
		userUpdateRequest.setAddress(user.getAddress());

		model.addAttribute("userUpdateRequest", userUpdateRequest);
		return "user/edit";
	}

	/**
	 * ユーザー更新
	 * @param userRequest リクエストデータ
	 * @param model Model
	 * @return ユーザー情報詳細画面
	 */
	@PostMapping("/user/update")
	public String update(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();

			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "user/edit";
		}

		// ユーザー情報の更新
		userService.update(userUpdateRequest);
		return String.format("redirect:/user/%d", userUpdateRequest.getId());
	}

	/**
	   * ユーザーID検索画面を表示
	   * @param model Model
	   * @return ユーザー情報一覧画面
	   */
	@GetMapping(value = "/user/idsearch")
	public String displaySearch(Model model) {
		model.addAttribute("userSearchRequest", new UserSearchRequest());
		return "user/idsearch";
	}

	/**
	 * ユーザーID情報検索
	 * @param userSearchRequest リクエストデータ
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@PostMapping("/user/id_search")
	public String search(@ModelAttribute UserSearchRequest userSearchRequest, Model model) {
		User user = userService.search(userSearchRequest);
		model.addAttribute("userinfo", user);
		return "user/idsearch";
	}

	/**
	 *  検索画面を表示するGETリクエスト
	 * @param model
	 * @return
	 */
	@GetMapping("/user/search")
	public String showSearchPage(Model model) {
		// 検索キーワード入力フォームの初期化
		model.addAttribute("keywordForm", new KeywordForm());
		model.addAttribute("userlist", userService.searchAll());

		return "user/search";
	}

	// 名前での検索
	@PostMapping("/user/search/name")
	public String searchByName(@Validated @ModelAttribute KeywordForm keywordForm, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("errorMessage", "入力エラーがあります。");
	        return "user/search";
	    }

	    String nameKeyword = keywordForm.getNameKeyword();
	    if (nameKeyword == null || nameKeyword.isEmpty()) {
	        model.addAttribute("errorMessage", "名前を入力してください。");
	        return "user/search";
	    }

	    String searchType = keywordForm.getNameSearchType();  // <-- 名前用のsearchTypeに変更
	    List<User> nameSearchResult;

	    switch (searchType) {
	        case "fromStart":
	            nameSearchResult = userService.searchByNameStartingWith(nameKeyword);
	            break;
	        case "endWith":
	            nameSearchResult = userService.searchByNameEndingWith(nameKeyword);
	            break;
	        case "including":
	        default:
	            nameSearchResult = userService.searchByNameContaining(nameKeyword);
	            break;
	    }

	    model.addAttribute("userlist", nameSearchResult);
	    return "user/search";
	}

	// 住所検索をするリクエスト
	@PostMapping("/user/search/address")
	public String searchByAdress(@Validated @ModelAttribute KeywordForm keywordForm, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("errorMessage", "入力エラーがあります。");
	        return "user/search";
	    }

	    String addressKeyword = keywordForm.getAddressKeyword();
	    if (addressKeyword == null || addressKeyword.isEmpty()) {
	        model.addAttribute("errorMessage", "住所を入力してください。");
	        return "user/search";
	    }

	    String searchType = keywordForm.getAddressSearchType();  // <-- 住所用のsearchTypeに変更
	    List<User> addressSearchResult;

	    switch (searchType) {
	        case "fromStart":
	            addressSearchResult = userService.searchByAddressStartingWith(addressKeyword);
	            break;
	        case "endWith":
	            addressSearchResult = userService.searchByAddressEndingWith(addressKeyword);
	            break;
	        case "including":
	        default:
	            addressSearchResult = userService.searchByAddressContaining(addressKeyword);
	            break;
	    }

	    model.addAttribute("userlist", addressSearchResult);
	    return "user/search";
	}


	/**
	 * ユーザーリストをCSV形式でエクスポートする
	 *
	 * @return CSVファイルのバイト配列
	 * @throws IOException 入出力例外が発生した場合
	 */
	@GetMapping("/user/csv")
	public ResponseEntity<byte[]> exportUserListToCSV() {
		try {
			List<User> userList = userService.searchAll();
			String csvData = userService.convertToCSV(userList);
			byte[] csvBytes = csvData.getBytes("Shift_JIS");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("text/csv"));
			headers.setContentDispositionFormData("attachment", "userlist.csv");
			headers.setContentLength(csvBytes.length);

			return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 *  CSVアップロード画面を表示するGETリクエスト
	 * @param model
	 * @return
	 */
	@GetMapping("/user/uploadcsv")
	public String showUploadPage(Model model) {

		return "user/uploadcsv";
	}

	/**
	 * CSVファイルをアップロードして一括変更を行う
	 * @param file CSVファイル
	 * @return user/uploadcsv
	 * @throws RuntimeException CSVファイルのパースに失敗した場合にスローされます
	 */
	@PostMapping("/user/upload-csv")
	public String uploadCSV(@RequestParam("file") MultipartFile file, Model model) {
		List<String> errorMessages = new ArrayList<>();
		try {
			// ファイルの内容を読み取るためのInputStreamを作成
			BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream(), "Shift_JIS"));

			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

			// 取得した全てのレコードに対して登録
			userService.parseAndSaveUsers(records, errorMessages);
		} catch (IOException e) {
			throw new RuntimeException("CSV file parsing failed.", e);
		}
		model.addAttribute("success", "編集を完了しました。");

		if (!errorMessages.isEmpty()) {
			model.addAttribute("errors", errorMessages);
		}
		return "user/uploadcsv";
	}

	/**
	 * 複数のユーザー情報を削除
	 * @param deleteFlags 削除するユーザーIDのリスト
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@PostMapping("/user/mutipledelete")
	public String deleteMultiple(@RequestParam List<Long> deleteFlags, Model model) {
		// ユーザー情報の削除
		userService.deleteMultiple(deleteFlags);
		return "redirect:/user/list";
	}

	/**
	   * ユーザー情報削除
	   * @param id 表示するユーザーID
	   * @param model Model
	   * @return ユーザー情報詳細画面
	   */
	@GetMapping("/user/{id}/delete")
	public String delete(@PathVariable Long id, Model model) {
		// ユーザー情報の削除
		userService.delete(id);
		return "redirect:/user/list";
	}

	/**
	 * ログインページを表示
	 *@return ユーザーログイン画面
	 */
	@GetMapping("/signin")
	public String signin(Model model, @RequestParam(value = "failed", required = false) String failed) {
		if (failed != null) {
			model.addAttribute("errorMessage", "ユーザー名またはパスワードが違います。");
		}
		model.addAttribute("signin", new Signin());
		return "signin/signin";
	}

	@PostMapping("/signout")
	public String logout() {

		return "redirect:/signin"; // リダイレクト先
	}
}
