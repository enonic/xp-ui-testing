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

    public final String COMPONENTS_GRID = "//div[contains(@id,'PageComponentsTreeGrid')]";

    private String COMPONENT_ITEM =
        DIALOG_CONTAINER + "//div[contains(@class,'slick-row') and descendant::h6[@class='main-name'  and text()='%s']]";

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

    public ItemViewContextMenu showItemViewContextMenu()
    {
        Actions builder = new Actions( getDriver() );
        builder.click( getDisplayedElement( By.xpath( COMPONENTS_GRID ) ) ).build().perform();
        sleep( 500 );
        return new ItemViewContextMenu( getSession() );
    }

    public PageComponentsViewDialog openMenu( String componentName )
    {
        String menuButton = String.format( COMPONENTS_GRID +
                                               "//div[contains(@class,'slick-row') and descendant::h6[@class='main-name'  and text()='%s']]//div[@class='menu-icon']",
                                           componentName );

        if ( !isElementDisplayed( menuButton ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err-comp_view_menu" ) );
            throw new TestFrameworkException( "menu button was not found for  " + componentName );
        }
        getDisplayedElement( By.xpath( menuButton ) ).click();
        sleep( 400 );
        return this;
    }

    public PageComponentsViewDialog selectMenuItem( String... items )
    {
        for ( int i = 0; i < items.length; i++ )
        {
            if ( !isElementDisplayed( String.format( CONTEXT_MENU_ITEM, items[i] ) ) )
            {
                TestUtils.saveScreenshot( getSession(), "err_" + items[i] );
                throw new TestFrameworkException( "" + items[i] );
            }
            getDisplayedElement( By.xpath( String.format( CONTEXT_MENU_ITEM, items[i] ) ) ).click();
            sleep( 500 );
        }
        return this;
    }

    public String getTextFromHeader()
    {
        return getDisplayedElement( By.xpath( DIALOG_CONTAINER + "//h2[@class='header']" ) ).getText();
    }

    public String getContentName()
    {
        return getDisplayedElement( By.xpath( DIALOG_CONTAINER + "//h6[@class='main-name']" ) ).getText();
    }

    public PageComponentsViewDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err-page-comp-dialog" ) );
            throw new TestFrameworkException( "Page Components Dialog was not opened!" );
        }
        return this;
    }

    public void doCloseDialog()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CLOSE_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_close-components-view" );
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
        List<String> names = getDisplayedStrings( By.xpath( DIALOG_CONTAINER + SLICK_VIEW_PORT +
                                                                "//div[contains(@id,'PageComponentsItemViewer')]//h6[@class='main-name']" ) );
        List<String> types = getDisplayedStrings( By.xpath( DIALOG_CONTAINER + SLICK_VIEW_PORT +
                                                                "//div[contains(@id,'PageComponentsItemViewer')]//p[@class='sub-name']" ) );
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
                "PageComponentsViewDialog: drag and drop failed. items were not found: " + sourceName + " " + targetName );
        }
        WebElement source = findElement( By.xpath( sourceItem ) );
        WebElement target = findElement( By.xpath( targetItem ) );
        dragAndDrop( source, target );
        sleep( 1000 );
        return this;
    }
}
