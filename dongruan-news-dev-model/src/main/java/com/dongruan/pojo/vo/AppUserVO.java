package com.dongruan.pojo.vo;

public class AppUserVO {
	private String id;
	private String nickname;
	private String face;
	private Integer activeStatus;

	private Integer myFansCounts;       // 我的粉丝数
	private Integer myFollowCounts;     // 我关注的人


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getMyFansCounts() {
		return myFansCounts;
	}

	public void setMyFansCounts(Integer myFansCounts) {
		this.myFansCounts = myFansCounts;
	}

	public Integer getMyFollowCounts() {
		return myFollowCounts;
	}

	public void setMyFollowCounts(Integer myFollowCounts) {
		this.myFollowCounts = myFollowCounts;
	}

	@Override
	public String toString() {
		return "AppUserVO{" +
				"id='" + id + '\'' +
				", nickname='" + nickname + '\'' +
				", face='" + face + '\'' +
				", activeStatus=" + activeStatus +
				", myFansCounts=" + myFansCounts +
				", myFollowCounts=" + myFollowCounts +
				'}';
	}
}