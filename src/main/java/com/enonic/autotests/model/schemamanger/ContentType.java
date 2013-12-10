package com.enonic.autotests.model.schemamanger;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;

/**
 * Model of ContentType.
 * 
 */
public class ContentType
{

	private String name;
	
	private String configData;

	private KindOfContentTypes kind;

	public void setDisplayNameInConfig(String newDisplayName)
	{
		String tagStart = "<display-name>";
		String tagEnd = "</display-name>";
		int start = configData.indexOf(tagStart);
		int end = configData.indexOf(tagEnd);
		configData = configData.substring(0, start + tagStart.length()) + newDisplayName + configData.substring(end);
	}

	public String getDisplayNameFromConfig()
	{
		String tagStart = "<display-name>";
		String tagEnd = "</display-name>";
		int start = configData.indexOf(tagStart);
		int end = configData.indexOf(tagEnd);
		return configData.substring(start + tagStart.length() , end);
	}
	
	public String getSuperTypeNameFromConfig()
	{
		String tagStart = "<super-type>";
		String tagEnd = "</super-type>";
		int start = configData.indexOf(tagStart);
		int end = configData.indexOf(tagEnd);
		return configData.substring(start + tagStart.length() , end);
		
	}
	public KindOfContentTypes getKind()
	{
		return kind;
	}

	public void setKind(KindOfContentTypes kind)
	{
		this.kind = kind;
	}

	

	public String getConfigData()
	{
		return configData;
	}

	public void setConfigData(String configData)
	{
		this.configData = configData;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
