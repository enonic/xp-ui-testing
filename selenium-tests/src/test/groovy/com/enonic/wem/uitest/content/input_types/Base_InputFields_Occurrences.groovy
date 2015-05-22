package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.TimeFormViewPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Base_InputFields_Occurrences
    extends BaseGebSpec

{
    @Shared
    String ALL_CONTENT_TYPES_MODULE_NAME = "com.enonic.xp.ui-testing.all-contenttypes";

    @Shared
    String PUBLISH_NOTIFICATION_WARNING = "The content cannot be published yet. One or more form values are not valid.";

    @Shared
    static String SITE_NAME = "inputtypes39507008";//NameHelper.uniqueName( "inputtypes" );

    @Shared
    String MODULE_DISPLAY_NAME = "All Content Types Module";

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();
    }

    def "create a site based on module with all content types"()
    {
        setup: "add a site, based on test module"
        addSite();

        when:
        filterPanel.typeSearchText( SITE_NAME );
        TestUtils.saveScreenshot( getSession(), SITE_NAME )

        then: " test site should be listed"
        contentBrowsePanel.exists( ContentPath.from( ContentPath.ROOT, SITE_NAME ) );
    }

    private void addSite()
    {
        Content site;
        ContentPath sitePath = ContentPath.from( ContentPath.ROOT, SITE_NAME );
        filterPanel.typeSearchText( SITE_NAME );
        if ( !contentBrowsePanel.exists( sitePath ) )
        {
            site = buildSite();
            contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
                site.getDisplayName() );
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "site_saved" ) );
        }
    }

    private Content buildSite()
    {
        PropertyTree data = new PropertyTree();
        data.addString( "moduleKey", MODULE_DISPLAY_NAME );
        data.addStrings( "description", "all content types  site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( SITE_NAME ).
            displayName( "site-contenttypes-based" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    protected ContentWizardPanel selectSiteOpenWizard( String contentTypeName )
    {
        filterPanel.typeSearchText( SITE_NAME );
        return contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType( contentTypeName );
    }

    protected Content buildTime0_0_Content( String time )
    {
        String name = "time";

        PropertyTree data = new PropertyTree();
        data.addStrings( TimeFormViewPanel.TIME_PROPERTY, time );

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":time" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildTime2_4_Content( String time )
    {
        String name = "time";

        PropertyTree data = new PropertyTree();
        data.addStrings( TimeFormViewPanel.TIME_PROPERTY, time );

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":time2_4" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildDateTime1_1_Content( String dateTime )
    {
        String name = "datetime";
        PropertyTree contentData = new PropertyTree();
        contentData.addStrings( DateTimeFormViewPanel.DATE_TIME_PROPERTY, dateTime );

        Content dateTimeContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":datetime1_1" ).data( contentData ).
            build();
        return dateTimeContent;
    }
}