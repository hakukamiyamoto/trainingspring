package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.ReportRequest;

@Controller
public class ReportController {

    // 日報入力画面を表示するためのエンドポイント
    @GetMapping("/user/report")
    public String showReportForm(Model model) {
        model.addAttribute("reportRequest", new ReportRequest());
        return "user/report";  
    }

    // 日報のデータを受け取るためのエンドポイント
    @PostMapping("/user/postreport")
    public String postReport(@Valid @ModelAttribute ReportRequest reportRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("validationError", bindingResult.getAllErrors());
            return "user/report";  // エラーがある場合、入力フォームにリダイレクト
        }

        // ここで日報のデータをデータベースに保存するロジックを追加することができます。

        return "redirect:/user/mypage";  
    }
}
