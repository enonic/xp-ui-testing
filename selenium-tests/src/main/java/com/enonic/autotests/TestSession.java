package com.enonic.autotests;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;

import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.vo.usermanager.User;

public class TestSession
{

    public final static String WEBDRIVER = "webdriver";

    public final static String START_URL = "bas_url";

    public final static String BROWSER_NAME = CapabilityType.BROWSER_NAME;

    public final static String BROWSER_VERSION = "browser_version";

    public final static String PLATFORM = "platform";

    public final String CURRENT_USER = "current_user";

    public final String CURRENT_WINDOW_NAME = "current_window";

    private String currentWindowHandle;

    private Map<String, Object> session = new HashMap<String, Object>();

    private boolean loggedIn;

    private boolean inLiveEditFrame;

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public void setLoggedIn( boolean loggedIn )
    {
        this.loggedIn = loggedIn;
    }

    public Object get( String key )
    {
        return session.get( key );
    }

    public void put( String key, Object value )
    {
        session.put( key, value );
    }

    public void putAll( Map<String, ? extends Object> params )
    {
        session.putAll( params );
    }

    public String getBaseUrl()
    {
        return (String) session.get( TestSession.START_URL );
    }

    public void setBaseUrl( String url )
    {
        session.put( TestSession.START_URL, url );
    }

    public String getPlatform()
    {
        return (String) session.get( TestSession.PLATFORM );
    }

    public String getBrowserVersion()
    {
        return (String) session.get( TestSession.BROWSER_VERSION );
    }

    public WebDriver getDriver()
    {
        return (WebDriver) session.get( TestSession.WEBDRIVER );
    }

    public void closeBrowser()
    {
        WebDriver driver = (WebDriver) session.get( TestSession.WEBDRIVER );
        driver.quit();
        loggedIn = false;

    }

    public Object setDriver( WebDriver driver )
    {
        return session.put( TestSession.WEBDRIVER, driver );
    }

    public void setUser( User user )
    {
        session.put( CURRENT_USER, user );
    }

    public User getCurrentUser()
    {
        return (User) session.get( CURRENT_USER );
    }

    public String getCurrentTabHandle()
    {
        return currentWindowHandle;
    }

    public void setCurrentTabHandle( String windowHandle )
    {
        this.currentWindowHandle = windowHandle;
    }

    public String getHandleForContentBrowseTab()
    {
        String contentBrowseTabHandle = (String) get( Application.CONTENT_STUDIO_TAB_HANDLE );
        if ( contentBrowseTabHandle == null )
        {
            throw new TestFrameworkException( "Handle for content browse panel was not set" );
        }
        return contentBrowseTabHandle;
    }

    public boolean isInLiveEditFrame()
    {
        return inLiveEditFrame;
    }

    public void setInLiveEditFrame( boolean value )
    {
        this.inLiveEditFrame = value;
    }

}
