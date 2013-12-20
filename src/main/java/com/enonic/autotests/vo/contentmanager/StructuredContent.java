package com.enonic.autotests.vo.contentmanager;

public class StructuredContent extends BaseAbstractContent
{
	
	public StructuredContent( Builder<?> builder )
	{
		super(builder);

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
