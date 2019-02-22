package com.enonic.autotests.pages.form.liveedit;


import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInspectionPanel;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContextWindow
    extends Application
{
    private final String DIV_CONTEXT_WINDOW = "//div[contains(@id,'ContextWindow') and not(contains(@class,'hidden'))]";

    private final String EMULATOR_TAB_LINK = DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, "Emulator" );

    private final String INSERT_TAB_LINK = DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, "Insert" );

    private final String INSPECT_TAB_LINK = DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, "Inspect" );

    private final String INSERTABLES_GRID = "//div[contains(@id,'InsertablesGrid')]";

    private final String GRID_ITEM =
        INSERTABLES_GRID + "//div[contains(@class,'grid-row') and descendant::div[@data-portal-component-type ='%s']]";

    private PageInspectionPanel inspectionPanel;

    ContentWizardPanel contentWizardPanel;

    private final String TOOLBAR_DIV = "//div[contains(@id,'ContentWizardToolbar')]";

    @FindBy(xpath = "//li[contains(@class,'tab-bar-item') and @title= 'Insert']")
    private WebElement insertToolbarItem;

    public ContextWindow( final TestSession session )
    {
        super( session );
        contentWizardPanel = new ContentWizardPanel( session );
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waitUntilWindowLoaded( long timeout )
    {
        boolean isPageLoaded = waitAndFind( By.xpath( DIV_CONTEXT_WINDOW ), timeout );
        if ( !isPageLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context_wind_load" ) );
            throw new TestFrameworkException( "LIVE EDIT:  ContextWindow was not loaded!" );
        }
    }

    public boolean isContextWindowPresent()
    {
        return waitUntilVisibleNoException( By.xpath( DIV_CONTEXT_WINDOW ), Application.EXPLICIT_NORMAL );
    }

    public ContextWindow clickOnInsertLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( INSERT_TAB_LINK ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_insert_link" );
            throw new TestFrameworkException( "Insert link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( INSERT_TAB_LINK ) ).click();
        sleep( 500 );
        return this;
    }

    public ContextWindow clickOnEmulatorLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( EMULATOR_TAB_LINK ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_emulator_link" );
            throw new TestFrameworkException( "Emulator link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( EMULATOR_TAB_LINK ) ).click();
        sleep( 500 );
        return this;
    }

    public ContextWindow clickOnInspectLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( INSPECT_TAB_LINK ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_inspect_link" );
            throw new TestFrameworkException( "'inspect' link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( INSPECT_TAB_LINK ) ).click();
        sleep( 500 );
        return this;
    }

    public ContextWindow clickOnTabBarItem(String name)
    {
        String selector =  DIV_CONTEXT_WINDOW + String.format( TAB_BAR_ITEM, name );
        if ( !waitUntilVisibleNoException( By.xpath( selector ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_tab_item_link" );
            throw new TestFrameworkException( name + " link was not found on the ContextWindow!" );
        }
        getDisplayedElement( By.xpath( selector ) ).click();
        sleep( 500 );
        return this;
    }


    public PageInspectionPanel getInspectionPanel()
    {
        if ( inspectionPanel == null )
        {
            return new PageInspectionPanel( getSession() );
        }
        return inspectionPanel;
    }
}