package jp.co.bbs.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.bbs.dto.UserDto;
import jp.co.bbs.form.UserForm;
import jp.co.bbs.service.UserService;
import jp.co.bbs.utils.CipherUtil;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login/", method = RequestMethod.GET)
	public String userGet(Model model) {
		UserForm form = new UserForm();
		model.addAttribute("userForm", form);
		return "login";
	}
	
	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public String userGet(@ModelAttribute UserForm form, HttpSession session, Model model){
		UserDto user = UserDto.getInstance(
				form.getLoginId(), CipherUtil.encrypt(form.getPassword()), null, 0, 0);
		user = userService.getUser(user);
		List<String> messages = new ArrayList<String>();
		if (isValid(form.getLoginId(), form.getPassword(), user, messages)) {
			session.setAttribute("loginUser", user);
			return "redirect:/";
		} else {
			model.addAttribute("userForm", form);
			session.setAttribute("errorMessages", messages);
			return "login";
		}
		
	}
	private boolean isValid (String loginId, String password, UserDto user, List<String> messages) {
		if (loginId.length() == 0) {
			messages.add("ログインIDを入力してください");
		}
		if (password.length() == 0){
			messages.add("パスワードを入力してください");
		}
		if (user != null) {
			if (!user.isStopped()) {
				messages.add("ログインに失敗しました");
			}
		} else {
			messages.add("ログインに失敗しました");
		}
		if (messages.size() != 0) {
			return false;
		} else {
			return true;
		}
	}
}
