package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;


public class FolderContent extends BaseAbstractContent
{

	protected FolderContent( Builder<?> builder )
	{
		super(builder);
		setContentTypeName(ContentTypeName.FOLDER.getValue());
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
