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
    public static final String DETAILS_PANEL = "//div[contains(@id,'ContentBrowsePanel')]//div[contains(@id,'DetailsPanel')]";

    private final String VERSION_HISTORY_OPTION = "//div[text()='Version history']";

    private final String DETAILS_CONTAINER = DETAILS_PANEL + "//div[contains(@id,'details-container')]";

    public ContentDetailsPanel( final TestSession session )
    {
        super( session );
    }

    public String getContentDisplayName()
    {
        String contentDisplayName =
            findElements( By.xpath( DETAILS_PANEL + H6_DISPLAY_NAME ) ).stream().filter( WebElement::isDisplayed ).map(
                WebElement::getText ).findFirst().get();
        return contentDisplayName;
    }

    public boolean isPanelEmpty()
    {
        return findElements( By.xpath( DETAILS_CONTAINER ) ).stream().filter( WebElement::isDisplayed ).count() == 0;
    }

    public ContentItemVersionsPanel openVersionHistory()
    {

        selectVersionHistoryOptionItem();
        return new ContentItemVersionsPanel( getSession() );
    }

    public ContentItemVersionsPanel selectVersionHistoryOptionItem()
    {
        if ( findElements( By.xpath( VERSION_HISTORY_OPTION ) ).stream().filter( WebElement::isDisplayed ).count() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_history_opt" ) );
            throw new TestFrameworkException( "Version history option was not found!" );
        }
        findElements( By.xpath( VERSION_HISTORY_OPTION ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().click();
        sleep( 700 );
        return new ContentItemVersionsPanel( getSession() );
    }

    public boolean isOpened( String contentDisplayName )
    {
        return
            findElements( By.xpath( DETAILS_PANEL + String.format( NAMES_VIEW_WITH_DISPLAY_NAME, contentDisplayName ) ) ).stream().filter(
                WebElement::isDisplayed ).count() > 0;
    }

    public boolean isDisplayed()
    {
        return findElements( By.xpath( DETAILS_PANEL ) ).stream().filter( WebElement::isDisplayed ).count() == 1;
    }
}