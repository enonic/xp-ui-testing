package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created  on 27.10.2016.
 * Dialog appears when a component was selected and 'right-click' done
 */
public class LiveEditComponentContextMenu
    extends Application
{
    private final String MENU_DIV = "//div[contains(@id,'api.liveedit.ItemViewContextMenu')]";

    private final String MENU_TITLE = MENU_DIV + "//div[contains(@id,'ComponentViewContextMenuTitle')]";

    private final String MENU_TITLE_TEXT = MENU_DIV + "//div[contains(@id,'ComponentViewContextMenuTitle')]" + H6_DISPLAY_NAME;

    private final String EDIT_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Edit']";

    private final String INSERT_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Insert']";

    private final String RESET_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Reset']";

    private final String REMOVE_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Remove']";

    private final String DUPLICATE_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Duplicate']";

    private final String SELECT_PARENT_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Select Parent']";

    private final String CREATE_FRAGMENT_MENU_ITEM = MENU_DIV + "//dt[contains(@id,'TreeMenuItem') and text()='Create Fragment']";


    public LiveEditComponentContextMenu( final TestSession session )
    {
        super( session );
    }

    public LiveEditComponentContextMenu waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( MENU_DIV ), EXPLICIT_NORMAL ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_show_context_menu" ) );
            throw new TestFrameworkException( "ComponentContext Menu Dialog was not opened!" );
        }
        return this;
    }

    public String getTitle()
    {
        return getDisplayedString( MENU_TITLE_TEXT );
    }

    public boolean isEditItemPresent()
    {
        return isElementDisplayed( EDIT_MENU_ITEM );
    }

    public void clickOnEdit()
    {
        getDisplayedElement( By.xpath( EDIT_MENU_ITEM ) ).click();
        sleep( 300 );
    }
}
