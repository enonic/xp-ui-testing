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
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ApplicationBrowsePanel
    extends BrowsePanel
{
    public final String START_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Start']]";

    public final String STOP_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Stop']]";

    public final String INSTALL_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Install']]";

    public final String UNINSTALL_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Uninstall']]";

    private ApplicationBrowseItemsSelectionPanel itemsSelectionPanel;

    private ApplicationItemStatisticsPanel itemStatisticsPanel;

    @FindBy(xpath = START_BUTTON)
    private WebElement startButton;

    @FindBy(xpath = STOP_BUTTON)
    private WebElement stopButton;

    @FindBy(xpath = INSTALL_BUTTON)
    private WebElement installButton;

    @FindBy(xpath = UNINSTALL_BUTTON)
    private WebElement uninstallButton;

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

    public ApplicationBrowsePanel clickOnToolbarInstall()
    {
        installButton.click();
        sleep( 300 );
        return this;
    }

    public UninstallApplicationDialog clickOnToolbarUninstall()
    {
        uninstallButton.click();
        sleep( 300 );
        return new UninstallApplicationDialog( getSession() );
    }


    public ApplicationBrowsePanel clickOnToolbarStart()
    {
        startButton.click();
        sleep( 3000 );
        return this;
    }

    public boolean isApplicationLocal( String appName )
    {
        boolean result;
        String iconLocal = String.format( SLICK_ROW_BY_NAME, appName ) + "//div[@title='Local application']";
        result = isElementDisplayed( iconLocal );
        return result;
    }

    @Override
    public WizardPanel clickToolbarEdit()
    {
        throw new TestFrameworkException( "not implemented for Application app" );
    }

    public boolean waitAndCheckIsButtonEnabled( String buttonXpath )
    {
        return waitUntilElementEnabledNoException( By.xpath( buttonXpath ), 2 );
    }

    public boolean isStartButtonEnabled()
    {
        return startButton.isEnabled();
    }

    public boolean isUninstallButtonEnabled()
    {
        return uninstallButton.isEnabled();
    }

    public boolean isInstallButtonEnabled()
    {
        return installButton.isEnabled();
    }

    public boolean isStopButtonEnabled()
    {
        return stopButton.isEnabled();
    }

    public String getApplicationStatus( String appName )
    {
        String stateCell = String.format( SLICK_ROW_BY_NAME, appName ) + "//div[contains(@class,'state')]";
        if ( findElements( By.xpath( stateCell ) ).size() == 0 )
        {
            throw new TestFrameworkException( "state was not found in the table ! application name is " + appName );
        }
        getLogger().info( "status of module is : " + findElements( By.xpath( stateCell ) ).get( 0 ).getText() );
        return findElements( By.xpath( stateCell ) ).get( 0 ).getText();
    }

    public boolean waitApplicationStatus( String appName, String state )
    {
        String stateCell = String.format( SLICK_ROW_BY_NAME, appName ) + "//div[contains(@class,'state')]";
        if ( !isElementDisplayed( stateCell ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_app_status" ) );
            throw new TestFrameworkException( "state was not found in the table ! application name is " + appName );
        }
        String expectedState =
            String.format( SLICK_ROW_BY_NAME, appName ) + String.format( "//div[contains(@class,'state')]//div[ text()='%s']", state );

        boolean result = waitUntilVisibleNoException( By.xpath( expectedState ), Application.EXPLICIT_NORMAL );
        getLogger().info( "status of module is : " + findElement( By.xpath( stateCell ) ).getText() );
        return result;
    }

    public ApplicationBrowseItemsSelectionPanel getItemSelectionPanel()
    {
        if ( itemsSelectionPanel == null )
        {
            itemsSelectionPanel = new ApplicationBrowseItemsSelectionPanel( getSession() );
        }
        return itemsSelectionPanel;
    }

    public ApplicationItemStatisticsPanel getItemStatisticPanel()
    {
        if ( itemStatisticsPanel == null )
        {
            itemStatisticsPanel = new ApplicationItemStatisticsPanel( getSession() );
        }
        return itemStatisticsPanel;
    }

    public ApplicationBrowsePanel deSelectAppInTable( String moduleName )
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
        throw new TestFrameworkException( "Delete button not present in Application's toolbar!" );
    }
}