package com.enonic.autotests.testdata.cm;

/**
 * Base class for all representation of Content.
 *
 */
public class AbstractContentXml
{
	private String caseInfo;
	

	private String contentType;
	
	private String name;
	
	private String displayName;
	
	//---- Getters and Setters:----------------------------

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getCaseInfo()
	{
		return caseInfo;
	}

	public void setCaseInfo(String caseInfo)
	{
		this.caseInfo = caseInfo;
	}
	
}
