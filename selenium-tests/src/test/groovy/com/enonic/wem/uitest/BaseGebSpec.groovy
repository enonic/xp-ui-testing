package com.enonic.wem.uitest

import com.enonic.autotests.TestSession
import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.utils.TestUtils
import geb.spock.GebSpec
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import spock.lang.Shared

class BaseGebSpec
    extends GebSpec
{
    @Shared
    Properties defaultProperties;

    @Shared
    String APP_CONTENT_TYPES_DISPLAY_NAME = "All Content Types App";

    @Shared
    String ALL_CONTENT_TYPES_APP_NAME = "com.enonic.xp.testing.contenttypes";

    @Shared
    TestSession session;

    @Override
    def cleanup()
    {
        if ( session != null )
        {
            session.setLoggedIn( false )
        }

        if ( session.get( HomePage.APP_TAB_HANDLE ) != null )
        {
            closeAllTabs( session.get( HomePage.HOME_PAGE_TAB_HANDLE ) );
        }
        resetBrowser();
    }
    // run before the first feature method
    def setupSpec()
    {
        initializeBaseUrl();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userAgent = (String) js.executeScript( "return navigator.userAgent;" );
        println "user agent is : " + userAgent;
    }

    def closeAllTabs( String homeHandle )
    {
        for ( String handle : driver.getWindowHandles() )
        {
            if ( !handle.equals( homeHandle ) )
            {
                getDriver().switchTo().window( handle );
                getDriver().close();
            }
        }
        session.put( HomePage.APP_TAB_HANDLE, null );
        getDriver().switchTo().window( homeHandle );
    }

    protected initializeBaseUrl()
    {
        if ( browser.baseUrl == null )
        {
            String baseUrl = System.getProperty( "geb.build.baseUrl" );
            if ( baseUrl == null )
            {

                loadProperties();
                println "   baseUrl was loaded from the properties file: " + defaultProperties.get( "base.url" );
                browser.driver.manage().window().setSize( new Dimension( 1700, 1100 ) )
            }
            else
            {
                println "baseUrl was loaded as system property " + baseUrl;
                browser.driver.manage().window().setSize( new Dimension( 1700, 1100 ) );
            }
        }
        browser.driver.manage().window().setSize( new Dimension( 1700, 1100 ) )
    }

    def setup()
    {
        if ( browser.baseUrl == null )
        {
            String baseUrl = System.getProperty( "geb.build.baseUrl" );

            if ( baseUrl == null )
            {
                browser.baseUrl = defaultProperties.get( "base.url" );
                println " baseUrl from defaultProperties  " + browser.baseUrl;
            }
        }

    }

    void saveScreenshot( String name )
    {
        TestUtils.saveScreenshot( getSession(), name );
    }

    TestSession getTestSession()
    {
        if ( session == null )
        {
            session = new TestSession();
            session.setDriver( browser.driver );
        }
        return session;
    }

    void setSessionBaseUrl( String navigationPath )
    {
        StringBuilder sb = new StringBuilder()
        sb.append( browser.baseUrl ).append( navigationPath )
        println "  buildUrl changed  now url is:  " + sb.toString()
        getTestSession().setBaseUrl( sb.toString() )
    }

    void loadProperties()
    {
        defaultProperties = new Properties()
        InputStream input = null

        try
        {
            input = new FileInputStream( "tests.properties" )
            // load a properties file
            defaultProperties.load( input );
            println defaultProperties.getProperty( "base.url" )

        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close()
                }
                catch ( IOException e )
                {
                    e.printStackTrace()
                }
            }
        }
    }
}
