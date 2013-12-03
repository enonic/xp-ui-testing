package com.enonic.autotests.model.cm;


public class UnstructuredContent extends BaseAbstractContent
{

	protected UnstructuredContent( Builder<?> builder )
	{
		super(builder);
	
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
