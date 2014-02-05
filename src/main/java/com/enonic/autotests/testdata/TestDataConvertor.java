package com.enonic.autotests.testdata;

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.testdata.accounts.SystemUserXml;
import com.enonic.autotests.testdata.accounts.UserInfoXml;
import com.enonic.autotests.testdata.cm.*;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.Space;
import com.enonic.autotests.vo.SystemUser;
import com.enonic.autotests.vo.UserInfo;
import com.enonic.autotests.vo.contentmanager.*;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent.Builder;
import com.enonic.autotests.vo.schemamanger.ContentType;

import java.util.ArrayList;
import java.util.List;


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
		ct.setName(xmlData.getName());
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

	/**
	 * Predefined names of content type used.
	 * @param xmlContent
	 * @return
	 */
	public static BaseAbstractContent convertXmlDataToContent(AbstractContentXml xmlContent)
	{
		
		Builder<?> builder = null;
		if (xmlContent instanceof PageXml)
		{
			builder = PageContent.builder();
			

		} else if (xmlContent instanceof SpaceXml)
		{
			builder = Space.builder();
		
		} else if (xmlContent instanceof MixinXml)
		{
			builder = MixinContent.builder();



		} else if (xmlContent instanceof UnstructuredXml)
		{
			builder =  UnstructuredContent.builder();
			

		} else if (xmlContent instanceof StructuredXml)
		{
			builder = StructuredContent.builder();
			

		} else if (xmlContent instanceof MediaXml)
		{
			builder = MediaContent.builder();
			

		} else if (xmlContent instanceof ShortcutXml)
		{
			builder = ShortcutContent.builder();
			

		} else if (xmlContent instanceof CitationXml)
		{
			builder = CitationContent.builder();
			

		} else if (xmlContent instanceof FolderXml)
		{
			builder= FolderContent.builder();
			

		}else if(xmlContent instanceof ArchiveXml)
		{
			builder = ArchiveContent.builder();
			
		}
		else if(xmlContent instanceof DataXml)
		{
			builder = DataContent.builder();
			
		}
		else if(xmlContent instanceof TextXml)
		{
			builder = TextContent.builder();
			
		}
		else
		{

			throw new UnsupportedOperationException("this content type  not supported " + xmlContent.getDisplayName());
		}
		builder.withName(xmlContent.getName());
		builder.withDisplayName(xmlContent.getDisplayName());
		ContentTypeName ctype = TestUtils.getInstance().getContentType(xmlContent.getContentType().toLowerCase());
		builder.withType(ctype.getValue());

		return builder.build();
	}
}
