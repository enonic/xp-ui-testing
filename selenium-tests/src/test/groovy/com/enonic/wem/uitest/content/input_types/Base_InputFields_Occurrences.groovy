package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.*
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ItemSetContent
import com.enonic.autotests.vo.contentmanager.TestItemSet
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.data.ValueFactory
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Base_InputFields_Occurrences
    extends BaseGebSpec

{
    @Shared
    String FL_IMAGE_NAME = "fl.jpg";

    @Shared
    String FL_IMAGE_DISPLAY_NAME = "fl";

    @Shared
    String NORD_IMAGE_NAME = "nord.jpg";

    @Shared
    String NORD_IMAGE_DISPLAY_NAME = "nord";

    @Shared
    String BOOK_IMAGE_NAME = "book.jpg";

    @Shared
    String BOOK_IMAGE_DISPLAY_NAME = "book";

    @Shared
    String MAN_IMAGE_DISPLAY_NAME = "man";

    @Shared
    String MAN_IMAGE_NAME = "man.jpg";

    @Shared
    String EXECUTABLE_BAT_DISPLAY_NAME = "server";

    @Shared
    static String SITE_NAME = "inputtypes199357987";//NameHelper.uniqueName( "inputtypes" );

    @Shared
    String TAG_1 = "tag1";

    @Shared
    String TAG_2 = "tag2";

    @Shared
    String TAG_3 = "tag3";

    @Shared
    String TAG_4 = "tag4";

    @Shared
    String TAG_5 = "tag5";

    @Shared
    String TAG_6 = "tag6";

    @Shared
    String MAX_SAFE_INTEGER = "9007199254740991";

    @Shared
    String MIN_SAFE_INTEGER = "-9007199254740991";

    @Shared
    String MORE_MAX_SAFE_INTEGER = "9007199254740992";

    @Shared
    Integer INITIAL_NUMBER_OF_VERSIONS = 2;

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();
    }

    def "create a site with 'all content types' application"()
    {
        when: "do add the site:"
        addSiteWithAllInputTypes();

        then: "test site should be listed"
        contentBrowsePanel.exists( SITE_NAME );
    }

    private void addSiteWithAllInputTypes()
    {
        Content site;
        filterPanel.typeSearchText( SITE_NAME );
        if ( !contentBrowsePanel.exists( SITE_NAME ) )
        {
            site = buildSiteWithAllTypes();
            ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData(
                site );
            wizard.showPublishMenu().clickOnMarkAsReadyMenuItem();
            //dialog.pressYesButton();
            wizard.closeBrowserTab().switchToBrowsePanelTab();
        }
    }

    void addContent( Content content )
    {
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData(
            content ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    private Content buildSiteWithAllTypes()
    {
        PropertyTree data = new PropertyTree();
        data.addString( SiteFormViewPanel.APP_KEY, APP_CONTENT_TYPES_DISPLAY_NAME );
        data.addStrings( "description", "all content types  site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( SITE_NAME ).
            displayName( "site-contenttypes-based" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected ContentWizardPanel selectSitePressNew( String contentTypeName )
    {
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + "time0_0" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildTime1_1_Content( String time )
    {
        PropertyTree data = null;
        String name = "time";
        if ( time != null )
        {
            data = new PropertyTree();
            data.addStrings( TimeFormViewPanel.TIME_PROPERTY, time );
        }
        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "reqired time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "time1_1" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildTime2_4_Content( String time )
    {
        PropertyTree data = null;
        String name = "time";
        if ( time != null )
        {
            data = new PropertyTree();
            data.addStrings( TimeFormViewPanel.TIME_PROPERTY, time );
        }
        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "time2_4" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildDateTime2_4_Content( String... datetime )
    {
        String name = "datetime";
        PropertyTree data = null;
        if ( datetime != null )
        {
            data = new PropertyTree();
            data.addStrings( TimeFormViewPanel.TIME_PROPERTY, datetime );
        }

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "datetime2_4" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildDateTime1_1_Content( String dateTime )
    {
        PropertyTree contentData = new PropertyTree(); ;
        if ( dateTime != null )
        {
            contentData.addStrings( DateTimeFormViewPanel.DATE_TIME_PROPERTY, dateTime );
        }
        String name = "datetime";
        Content dateTimeContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "datetime1_1" ).data( contentData ).
            build();
        return dateTimeContent;
    }

    protected Content buildDate1_1_Content( String date )
    {
        PropertyTree contentData = new PropertyTree(); ;
        if ( date != null )
        {
            contentData.addStrings( DateFormViewPanel.DATE_PROPERTY, date );
        }
        String name = "date";
        Content dateTimeContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "date1_1" ).data( contentData ).
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + "imageselector0_1" ).data( data ).
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + "imageselector1_1" ).data( data ).
            build();
        return imageSelectorContent;
    }

    protected Content buildCustomSelector0_2_Content( String... options )
    {
        PropertyTree data = null;
        if ( options != null )
        {
            data = new PropertyTree();
            data.addStrings( CustomSelectorFormViewPanel.CUSTOM_SELECTOR_OPTIONS, options );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "custom_sel" ) ).
            displayName( "custom selector" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "custom-selector0_2" ).data( data ).
            build();
        return imageSelectorContent;
    }

    protected Content buildCustomSelector1_1_Content( String... options )
    {
        PropertyTree data = null;
        if ( options != null )
        {
            data = new PropertyTree();
            data.addStrings( CustomSelectorFormViewPanel.CUSTOM_SELECTOR_OPTIONS, options );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "custom_sel" ) ).
            displayName( "custom selector" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "custom-selector1_1" ).data( data ).
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + "imageselector0_0" ).data( data ).
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + "imageselector2_4" ).data( data ).
            build();
        return imageSelectorContent;
    }

    protected Content buildDate0_1_Content( String date )
    {
        String name = "date";
        PropertyTree data = new PropertyTree();
        data.addStrings( DateFormViewPanel.DATE_PROPERTY, date );

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "date0_1" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildAttachment1_1_Content( String attachmentName )
    {
        String name = "date";
        PropertyTree data = new PropertyTree();
        data.addStrings( AttachmentsFormView.ATTACHMENT_PROPERTY, attachmentName );

        Content attachmentContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "attachment content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "attachment1_1" ).data( data ).
            build();
        return attachmentContent;
    }

    protected Content buildAttachment2_4_Content( String... attachmentNames )
    {
        String name = "date";
        PropertyTree data = new PropertyTree();
        data.addStrings( AttachmentsFormView.ATTACHMENT_PROPERTY, attachmentNames );

        Content attachmentsContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "attachments content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "attachment2_4" ).data( data ).
            build();
        return attachmentsContent;
    }


    protected Content buildDateTime0_1_Content( String dateTime )
    {
        String name = "datetime";
        PropertyTree contentData = new PropertyTree();
        if ( dateTime != null )
        {
            contentData.addStrings( DateTimeFormViewPanel.DATE_TIME_PROPERTY, dateTime );
        }
        Content dateTimeContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "datetime0_1" ).data( contentData ).
            build();
        return dateTimeContent;
    }

    protected Content buildDouble0_1_Content( String doubleValue )
    {
        PropertyTree data = new PropertyTree();
        String name = "double";
        if ( doubleValue != null )
        {
            data.addStrings( DoubleFormViewPanel.DOUBLE_VALUES, doubleValue );
        }
        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "double content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "double0_1" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildDouble0_0_Content( String doubleValue )
    {
        PropertyTree data = new PropertyTree();
        String name = "double";
        if ( doubleValue != null )
        {
            data.addStrings( DoubleFormViewPanel.DOUBLE_VALUES, doubleValue );
        }
        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "double content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "double0_0" ).data( data ).
            build();
        return dateContent;
    }


    protected Content buildDouble1_1_Content( String doubleValue )
    {
        PropertyTree data = new PropertyTree();
        String name = "double";
        if ( doubleValue != null )
        {
            data.addStrings( DoubleFormViewPanel.DOUBLE_VALUES, doubleValue );
        }

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "double content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "double1_1" ).data( data ).
            build();
        return dateContent;
    }


    protected Content buildDouble2_4_Content( String... doubleValues )
    {
        PropertyTree data = new PropertyTree();
        String name = "double";
        if ( doubleValues != null )
        {
            data.addStrings( DoubleFormViewPanel.DOUBLE_VALUES, doubleValues );
        }

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "double content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "double2_4" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildLong0_1_Content( String longValue )
    {
        String name = "long";
        PropertyTree data = new PropertyTree();
        if ( longValue != null )
        {
            data.addStrings( LongFormViewPanel.LONG_VALUE, longValue );
        }
        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "long content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "long0_1" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildLong1_1_Content( String longValue )
    {
        String name = "long";
        PropertyTree data = new PropertyTree();
        if ( longValue != null )
        {
            data.addStrings( LongFormViewPanel.LONG_VALUE, longValue );
        }
        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "long content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "long1_1" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildGeoPoint0_0_Content( String value )
    {
        String name = "geopoint";
        PropertyTree data = new PropertyTree();
        data.addStrings( GeoPointFormViewPanel.GEO_POINT_PROPERTY, value );

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "geo point content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "geopoint0_0" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildGeoPoint1_1_Content( String value )
    {
        String name = "geopoint";
        PropertyTree data = new PropertyTree();
        data.addStrings( GeoPointFormViewPanel.GEO_POINT_PROPERTY, value );

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "geo point content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "geopoint1_1" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildCheckBoxContent( boolean value )
    {
        String name = "checkbox";
        PropertyTree data = new PropertyTree();
        data.addBoolean( CheckBoxFormViewPanel.CHECKBOX_PROPERTY, value );

        Content checkboxContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "checkbox content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "checkbox" ).data( data ).
            build();
        return checkboxContent;
    }

    protected Content buildRadioButtonsContent( String option )
    {
        PropertyTree data = ContentUtils.buildSingleSelectionData( option );
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "radiobutton" ) ).
            displayName( "radiobutton content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "radiobutton" ).data( data ).
            build();
        return textLineContent;
    }

    protected Content buildTextLine0_1_Content( String text )
    {
        String name = "textline0_1";
        PropertyTree data = new PropertyTree();
        data.addString( TextLine0_1_FormViewPanel.TEXT_INPUT_PROPERTY, text );

        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "textline0_1" ).data( data ).
            build();
        return textLineContent;
    }

    protected Content buildTextLine1_0_Content( String text )
    {
        String name = "textline1_0";
        PropertyTree data = new PropertyTree();
        data.addProperty( "0", ValueFactory.newString( text ) );

        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline1_0 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "textline1_0" ).data( data ).
            build();
        return textLineContent;
    }

    protected Content buildTextLine1_1_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        String name = "textline1_1";
        if ( text != null )
        {
            data.addStrings( TextLine1_1_FormViewPanel.TEXT_INPUT_PROPERTY, text );
        }
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline1_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "textline1_1" ).data( data ).
            build();
        return textLineContent;
    }

    ContentBrowsePanel findAndSelectContent( String name )
    {
        filterPanel.typeSearchText( name );
        if ( !contentBrowsePanel.isRowSelected( name ) )
        {
            contentBrowsePanel.clickCheckboxAndSelectRow( name );
        }
        return contentBrowsePanel;
    }

    protected AllContentVersionsView openVersionPanel()
    {
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.openContentDetailsPanel();
        AllContentVersionsView contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        return contentItemVersionsPanel;
    }

    protected PropertyTree buildTag2_5_Data( int numberOfTags )
    {
        PropertyTree data = new PropertyTree();
        data.setLong( "min", 2 );
        data.setLong( "max", 5 );
        switch ( numberOfTags )
        {
            case 0:
                break;
            case 1:
                data.addString( "tags", TAG_1 );
                break;
            case 2:
                data.addString( "tags", TAG_1 );
                data.addString( "tags", TAG_2 );
                break;
            case 5:
                data.addString( "tags", TAG_1 );
                data.addString( "tags", TAG_2 );
                data.addString( "tags", TAG_3 );
                data.addString( "tags", TAG_4 );
                data.addString( "tags", TAG_5 );
                break;
            default:
                throw new TestFrameworkException( "data not implemented" );
        }
        return data;
    }

    protected Content buildTag_2_5_Content( int numberOfTags )
    {
        PropertyTree data = buildTag2_5_Data( numberOfTags );
        String name = "tag2_5";
        Content tagContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "tag2_5 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "tag2_5" ).data( data ).
            build();
        return tagContent;
    }

    protected Content buildComboBox2_4_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content comboboxContent = Content.builder().
            name( NameHelper.uniqueName( "cbox2_4_" ) ).
            displayName( "combobox2_4 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "combobox2_4" ).data( data ).
            build();
        return comboboxContent;
    }

    protected Content buildHtmlArea0_1_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        if ( text != null )
        {
            data.addStrings( HtmlArea0_1_FormViewPanel.STRING_PROPERTY, text );
        }
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "html0_1_" ) ).
            displayName( "html0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "htmlarea0_1" ).data( data ).
            build();
        return tinyMceContent;
    }

    protected Content buildItemSetContent()
    {
        Content itemSetContent = ItemSetContent.builder().
            name( NameHelper.uniqueName( "itemset" ) ).
            displayName( "item set content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "item-set0_0" ).
            build();
        return itemSetContent;
    }

    protected Content buildItemSetContentWitData( PropertyTree data )
    {
        Content itemSetContent = ItemSetContent.builder().
            name( NameHelper.uniqueName( "itemset" ) ).
            displayName( "item set content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "item-set0_0" ).data( data ).
            build();
        return itemSetContent;
    }

    protected PropertyTree build_ItemSet_Data( List<TestItemSet> itemSets )
    {
        PropertyTree data = new PropertyTree();
        for ( TestItemSet itemSet : itemSets )
        {
            if ( itemSet.getTextLineText() == null )
            {
                data.addString( ItemSetViewPanel.TEXT_LINE_VALUES, "" );
            }
            else
            {
                data.addString( ItemSetViewPanel.TEXT_LINE_VALUES, itemSet.getTextLineText() );
            }

            if ( itemSet.getTextLineText() == null )
            {
                data.addString( ItemSetViewPanel.HTML_AREA_VALUES, "" );
            }
            else
            {
                data.addString( ItemSetViewPanel.HTML_AREA_VALUES, itemSet.getHtmlAreaText() );
            }

        }
        return data;
    }

    protected Content build_TextArea0_1_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        if ( !text.isEmpty() )
        {
            data.addString( "text", text );
        }
        Content textAreaContent = Content.builder().
            name( NameHelper.uniqueName( "textarea" ) ).
            displayName( "text_area content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "textarea0_1" ).data( data ).
            build();
        return textAreaContent;
    }

    protected Content build_TextArea1_1_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        if ( !text.isEmpty() )
        {
            data.addString( "text", text );
        }
        Content textAreaContent = Content.builder().
            name( NameHelper.uniqueName( "textarea" ) ).
            displayName( "text_area content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "textarea1_1" ).data( data ).
            build();
        return textAreaContent;
    }

    protected TestItemSet buildItemSetValues( String textLine, String htmlareaText )
    {
        return TestItemSet.builder().textLineText( textLine ).htmlAreaText( htmlareaText ).build();
    }
}