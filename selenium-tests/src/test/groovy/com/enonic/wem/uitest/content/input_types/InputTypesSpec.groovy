package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.form.*
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
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
    String TEST_TIME = "19:01";

    @Shared
    String TEST_DOUBLE = "123.4";

    @Shared
    String TEST_DATE_TIME = "2015-02-28 19:01";

    @Shared
    String TEST_GEOLOCATION = "10,10";

    @Shared
    String SITE_NAME;

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
        given:
        Content site = buildSite();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
            site.getDisplayName() );

        then: " new site should be listed"
        contentBrowsePanel.exists( site.getPath() );
    }


    def "GIVEN content type with name 'Date' selected and wizard opened WHEN date typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Date'"
        Content dateContent = buildDateContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            dateContent.getContentTypeName() ).typeData( dateContent ).save().close( dateContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        filterPanel.typeSearchText( dateContent.getName() );
        //contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.clickCheckboxAndSelectRow( dateContent.getPath() ).clickToolbarEdit();
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        formViewPanel.getDateValue().equals( TEST_DATE )

    }

    def "GIVEN content type with name 'DateTime' selected and wizard opened WHEN date typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Date Time'"
        Content dateTimeContent = buildDateTimeContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            dateTimeContent.getContentTypeName() ).typeData( dateTimeContent ).save().close( dateTimeContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        filterPanel.typeSearchText( dateTimeContent.getName() );
        //contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.clickCheckboxAndSelectRow( dateTimeContent.getPath() ).clickToolbarEdit();
        DateTimeFormViewPanel dateTimeFormViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        dateTimeFormViewPanel.getDateTimeValue().equals( TEST_DATE_TIME );

    }

    def "GIVEN content type with name 'Time' selected and wizard opened WHEN time typed and content saved THEN new content with correct date listed "()
    {
        given: "add a content with type 'Time'"
        Content timeContent = buildTimeContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            timeContent.getContentTypeName() ).typeData( timeContent ).save().close( timeContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        //contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        filterPanel.typeSearchText( timeContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( timeContent.getPath() ).clickToolbarEdit();
        TimeFormViewPanel timeFormViewPanel = new TimeFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        timeFormViewPanel.getTimeValue().equals( TEST_TIME );

    }


    def "GIVEN content type with name 'Double' selected and wizard opened WHEN double value typed and content saved THEN new content with correct Double value  listed "()
    {
        given: "add a content with type 'Double'"
        Content doubleContent = buildDoubleContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            doubleContent.getContentTypeName() ).typeData( doubleContent ).save().close( doubleContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        filterPanel.typeSearchText( doubleContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( doubleContent.getPath() ).clickToolbarEdit();
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        doubleFormViewPanel.getDoubleValue().equals( TEST_DOUBLE );

    }


    def "GIVEN content type with name 'Geo Location' selected and wizard opened WHEN geo point value typed and content saved THEN new content with correct value listed "()
    {
        given: "add a content with type 'Geo point'"
        Content doubleContent = buildGeoPointContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            doubleContent.getContentTypeName() ).typeData( doubleContent ).save().close( doubleContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        filterPanel.typeSearchText( doubleContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( doubleContent.getPath() ).clickToolbarEdit();
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        geoPointFormViewPanel.getGeoPointValue().equals( TEST_GEOLOCATION );

    }

    @Ignore
    def "GIVEN content type with name 'checkbox' selected and wizard opened WHEN  the checkbox selected and content saved THEN new content with correct boolean value listed "()
    {
        given: "add a content with type 'checkbox'"
        Content doubleContent = buildCheckBoxContent();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            doubleContent.getContentTypeName() ).typeData( doubleContent ).save().close( doubleContent.getDisplayName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        filterPanel.typeSearchText( doubleContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( doubleContent.getPath() ).clickToolbarEdit();
        CheckBoxFormViewPanel checkBoxFormViewPanel = new CheckBoxFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        checkBoxFormViewPanel.isChecked();

    }

    private Content buildCheckBoxContent()
    {
        String name = "checkbox";

        PropertyTree data = new PropertyTree();
        data.addBoolean( CheckBoxFormViewPanel.CHECKBOX_PROPERTY, true );


        Content checkboxContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "checkbox content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":checkbox" ).data( data ).
            build();
        return checkboxContent;
    }


    private Content buildDateContent()
    {
        String name = "date";

        PropertyTree data = new PropertyTree();
        data.addStrings( DateFormViewPanel.DATE_PROPERTY, TEST_DATE );


        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":date" ).data( data ).
            build();
        return dateContent;
    }

    private Content buildDoubleContent()
    {
        String name = "double";

        PropertyTree data = new PropertyTree();
        data.addStrings( DoubleFormViewPanel.DOUBLE_PROPERTY, TEST_DOUBLE );


        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "double content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":double" ).data( data ).
            build();
        return dateContent;
    }

    private Content buildGeoPointContent()
    {
        String name = "geopoint";

        PropertyTree data = new PropertyTree();
        data.addStrings( GeoPointFormViewPanel.GEO_POINT_PROPERTY, TEST_GEOLOCATION );


        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "geo point content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":geopoint" ).data( data ).
            build();
        return dateContent;
    }


    private Content buildTimeContent()
    {
        String name = "time";

        PropertyTree data = new PropertyTree();
        data.addStrings( TimeFormViewPanel.TIME_PROPERTY, TEST_TIME );


        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":time" ).data( data ).
            build();
        return dateContent;
    }

    private Content buildDateTimeContent()
    {
        String name = "datetime";

        PropertyTree contentData = new PropertyTree();
        contentData.addStrings( DateTimeFormViewPanel.DATE_TIME_PROPERTY, TEST_DATE_TIME );


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
        SITE_NAME = NameHelper.uniqueName( "inputtypes" );
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
