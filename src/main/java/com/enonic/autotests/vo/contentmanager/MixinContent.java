package com.enonic.autotests.vo.contentmanager;


public class MixinContent extends BaseAbstractContent
{
	protected MixinContent( Builder<?> builder )
	{
		super(builder);
		
	}

	public static abstract class Builder<T extends MixinContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<MixinContent>()
		{
			@Override
			public MixinContent build()
			{
				return new MixinContent(this);
			}
		};
	}

	
}
