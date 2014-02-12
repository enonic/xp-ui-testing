package com.enonic.wem.uitest
import org.openqa.selenium.firefox.FirefoxDriver;

import spock.lang.Shared;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;

import geb.report.ReporterSupport;
import geb.spock.GebSpec;

class BaseGebSpec extends  GebSpec 
{
	@Shared TestSession session ;
	@Shared ContentService contentService = new ContentService();
	
	@Override
	def cleanup()
	{
		if(session!=null) {
			session.setLoggedIn(false)
		}

		resetBrowser();
	}
	
	TestSession getTestSession() 
	{
		println "    geTestSesion called!"
		if(session == null) {
			println "creating new test session"
			session =  new TestSession()
			session.setDriver(browser.driver)
			session.setIsRemote(false)
			println "testSession is" + session.getBaseUrl();
		}
		return session
	}

	BaseAbstractContent addContentToBeDeleted()
	{
		String name = "deletecontent" + +Math.abs( new Random().nextInt() )
		BaseAbstractContent content = FolderContent.builder().withName(name).withDisplayName("contenttodelete").build();
		contentService.addContent(getTestSession(), content, true)
		return content;
	}
	void setSessionBaseUrl(String navigationPath) 
	{
		StringBuilder sb = new StringBuilder()
		sb.append(browser.baseUrl).append(navigationPath)
		println "  buildUrl changed  now url is:  " + sb.toString()
		getTestSession().setBaseUrl(sb.toString())
	}
}