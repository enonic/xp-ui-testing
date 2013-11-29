package com.enonic.autotests.testdata.accounts;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "systemUsers" }, name = "testdata")
@XmlRootElement
public class SystemUsersTestData {

	private List<SystemUserXml> systemUsers = new ArrayList<SystemUserXml>();

	@XmlElementWrapper(name = "systemUsers")
	@XmlElement(name = "systemUser")
	public List<SystemUserXml> getSystemUsers() {
		return systemUsers;
	}

	public void setSystemUsers(List<SystemUserXml> systemUsers) {
		this.systemUsers = systemUsers;
	}

}