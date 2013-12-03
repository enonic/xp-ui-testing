package com.enonic.autotests.model.cm;



public class ShortcutContent extends BaseAbstractContent
{

	protected ShortcutContent( Builder<?> builder )
	{
		super(builder);
		
	}

	public static abstract class Builder<T extends ShortcutContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<ShortcutContent>()
		{
			@Override
			public ShortcutContent build()
			{
				return new ShortcutContent(this);
			}
		};
	}


}
