package com.enonic.autotests.testdata.schemamanger;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlType(propOrder = { "contentTypes" }, name = "testdata")
@XmlRootElement
public class ContentTypeTestData {

	private List<ContentTypeXml> contentTypes = new ArrayList<ContentTypeXml>();

	@XmlElementWrapper(name = "contentTypes")
	@XmlElement(name = "contentType")
	public List<ContentTypeXml> getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(List<ContentTypeXml> contentTypes) {
		this.contentTypes = contentTypes;
	}

}
