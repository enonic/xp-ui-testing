package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel;
import com.enonic.autotests.services.NavigatorHelper;

/**
 * Page Object for 'Home' page. Version 5.0
 */
public class HomePage
    extends Page
{
    public static String TITLE = "Enonic CMS - Boot Page";

    @FindBy(xpath = "//a[contains(@href,'content-manager')]//div[contains(.,'Content Manager')]")
    private WebElement contentManager;


    @FindBy(xpath = "//div[@class = 'name-container' and text()='Schema Manager']")
    private WebElement schemaManager;

    @FindBy(xpath = "//div[@class = 'name-container' and text()='Accounts']")
    private WebElement accounts;

    /**
     * @param session
     */
    public HomePage( TestSession session )
    {
        super( session );
    }

    public void open( String username, String password )
    {
        String wh = getSession().getWindowHandle();
        if ( wh == null )
        {
            getSession().setWindowHandle( getDriver().getWindowHandle() );
        }
        // open page via the driver.get(BASE_URL)
        if ( getSession().getBaseUrl() != null )
        {
            getDriver().get( getSession().getBaseUrl() );
        }

        if ( !getSession().isLoggedIn() )
        {
            getLogger().info( "try to login with userName:" + username + " password: " + password );
            LoginPage loginPage = new LoginPage( getSession() );
            loginPage.doLogin( username, password );

            getSession().setLoggedIn( true );
        }
        else
        {
            getDriver().switchTo().window( wh );
        }
        waitUntilAllFramesLoaded();

    }

    /**
     *
     */
    public void waitUntilAllFramesLoaded()
    {
    	waitUntilVisibleNoException(By.xpath("//div[contains(@id,'app.launcher.AppSelector')]"), Application.EXPLICIT_3);
    }

    /**
     * @return
     */
    public SchemaBrowsePanel openSchemaManagerApplication()
    {
        schemaManager.click();
        String whandle = getSession().getDriver().getWindowHandle();
        getSession().setWindowHandle( whandle );
        NavigatorHelper.switchToIframe( getSession(), Application.APP_SCHEMA_MANAGER_FRAME_XPATH );

        return new SchemaBrowsePanel( getSession() );
    }


    public ContentBrowsePanel openContentManagerApplication()
    {
        contentManager.click();
        String whandle = getSession().getDriver().getWindowHandle();
        getSession().setWindowHandle( whandle );
        NavigatorHelper.switchToIframe( getSession(), Application.APP_CONTENT_MANAGER_FRAME_XPATH );
        ContentBrowsePanel panel = new ContentBrowsePanel( getSession() );
        panel.waituntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        getLogger().info( "Content App loaded" );
        return panel;
    }
}
