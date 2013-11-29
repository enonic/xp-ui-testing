package com.enonic.autotests.model.schemamanger;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;

/**
 * Model of ContentType.
 * 
 */
public class ContentType
{
	private String displayName;

	private String name;

	private String configData;

	private KindOfContentTypes kind;

	public void changeNameInConfigData(String newname)
	{
		int start = configData.indexOf("<name>");
		int end = configData.indexOf("</name>");
		configData = configData.substring(0, start + 6) + newname + configData.substring(end);
		this.name = newname;
	}

	public void changeDisplayNameInConfigData(String newdisplayname)
	{
		String tagStart = "<display-name>";
		String tagEnd = "</display-name>";
		int start = configData.indexOf(tagStart);
		int end = configData.indexOf(tagEnd);
		configData = configData.substring(0, start + tagStart.length()) + newdisplayname + configData.substring(end);
		this.displayName = newdisplayname;
	}

	public KindOfContentTypes getKind()
	{
		return kind;
	}

	public void setKind(KindOfContentTypes kind)
	{
		this.kind = kind;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
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
