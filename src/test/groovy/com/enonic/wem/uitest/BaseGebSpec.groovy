package com.enonic.wem.uitest

import com.enonic.autotests.TestSession
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.ContentTypeService
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.ArchiveContent
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.api.content.ContentPath
import geb.spock.GebSpec
import spock.lang.Shared

class BaseGebSpec
    extends GebSpec
{
    @Shared
    Properties defaultProperties;

    @Shared
    TestSession session;

    @Shared
    ContentTypeService contentTypeService = new ContentTypeService();

    @Override
    def cleanup()
    {
        if ( session != null )
        {
            session.setLoggedIn( false )
        }

        resetBrowser();
    }

    def setupSpec()
    {
        String baseUrl = System.getProperty( "geb.build.baseUrl" );
        println baseUrl
        if ( baseUrl == null )
        {
            loadProperties();
        }

    }

    def setup()
    {
        String baseUrl = System.getProperty( "geb.build.baseUrl" );
        if ( baseUrl == null )
        {
            browser.baseUrl = defaultProperties.get( "base.url" )
        }
    }


    TestSession getTestSession()
    {
        println "    geTestSesion called!"
        if ( session == null )
        {
            println "creating new test session"
            session = new TestSession()
            session.setDriver( browser.driver )
            session.setIsRemote( false )
            println "testSession is" + session.getBaseUrl();
            println browser.baseUrl
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

    BaseAbstractContent addRootContentToBeDeleted()
    {
        String name = NameHelper.unqiueName( "deletecontent" )
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "contenttodelete" ).
            withParent( ContentPath.ROOT ).build();

        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        addContent( contentBrowsePanel, content, true )
        return content;
    }

    BaseAbstractContent addArchiveToParent( String parentName )
    {
        String name = NameHelper.unqiueName( "archive" )
        BaseAbstractContent content = ArchiveContent.builder().
            withName( name ).
            withDisplayName( "archive" ).
            withParent( ContentPath.from( parentName ) ).build();

        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        addContent( contentBrowsePanel, content, true )
        return content;
    }

    void addContent( ContentBrowsePanel contentBrowsePanel, BaseAbstractContent content, boolean isClose )
    {
        if ( !content.getPath().isRoot() )
        {
            contentBrowsePanel.selectParentForContent( content.getPath().getParentPath() );
        }
        ContentWizardPanel wizard = contentBrowsePanel.openNewContentDialog().selectContentType( content.getContentTypeName() )
        wizard.typeData( content ).save();
        if ( isClose )
        {
            wizard.close();
            contentBrowsePanel.waituntilPageLoaded( 1 )
        }

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
