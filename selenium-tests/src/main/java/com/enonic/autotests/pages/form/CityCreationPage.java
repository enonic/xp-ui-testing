package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;


public class CityCreationPage
    extends Application
{
    public CityCreationPage( final TestSession session )
    {
        super( session );
    }

    public CityCreationPage typeCityName( String cityName )
    {
        // findElements( By.xpath( "//input[@name='cityName']" ) ).get( 0 ).sendKeys( cityName );
        clearAndType( findElements( By.xpath( "//input[@name='cityName']" ) ).get( 0 ), cityName );
        return this;
    }

    public CityCreationPage typeCityPopulation( String cityPopulation )
    {
        //findElements( By.xpath( "//input[@name='cityPopulation']" ) ).get( 0 ).sendKeys( cityPopulation );
        clearAndType( findElements( By.xpath( "//input[@name='cityPopulation']" ) ).get( 0 ), cityPopulation );
        return this;
    }

    public CityCreationPage typeCityLocation( String cityLocation )
    {
        clearAndType( findElements( By.xpath( "//input[@name='cityLocation']" ) ).get( 0 ), cityLocation );

        return this;
    }

    public CityCreationPage clickSubmit()
    {
        findElements( By.xpath( "//input[@type='submit']" ) ).get( 0 ).click();
        return this;
    }


}
