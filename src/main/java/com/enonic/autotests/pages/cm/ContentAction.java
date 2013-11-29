package com.enonic.autotests.pages.cm;

public enum ContentAction
{
	NEW("New"), EDIT("Edit"),DELETE("Delete"),DUPLICATE("Duplicate"),MOVE("Move"),EXPORT("Export");
	private String value;

	public String getValue()
	{
		return value;
	}

	private ContentAction( String value )
	{
		this.value = value;
	}
}
