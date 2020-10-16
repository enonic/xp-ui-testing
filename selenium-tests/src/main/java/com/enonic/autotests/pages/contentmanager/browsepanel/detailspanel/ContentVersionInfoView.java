package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentVersionInfoView
    extends Application
{

    private String EXPANDED_VERSION_ITEM = "//li[contains(@class,'version-list-item') and contains(@class,'expanded')]";

    private String VERSION_ITEM = "//ul[contains(@id,'VersionHistoryList')]//li[contains(@class,'version-list-item')]";


    private String REVERT_BUTTON = EXPANDED_VERSION_ITEM + "//button[child::span[text()='Revert']]";

    private String OWNER_NAME_VALUE = VERSION_ITEM + "//div[contains(@id,'ContentVersionViewer')]" + P_NAME;

    public ContentVersionInfoView( final TestSession session )
    {
        super( session );
    }

    public void doRestoreVersion()
    {
        waitUntilVisibleNoException( By.xpath( REVERT_BUTTON ), EXPLICIT_NORMAL );
        getDisplayedElement( By.xpath( REVERT_BUTTON ) ).click();
        sleep( 1000 );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }

    public boolean isRevertButtonDisplayed()
    {
        return isElementDisplayed( REVERT_BUTTON );
    }

    public String getOwnerName( int index )
    {
        List<WebElement> allOwners = findElements( By.xpath( OWNER_NAME_VALUE ) );
        if ( allOwners.size() == 0 )
        {
            throw new TestFrameworkException( "owner name was not found! " + index );
        }
        return allOwners.get( index ).getText();
    }

    //New, Published....
    public String getContentStatus( int index )
    {
        List<WebElement> items = findElements( By.xpath( VERSION_ITEM ) );
        List<WebElement> status = items.get( index ).findElements( By.xpath( "./div[contains(@class,'status')]" ) );
        if ( status.size() == 0 )
        {
            return "";
        }
        else
        {
            return status.get( 0 ).getText();
        }
    }
}
