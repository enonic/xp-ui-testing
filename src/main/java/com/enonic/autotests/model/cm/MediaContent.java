package com.enonic.autotests.model.cm;


public class MediaContent extends BaseAbstractContent
{

	protected MediaContent( Builder<?> builder )
	{
		super(builder);
		
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
