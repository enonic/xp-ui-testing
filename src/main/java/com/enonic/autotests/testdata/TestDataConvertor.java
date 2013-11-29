package com.enonic.autotests.testdata;

import java.util.ArrayList;
import java.util.List;

import com.enonic.autotests.model.Space;
import com.enonic.autotests.model.SystemUser;
import com.enonic.autotests.model.UserInfo;
import com.enonic.autotests.model.cm.Address;
import com.enonic.autotests.model.cm.ArchiveContent;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.model.cm.CitationContent;
import com.enonic.autotests.model.cm.FolderContent;
import com.enonic.autotests.model.cm.MediaContent;
import com.enonic.autotests.model.cm.MixinContent;
import com.enonic.autotests.model.cm.PageContent;
import com.enonic.autotests.model.cm.ShortcutContent;
import com.enonic.autotests.model.cm.StructuredContent;
import com.enonic.autotests.model.cm.UnstructuredContent;
import com.enonic.autotests.model.schemamanger.ContentType;
import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypes;
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.testdata.accounts.SystemUserXml;
import com.enonic.autotests.testdata.accounts.UserInfoXml;
import com.enonic.autotests.testdata.cm.AbstractContentXml;
import com.enonic.autotests.testdata.cm.AdressXml;
import com.enonic.autotests.testdata.cm.ArchiveXml;
import com.enonic.autotests.testdata.cm.CitationXml;
import com.enonic.autotests.testdata.cm.FolderXml;
import com.enonic.autotests.testdata.cm.MediaXml;
import com.enonic.autotests.testdata.cm.MixinXml;
import com.enonic.autotests.testdata.cm.PageXml;
import com.enonic.autotests.testdata.cm.ShortcutXml;
import com.enonic.autotests.testdata.cm.SpaceXml;
import com.enonic.autotests.testdata.cm.StructuredXml;
import com.enonic.autotests.testdata.cm.UnstructuredXml;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;

public class TestDataConvertor
{

	public static SystemUser convertXmlDataToSystemUser(SystemUserXml xmlData)
	{
		SystemUser sysUser = new SystemUser();
		UserInfoXml uinfoXml = xmlData.getUserInfo();
		UserInfo userInfo = convertXmlDataToUserInfo(uinfoXml);
		sysUser.setUserInfo(userInfo);
		return sysUser;
	}

	public static UserInfo convertXmlDataToUserInfo(UserInfoXml xmlData)
	{
		UserInfo uinfo = new UserInfo();
		uinfo.setCountry(xmlData.getCountry());
		uinfo.setDisplayName(xmlData.getDisplayName());
		uinfo.setName(xmlData.getName());
		uinfo.setEmail(xmlData.getEmail());
		uinfo.setPassword(xmlData.getPassword());
		uinfo.setLocale(xmlData.getLocale());
		uinfo.setGlobalPosition(xmlData.getGlobalPosition());
		uinfo.setRepeatPassword(xmlData.getRepeatPassword());
		return uinfo;
	}

	public static Address convertXmlDataToAddress(AdressXml xmlAddress)
	{
		Address addr = new Address();
		addr.setStreet(xmlAddress.getStreet());
		addr.setPostalCode(xmlAddress.getPostalCode());
		addr.setPostalPlace(xmlAddress.getPostalCode());
		return addr;
	}
	public static List<Address> convertXmlDataToAddresList(List<AdressXml> xmlAddressList)
	{
		List<Address> list =  new ArrayList<>();
		for(AdressXml adr: xmlAddressList)
		{
			list.add(convertXmlDataToAddress(adr));
		}
				
		return list;
	}
	
	public static ContentType convertXmlDataToContentType(ContentTypeXml xmlData)
	{
		ContentType ct = new  ContentType();
		ct.setConfigData(xmlData.getConfig());
		ct.setDisplayName(xmlData.getDisplayName());
		KindOfContentTypes kind = getKind(xmlData.getKind());
		if(kind == null)
		{
			throw new IllegalArgumentException("wrong kind in the xmlData!");
		}
		ct.setKind(kind);
		ct.setName(xmlData.getName());
		return ct;
		
	}
	
	private static KindOfContentTypes getKind(String ctype)
	{
		KindOfContentTypes result = null;
		KindOfContentTypes[] values = KindOfContentTypes.values();
		for(KindOfContentTypes val: values )
		{
			if(val.getValue().equals(ctype))
			{
				result =  val;
				
			}
		}
		return result;
	}

	public static BaseAbstractContent convertXmlDataToContent(AbstractContentXml xmlContent)
	{
		BaseAbstractContent content = null;

		if (xmlContent instanceof PageXml)
		{
			PageContent page = new PageContent();
			content = page;

		} else if (xmlContent instanceof SpaceXml)
		{
			Space space = new Space();
			content = space;
		} else if (xmlContent instanceof MixinXml)
		{
			MixinContent mixin = new MixinContent();

			
			List<AdressXml> xmlAdrList = ((MixinXml) xmlContent).getAdressList();
			List<Address> addressList = convertXmlDataToAddresList(xmlAdrList);
			mixin.setAddressList(addressList);
			content = mixin;

		} else if (xmlContent instanceof UnstructuredXml)
		{
			UnstructuredContent unstr = new UnstructuredContent();
			content = unstr;

		} else if (xmlContent instanceof StructuredXml)
		{
			StructuredContent structured = new StructuredContent();
			content = structured;

		} else if (xmlContent instanceof MediaXml)
		{
			MediaContent media = new MediaContent();
			content = media;

		} else if (xmlContent instanceof ShortcutXml)
		{
			ShortcutContent shortcut = new ShortcutContent();
			content = shortcut;

		} else if (xmlContent instanceof CitationXml)
		{
			CitationContent citation = new CitationContent();
			content = citation;

		} else if (xmlContent instanceof FolderXml)
		{
			FolderContent folder = new FolderContent();
			content = folder;

		}else if(xmlContent instanceof ArchiveXml)
		{
			ArchiveContent archive = new ArchiveContent();
			content = archive;
		}
		
		else
		{

			throw new UnsupportedOperationException("this content type  not supported " + xmlContent.getDisplayName());
		}
		content.setName(xmlContent.getName());
		content.setDisplayName(xmlContent.getDisplayName());
		content.setType(xmlContent.getContentType());
		return content;
	}
}
