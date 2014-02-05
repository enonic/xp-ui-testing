package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;

public class CitationContent extends BaseAbstractContent
{

	protected CitationContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.CITATION.getValue());

	}

	public static abstract class Builder<T extends CitationContent> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<CitationContent>()
		{
			@Override
			public CitationContent build()
			{
				return new CitationContent(this);
			}
		};
	}
}
