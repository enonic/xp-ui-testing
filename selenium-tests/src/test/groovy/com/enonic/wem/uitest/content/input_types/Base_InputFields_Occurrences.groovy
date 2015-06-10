package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
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
    static String SITE_NAME = NameHelper.uniqueName( "inputtypes" );

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
        when: "add a site, based on test module"
        addSite();

        then: " test site should be listed"
        contentBrowsePanel.exists( SITE_NAME );
    }

    private void addSite()
    {
        Content site;
        filterPanel.typeSearchText( SITE_NAME );
        if ( !contentBrowsePanel.exists( SITE_NAME ) )
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

    protected Content buildDateTime2_4_Content( String... datetime )
    {
        String name = "datetime";

        PropertyTree data = new PropertyTree();
        data.addStrings( TimeFormViewPanel.TIME_PROPERTY, datetime );

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":datetime2_4" ).data( data ).
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

    protected Content buildImageSelector0_1_Content( String... imageNames )
    {
        PropertyTree data = null;
        if ( imageNames != null )
        {
            data = new PropertyTree();
            data.addStrings( ImageSelectorFormViewPanel.IMAGES_PROPERTY, imageNames );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "img0_1_" ) ).
            displayName( "img_sel 0_1" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":imageselector0_1" ).data( data ).
            build();
        return imageSelectorContent;
    }

    protected Content buildImageSelector1_1_Content( String imageName )
    {
        PropertyTree data = null;
        if ( imageName != null )
        {
            data = new PropertyTree();
            data.addString( ImageSelectorFormViewPanel.IMAGES_PROPERTY, imageName );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "img1_1_" ) ).
            displayName( "img_sel 1_1" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":imageselector1_1" ).data( data ).
            build();
        return imageSelectorContent;
    }

    protected Content buildImageSelector0_0_Content( String... imageNames )
    {
        PropertyTree data = null;
        if ( imageNames != null )
        {
            data = new PropertyTree();
            data.addStrings( ImageSelectorFormViewPanel.IMAGES_PROPERTY, imageNames );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "img0_0_" ) ).
            displayName( "img_sel 0_0" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":imageselector0_0" ).data( data ).
            build();
        return imageSelectorContent;
    }

    protected Content buildImageSelector2_4_Content( String... imageNames )
    {
        PropertyTree data = null;
        if ( imageNames != null )
        {
            data = new PropertyTree();
            data.addStrings( ImageSelectorFormViewPanel.IMAGES_PROPERTY, imageNames );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "img2_4_" ) ).
            displayName( "img_sel 2_4" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":imageselector2_4" ).data( data ).
            build();
        return imageSelectorContent;
    }
}