package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.CountryFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class BaseSiteSpec
    extends BaseContentSpec
{

    @Shared
    String COUNTRY_PART_NAME = "Country part";

    @Shared
    String COUNTRY_PART_DEFAULT_NAME = "country";

    @Shared
    String LIVE_EDIT_FRAME_SITE_HEADER = "//h1[text()='Country']";

    @Shared
    String COUNTRY_REGION_TITLE = "Country Region";

    @Shared
    String COUNTRY_LIST_CONTROLLER = "country-list";

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

    protected ContentWizardPanel selectSitePressNew( String contentTypeName, String siteName )
    {
        filterPanel.typeSearchText( siteName );
        return contentBrowsePanel.clickCheckboxAndSelectRow( siteName ).clickToolbarNew().selectContentType( contentTypeName );
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
}
