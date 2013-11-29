package com.enonic.autotests.testdata.cm;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "adressList" }, name = "adressList")
public class MixinXml extends AbstractContentXml
{
	private List<AdressXml> adressList;

	@XmlElementWrapper(name = "adressList")
	@XmlElement(name = "adress")
	public List<AdressXml> getAdressList()
	{
		return adressList;
	}

	public void setAdressList(List<AdressXml> adressList)
	{
		this.adressList = adressList;
	}

	
	

}
