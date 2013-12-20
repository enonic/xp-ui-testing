package com.enonic.autotests.pages.contentmanager.browsepanel;

public enum ActionMenuItems
{
	NEW("New"), EDIT("Edit"),OPEN("Open"),DELETE("Delete"),DUPLICATE("Duplicate"),MOVE("Move");
	private String value;

	public String getValue()
	{
		return value;
	}

	private ActionMenuItems( String value )
	{
		this.value = value;
	}
}
