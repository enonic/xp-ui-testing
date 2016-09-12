package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentVersionInfoView
    extends Application
{
    public static final String ACTIVE_VERSION = "This version is active";

    public static final String RESTORE_THIS = "Restore this version";

    private String ITEM_BY_ID = "//li[contains(@class,'content-version-item') and descendant::span[text()='%s']]";

    private final String VERSION_INFO_DIV = "//div[contains(@class,'version-info')]";

    private final String CLOSE_INFO_VIEW_BUTTON = ITEM_BY_ID + "//div[contains(@class,'close-version-info-button')]";

    private final String VERSION_INFO_DISPLAY_NAME = "//div[@class='version-info-display-name']/span[2]";

    private final String VERSION_INFO_ID = "//div[@class='version-info-version-id']/span[2]";

    private String RESTORE_BUTTON = ITEM_BY_ID + "//button";

    private String RESTORE_BUTTON_TEXT = ITEM_BY_ID + "//button/span";

    private String TIMESTAMP_VALUE = ITEM_BY_ID + "//div[@class='version-info-timestamp']/span[2]";

    private String OWNER_NAME_VALUE = ITEM_BY_ID + "//div[@class='viewer description']//h6[@class='main-name']";

    public ContentVersionInfoView( final TestSession session )
    {
        super( session );
    }

    public void doCloseVersionInfo( String versionId )
    {
        String closeXpath = String.format( CLOSE_INFO_VIEW_BUTTON, versionId );
        if ( !isElementDisplayed( closeXpath ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_close_version_info" );
            throw new TestFrameworkException( "'close info' button not displayed! " + versionId );
        }
        getDisplayedElement( By.xpath( closeXpath ) ).click();
        sleep( 200 );
    }

    public void doRestoreVersion( String versionId )
    {
        getDisplayedElement( By.xpath( String.format( RESTORE_BUTTON, versionId ) ) ).click();
        sleep( 700 );
    }

    public void isVersionInfoExpanded( String versionId )
    {
        String itemXpath = String.format( ITEM_BY_ID, versionId );
        if ( findElements( By.xpath( itemXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "version info item was not found! " + versionId );
        }
    }

    public String getId()
    {
        if ( !isElementDisplayed( VERSION_INFO_ID ) )
        {
            saveScreenshot( "err_det_panel_version_id" );
            throw new TestFrameworkException( "version id was not found! " );
        }
        return getDisplayedString( VERSION_INFO_ID );
    }

    public String getDisplayName()
    {
        if ( isElementDisplayed( VERSION_INFO_DISPLAY_NAME ) )
        {
            return getDisplayedString( VERSION_INFO_DISPLAY_NAME );
        }
        else
        {
            return "";
        }
    }

    public String getOwnerName( String versionId )
    {
        String ownerName = String.format( OWNER_NAME_VALUE, versionId );
        if ( !isElementDisplayed( ownerName ) )
        {
            throw new TestFrameworkException( "owner name was not found! " + versionId );
        }
        return getDisplayedString( ownerName );
    }

    public String getTimeStamp( String versionId )
    {
        String timestampXpath = String.format( TIMESTAMP_VALUE, versionId );
        if ( !isElementDisplayed( timestampXpath ) )
        {
            throw new TestFrameworkException( "timestamp was not found! " + versionId );
        }
        return getDisplayedString( timestampXpath );
    }

    public boolean isVersionInfoDisplayed()
    {
        return isElementDisplayed( VERSION_INFO_DIV );
    }

    public boolean isCloseButtonDisplayed( String versionId )
    {
        String closeXpath = String.format( CLOSE_INFO_VIEW_BUTTON, versionId );
        return isElementDisplayed( closeXpath );
    }

    public String getVersionStatus( String versionId )
    {
        return getDisplayedString( String.format( RESTORE_BUTTON_TEXT, versionId ) );
    }

    public String getContentStatus( String versionId )
    {
        String status = String.format( ITEM_BY_ID + "/div[contains(@class,'status')]", versionId );
        if ( !isElementDisplayed( status ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_version_info_status" );
            throw new TestFrameworkException( "status was not found!" );
        }
        return getDisplayedString( String.format( ITEM_BY_ID + "/div[contains(@class,'status')]", versionId ) );
    }
}
