package com.enonic.autotests.model.cm;


public class FolderContent extends BaseAbstractContent
{

	protected FolderContent( Builder<?> builder )
	{
		super(builder);
		
	}

	public static abstract class Builder<T extends FolderContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<FolderContent>()
		{
			@Override
			public FolderContent build()
			{
				return new FolderContent(this);
			}
		};
	}
}
