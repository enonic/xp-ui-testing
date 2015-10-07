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
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.ContentVersion;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentItemVersionsPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'ContentItemVersionsPanel')]";

    private final String TAB_BAR = CONTAINER + "//ul[contains(@id,'api.ui.tab.TabBar')]";

    private final String ALL_VERSIONS_ITEM = TAB_BAR + "//li[contains(@id,'TabBarItem') and child::span[text()='All Versions']]";

    private final String ACTIVE_VERSIONS_ITEM = TAB_BAR + "//li[contains(@id,'TabBarItem') and child::span[text()='Active Versions']]";

    protected String MENU_ITEM_XPATH = "//ul[@class='menu']//li[contains(@id,'TabMenuItem') and child::span[text()='%s']]";

    protected final String TAB_MENU_BUTTON = "//div[contains(@id,'TabMenuButton') and child::span[text()='Version History']]";

    private final String ALL_CONTENT_VERSION_GRID = "//div[contains(@id,'AllContentVersionsTreeGrid')]";

    private final String ACTIVE_CONTENT_VERSION_GRID = "//div[contains(@id,'ActiveContentVersionsTreeGrid')]";

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

    public LinkedList<String> getActiveContentVersionsInfo()
    {
        List<WebElement> elements = findElements(
            By.xpath( ACTIVE_CONTENT_VERSION_GRID + CONTENT_VERSION_VIEWER + "//div[contains(@id,'NamesView')]//h6[@class='main-name']" ) );
        return elements.stream().map( WebElement::getText ).collect( Collectors.toCollection( LinkedList::new ) );
    }

    public LinkedList<ContentVersion> getActiveContentVersions()
    {
        LinkedList<String> info = getActiveContentVersionsInfo();
        String statusXpath = ACTIVE_CONTENT_VERSION_GRID +
            "//div[contains(@class,'slick-row')]//div[contains(@class,'status')]//span[contains(@class,'badge')]";
        List<String> statuses = findElements( By.xpath( statusXpath ) ).stream().map( WebElement::getText ).collect( Collectors.toList() );
        return buildContentVersions( info, statuses );
    }

    public LinkedList<ContentVersion> getAllContentVersions()
    {
        LinkedList<String> info = getAllContentVersionsInfo();
        String statusXpath = ALL_CONTENT_VERSION_GRID + "//div[contains(@class,'slick-row')]//div[contains(@class,'status')]";
        List<String> status = findElements( By.xpath( statusXpath ) ).stream().map( WebElement::getText ).collect( Collectors.toList() );
        return buildContentVersions( info, status );
    }

    private LinkedList<ContentVersion> buildContentVersions( List<String> infoStrings, List<String> statusStrings )
    {

        ContentVersion contentVersion = null;
        LinkedList<ContentVersion> list = new LinkedList<>();
        for ( int i = 0; i < infoStrings.size(); i++ )
        {
            contentVersion = ContentVersion.builder().info( infoStrings.get( i ) ).status( statusStrings.get( i ) ).build();
            list.add( contentVersion );
        }
        return list;
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

    public ContentItemVersionsPanel clickOnTabMenuButton()
    {
        tabMenuButton.click();
        return this;
    }

    public ContentBrowseItemsSelectionPanel openItemsSelectionPanel()
    {
        clickOnTabMenuButton();
        selectPreviewMenuItem();
        return new ContentBrowseItemsSelectionPanel( getSession() );
    }

    private void selectPreviewMenuItem()
    {
        String itemXpath = String.format( MENU_ITEM_XPATH, "Preview" );
        boolean result = waitUntilVisibleNoException( By.xpath( itemXpath ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "go_preview" ) );
            throw new TestFrameworkException( "the 'Version History' menu item was not found!" );
        }
        findElements( By.xpath( itemXpath ) ).get( 0 ).click();
    }

    public boolean isAllVersionsTabBarItemPresent()
    {
        return findElements( By.xpath( ALL_VERSIONS_ITEM ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isActiveVersionsTabBarItemPresent()
    {
        return findElements( By.xpath( ACTIVE_VERSIONS_ITEM ) ).stream().filter( WebElement::isDisplayed ).count() > 0;

    }

    public ContentItemVersionsPanel clickOnActiveVersionsButton()
    {
        WebElement activeVersionsButton =
            findElements( By.xpath( ACTIVE_VERSIONS_ITEM ) ).stream().filter( WebElement::isDisplayed ).findFirst().get();
        activeVersionsButton.click();
        sleep( 500 );
        boolean result = findElements( By.xpath( ACTIVE_CONTENT_VERSION_GRID + CONTENT_VERSION_VIEWER ) ).stream().filter(
            WebElement::isDisplayed ).count() > 0;
        //waitUntilVisibleNoException( By.xpath( ACTIVE_CONTENT_VERSION_GRID + CONTENT_VERSION_VIEWER ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            throw new TestFrameworkException( "Table with active version not showed" );
        }
        return this;
    }

    public ContentItemVersionsPanel clickOnAllVersionsButton()
    {
        WebElement allVersionsButton =
            findElements( By.xpath( ALL_VERSIONS_ITEM ) ).stream().filter( WebElement::isDisplayed ).findFirst().get();
        allVersionsButton.click();
        sleep( 500 );
        boolean result = findElements( By.xpath( ALL_CONTENT_VERSION_GRID ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
        if ( !result )
        {
            throw new TestFrameworkException( "Table with all versions not showed" );
        }
        return this;
    }
}
