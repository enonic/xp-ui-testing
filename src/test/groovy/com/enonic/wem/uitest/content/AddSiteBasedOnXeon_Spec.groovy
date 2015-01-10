package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.data.PropertyTree
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class AddSiteBasedOnXeon_Spec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String SITE_NAME;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN creating new Site based on Xeon on root WHEN saved and wizard closed THEN new site should be listed"()
    {
        given:
        Content site = buildSiteBasedOnXeon();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close();

        then: " new site should be listed"
        contentBrowsePanel.exists( site.getPath() );

    }


    def "GIVEN exists on root a Site based on Xeon WHEN site expanded and templates folder selected AND page-template added  THEN new template should be listed beneath a 'Templates' folder"()
    {
        given:
        Content pageTemplate = buildPageTemplate();

        when: "site expanded and 'Templates' folder selected and page-template added"
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.selectContentInTable( ContentPath.from( SITE_NAME + "/_templates" ) ).clickToolbarNew().selectContentType(
            pageTemplate.getContentTypeName() ).typeData( pageTemplate ).save().close();
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME + "/_templates" ) );

        then: " new template should be listed beneath a 'Templates' folder"
        contentBrowsePanel.exists( pageTemplate.getPath() );
    }

    private Content buildSiteBasedOnXeon()
    {
        SITE_NAME = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addString( "moduleKey", "Xeon Sample Module" );
        data.addStrings( "description", "site based on XEON" )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( SITE_NAME ).
            displayName( "site-xeon-based" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    private Content buildPageTemplate()
    {
        String name = "pagetemplate";

        PropertyTree data = new PropertyTree();
        data.addStrings( "nameInMenu", "item1" );
        data.addStrings( "pageController", "Main page" );

        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "xeon-page-template" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ContentTypeName.pageTemplate() ).data( data ).
            build();
        return pageTemplate;
    }
}
