package com.enonic.wem.uitest

import com.enonic.autotests.TestSession
import com.enonic.autotests.services.ContentService
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import geb.spock.GebSpec
import spock.lang.Shared

class BaseGebSpec extends GebSpec
{
    @Shared TestSession session;
    @Shared ContentService contentService = new ContentService();

    @Override
    def cleanup( )
    {
        if ( session != null )
        {
            session.setLoggedIn( false )
        }

        resetBrowser();
    }

    TestSession getTestSession( )
    {
        println "    geTestSesion called!"
        if ( session == null )
        {
            println "creating new test session"
            session = new TestSession()
            session.setDriver( browser.driver )
            session.setIsRemote( false )
            println "testSession is" + session.getBaseUrl();
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

    BaseAbstractContent addContentToBeDeleted( )
    {
        String name = NameHelper.unqiueContentName( "deletecontent" );
        BaseAbstractContent content = FolderContent.builder().withName( name ).withDisplayName( "contenttodelete" ).build();
        contentService.addContent( getTestSession(), content, true )
        return content;
    }
}