package com.enonic.autotests.pages.modules;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowsePanel;

public class ModuleBrowsePanel
    extends BrowsePanel
{

    public final String INSTALL_BUTTON =
        "//div[contains(@id,'app.browse.ModuleBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Install']]";

    @FindBy(xpath = INSTALL_BUTTON)
    private WebElement installButton;


    /**
     * The Constructor
     *
     * @param session
     */
    public ModuleBrowsePanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public BrowsePanel goToAppHome()
    {
        return null;
    }

    public InstallModuleDialog clickToolbarInstall()
    {
        installButton.click();
        InstallModuleDialog dialog = new InstallModuleDialog( getSession() );
        return dialog;
    }
}
