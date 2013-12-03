package com.enonic.autotests.model;

import java.util.List;

import com.enonic.autotests.model.cm.BaseAbstractContent;

public class Space extends BaseAbstractContent
{
	

	protected Space( Builder<?> builder )
	{
		super(builder);
	
	}
	

	public static abstract class Builder<T extends Space> extends BaseAbstractContent.Builder<T>
	{

		public abstract T build();
	}

	public static Builder<?> builder()
	{
		return new Builder<Space>()
		{
			@Override
			public Space build()
			{
				return new Space(this);
			}
		};
	}


	private String template;

	private List<BaseAbstractContent> content;

	public List<BaseAbstractContent> getContent()
	{
		return content;
	}

	public void setContent(List<BaseAbstractContent> content)
	{
		this.content = content;
	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}
}
