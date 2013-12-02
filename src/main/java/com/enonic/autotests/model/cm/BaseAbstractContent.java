package com.enonic.autotests.model.cm;

import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypes;


/**
 * Base class for all types of content.
 * 
 * 
 */
public abstract class BaseAbstractContent
{
	private String name;
	private String displayName;
	private ContentTypes type;
	private String[] parentNames;

	public String[] getParentNames()
	{
		return parentNames;
	}

	public void setParentNames(String[] parentNames)
	{
		this.parentNames = parentNames;
	}

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

	public ContentTypes getType()
	{
		return type;
	}

	public void setType(ContentTypes type)
	{
		this.type = type;
	}
	
	
}
