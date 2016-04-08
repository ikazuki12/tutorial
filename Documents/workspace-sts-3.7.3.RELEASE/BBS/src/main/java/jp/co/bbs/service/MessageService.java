package jp.co.bbs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.bbs.dto.MessageDto;
import jp.co.bbs.mapper.MessageMapper;

@Service
public class MessageService {

	@Autowired
	private MessageMapper messageMapper;
	
	public List<MessageDto> getMessages() {
		List<MessageDto> messageList = messageMapper.getMessage();
		return messageList;
	}
	
	public void messageInsert(MessageDto message) {
		messageMapper.messageInsert(message);
	}
	
	public List<MessageDto> getSelectMessage(String category, String startDate, String endDate){
		List<MessageDto> ret = new ArrayList<MessageDto>();
		if (category == null) {
			if (startDate == null && endDate == null) {
				ret = messageMapper.getMessage();
			} else if (startDate.equals("0000-00-00")) {
				if (!endDate.equals("0000-00-00")) {
					ret = messageMapper.getSelectMessageEndDateNullCategory(endDate);
				}
			} else {
				if (!endDate.equals("0000-00-00")) {
					ret = messageMapper.getSelectMessagePeriodNullCategory(startDate, endDate);
				} else {
					ret = messageMapper.getSelectMessageNullCategory(startDate);					
				}
			}
		} else {
			if (startDate == null && endDate == null) {
				ret = messageMapper.getSelectMessageCategory(category);
			} else if (startDate.equals("0000-00-00")) {
				if (!endDate.equals("0000-00-00")) {
					ret = messageMapper.getSelectMessagePeriod(category, startDate, endDate);
				}
			} else if (!startDate.equals("0000-00-00")) {
				if (endDate.equals("0000-00-00")) {
					ret = messageMapper.getSelectMessage(category, startDate);
				} else {
					ret = messageMapper.getSelectMessagePeriod(category, startDate, endDate);
				}
			}
		}
		return ret;
	}
	
	public void deleteMessage(MessageDto message) {
		messageMapper.deleteMessage(message);
	}
}
