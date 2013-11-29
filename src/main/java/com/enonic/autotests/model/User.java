package com.enonic.autotests.model;

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
