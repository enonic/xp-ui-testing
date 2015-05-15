package com.enonic.autotests.pages.contentmanager.browsepanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;

public class ContentItemVersionsPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'ContentItemVersionsPanel')]";

    private final String TAB_BAR = CONTAINER + "//ul[contains(@id,'api.ui.tab.TabBar')]";

    private final String ALL_VERSIONS_ITEM = TAB_BAR + "//li[contains(@id,'TabBarItem') and child::span[@title='All Versions']]";

    private final String ACTIVE_VERSIONS_ITEM = TAB_BAR + "//li[contains(@id,'TabBarItem') and child::span[@title='Active Versions']]";

    protected String MENU_ITEM_XPATH = "//ul[@class='menu']//li[contains(@id,'TabMenuItem') and child::span[@title='%s']]";

    protected final String TAB_MENU_BUTTON = "//div[contains(@id,'TabMenuButton') and child::span[@title='Version History']]";

    @FindBy(xpath = ALL_VERSIONS_ITEM)
    private WebElement allVersionsButton;


    @FindBy(xpath = ACTIVE_VERSIONS_ITEM)
    private WebElement activeVersionsButton;

    @FindBy(xpath = TAB_MENU_BUTTON)
    WebElement tabMenuButton;


    public ContentItemVersionsPanel( final TestSession session )
    {
        super( session );
    }

    public ContentItemVersionsPanel waitUntilLoaded()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL ) )
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
        return allVersionsButton.isDisplayed();
    }

    public boolean isActiveVersionsTabBarItemPresent()
    {
        return activeVersionsButton.isDisplayed();
    }
}
