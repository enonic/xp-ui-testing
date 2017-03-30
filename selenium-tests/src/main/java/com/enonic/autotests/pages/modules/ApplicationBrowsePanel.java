package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ApplicationBrowsePanel
    extends BrowsePanel
{
    public final String START_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Start']]";

    public final String STOP_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Stop']]";

    public final String INSTALL_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Install']]";

    public final String UNINSTALL_BUTTON = BROWSE_TOOLBAR_XPATH + "/*[contains(@id, 'ActionButton') and child::span[text()='Uninstall']]";

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
    public BrowsePanel pressAppHomeButton()
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
        boolean isClickable = waitUntilClickableNoException( By.xpath( STOP_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_stop_button" );
            throw new TestFrameworkException( "button 'stop' is not clickable!" );
        }
        stopButton.click();
        sleep( 1500 );
        return this;
    }

    public ApplicationBrowsePanel clickOnToolbarInstall()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( INSTALL_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_insatll_button" );
            throw new TestFrameworkException( "button 'install' is not clickable!" );
        }
        installButton.click();
        sleep( 300 );
        return this;
    }

    public UninstallApplicationDialog clickOnToolbarUninstall()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( UNINSTALL_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_uninstall_button" );
            throw new TestFrameworkException( "button 'uninstall' is not clickable!" );
        }
        uninstallButton.click();
        sleep( 300 );
        return new UninstallApplicationDialog( getSession() );
    }


    public ApplicationBrowsePanel clickOnToolbarStart()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( START_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_start_button" );
            throw new TestFrameworkException( "button 'start' is not clickable!" );
        }
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

    public boolean isApplicationByDisplayNameLocal( String appDisplayName )
    {
        String iconLocal = String.format( SLICK_ROW_BY_DISPLAY_NAME, appDisplayName ) + "//div[@title='Local application']";
        return isElementDisplayed( iconLocal );
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
        getLogger().info( "status of module is : " + findElement( By.xpath( stateCell ) ).getText() );
        return findElement( By.xpath( stateCell ) ).getText();
    }

    public String findAppByDisplayNameAndGetStatus( String appDisplayName )
    {
        String stateCell = String.format( SLICK_ROW_BY_DISPLAY_NAME, appDisplayName ) + "//div[contains(@class,'state')]";
        if ( findElements( By.xpath( stateCell ) ).size() == 0 )
        {
            throw new TestFrameworkException( "state was not found in the table ! application name is " + appDisplayName );
        }
        getLogger().info( "status of module is : " + findElement( By.xpath( stateCell ) ).getText() );
        return findElement( By.xpath( stateCell ) ).getText();
    }

    public String getApplicationDescription( String appDisplayName )
    {
        String description = String.format( NAMES_VIEW_BY_DISPLAY_NAME + P_SUB_NAME, appDisplayName );
        if ( !isElementDisplayed( description ) )
        {
            saveScreenshot( "err_app_description" );
            throw new TestFrameworkException( "description for:" + appDisplayName + "was not found" );
        }
        return getDisplayedString( description );
    }

    public boolean waitApplicationStatus( String appName, String state )
    {
        String stateCell = String.format( SLICK_ROW_BY_NAME, appName ) + "//div[contains(@class,'state')]";
        if ( !isElementDisplayed( stateCell ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_app_status" ) );
            throw new TestFrameworkException( "state was not found in the table ! application name is " + appName );
        }
        String expectedState =
            String.format( SLICK_ROW_BY_NAME, appName ) + String.format( "//div[contains(@class,'state')]//div[ text()='%s']", state );

        boolean result = waitUntilVisibleNoException( By.xpath( expectedState ), Application.EXPLICIT_NORMAL );
        getLogger().info( "status of module is : " + findElement( By.xpath( stateCell ) ).getText() );
        return result;
    }

    public void waitNewInstalledApplicationAppears( String appName, long timeout )
    {
        String appXpath = String.format( SLICK_ROW_BY_NAME, appName );
        boolean result = waitUntilVisibleNoException( By.xpath( appXpath ), timeout );
        if ( !result )
        {
            saveScreenshot( "err_" + appName );
            throw new TestFrameworkException( "application has not appeared in the grid after " + timeout + "seconds" + "app: " + appName );
        }
    }

    public ApplicationItemStatisticsPanel getItemStatisticPanel()
    {
        if ( itemStatisticsPanel == null )
        {
            itemStatisticsPanel = new ApplicationItemStatisticsPanel( getSession() );
        }
        return itemStatisticsPanel;
    }

    public ApplicationBrowsePanel deSelectAppInTable( String appDisplayName )
    {

        if ( isRowByDisplayNameSelected( appDisplayName ) )
        {
            clickCheckboxAndSelectRowByDisplayName( appDisplayName );
        }
        return this;
    }

    @Override
    public Application clickToolbarDelete()
    {
        throw new TestFrameworkException( "Delete button not present in Application's toolbar!" );
    }
}