package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;


public class MediaContent extends BaseAbstractContent
{

	protected MediaContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.MEDIA.getValue());
		
	}
	
	public static abstract class Builder<T extends MediaContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<MediaContent>()
		{
			@Override
			public MediaContent build()
			{
				return new MediaContent(this);
			}
		};
	}

}
