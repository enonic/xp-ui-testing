package com.enonic.autotests.model;

public class UserInfo
{
	private String name;
	private String displayName;
	private String password;
	private String repeatPassword;
	private String email;
	private String country;
	private String locale;
	private String timezone;
	private String globalPosition;

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public String getTimezone()
	{
		return timezone;
	}

	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}

	public String getGlobalPosition()
	{
		return globalPosition;
	}

	public void setGlobalPosition(String globalPosition)
	{
		this.globalPosition = globalPosition;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getRepeatPassword()
	{
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword)
	{
		this.repeatPassword = repeatPassword;
	}

	public static class Builder
	{

		private String name;

		private String displayName;

		private String password;

		private String email;

		private String repeatPassword;
		private String country;
		private String locale;
		private String timezone;
		private String globalPosition;

		public Builder()
		{
		}

		public Builder( String name, String dispalayName, String password, String repeatPassword, String email, String country, String locale,
				String globalPosition, String timezone )
		{
			super();
			this.name = name;
			this.displayName = dispalayName;
			this.email = email;
			this.password = password;
			this.repeatPassword = repeatPassword;
			this.globalPosition = globalPosition;
			this.locale = locale;
			this.country = country;
			this.timezone = timezone;

		}

		public Builder name(String name)
		{
			this.name = name;
			return this;
		}

		public Builder password(String password)
		{
			this.password = password;
			return this;
		}

		public Builder displayName(String displayName)
		{
			this.displayName = displayName;
			return this;
		}

		public Builder email(String email)
		{
			this.email = email;
			return this;
		}

		public Builder repatPassword(String repeatPassword)
		{
			this.repeatPassword = repeatPassword;
			return this;
		}

		public Builder country(String country)
		{
			this.country = country;
			return this;
		}

		public UserInfo build()
		{
			UserInfo userinfo = new UserInfo();
			userinfo.name = name;
			userinfo.displayName = displayName;
			userinfo.email = email;
			userinfo.repeatPassword = repeatPassword;
			userinfo.password = password;
			userinfo.country = country;
			userinfo.timezone = timezone;
			userinfo.locale = locale;
			userinfo.globalPosition = globalPosition;
			return userinfo;
		}
	}

	public static Builder with()
	{
		return new Builder();
	}

	public static Builder clone(UserInfo toClone)
	{
		return new Builder(toClone.getName(), toClone.getDisplayName(), toClone.getPassword(), toClone.getRepeatPassword(), toClone.getEmail(),
				toClone.getCountry(), toClone.getLocale(), toClone.getGlobalPosition(), toClone.getTimezone());

	}

}
