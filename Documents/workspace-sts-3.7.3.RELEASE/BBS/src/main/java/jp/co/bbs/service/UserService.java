package jp.co.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.bbs.dto.UserDto;
import jp.co.bbs.mapper.UserMapper;
import jp.co.bbs.utils.CipherUtil;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public void insertUser(UserDto user) {
		userMapper.insertUser(user);
	}
	
	public UserDto getUser(UserDto user) {
		UserDto userDto = userMapper.getUser(user);
		return userDto;
	}
	
	public List<UserDto> getUsers() {
		List<UserDto> userList = userMapper.getUsers();
		return userList;
	}
	
	public void stoppedUser(int id, boolean stopped) {
		if (stopped) {
			userMapper.stoppedUser(id);
		} else {
			userMapper.stopUser(id);
		}
	}
	
	public UserDto getSelectUser(int id) {
		UserDto ret = userMapper.getSelectUser(id);
		return ret;
	}
	
	public void updeteUser(UserDto user) {
		if (user.getPassword().equals("")) {
			userMapper.updeteUserNullPassword(user);
		} else {
			user.setPassword(CipherUtil.encrypt(user.getPassword()));
			userMapper.updeteUser(user);
		}
	}
	
	public List<UserDto> getExsitUser(UserDto user) {
		List<UserDto> ret = userMapper.getExsitUser(user);
		return ret;
	}
	
}
