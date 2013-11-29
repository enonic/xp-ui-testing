package com.enonic.autotests.testdata.accounts;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "systemUser")
@XmlType(propOrder = { "name", "displayName", "password","email","country","locale","timezone" ,"globalPosition","repeatPassword"}, name = "userInfo")
public class UserInfoXml {

	private String repeatPassword;
	
	private String name;
	private String displayName;
	private String password;
	private String email;
	private String country;
	private String locale;
	private String timezone;
	private String globalPosition;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getGlobalPosition() {
		return globalPosition;
	}
	public void setGlobalPosition(String globalPosition) {
		this.globalPosition = globalPosition;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}
