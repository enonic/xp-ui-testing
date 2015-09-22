package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.CountryFormView
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared

class BaseSiteSpec
    extends BaseContentSpec
{
    @Shared
    String COUNTRY_REGION_PAGE_CONTROLLER = "Country Region";

    @Shared
    String SUPPORTS_TYPE = "country";

    @Shared
    String COUNTRY_PART_NAME = "country";

    @Shared
    String LIVE_EDIT_FRAME_SITE_HEADER = "//h1[text()='Country']";

    @Shared
    String COUNTRY_REGION_TITLE = "Country Region";

    @Shared
    String COUNTRY_REGION_HEADER = "Country"

    @Shared
    String USA_POPULATION = "300 000 000";

    @Shared
    String USA_DESCRIPTION = "USA country";

    @Shared
    String NOR_DESCRIPTION = "Norway country";

    @Shared
    String MY_FIRST_APP_NAME = "com.enonic.myfirstapp";

    @Shared
    static String FIRST_SITE_NAME = NameHelper.uniqueName( "first-site" );

    def "create a site based on the application with all content types"()
    {
        when: "add a site, based on the test application"
        addSite();

        then: " test site should be listed"
        contentBrowsePanel.exists( FIRST_SITE_NAME );
    }

    private void addSite()
    {
        Content site;
        filterPanel.typeSearchText( FIRST_SITE_NAME );
        if ( !contentBrowsePanel.exists( FIRST_SITE_NAME ) )
        {
            site = buildMySite( FIRST_SITE_NAME );
            contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
                site.getDisplayName() );
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "first_site_saved" ) );
        }
    }

    protected ContentWizardPanel selectSiteOpenWizard( String contentTypeName, String siteName )
    {
        filterPanel.typeSearchText( siteName );
        return contentBrowsePanel.clickCheckboxAndSelectRow( siteName ).clickToolbarNew().selectContentType( contentTypeName );
    }

    protected Content buildMySite( String siteName )
    {
        PropertyTree data = new PropertyTree();
        data.addString( "applicationKey", "My First App" );
        data.addStrings( "description", "My first Site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( siteName ).
            displayName( "my-site" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    protected Content buildPageTemplate( String pageDescriptorName, String supports, String displayName, String parentName )
    {
        String name = "template";
        PropertyTree data = new PropertyTree();
        data.addStrings( PageTemplateFormViewPanel.PAGE_CONTROLLER, pageDescriptorName );
        data.addStrings( PageTemplateFormViewPanel.SUPPORTS, supports );

        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( ContentPath.from( parentName ) ).
            contentType( ContentTypeName.pageTemplate() ).data( data ).
            build();
        return pageTemplate;
    }

    protected Content buildCountry_Content( String countryName, String description, String population, String parentName )
    {
        PropertyTree data = null;
        if ( population != null )
        {
            data = new PropertyTree();
            data.addStrings( CountryFormView.POPULATION, population );
            data.addStrings( CountryFormView.DESCRIPTION, description );
        }

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( countryName.toLowerCase() ) ).
            displayName( countryName ).
            parent( ContentPath.from( parentName ) ).
            contentType( MY_FIRST_APP_NAME + ":country" ).data( data ).
            build();
        return dateContent;
    }

    protected Content buildCity_Content( String cityName, String location, String population, String parentName )
    {
        PropertyTree data = null;
        if ( population != null )
        {
            data = new PropertyTree();
            data.addStrings( CityFormView.POPULATION, population );
            data.addStrings( CityFormView.LOCATION, location );
        }

        Content dateContent = Content.builder().
            name( NameHelper.uniqueName( cityName.toLowerCase() ) ).
            displayName( cityName ).
            parent( ContentPath.from( parentName ) ).
            contentType( MY_FIRST_APP_NAME + ":city" ).data( data ).
            build();
        return dateContent;
    }

    protected void openResourceInMaster( String resource )
    {
        getDriver().navigate().to( browser.baseUrl + "admin/portal/preview/master/" + resource );
    }

    protected void openResourceInDraft( String resource )
    {
        getDriver().navigate().to( browser.baseUrl + "admin/portal/preview/draft/" + resource );
    }

    protected void openHomePage()
    {
        getDriver().navigate().to( browser.baseUrl + "admin/#/home" );
    }
}
