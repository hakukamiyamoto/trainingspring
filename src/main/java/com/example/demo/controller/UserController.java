package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.BulkUserRequests;
import com.example.demo.dto.UserRequest;
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

	/**
	 * ユーザー情報一覧画面を表示
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@GetMapping(value = "/user/list")
	public String displayList(Model model) {
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
	 * ユーザー一括登録処理
	 * @param bulkUserRequests ユーザー情報のリクエスト
	 * @param model Model
	 * @return ユーザー情報一覧ページ
	 */
	@PostMapping("/user/bulkcreate")
	public String bulkCreate(@ModelAttribute BulkUserRequests bulkUserRequests, Model model) {
		List<UserRequest> userRequests = bulkUserRequests.getUserRequests();

		List<String> validationErrorList = new ArrayList<>();

		// 入力チェックとバリデーションエラーの処理
		for (int i = 0; i < userRequests.size(); i++) {
			UserRequest userRequest = userRequests.get(i);

			// 名前のバリデーション
			if (userRequest.getName() == null || userRequest.getName().isEmpty()) {
				validationErrorList.add("ユーザー" + (i + 1) + "の名前は必須です。");
			}

			// 電話番号のバリデーション
			if (userRequest.getPhone() == null || userRequest.getPhone().isEmpty()) {
				validationErrorList.add("ユーザー" + (i + 1) + "の電話番号は必須です。");
			}
		}

		// バリデーションエラーがある場合はエラーメッセージを表示
		if (!validationErrorList.isEmpty()) {
			model.addAttribute("validationError", validationErrorList);
			return "user/bulkadd";
		}

		// ユーザーの一括登録処理
		userService.bulkCreate(userRequests);

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

}