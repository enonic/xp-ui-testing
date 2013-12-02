package com.enonic.autotests.model.cm;

import com.enonic.autotests.model.cm.ShortcutContent.Builder;
import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypes;

public class StructuredContent extends BaseAbstractContent
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

		public Builder name(String name)
		{
			this.bName = name;
			return this;
		}
		public Builder displayName(String dname)
		{
			this.bDisplayName = dname;
			return this;
		}
		public Builder type(ContentTypes type)
		{
			this.bType = type;
			return this;
		}
		public Builder parents(String[] parents)
		{
			this.bParentNames = parents;
			return this;
		}
		   public StructuredContent build()
		   {
			   StructuredContent content = new StructuredContent();
			   content.setName( this.bName);
			   content.setDisplayName(this.bDisplayName);
			   content.setType(this.bType);
			   content.setParentNames(this.bParentNames);
			   return content;
		   }
	}
}
