package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CountryFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MyFirstApp_Spec
    extends BaseContentSpec
{
    @Shared
    String COUNTRY_REGION_TITLE = "First Region";

    @Shared
    String COUNTRY_REGION_HEADER = "Country"

    @Shared
    Content MY_FIRST_SITE

    @Shared
    Content USA_CONTENT;

    @Shared
    String MY_FIRST_APP_NAME = "com.enonic.myfirstapp";

    def "GIVEN creating new Site based on 'My First App' WHEN saved and wizard closed THEN new site should be listed"()
    {
        given:
        MY_FIRST_SITE = buildMySite();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( MY_FIRST_SITE.getContentTypeName() ).typeData( MY_FIRST_SITE ).save().close(
            MY_FIRST_SITE.getDisplayName() );

        then: " new site should be listed"
        contentBrowsePanel.exists( MY_FIRST_SITE.getName() );

    }

    def "GIVEN adding a content with type 'country' WHEN 'save' pressed THEN new added content listed"()
    {
        given:
        USA_CONTENT = buildCountry_Content( "USA", "USA country", "300 000 000" );
        ContentWizardPanel wizard = selectSiteOpenWizard( MY_FIRST_SITE.getName(), USA_CONTENT.getContentTypeName() );

        when: "data typed and saved and wizard closed"
        String message = wizard.typeData( USA_CONTENT ).save().waitNotificationMessage();
        wizard.close( USA_CONTENT.getDisplayName() );

        then: " new 'country' content should be listed"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        contentBrowsePanel.exists( USA_CONTENT.getName() );
    }

    def "GIVEN the 'country' content opened for edit WHEN region selected and 'Preview' button pressed THEN region component correctly shown in the new browser window"()
    {
        given: "a page descriptor added for existing country-content"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnLiveToolbarButton().selectPageDescriptor( COUNTRY_REGION_TITLE ).save();

        when: "the 'Preview' button pressed on the wizard-toolbar"
        wizard.clickToolbarPreview();

        then: "the region page opened in a browser with correct title and correct header"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( COUNTRY_REGION_TITLE );
        and: "correct header displayed"
        source.contains( COUNTRY_REGION_HEADER );

    }

    private Content buildMySite()
    {
        String name = NameHelper.uniqueName( "mysite" );
        PropertyTree data = new PropertyTree();
        data.addString( "applicationKey", "My First App" );
        data.addStrings( "description", "My first Site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "my-first-site" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    private Content buildPageTemplate( String pageDescriptorName )
    {
        String name = "pagetemplate";

        PropertyTree data = new PropertyTree();
        data.addStrings( "nameInMenu", "item1" );
        data.addStrings( "pageController", pageDescriptorName );

        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "simple-page-template" ).
            parent( ContentPath.from( MY_FIRST_SITE.getName() ) ).
            contentType( ContentTypeName.pageTemplate() ).data( data ).
            build();
        return pageTemplate;
    }

    protected Content buildCountry_Content( String countryName, String description, String population )
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
            parent( ContentPath.from( MY_FIRST_SITE.getName() ) ).
            contentType( MY_FIRST_APP_NAME + ":country" ).data( data ).
            build();
        return dateContent;
    }
}
