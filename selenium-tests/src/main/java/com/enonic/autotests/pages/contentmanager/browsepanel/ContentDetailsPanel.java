package com.enonic.autotests.pages.contentmanager.browsepanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

public class ContentDetailsPanel
    extends Application
{
    public static final String CONTAINER_DIV = "//div[contains(@id,'ContentBrowsePanel')]//div[contains(@id,'DetailsPanel')]";

    private final String VERSION_HISTORY_OPTION = "//div[text()='Version history']";

    public ContentDetailsPanel( final TestSession session )
    {
        super( session );
    }

    public String getContentDisplayName()
    {
        String contentDisplayName =
            findElements( By.xpath( CONTAINER_DIV + H6_DISPLAY_NAME ) ).stream().filter( WebElement::isDisplayed ).map(
                WebElement::getText ).findFirst().get();
        return contentDisplayName;
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
            throw new TestFrameworkException( "Version history option was not found!" );
        }
        findElements( By.xpath( VERSION_HISTORY_OPTION ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().click();
        return new ContentItemVersionsPanel( getSession() );
    }

    public boolean isOpened( String contentDisplayName )
    {
        return findElements( By.xpath( CONTAINER_DIV + String.format( DIV_DISPLAY_NAME_VIEW, contentDisplayName ) ) ).stream().filter(
            WebElement::isDisplayed ).count() > 0;
    }
}