package com.enonic.autotests.vo;

public abstract class User
{
	private UserInfo userInfo;

	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}

}
