package com.enonic.autotests.model.cm;

import com.enonic.autotests.pages.Page;

/**
 * Base class for all types of content.
 * 
 * 
 */
public abstract class BaseAbstractContent
{

	private Page mainTab;



	private String name;
	private String displayName;
	
	private String type;

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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Page getMainTab()
	{
		return mainTab;
	}

	public void setMainTab(Page mainTab)
	{
		this.mainTab = mainTab;
	}
}
