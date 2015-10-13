package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.ContentVersion;

public class ContentItemVersionsPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'ContentItemVersionsPanel')]";

    private final String ALL_CONTENT_VERSION_GRID = "//div[contains(@id,'AllContentVersionsTreeGrid')]";

    protected final String TAB_MENU_BUTTON = "//div[contains(@id,'TabMenuButton') and child::span[text()='Version History']]";

    private final String CONTENT_VERSION_VIEWER = "//div[contains(@id,'api.content.ContentVersionViewer')]";

    @FindBy(xpath = TAB_MENU_BUTTON)
    WebElement tabMenuButton;

    public ContentItemVersionsPanel( final TestSession session )
    {
        super( session );
    }

    public LinkedList<String> getAllContentVersionsInfo()
    {
        List<WebElement> elements = findElements(
            By.xpath( ALL_CONTENT_VERSION_GRID + CONTENT_VERSION_VIEWER + "//div[contains(@id,'NamesView')]//h6[@class='main-name']" ) );
        return elements.stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect(
            Collectors.toCollection( LinkedList::new ) );
    }

    public long getRowsNumber()
    {
        return findElements( By.xpath( ALL_CONTENT_VERSION_GRID + GRID_ROW ) ).stream().filter( WebElement::isDisplayed ).count();
    }

    public LinkedList<ContentVersion> getAllContentVersions()
    {
        LinkedList<String> rowStyles =
            findElements( By.xpath( ALL_CONTENT_VERSION_GRID + GRID_ROW ) ).stream().filter( WebElement::isDisplayed ).map(
                e -> e.getAttribute( "style" ) ).map( e -> getTopValue( e ) ).collect( Collectors.toCollection( LinkedList::new ) );
        LinkedList<ContentVersion> contentVersions =
            rowStyles.stream().map( e -> getVersionHistoryInfo( e ) ).collect( Collectors.toCollection( LinkedList::new ) );
        return contentVersions;
    }

    String getTopValue( String style )
    {
        String result =
            style.substring( 0, style.indexOf( ":" ) + 1 ) + style.substring( style.indexOf( ":" ) + 1, style.indexOf( ";" ) ).trim();
        return result;
    }

    private ContentVersion getVersionHistoryInfo( String rowStyle )
    {
        String modifierNameXpath = String.format( ALL_CONTENT_VERSION_GRID + SLICK_ROW_WITH_STYLE + H6_DISPLAY_NAME, rowStyle );
        String statusXpath = String.format( ALL_CONTENT_VERSION_GRID + SLICK_ROW_WITH_STYLE + "//p[contains(@class,'badge ')]", rowStyle );
        String whenModifiedXpath =
            String.format( ALL_CONTENT_VERSION_GRID + SLICK_ROW_WITH_STYLE + "//p[@class='sub-name']//span", rowStyle );

        String modifierName = findElement( By.xpath( modifierNameXpath ) ).getText();
        String status = "";
        if ( isElementDisplayed( statusXpath ) )
        {
            status = findElement( By.xpath( statusXpath ) ).getText();
        }

        String whenModified = findElement( By.xpath( whenModifiedXpath ) ).getText();
        return ContentVersion.builder().modifier( modifierName ).status( status ).modified( whenModified ).build();
    }

    public ContentItemVersionsPanel isLoaded()
    {
        if ( findElements( By.xpath( CONTAINER ) ).stream().filter( WebElement::isDisplayed ).count() == 0 )
        {
            throw new TestFrameworkException( "ContentItemVersionsPanel was not loaded!" );
        }
        return this;
    }

    public boolean waitUntilPanelNotVisible()
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL );
    }

}
