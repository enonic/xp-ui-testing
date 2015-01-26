package com.enonic.autotests.pages;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

public class WemStartPage
    extends Page
{
    private String CLEAR_DATA_LINK = "//a[contains(@href,'cleanData')]";

    private String INITIALIZE_DEMO_DATA_LINK = "//a[contains(@href,'initializeData')]";


    public WemStartPage( TestSession session )
    {
        super( session );

    }

    public WemStartPage clickOnClearData()
    {
        if ( findElements( By.xpath( CLEAR_DATA_LINK ) ).size() == 0 )
        {
            throw new TestFrameworkException( "clear data link was not found: " + CLEAR_DATA_LINK );
        }
        findElements( By.xpath( CLEAR_DATA_LINK ) ).get( 0 ).click();
        return this;
    }

    public WemStartPage clickOnInitializeData()
    {
        if ( findElements( By.xpath( INITIALIZE_DEMO_DATA_LINK ) ).size() == 0 )
        {
            throw new TestFrameworkException( "initialize  data link was not found: " + INITIALIZE_DEMO_DATA_LINK );
        }
        findElements( By.xpath( INITIALIZE_DEMO_DATA_LINK ) ).get( 0 ).click();
        return this;
    }


}