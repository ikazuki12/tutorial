package jp.co.bbs.entity;

public class User {

	private Integer id;
	private String loginId;
	private String password;
	private String name;
	private int branchId;
	private int positionId;
	private boolean stopped;
	
	public static User getInstance(
			String loginId, String password, String name, int branchId, int positionId) {
		User ret = new User();
		ret.setLoginId(loginId);
		ret.setPassword(password);
		ret.setName(name);
		ret.setBranchId(branchId);
		ret.setPositionId(positionId);
		return ret;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public boolean isStopped() {
		return stopped;
	}
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
