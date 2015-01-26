package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class InstallModuleDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'app.browse.InstallModuleDialog')]";

    private final String DIALOG_TITLE = DIALOG_CONTAINER + "//h2[text()='Install module']";

    @FindBy(xpath = DIALOG_CONTAINER + "//input")
    private WebElement urlTextInput;

    @FindBy(xpath = DIALOG_CONTAINER + "//button[child::span[text()='Install']]")
    private WebElement installButton;

    @FindBy(xpath = DIALOG_CONTAINER + "//button[child::span[text()='Cancel']]")
    private WebElement cancelButton;

    public InstallModuleDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).size() > 0;
    }


    public InstallModuleDialog waitForLoaded( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            throw new TestFrameworkException( "InstallModuleDialog was not showed!" );
        }
        return this;
    }

    public InstallModuleDialog typeModuleURL( String moduleUrl )
    {
        urlTextInput.sendKeys( moduleUrl );
        return this;
    }

    public ModuleBrowsePanel clickOnInstall()
    {
        installButton.click();
        sleep( 400 );
        return new ModuleBrowsePanel( getSession() );
    }

}
