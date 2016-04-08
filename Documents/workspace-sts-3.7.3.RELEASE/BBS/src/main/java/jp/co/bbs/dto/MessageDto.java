package jp.co.bbs.dto;

import java.util.Date;

public class MessageDto {

	private int id;
	private int userId;
	private String subject;
	private String text;
	private String category;
	private Date insertDate;
	
	public static MessageDto getInstance(
			int userId, String subject, String text, String category, Date date){
		
		MessageDto ret = new MessageDto();
		ret.setUserId(userId);
		ret.setSubject(subject);
		ret.setText(text);
		ret.setCategory(category);
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
}
