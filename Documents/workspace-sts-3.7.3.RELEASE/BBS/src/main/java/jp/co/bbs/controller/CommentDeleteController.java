package jp.co.bbs.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.bbs.dto.CommentDto;
import jp.co.bbs.dto.UserDto;
import jp.co.bbs.form.CommentForm;
import jp.co.bbs.service.CommentService;
import jp.co.bbs.service.UserService;

@Controller
public class CommentDeleteController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/comment/delete/", method = RequestMethod.POST)
	public String deleteComment(@ModelAttribute CommentForm form, Model model, HttpSession session) {
		UserDto loginUser = (UserDto) session.getAttribute("loginUser");
		UserDto user = userService.getSelectUser(form.getUserId());
		CommentDto comment = CommentDto.getInstance(form.getUserId(), form.getMessageId(), null, null);
		comment.setId(form.getId());
		if (loginUser.getPositionId() == 2) {
			commentService.deleteComment(comment.getId());
		} else if (loginUser.getPositionId() == 3) {
			if (loginUser.getBranchId() == user.getBranchId() && user.getPositionId() == 4) {
				commentService.deleteComment(comment.getId());
			}
		}
		
		return "redirect:/";
		
	}
}
