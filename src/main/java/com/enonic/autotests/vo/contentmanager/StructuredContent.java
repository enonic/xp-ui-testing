package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;

public class StructuredContent extends BaseAbstractContent
{
	
	public StructuredContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.STRUCTURED.getValue());
		

	}

	public static abstract class Builder<T extends StructuredContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<StructuredContent>()
		{
			@Override
			public StructuredContent build()
			{
				return new StructuredContent(this);
			}
		};
	}
}
