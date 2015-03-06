package com.enonic.wem.uitest

import com.enonic.autotests.TestSession
import geb.spock.GebSpec
import spock.lang.Shared

class BaseGebSpec
    extends GebSpec
{
    @Shared
    Properties defaultProperties;

    @Shared
    TestSession session;

    @Override
    def cleanup()
    {
        if ( session != null )
        {
            session.setLoggedIn( false )
        }

        resetBrowser();
    }
    // run before the first feature method
    def setupSpec()
    {
        initializeBaseUrl();
    }

    protected initializeBaseUrl()
    {
        println " browser.baseUrl in GebConfig.groovy is : " + browser.baseUrl;
        if ( browser.baseUrl == null )
        {
            String baseUrl = System.getProperty( "geb.build.baseUrl" );
            if ( baseUrl == null )
            {

                loadProperties();
                println "   baseUrl was loaded from the properties file: " + defaultProperties.get( "base.url" );
            }
            else
            {
                println "baseUrl was loaded as system property " + baseUrl;
            }
        }
    }

    def setup()
    {
        println "browser.baseUrl in GebConfig.groovy is   " + browser.baseUrl;
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


    TestSession getTestSession()
    {
        if ( session == null )
        {
            session = new TestSession()
            session.setDriver( browser.driver )
            session.setIsRemote( false )
        }
        return session
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
