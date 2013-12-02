package com.enonic.autotests.model.cm;

import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypes;

public class ShortcutContent extends BaseAbstractContent
{

	public static Builder with()
	{
		return new Builder();
	}
	public static class Builder
	{
		private String bName;
		private String bDisplayName;
		private ContentTypes bType;
		private String[] bParentNames;

		   public ShortcutContent build()
		   {
			   ShortcutContent content = new ShortcutContent();
			   content.setName( this.bName);
			   content.setDisplayName(this.bDisplayName);
			   content.setType(this.bType);
			   content.setParentNames(this.bParentNames);
			   return content;
		   }
	}

}
