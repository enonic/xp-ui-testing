package com.enonic.autotests;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;

import com.enonic.autotests.vo.User;

public class TestSession
{

    public final static String WEBDRIVER = "webdriver";

    public final static String START_URL = "bas_url";

    public final static String HUB_URL = "hub_url";

    public final static String BROWSER_NAME = CapabilityType.BROWSER_NAME;

    public final static String BROWSER_VERSION = "browser_version";

    public final static String PLATFORM = "platform";

    public final static String IS_REMOTE = "isRemote";

    public final static String CURRENT_USER = "current_user";

    private String windowHandle;

    private Map<String, Object> session = new HashMap<String, Object>();

    private boolean loggedIn;

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

    public String getBrowserName()
    {
        return (String) session.get( TestSession.BROWSER_NAME );
    }

    public String getPlatform()
    {
        return (String) session.get( TestSession.PLATFORM );
    }

    public String getBrowserVersion()
    {
        return (String) session.get( TestSession.BROWSER_VERSION );
    }

    public Boolean getIsRemote()
    {
        return (Boolean) session.get( TestSession.IS_REMOTE );
    }

    public void setIsRemote( boolean isRemote )
    {
        session.put( TestSession.IS_REMOTE, isRemote );
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
        session.put( TestSession.CURRENT_USER, user );
    }

    public User getCurrentUser()
    {
        return (User) session.get( TestSession.CURRENT_USER );
    }

    public String getWindowHandle()
    {
        return windowHandle;
    }

    public void setWindowHandle( String windowHandle )
    {
        this.windowHandle = windowHandle;
    }

}
