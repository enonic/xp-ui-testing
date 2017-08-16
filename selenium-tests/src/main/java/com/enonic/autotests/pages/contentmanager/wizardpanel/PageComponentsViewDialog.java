package com.enonic.autotests.pages.contentmanager.wizardpanel;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.PageComponent;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PageComponentsViewDialog
    extends Application
{
    public static final String DIALOG_HEADER = "Components";

    public final String DIALOG_CONTAINER = "//div[contains(@id,'PageComponentsView')]";

    public final String COMPONENT_VIEWER = "//div[contains(@id,'PageComponentsItemViewer')]";

    public final String COMPONENTS_GRID = "//div[contains(@id,'PageComponentsTreeGrid')]";

    private String COMPONENT_ITEM = DIALOG_CONTAINER + COMPONENTS_GRID +
        "//div[contains(@id,'PageComponentsItemViewer') and descendant::h6[contains(@class,'main-name')  and text()='%s']]";

    private String TOGGLE_ICON = DIALOG_CONTAINER + COMPONENTS_GRID +
        "//div[contains(@class,'slick-cell') and descendant::h6[contains(@class,'main-name')  and text()='%s']]/span[@class='toggle icon']";

    private String LAYOUT_BY_DISPLAY_NAME = DIALOG_CONTAINER +
        "//div[contains(@id,'PageComponentsItemViewer') and descendant::div[contains(@class,'icon-layout')]]" + NAMES_VIEW_BY_DISPLAY_NAME;

    private String FRAGMENT_BY_DISPLAY_NAME = DIALOG_CONTAINER +
        "//div[contains(@id,'PageComponentsItemViewer') and descendant::div[contains(@class,'icon-fragment')]]" +
        NAMES_VIEW_BY_DISPLAY_NAME;

    private String FRAGMENT_DISPLAY_NAMES = DIALOG_CONTAINER +
        "//div[contains(@id,'PageComponentsItemViewer') and descendant::div[contains(@class,'icon-fragment')]]" + H6_DISPLAY_NAME;

    public final String CLOSE_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'CloseButton')]";

    private final String SLICK_VIEW_PORT = "//div[@class='slick-viewport']";

    private String CONTEXT_MENU_ITEM = "//dl[contains(@id,'TreeContextMenu')]//*[contains(@id,'TreeMenuItem') and text()='%s']";

    @FindBy(xpath = CLOSE_BUTTON)
    WebElement closeButton;

    public PageComponentsViewDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public List<String> getFragmentDisplayNames()
    {
        return getDisplayedStrings( By.xpath( FRAGMENT_DISPLAY_NAMES ) );
    }

    public ItemViewContextMenu showItemViewContextMenu()
    {
        Actions builder = new Actions( getDriver() );
        builder.click( getDisplayedElement( By.xpath( COMPONENTS_GRID ) ) ).build().perform();
        sleep( 500 );
        return new ItemViewContextMenu( getSession() );
    }

    public PageComponentsViewDialog openMenu( String componentName )
    {
        String menuButton = String.format( COMPONENT_ITEM + "/../..//div[@class='menu-icon']", componentName );
        if ( !isElementDisplayed( menuButton ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-comp_view_menu" ) );
            throw new TestFrameworkException( "menu button was not found for  " + componentName );
        }
        getDisplayedElement( By.xpath( menuButton ) ).click();
        sleep( 400 );
        return this;
    }

    /**
     * Navigates in the Context Menu and selects a menu item
     * This method should be called after the 'openMenu'
     *
     * @param items item or path to the menu item
     * @return {@link PageComponentsViewDialog instance}
     */
    public PageComponentsViewDialog selectMenuItem( String... items )
    {
        for ( int i = 0; i < items.length; i++ )
        {
            if ( !isElementDisplayed( String.format( CONTEXT_MENU_ITEM, items[i] ) ) )
            {
                saveScreenshot( "err_" + items[i] );
                throw new TestFrameworkException( "" + items[i] );
            }
            getDisplayedElement( By.xpath( String.format( CONTEXT_MENU_ITEM, items[i] ) ) ).click();
            sleep( 500 );
        }
        return this;
    }

    public boolean isMenuItemPresent( String menuItem )
    {
        return isElementDisplayed( String.format( CONTEXT_MENU_ITEM, menuItem ) );
    }

    public String getTextFromHeader()
    {
        return getDisplayedElement( By.xpath( DIALOG_CONTAINER + "//h2[@class='header']" ) ).getText();
    }

    public String getContentName()
    {
        return getDisplayedElement( By.xpath( DIALOG_CONTAINER + H6_MAIN_NAME ) ).getText();
    }

    public PageComponentsViewDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-page-comp-dialog" ) );
            throw new TestFrameworkException( "Page Components Dialog was not opened!" );
        }
        return this;
    }

    public void doCloseDialog()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CLOSE_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "err_close-components-view" );
            throw new TestFrameworkException( "close button was not found!" );
        }
        closeButton.click();
        sleep( 500 );
    }

    public boolean isCloseButtonPresent()
    {
        return waitUntilVisibleNoException( By.xpath( CLOSE_BUTTON ), Application.EXPLICIT_NORMAL );
    }

    public List<PageComponent> getPageComponents()
    {
        List<PageComponent> result = new ArrayList<>();
        List<String> names = getDisplayedStrings( By.xpath( DIALOG_CONTAINER + SLICK_VIEW_PORT + COMPONENT_VIEWER + H6_MAIN_NAME ) );
        List<String> types = getDisplayedStrings( By.xpath( DIALOG_CONTAINER + SLICK_VIEW_PORT + COMPONENT_VIEWER + P_NAME ) );
        for ( int i = 0; i < names.size(); i++ )
        {
            result.add( PageComponent.builder().name( names.get( i ) ).type( types.get( i ) ).build() );
        }
        return result;
    }

    public PageComponentsViewDialog swapComponents( String sourceName, String targetName )
    {
        String sourceItem = String.format( COMPONENT_ITEM, sourceName );
        String targetItem = String.format( COMPONENT_ITEM, targetName );
        if ( !isElementDisplayed( sourceItem ) || !isElementDisplayed( targetItem ) )
        {
            throw new TestFrameworkException(
                "PageComponentsView: swap is failed. Items were not found: " + sourceName + " " + targetName );
        }
        WebElement source = findElement( By.xpath( sourceItem ) );
        WebElement target = findElement( By.xpath( targetItem ) );
        TestUtils.dragAndDrop( getDriver(), source, target );
        sleep( 1000 );
        return this;
    }

    public PageComponentsViewDialog clickOnLayout( String layoutDisplayName )
    {
        String xpath = String.format( LAYOUT_BY_DISPLAY_NAME, layoutDisplayName );
        if ( !isElementDisplayed( xpath ) )
        {
            saveScreenshot( "layout was not found" );
            throw new TestFrameworkException( "layout was not found: " + layoutDisplayName );
        }
        getDisplayedElement( By.xpath( xpath ) ).click();
        return this;
    }

    public PageComponentsViewDialog clickOnFragment( String fragmentDisplayName )
    {
        String xpath = String.format( FRAGMENT_BY_DISPLAY_NAME, fragmentDisplayName );
        if ( !isElementDisplayed( xpath ) )
        {
            saveScreenshot( "fragment was not found" );
            throw new TestFrameworkException( "fragment was not found: " + fragmentDisplayName );
        }
        getDisplayedElement( By.xpath( xpath ) ).click();
        return this;
    }

    public PageComponentsViewDialog clickOnComponent( String itemName )
    {
        String xpath = String.format( DIALOG_CONTAINER + H6_DISPLAY_NAME, itemName );
        if ( !isElementDisplayed( xpath ) )
        {
            saveScreenshot( "component was not found" );
            throw new TestFrameworkException( "component was not found: " + itemName );
        }
        getDisplayedElement( By.xpath( xpath ) ).click();
        return this;
    }
}
