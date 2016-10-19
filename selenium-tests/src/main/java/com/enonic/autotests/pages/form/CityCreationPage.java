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
        clearAndType( findElement( By.xpath( "//input[@name='cityName']" ) ), cityName );
        return this;
    }

    public CityCreationPage typeCityPopulation( String cityPopulation )
    {
        clearAndType( findElement( By.xpath( "//input[@name='cityPopulation']" ) ), cityPopulation );
        return this;
    }

    public CityCreationPage typeCityLocation( String cityLocation )
    {
        clearAndType( findElement( By.xpath( "//input[@name='cityLocation']" ) ), cityLocation );

        return this;
    }

    public CityCreationPage clickSubmit()
    {
        findElements( By.xpath( "//input[@type='submit']" ) ).get( 0 ).click();
        return this;
    }
}
