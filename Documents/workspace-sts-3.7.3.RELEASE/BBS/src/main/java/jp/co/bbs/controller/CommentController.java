package jp.co.bbs.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.bbs.dto.UserDto;
import jp.co.bbs.form.CommentForm;
import jp.co.bbs.service.CommentService;

@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@RequestMapping(value = "/comment/", method = RequestMethod.POST)
	public String commentInsert(@ModelAttribute CommentForm form, Model model, HttpSession session) {
		UserDto loginUser = (UserDto) session.getAttribute("loginUser");
		Date date = new Date();
		List<String> messages = new ArrayList<String>();
		if (isValid(form.getText(), messages)) {
			commentService.commentInsert(
					loginUser.getId(), form.getMessageId(), form.getText(), date);
			return "redirect:/";
		} else {
			session.setAttribute("errorMessages",messages);	
			return "redirect:/";
		}
	}
	
	private boolean isValid(String text, List<String> messages) {
		
		if (text.equals("")) {
			messages.add("内容を入力してください");
		} else if (500 < text.length()) {
			messages.add("500文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
