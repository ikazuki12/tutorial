package jp.co.bbs.dto;

import java.util.Date;

public class CommentDto {

	private int id;
	private int userId;
	private int messageId;
	private String text;
	private Date insertDate;
	public static CommentDto getInstance(Integer userId, int messageId, String text, Date date) {
		CommentDto ret = new CommentDto();
		ret.setUserId(userId);
		ret.setMessageId(messageId);
		ret.setText(text);
		ret.setInsertDate(date);
		
		return ret;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
}
