package com.enonic.autotests.pages.schemamanager;

public enum KindOfContentTypes
{
	MIXIN("Mixin"), CONTENT_TYPE("Content Type"), RELATIONSHIP_TYPE("Relationship Type");
	private String value;

	public String getValue()
	{
		return value;
	}

	private KindOfContentTypes( String value )
	{
		this.value = value;
	}
}
