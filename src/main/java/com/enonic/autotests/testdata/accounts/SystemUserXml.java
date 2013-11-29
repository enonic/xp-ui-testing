package com.enonic.autotests.testdata.accounts;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "systemUsers")
@XmlType(propOrder = { "userInfo","caseInfo"}, name = "systemUser")
public class SystemUserXml {

	private String caseInfo;
	
	private UserInfoXml userInfo;

	public UserInfoXml getUserInfo() {
		return userInfo;
	}

	public String getCaseInfo() {
		return caseInfo;
	}

	public void setCaseInfo(String caseInfo) {
		this.caseInfo = caseInfo;
	}
	public void setUserInfo(UserInfoXml userInfo) {
		this.userInfo = userInfo;
	}
}
