package jp.co.bbs.controller;

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

@Controller
public class UserControlController {

	@Autowired
	private UserService userService;
	@Autowired
	private BranchService branchService;
	@Autowired
	private PositionService positionService;
	
	@RequestMapping(value = "/control/", method = RequestMethod.GET)
	public String stoppedUser(Model model){
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		List<UserDto> users = userService.getUsers();
		model.addAttribute("users", users);
		List<BranchDto> branches = branchService.getBranch();
	    List<PositionDto> positions = positionService.getPosition();
		model.addAttribute("branches", branches);
	    model.addAttribute("positions", positions);
	    return "control";
	}
	
	@RequestMapping(value = "/control/", method = RequestMethod.POST)
	public String stoppedUser(@ModelAttribute UserForm form, Model model) {
		userService.stoppedUser(form.getId(), form.isStopped());
		return "redirect:/control/";
	}
	
}
