package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;


public class MixinContent extends BaseAbstractContent
{
	protected MixinContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.MIXIN.getValue());
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
