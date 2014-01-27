package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent.Builder;


public class UnstructuredContent extends BaseAbstractContent
{

	protected UnstructuredContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.UNSTRUCTURED.getValue());
	
	}
	
	public static abstract class Builder<T extends UnstructuredContent> extends BaseAbstractContent.Builder<T>
	{
		public abstract T build();

	}

	public static Builder<?> builder()
	{
		return new Builder<UnstructuredContent>()
		{
			@Override
			public UnstructuredContent build()
			{
				return new UnstructuredContent(this);
			}
		};
	}
}
