package com.enonic.autotests.testdata.cm;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "title","preface","body" }, name = "article")
public class ArticleXml extends AbstractContentXml
{
	private String title;
	
	private String preface;
	
	private String body;
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getPreface()
	{
		return preface;
	}
	public void setPreface(String preface)
	{
		this.preface = preface;
	}
	public String getBody()
	{
		return body;
	}
	public void setBody(String body)
	{
		this.body = body;
	}
}
