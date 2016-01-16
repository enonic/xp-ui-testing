package com.enonic.autotests.pages.contentmanager.browsepanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentDetailsPanel
    extends Application
{
    public static final String DETAILS_PANEL =
        "//div[contains(@id,'ContentBrowsePanel')]//div[contains(@id,'app.view.detail.DetailsPanel')]";

    private final String VERSION_HISTORY_OPTION = "//div[text()='Version history']";

    private final String CONTENT_INFO_OPTION = DETAILS_PANEL + "//div[contains(@id,'InfoWidgetToggleButton')]";

    private final String DETAILS_CONTAINER = DETAILS_PANEL + "//div[contains(@id,'details-container')]";

    private UserAccessWidgetItemView userAccessWidgetItemView;

    public ContentDetailsPanel( final TestSession session )
    {
        super( session );
    }

    public UserAccessWidgetItemView getUserAccessWidgetItemView()
    {
        if ( userAccessWidgetItemView == null )
        {
            return new UserAccessWidgetItemView( getSession() );
        }
        return userAccessWidgetItemView;
    }

    public boolean isPanelEmpty()
    {
        return findElements( By.xpath( DETAILS_CONTAINER ) ).stream().filter( WebElement::isDisplayed ).count() == 0;
    }

    public ContentItemVersionsPanel openVersionHistory()
    {
        if ( !isElementDisplayed( VERSION_HISTORY_OPTION ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_history_opt" ) );
            throw new TestFrameworkException( "Version history option was not found!" );
        }
        getDisplayedElement( By.xpath( VERSION_HISTORY_OPTION ) ).click();
        sleep( 700 );
        return new ContentItemVersionsPanel( getSession() );
    }

    public ContentInfoWidget openInfoWidget()
    {
        if ( !isElementDisplayed( CONTENT_INFO_OPTION ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_info_opt" ) );
            throw new TestFrameworkException( "Info widget was not opened!" );
        }
        getDisplayedElement( By.xpath( CONTENT_INFO_OPTION ) ).click();
        sleep( 700 );
        return new ContentInfoWidget( getSession() );
    }

    public boolean isOpened( String contentDisplayName )
    {
        return isElementDisplayed( DETAILS_PANEL + String.format( NAMES_VIEW_BY_DISPLAY_NAME, contentDisplayName ) );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DETAILS_PANEL );
    }

    public String getContentDisplayName()
    {
        return getDisplayedString( DETAILS_PANEL + H6_DISPLAY_NAME );
    }
}