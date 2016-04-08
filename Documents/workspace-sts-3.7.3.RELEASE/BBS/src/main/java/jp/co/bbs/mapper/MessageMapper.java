package jp.co.bbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.co.bbs.dto.MessageDto;

public interface MessageMapper {

	List<MessageDto> getMessage();
	void messageInsert(MessageDto message);
	List<MessageDto> getSelectMessage(
			@Param("category") String category, 
			@Param("startDate") String startDate
			);
	List<MessageDto> getSelectMessagePeriod(
			@Param("category") String category, 
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate);
	List<MessageDto> getSelectMessageCategory(String category);
	List<MessageDto> getSelectMessageNullCategory(String startDate);
	List<MessageDto> getSelectMessagePeriodNullCategory(
			@Param("startDate") String startDate,
			@Param("endDate") String endDate
			);
	List<MessageDto> getSelectMessageEndDateNullCategory(String endDate);
	void deleteMessage(MessageDto message);
}
