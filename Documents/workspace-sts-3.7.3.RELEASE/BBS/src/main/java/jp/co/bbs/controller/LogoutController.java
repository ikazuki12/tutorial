package jp.co.bbs.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {

	@RequestMapping(value = "/logout/")
	public String logOut(Model model, HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/";
	}
}
