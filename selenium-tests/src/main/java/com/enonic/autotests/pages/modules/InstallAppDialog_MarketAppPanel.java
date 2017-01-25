package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

public class InstallAppDialog_MarketAppPanel
    extends Application
{
    private final String PANEL_DIV = "//div[contains(@id,'MarketAppPanel')]";

    private final String APP_GRID = InstallAppDialog.INSTALL_DIALOG_DIV + "//div[contains(@id,'MarketAppsTreeGrid')]";

    private String APP_ROW_BY_DISPLAY_NAME = APP_GRID + SLICK_ROW_BY_DISPLAY_NAME;


    private String INSTALL_APP_BUTTON = APP_ROW_BY_DISPLAY_NAME + "//a[@class='install']";

    private String INSTALLED_APP_BUTTON = APP_ROW_BY_DISPLAY_NAME + "//a[@class='installed']";

    public InstallAppDialog_MarketAppPanel( final TestSession session )
    {
        super( session );
    }

    public void doInstallApp( String appDisplayName )
    {
        String installButton = String.format( INSTALL_APP_BUTTON, appDisplayName );
        String installedButton = String.format( INSTALLED_APP_BUTTON, appDisplayName );
        WebElement installWebElement = getDynamicElement( By.xpath( installButton ), 3 );
        if ( installWebElement == null )
        {
            saveScreenshot( "err_install_" + appDisplayName );
            throw new TestFrameworkException( "install button was not found! " + appDisplayName );
        }
        installWebElement.click();
        boolean result = waitUntilVisibleNoException( By.xpath( installedButton ), APP_INSTALL_TIMEOUT );
        if ( !result )
        {
            saveScreenshot( "err_install_timeout" );
        }
    }

    public boolean isApplicationAlreadyInstalled( String appDisplayName )
    {
        String installedButton = String.format( INSTALLED_APP_BUTTON, appDisplayName );
        return isElementDisplayed( installedButton );
    }

    public boolean isDisplayed()
    {

        return isElementDisplayed( PANEL_DIV );
    }
}
