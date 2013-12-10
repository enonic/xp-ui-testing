package com.enonic.autotests.testdata.cm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "contentList" }, name = "testdata")
@XmlRootElement
public class ContentTestData
{
	private List<AbstractContentXml> contentList = new ArrayList<>();

	@XmlElements({ @XmlElement(name = "mixin", type = MixinXml.class), @XmlElement(name = "page", type = PageXml.class),
	               @XmlElement(name = "folder", type = FolderXml.class) , @XmlElement(name = "space", type = SpaceXml.class) ,
	               @XmlElement(name = "shortcut", type = ShortcutXml.class) , @XmlElement(name = "unstructured", type = UnstructuredXml.class),
	               @XmlElement(name = "structured", type = StructuredXml.class) , @XmlElement(name = "media", type = MediaXml.class),
	               @XmlElement(name = "citation", type = CitationXml.class),@XmlElement(name = "article", type = ArticleXml.class),
	               @XmlElement(name = "archive", type = ArchiveXml.class),@XmlElement(name = "text", type = TextXml.class),
	               @XmlElement(name = "data", type = DataXml.class)
	             })
	public List<AbstractContentXml> getContentList()
	{
		return contentList;
	}

	public void setContentList(List<AbstractContentXml> contentList)
	{
		this.contentList = contentList;
	}

}
