package com.enonic.autotests.pages.cm;

public enum LastModified
{

	HOUR("1 hour"), DAY("1 day"), WEEK("1 week");
	private String value;

	public String getValue()
	{
		return value;
	}

	private LastModified( String value )
	{
		this.value = value;
	}
}
