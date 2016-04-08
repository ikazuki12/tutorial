package jp.co.bbs.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.bbs.dto.MessageDto;
import jp.co.bbs.dto.UserDto;
import jp.co.bbs.form.MessageForm;
import jp.co.bbs.service.CommentService;
import jp.co.bbs.service.MessageService;
import jp.co.bbs.service.UserService;

@Controller
public class MessageDeleteController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	
	@RequestMapping(value = "/message/delete")
	public String dateteMessage(Model model, @ModelAttribute MessageForm form, HttpSession session) {
		UserDto loginUser = (UserDto) session.getAttribute("loginUser");
		UserDto user = userService.getSelectUser(form.getUserId());
		MessageDto message = MessageDto.getInstance(form.getUserId(), null, null, null, null);
		message.setId(form.getId());
		if (loginUser.getPositionId() == 2) {
			messageService.deleteMessage(message);
			commentService.deleteMessageComment(message.getId());
		} else if (loginUser.getPositionId() == 3) {
			if (loginUser.getBranchId() == user.getBranchId() && user.getPositionId() == 4) {
				messageService.deleteMessage(message);
				commentService.deleteMessageComment(message.getId());
			}
		}
		
		return "redirect:/";
	}
}
