package com.enonic.autotests.vo;

public class SystemUser extends User
{

	public static Builder with()
	{
		return new Builder();
	}

	public static class Builder
	{
		private UserInfo userInfo;

		public Builder()
		{

		}

		public Builder( UserInfo userInfo )
		{
			this.userInfo = userInfo;
		}

		public Builder userInfo(UserInfo userInfo)
		{
			this.userInfo = userInfo;
			return this;
		}

		public SystemUser build()
		{
			SystemUser user = new SystemUser();
			user.setUserInfo(userInfo);
			return user;
		}

	}

}
