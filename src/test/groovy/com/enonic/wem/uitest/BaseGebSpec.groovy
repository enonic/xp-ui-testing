package com.enonic.wem.uitest
import spock.lang.Shared;

import com.enonic.autotests.TestSession;

import geb.spock.GebSpec;

class BaseGebSpec extends GebSpec
{
	@Shared TestSession session ;
	
	TestSession getTestSession()
	{
		println "         geTestSesion called!"
		if(session == null)
		{
			
			println "creating new test session"
			session =  new TestSession();
			session.setDriver(browser.driver)
			session.setBaseUrl(browser.baseUrl)
			println "testSession is" + session.getBaseUrl();
		}
	
		return session
	}
	
	void setSessionBaseUrl(String navigationPath)
	{
		StringBuilder sb = new StringBuilder()
		
		sb.append(browser.baseUrl).append(navigationPath)
		println "     buildUrl changed  now url is:  " + sb.toString()
		getTestSession().setBaseUrl(sb.toString())
	}
}