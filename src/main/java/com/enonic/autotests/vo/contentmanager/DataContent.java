package com.enonic.autotests.vo.contentmanager;


public class DataContent extends BaseAbstractContent
{

	protected DataContent( Builder<?> builder )
	{
		super(builder);
		
	}
	
	public static abstract class Builder<T extends DataContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<DataContent>()
		{
			@Override
			public DataContent build()
			{
				return new DataContent(this);
			}
		};
	}

}