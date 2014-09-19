package com.enonic.autotests.pages.versiontest;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;


public class VersiontestPage
    extends Page
{
    private String ENONIC_RESET_BUTTON = "//tr[child::td[contains(.,'enonic-wem-selenium')]]//button[contains(@class,'js-button-reset')]";


    public VersiontestPage( TestSession session )
    {
        super( session );

    }

    public VersiontestPage clickResetWem()
    {
        findElements( By.xpath( ENONIC_RESET_BUTTON ) ).get( 0 ).click();
        return this;
    }

    public boolean waitUntilResetWemButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( ENONIC_RESET_BUTTON ), 10 );
    }
}
