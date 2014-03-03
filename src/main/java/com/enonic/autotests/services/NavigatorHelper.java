package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.accounts.AccountsPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.User;

public class NavigatorHelper
{

    /**
     * Opens 'Content Manager' application.
     *
     * @param testSession {@link TestSession} instance.
     * @return {@link ContentBrowsePanel} instance.
     */
    public static ContentBrowsePanel openContentApp( TestSession testSession )
    {
        if ( testSession.isLoggedIn() )
        {
            if ( ContentBrowsePanel.isOpened( testSession ) )
            {
                return new ContentBrowsePanel( testSession );
            }
            boolean isHomeButtonPresent = WaitHelper.waitAndFind( By.xpath( Application.HOME_BUTTON_XPATH ), testSession.getDriver(), 1 );
            if ( isHomeButtonPresent )
            {
                testSession.getDriver().findElement( By.xpath( Application.HOME_BUTTON_XPATH ) ).click();
                testSession.getDriver().switchTo().window( testSession.getWindowHandle() );
                HomePage homepage = new HomePage( testSession );
                homepage.openContentManagerApplication();
                return new ContentBrowsePanel( testSession );
            }
            else
            {
                HomePage homepage = new HomePage( testSession );
                return homepage.openContentManagerApplication();
            }

        }
        // if user not logged in:
        else
        {

            HomePage home = loginAndOpenHomePage( testSession );
            ContentBrowsePanel cmPage = home.openContentManagerApplication();
            cmPage.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT );
            return cmPage;
        }

    }

    public static SchemaBrowsePanel openSchemaManager( TestSession testSession )
    {
        if ( SchemaBrowsePanel.isOpened( testSession ) )
        {
            return new SchemaBrowsePanel( testSession );
        }
        else
        {
            HomePage home = loginAndOpenHomePage( testSession );
            SchemaBrowsePanel schemasPage = home.openSchemaManagerApplication();
            schemasPage.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT );
            return schemasPage;
        }

    }

    /**
     * Open 'Home' page, click by 'Accounts' link and open application's page.
     *
     * @param testSession {@link TestSession} instance.
     * @return {@link AccountsPage} instance.
     */
    public static AccountsPage openAccounts( TestSession testSession )
    {
        HomePage home = loginAndOpenHomePage( testSession );
        return home.openAccountsApplication();
    }

    /**
     * @param testSession {@link TestSession} instance.
     * @param iframeXpath frame's xpath.
     */
    public static void switchToIframe( TestSession testSession, String iframeXpath )
    {
        String whandle = testSession.getDriver().getWindowHandle();
        testSession.getDriver().switchTo().window( whandle );
        List<WebElement> frames = testSession.getDriver().findElements( By.xpath( iframeXpath ) );
        if ( frames.size() == 0 )
        {
            throw new TestFrameworkException( "Unable to switch to the iframe" + iframeXpath );
        }
        testSession.getDriver().switchTo().frame( frames.get( 0 ) );
    }

    /**
     * 'Login' to cms and opens the 'Home' page that contains links to all
     * applications.
     *
     * @param testSession {@link TestSession} instance.
     * @return {@link HomePage} instance.
     */
    public static HomePage loginAndOpenHomePage( TestSession testSession )
    {
        User user = testSession.getCurrentUser();
        HomePage home = new HomePage( testSession );
        if ( user != null )
        {
            home.open( user.getUserInfo().getName(), user.getUserInfo().getPassword() );
        }
        else
        {
            home.open( "admin", "pass" );
        }
        return home;
    }

}
