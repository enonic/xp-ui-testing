package com.enonic.autotests.pages.form.liveedit;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;


public class ItemViewContextMenu
    extends Application
{
    public final String CONTAINER_DIV = "//div[contains(@id,'ItemViewContextMenu')]";

    private final String CUSTOMIZE_MENU_ITEM = CONTAINER_DIV + "//dl//dt[text()='Customize Page']";

    public ItemViewContextMenu( TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER_DIV );
    }

    public void clickOnCustomizeMenuItem()
    {
        if ( !isElementDisplayed( CUSTOMIZE_MENU_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "unlock" ) );
            throw new TestFrameworkException( "Unlock LiveEdit: Customize menu-item not displayed!" );
        }
        getDisplayedElement( By.xpath( CUSTOMIZE_MENU_ITEM ) ).click();
    }

    public void waitForMenuOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER_DIV ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_load_item_view_context_menu" );
            throw new TestFrameworkException( "Item view context menu not opened!" );
        }
    }
}
