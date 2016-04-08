package jp.co.bbs.mapper;

import java.util.List;

import jp.co.bbs.dto.UserDto;

public interface UserMapper {

	void insertUser(UserDto user);
	UserDto getUser(UserDto userDto);
	List<UserDto> getUsers();
	void stoppedUser(int id);
	void stopUser(int id);
	UserDto getSelectUser(int id);
	void updeteUser(UserDto user);
	void updeteUserNullPassword(UserDto user);
	List<UserDto> getExsitUser(UserDto user);
//	insertUser(String loginId, String password, String name, int branchId, int positionId);
}
