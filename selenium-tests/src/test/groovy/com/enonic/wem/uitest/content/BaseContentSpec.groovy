package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseItemPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentItemPreviewPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.ShortcutFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared

class BaseContentSpec
    extends BaseGebSpec
{
    @Shared
    String TEMPLATE_SUPPORTS_COUNTRY = "country";

    @Shared
    String NORSK_LANGUAGE = "norsk (no)";

    @Shared
    String ENGLISH_LANGUAGE = "English (en)";

    @Shared
    String TEMPLATE_SUPPORTS_SITE = "site";

    @Shared
    String CITY_CREATION_PART = "City Creation";

    @Shared
    String COUNTRY_REGION_PAGE_CONTROLLER = "Country Region";

    @Shared
    String MAIN_REGION_PAGE_DESCRIPTOR_NAME = "main region";

    @Shared
    String COUNTRY_LIST_PAGE_CONTROLLER = "country-list";

    @Shared
    String IMPORTED_FOLDER_NAME = "all-content-types-images";

    @Shared
    String IMPORTED_FOLDER_NAME2 = "imagearchive";

    @Shared
    String IMAGES_FOLDER_DISPLAY_NAME = "All Content types images";

    @Shared
    String IMPORTED_IMAGE_BOOK_NAME = "book.jpg";

    @Shared
    String IMPORTED_IMAGE_NORD_NAME = "nord";

    @Shared
    String IMPORTED_IMAGE_BOOK_DISPLAY_NAME = "book";

    @Shared
    String IMPORTED_MAN_IMAGE = "man.jpg";

    @Shared
    String IMPORTED_SPUMANS_IMAGE = "spumans.jpg";

    @Shared
    String IMPORTED_MAN2_IMAGE = "man2.jpg";

    @Shared
    String IMPORTED_MAN2_IMAGE_DISPLAY_NAME = "man2";

    @Shared
    String HAND_IMAGE_NAME = "hand.jpg";

    @Shared
    String HAND_IMAGE_DISPLAY_NAME = "hand";

    @Shared
    String IMPORTED_ELEPHANT_IMAGE = "elephant.jpg";

    @Shared
    String ELEPHANT_IMAGE_DISPLAY_NAME = "elephant";

    @Shared
    String WHALE_IMAGE_NAME = "whale.jpg";

    @Shared
    String WHALE_IMAGE_DISPLAY_NAME = "whale";

    @Shared
    String CIRCLES = "circles.svg"

    @Shared
    String SIMPLE_SITE_APP = "Simple Site App";

    @Shared
    String MY_FIRST_APP = "My First App";

    @Shared
    String SIMPLE_TEST_APP_NAME = "Simple Test Selenium App";

    @Shared
    String FIRST_TEST_APP_NAME = "First Selenium App";

    @Shared
    String COUNTRY_SITE_HTML_HEADER = "<title>Country Region</title>";

    @Shared
    String DEFAULT_NUMBER_OF_CONTENT_IN_SITE = "2";

    @Shared
    String SUPER_USER = "Super User";

    @Shared
    String ANONYMOUS_USER = "Anonymous User";

    @Shared
    String EXECUTABLE_BAT = "server.bat";

    @Shared
    String EXECUTABLE_BAT_DISPLAY_NAME = "server";

    @Shared
    String EXECUTABLE_SH = "server.sh";

    @Shared
    String EXECUTABLE_EXE = "Notepad2.exe";

    @Shared
    Integer INITIAL_NUMBER_OF_VERSIONS = 2;

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseItemPanel contentBrowseItemPanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;

    @Shared
    ContentDetailsPanel contentDetailsPanel;

    @Shared
    ContentItemPreviewPanel contentItemPreviewPanel;


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();
        contentBrowseItemPanel = new ContentBrowseItemPanel( getSession() );
        contentItemPreviewPanel = contentBrowseItemPanel.getContentItemStatisticsPanel().getContentItemPreviewPanel();
        contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
    }

    public Content buildFolderContent( String name, String displayName )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( "Folder" ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }

    protected Content buildShortcut( String name, String parentName, String displayName )
    {
        ContentPath parent = null;
        if ( parentName != null )
        {
            parent = ContentPath.from( parentName )
        }
        else
        {
            parent = ContentPath.ROOT;
        }
        Content shortcut = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( parent ).
            contentType( "Shortcut" ).
            build();
        return shortcut;
    }

    protected Content buildShortcutWithSettingsAndTarget( String name, String parentName, String displayName, String target,
                                                          ContentSettings settings )
    {
        PropertyTree data = null;
        ContentPath parent = null;
        if ( target != null )
        {
            data = new PropertyTree();
            data.addString( ShortcutFormViewPanel.SHORTCUT_PROPERTY, target );
        }
        if ( parentName != null )
        {
            parent = ContentPath.from( parentName )
        }
        else
        {
            parent = ContentPath.ROOT;
        }
        Content shortcut = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( parent ).
            contentType( "Shortcut" ).settings( settings ).data( data ).build();
        return shortcut;
    }

    protected Content buildShortcutWithTarget( String name, String parentName, String displayName, String target )
    {
        PropertyTree data = null;
        ContentPath parent = null;
        if ( target != null )
        {
            data = new PropertyTree();
            data.addString( ShortcutFormViewPanel.SHORTCUT_PROPERTY, target );
        }
        if ( parentName != null )
        {
            parent = ContentPath.from( parentName )
        }
        else
        {
            parent = ContentPath.ROOT;
        }
        Content shortcut = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( parent ).
            contentType( "Shortcut" ).data( data ).build();
        return shortcut;
    }

    public Content buildFolderWithSettingsContent( String name, String displayName, ContentSettings settings )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( "Folder" ).
            parent( ContentPath.ROOT ).settings( settings ).
            build();
        return content;
    }

    public Content buildFolderWithEmptyDisplayNameContent( String name )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            contentType( "Folder" ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }

    public Content buildFolderContentWithParent( String name, String displayName, String parentName )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( "Folder" ).
            parent( ContentPath.from( parentName ) ).
            build();
        return content;
    }

    public void addContent( Content content )
    {
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    public ContentBrowsePanel findAndSelectContent( String name )
    {
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        filterPanel.typeSearchText( name );
        selectContentByName( name );
        return contentBrowsePanel;
    }

    protected ContentBrowsePanel selectContentByName( String name )
    {
        if ( !contentBrowsePanel.isRowSelected( name ) )
        {
            contentBrowsePanel.clickCheckboxAndSelectRow( name );
        }
        return contentBrowsePanel;
    }

    protected ContentWizardPanel selectSitePressNew( String siteName, String contentTypeName )
    {
        filterPanel.typeSearchText( siteName );
        return contentBrowsePanel.clickCheckboxAndSelectRow( siteName ).clickToolbarNew().selectContentType( contentTypeName );
    }

    protected Content buildSiteBasedOnFirstApp()
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addStrings( SiteFormViewPanel.APP_KEY, "First Selenium App" );
        data.addStrings( "description", "site based on the First App " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "sample site" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected Content buildMyFirstAppSite( String siteName )
    {
        PropertyTree data = new PropertyTree();
        data.addStrings( SiteFormViewPanel.APP_KEY, "My First App" );
        data.addStrings( "description", "My first Site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( siteName ) ).
            displayName( "my-site" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected Content buildSimpleSiteApp()
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addStrings( SiteFormViewPanel.APP_KEY, "Simple Site App" );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "simple site" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected Content buildSiteWithApps( String... appNames )
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addStrings( SiteFormViewPanel.APP_KEY, appNames );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "test site " ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected Content buildSiteWithAppsAndSettings( String displayName, ContentSettings settings, String... appNames )
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addStrings( SiteFormViewPanel.APP_KEY, appNames );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( displayName ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).settings( settings ).
            build();
        return site;
    }

    protected Content buildSiteWithNameAndDispalyNameAndDescription( String name, String displayName, String description )
    {
        String siteName = NameHelper.uniqueName( name );
        PropertyTree data = new PropertyTree();
        data.addStrings( "description", description )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( siteName ).
            displayName( displayName ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected void addSite( Content site )
    {
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData(
            site ).save();
        sleep( 500 );
        wizard.closeBrowserTab().switchToNewWizardTab();
    }

    protected Content buildPageTemplate( String pageDescriptorName, String supports, String displayName, String parentName )
    {
        String name = "template";
        PropertyTree data = new PropertyTree();
        data.addStrings( PageTemplateFormViewPanel.PAGE_CONTROLLER, pageDescriptorName );
        if ( supports != null )
        {
            data.addStrings( PageTemplateFormViewPanel.SUPPORTS, supports );
        }
        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( ContentPath.from( parentName ) ).
            contentType( "Page Template" ).data( data ).
            build();
        return pageTemplate;
    }

    protected DependenciesWidgetItemView openDependenciesWidgetView()
    {
        contentBrowsePanel.openContentDetailsPanel();
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        DependenciesWidgetItemView dependenciesWidget = contentDetailsPanel.openDependenciesWidget();
        return dependenciesWidget;
    }

    protected void openResourceInMaster( String resource )
    {
        getDriver().navigate().to( browser.baseUrl + "admin/portal/preview/master/" + resource );
    }

    protected void openResourceInDraft( String resource )
    {
        getDriver().navigate().to( browser.baseUrl + "admin/portal/preview/draft/" + resource );
    }

    protected void getService( String serviceName, String appName )
    {
        getDriver().navigate().to( browser.baseUrl + "portal/draft/_/service/" + appName + "/" + serviceName );
    }

    protected void openHomePage()
    {
        getDriver().navigate().to( browser.baseUrl + "admin/#/home" );
    }

    protected void addSiteWithAllInputTypes( String siteName )
    {
        Content site = buildSiteWithAllTypes( siteName );
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData(
            site ).save().closeBrowserTab().switchToBrowsePanelTab();
    }

    protected Content buildSiteWithAllTypes( String siteName )
    {
        PropertyTree data = new PropertyTree();
        data.addString( SiteFormViewPanel.APP_KEY, APP_CONTENT_TYPES_DISPLAY_NAME );
        data.addStrings( "description", "all content types  site " )
        Content site = Content.builder().
            name( siteName ).
            displayName( "site with all content types" ).
            parent( ContentPath.ROOT ).
            contentType( "Site" ).data( data ).
            build();
        return site;
    }

    protected Content buildImageSelector1_1_Content( String siteName, String imageName )
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
            parent( ContentPath.from( siteName ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "imageselector1_1" ).data( data ).
            build();
        return imageSelectorContent;
    }

    public Content buildFolderWithName( String name, String displayName )
    {
        Content content = Content.builder().
            name( name ).
            displayName( displayName ).
            contentType( "Folder" ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }

    protected AllContentVersionsView openVersionPanel()
    {
        contentBrowsePanel.clickOnDetailsToggleButton();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        AllContentVersionsView contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        contentItemVersionsPanel.waitUntilLoaded();
        return contentItemVersionsPanel;
    }

    protected String buildFragmentName( String resourceDisplayName )
    {
        return "fragment-" + resourceDisplayName;
    }
}