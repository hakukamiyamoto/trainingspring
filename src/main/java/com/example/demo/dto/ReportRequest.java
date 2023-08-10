package com.example.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ReportRequest {

	/**
	 * タイトル
	 */
	@NotEmpty(message = "タイトルを入力してください")
	@Size(max = 100, message = "タイトルは100桁以内で入力してください")
	private String title;

    // titleのゲッター
    public String getTitle() {
        return title;
    }

    // titleのセッター
    public void setTitle(String title) {
        this.title = title;
    }
	
	
	/**
	 * 本文
	 */
	@NotEmpty(message = "本文を入力してください")
	private String report;

    // titleのゲッター
    public String getReport() {
        return report;
    }

    // titleのセッター
    public void setReport(String report) {
        this.report = report;
    }

}
