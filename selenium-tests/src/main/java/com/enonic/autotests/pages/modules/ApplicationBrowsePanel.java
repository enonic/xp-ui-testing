package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.pages.BaseDeleteDialog;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.WizardPanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ApplicationBrowsePanel
    extends BrowsePanel
{
    public final String START_BUTTON =
        BASE_TOOLBAR_XPATH + "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Start']]";

    public final String STOP_BUTTON = BASE_TOOLBAR_XPATH + "/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Stop']]";

    private ApplicationBrowseItemsSelectionPanel itemsSelectionPanel;


    @FindBy(xpath = START_BUTTON)
    private WebElement startButton;

    @FindBy(xpath = STOP_BUTTON)
    private WebElement stopButton;

    /**
     * The Constructor
     *
     * @param session
     */
    public ApplicationBrowsePanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public BrowsePanel goToAppHome()
    {
        return null;
    }

    @Override
    public <T extends Application> T clickToolbarNew()
    {
        throw new TestFrameworkException( "method not exists for module app" );
    }

    @Override
    public BaseBrowseFilterPanel getFilterPanel()
    {
        return null;
    }

    public ApplicationBrowsePanel clickOnToolbarStop()
    {
        stopButton.click();
        sleep( 1500 );
        return this;
    }


    public ApplicationBrowsePanel clickOnToolbarStart()
    {
        startButton.click();
        sleep( 3000 );
        return this;
    }

    @Override
    public WizardPanel clickToolbarEdit()
    {
        throw new TestFrameworkException( "not implemented for Modules app" );
    }

    public boolean waitAndCheckIsButtonEnabled( String buttonXpath )
    {
        return waitUntilElementEnabledNoException( By.xpath( buttonXpath ), 2 );
    }

    public boolean isStartButtonEnabled()
    {
        return startButton.isEnabled();
    }

    public boolean isStopButtonEnabled()
    {
        return stopButton.isEnabled();
    }

    public String getModuleStatus( String moduleName )
    {
        String stateCell = String.format( GRID_ROW, moduleName ) + "//div[contains(@class,'state')]";
        if ( findElements( By.xpath( stateCell ) ).size() == 0 )
        {
            throw new TestFrameworkException( "state was not found in the table ! module name is " + moduleName );
        }
        getLogger().info( "status of module is : " + findElements( By.xpath( stateCell ) ).get( 0 ).getText() );
        return findElements( By.xpath( stateCell ) ).get( 0 ).getText();
    }

    public ApplicationBrowseItemsSelectionPanel getItemSelectionPanel()
    {
        if ( itemsSelectionPanel == null )
        {
            itemsSelectionPanel = new ApplicationBrowseItemsSelectionPanel( getSession() );
        }
        return itemsSelectionPanel;
    }

    public ApplicationBrowsePanel deSelectModuleInTable( String moduleName )
    {

        if ( isRowSelected( moduleName ) )
        {
            clickCheckboxAndSelectRow( moduleName );
        }
        return this;
    }

    @Override
    public BaseDeleteDialog clickToolbarDelete()
    {
        throw new TestFrameworkException( "Delete button not present in Modules!" );
    }
}