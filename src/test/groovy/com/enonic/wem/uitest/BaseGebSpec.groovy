package com.enonic.wem.uitest
import org.openqa.selenium.firefox.FirefoxDriver;

import spock.lang.Shared;

import com.enonic.autotests.TestSession;

import geb.spock.GebSpec;

class BaseGebSpec extends GebSpec
{
	@Shared TestSession session ;
	@Shared def cachedDriver
	
	def setupSpec(){
		cachedDriver = new FirefoxDriver(  )
	  }
	   
	  def setup(){
		// assign this as the default driver on the browser for each test
		browser.driver = cachedDriver
	  }
	   
	  def cleanupSpec(){
		// after running the spec, kill the driver
		cachedDriver.quit()
	  }
	TestSession getTestSession()
	{
		println "    geTestSesion called!"
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
		println "  buildUrl changed  now url is:  " + sb.toString()
		getTestSession().setBaseUrl(sb.toString())
	}
	
	
}