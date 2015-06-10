package com.enonic.autotests.pages;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentItemVersionsPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class BrowseItemsSelectionPanel
    extends Application
{
    protected String CONTAINER;

    protected String ITEMS_SELECTION_PANEL;

    protected String ALL_SELECTED_ITEMS;

    protected String SELECTED_ITEM_NAME = "//p[@class='sub-name']";

    protected String SELECTED_ITEM_DISPLAY_NAME = "//h6[@class='main-name']";

    protected final String TAB_MENU_BUTTON =
        "//div[contains(@id,'TabMenuButton') and (child::span[@title='Preview'] or child::span[@title='Version History'])]";

    protected String MENU_ITEM_XPATH = "//ul[@class='menu']//li[contains(@id,'TabMenuItem') and child::span[text()='%s']]";

    @FindBy(xpath = TAB_MENU_BUTTON)
    WebElement tabMenuButton;

    public BrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }

    public BrowseItemsSelectionPanel clickOnTabMenuButton()
    {
        tabMenuButton.click();
        return this;
    }

    boolean isPresentMenuItem( String itemName )
    {
        String itemXpath = String.format( MENU_ITEM_XPATH, itemName );
        return waitUntilVisibleNoException( By.xpath( itemXpath ), Application.EXPLICIT_NORMAL );
    }

    public boolean waitTabMenuButtonVisible()
    {
        return waitUntilVisibleNoException( By.xpath( TAB_MENU_BUTTON ), Application.EXPLICIT_NORMAL );
    }

    public ContentItemVersionsPanel openVersionHistory()
    {
        clickOnTabMenuButton();
        selectVersionHistoryMenuItem();
        return new ContentItemVersionsPanel( getSession() );
    }

    private void selectVersionHistoryMenuItem()
    {
        String itemXpath = String.format( MENU_ITEM_XPATH, "Version History" );
        boolean result = waitUntilVisibleNoException( By.xpath( itemXpath ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "version_hist" ) );
            throw new TestFrameworkException( "the 'Version History' menu item was not found!" );
        }
        findElements( By.xpath( itemXpath ) ).get( 0 ).click();
        sleep( 500 );
    }

    public int getSelectedItemCount()
    {
        List<WebElement> divElements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS ) );
        return divElements.size();
    }

    public List<String> getSelectedItemNames()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
        }
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + SELECTED_ITEM_NAME ) );
        return h6Elements.stream().map( WebElement::getText ).collect( Collectors.toList() );

    }

    public List<String> getSelectedItemDisplayNames()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
        }
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + SELECTED_ITEM_DISPLAY_NAME ) );
        return h6Elements.stream().map( WebElement::getText ).collect( Collectors.toList() );

    }

    public boolean isVisible()
    {
        List<WebElement> elements = findElements( By.xpath( ITEMS_SELECTION_PANEL ) );
        if ( elements.size() == 0 )
        {
            return false;
        }
        else
        {
            return !getAttribute( elements.get( 0 ), "style", 1 ).contains( "display: none" );
        }

    }
}
