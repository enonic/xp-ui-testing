package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;


public class TextContent extends BaseAbstractContent
{

	protected TextContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.TEXT.getValue());

	}

	public static abstract class Builder<T extends TextContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<TextContent>()
		{
			@Override
			public TextContent build()
			{
				return new TextContent(this);
			}
		};
	}
}
