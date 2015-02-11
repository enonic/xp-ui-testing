package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.data.PropertyTree
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class InputTypesSpec
    extends BaseGebSpec
{

    @Shared
    String ALL_CONTENT_TYPES_MODULE_NAME = "com.enonic.xp.ui-testing.all-contenttypes";

    @Shared
    String MODULE_DISPLAY_NAME = "All Content Types Module";

    @Shared
    String TEST_DATE = "2015-02-28";

    @Shared
    String TEST_DATE_TIME = "2015-02-28 19:01";

    @Shared
    String SITE_NAME;

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ModuleBrowsePanel moduleBrowsePanel;


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "create a site based on module with all content types"()
    {
        given:
        Content site = buildSite();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
            site.getDisplayName() );

        then: " new site should be listed"
        contentBrowsePanel.exists( site.getPath() );
    }

    def "GIVEN 'Date' content type selected and wizard opened WHEN date typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Date'"
        Content date = buildDateContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType( date.getContentTypeName() ).typeData(
            date ).save().close( date.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.clickCheckboxAndSelectRow( date.getPath() ).clickToolbarEdit();
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        formViewPanel.getRequiredDateValue().equals( TEST_DATE )


    }

    def "GIVEN 'DateTime' content type selected and wizard opened WHEN date typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Date Time'"
        Content date = buildDateTimeContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType( date.getContentTypeName() ).typeData(
            date ).save().close( date.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.clickCheckboxAndSelectRow( date.getPath() ).clickToolbarEdit();
        DateTimeFormViewPanel dateTimeFormViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        dateTimeFormViewPanel.getRequiredDateTimeValue().equals( TEST_DATE_TIME )


    }


    private Content buildDateContent()
    {
        String name = "date";

        PropertyTree data = new PropertyTree();
        data.addStrings( DateFormViewPanel.REQUIRED_DATE_PROPERTY, TEST_DATE );


        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":date" ).data( data ).
            build();
        return dateContent;
    }

    private Content buildDateTimeContent()
    {
        String name = "datetime";

        PropertyTree contentData = new PropertyTree();
        contentData.addStrings( DateTimeFormViewPanel.REQUIRED_DATE_TIME_PROPERTY, TEST_DATE_TIME );


        Content dateTimeContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":datetime" ).data( contentData ).
            build();
        return dateTimeContent;
    }

    private Content buildSite()
    {
        SITE_NAME = NameHelper.uniqueName( "site" );
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
}
