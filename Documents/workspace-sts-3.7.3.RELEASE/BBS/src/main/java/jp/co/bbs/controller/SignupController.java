package jp.co.bbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.bbs.dto.BranchDto;
import jp.co.bbs.dto.PositionDto;
import jp.co.bbs.dto.UserDto;
import jp.co.bbs.form.UserForm;
import jp.co.bbs.service.BranchService;
import jp.co.bbs.service.PositionService;
import jp.co.bbs.service.UserService;
import jp.co.bbs.utils.CipherUtil;

@Controller
public class SignupController {

	@Autowired
	private UserService userService;
	@Autowired
	private BranchService branchService;
	@Autowired
	private PositionService positionService;
	
	@RequestMapping(value = "/signup/", method = RequestMethod.GET)
	public String userInsert(Model model) {
		List<BranchDto> branches = branchService.getBranch();
	    List<PositionDto> positions = positionService.getPosition();
	    UserForm form = new UserForm();
	    model.addAttribute("userForm", form);
	    model.addAttribute("branches", branches);
	    model.addAttribute("positions", positions);
	    return "signup";
	}

	@RequestMapping(value = "/signup/", method = RequestMethod.POST)
	public String userInsert(@ModelAttribute UserForm form, Model model) throws Exception {
		UserDto user = UserDto.getInstance(form.getLoginId(), form.getPassword(), 
					form.getName(), form.getBranchId(), 
					form.getPositionId());
		List<String> errorMessages = new ArrayList<String>();
		if (isValid(user, errorMessages)){
			user.setPassword(CipherUtil.encrypt(user.getPassword()));
			userService.insertUser(user);
			return "redirect:/control/";
		} else {
			model.addAttribute("errorMessages",errorMessages);
			return "signup";
		}
	}
	
	private boolean isValid(UserDto user, List<String> messages) {
		List<UserDto> exsitUser = userService.getExsitUser(user);
		if (user.getLoginId().equals("")) {
			messages.add("ログインIDを入力してください");
		} else if (user.getLoginId().length() < 6 || user.getLoginId().length() > 20) {
			messages.add("ログインIDの文字数は6文字以上20文字以下で入力してください");
		} else if (!user.getLoginId().matches("[a-zA-Z0-9]{6,20}")) {
			messages.add("ログインIDは半角英数字で入力してください");
		}
		if (user.getPassword().equals("")) {
			messages.add("ログインIDを入力してください");
		} else if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
			messages.add("パスワードの文字数は6文字以上20文字以下で入力してください");
		} else if (!user.getPassword().matches("[a-zA-Z0-9]{6,255}")) {
			messages.add("パスワードは半角英数字で入力してください");
		}
		if (user.getName().equals("")) {
			messages.add("名前を入力してください");
		} else if (user.getName().length() > 10) {
			messages.add("名前は10文字以下で入力してください");
		}
		for (int i = 0, size = exsitUser.size(); i < size; i++) {
			if (user.getId() != exsitUser.get(i).getId()) {
				messages.add("既に" + user.getLoginId() + "は使用されています");
			}
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
