package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class InstallAppDialog_MarketAppPanel
    extends Application
{
    private final String PANEL_DIV = "//div[contains(@id,'MarketAppPanel')]";

    private final String APP_GRID = InstallAppDialog.INSTALL_DIALOG_DIV + "//div[contains(@id,'MarketAppsTreeGrid')]";

    private String APP_ROW_BY_DISPLAY_NAME = APP_GRID + SLICK_ROW_BY_DISPLAY_NAME;


    private String INSTALL_APP_BUTTON = APP_ROW_BY_DISPLAY_NAME + "//div[@class='install']";

    private String INSTALLED_APP_BUTTON = APP_ROW_BY_DISPLAY_NAME + "//div[@class='installed']";

    public InstallAppDialog_MarketAppPanel( final TestSession session )
    {
        super( session );
    }

    public void doInstallApp( String appDisplayName )
    {
        String installButton = String.format( INSTALL_APP_BUTTON, appDisplayName );
        getDynamicElement( By.xpath( installButton ), 3 ).click();
        //TODO wait for progress bar  disappears
        sleep( 10000 );
    }

    public boolean isApplicationAlreadyInstalled( String appDisplayName )
    {
        String installedButton = String.format( INSTALLED_APP_BUTTON, appDisplayName );
        return isElementDisplayed( installedButton );
    }
}
