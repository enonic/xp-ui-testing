package com.enonic.wem.uitest

import com.enonic.autotests.TestSession
import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.utils.TestUtils
import geb.spock.GebSpec
import org.openqa.selenium.Alert
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
    String ALL_CONTENT_TYPES_APP_NAME = "com.enonic.xp.ui_testing.contenttypes";

    @Shared
    TestSession session;

/**Run code after each test method*/
    @Override
    def cleanup()
    {
        if ( session != null )
        {
            session.setLoggedIn( false );
            session.setInLiveEditFrame( false );
        }
        if ( session.get( HomePage.HOME_PAGE_TAB_HANDLE ) != null )
        {
            closeAlerts();
            closeAllTabs( session.get( HomePage.HOME_PAGE_TAB_HANDLE ) );
        }

        resetBrowser();
    }
    // run before the first feature method
    def setupSpec()
    {
        initializeBaseUrl();
    }

    def closeAlerts()
    {
        try
        {
            Alert alert = getDriver().switchTo().alert();
            alert.dismiss();
        }
        catch ( Exception e )
        {
            println "no alerts";
        }
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
/**Run code before each test method*/
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
            println defaultProperties.getProperty( "base.url" );
            System.setProperty( "geb.build.baseUrl", defaultProperties.getProperty( "base.url" ) );

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
