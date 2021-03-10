package com.enonic.autotests.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.LoginPage;
import com.enonic.autotests.pages.XpTourDialog;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.usermanager.User;

public class NavigatorHelper
{

    public static void closeXpTourDialogIfPresent( TestSession testSession )
    {
        XpTourDialog xpTourDialog = new XpTourDialog( testSession );
        if ( xpTourDialog.isOpened() )
        {
            xpTourDialog.clickOnCancelButton();
        }
    }

    /**
     * Opens 'Content Studio' application.
     *
     * @param testSession {@link TestSession} instance.
     * @return {@link ContentBrowsePanel} instance.
     */
    public static ContentBrowsePanel openContentStudioApp( TestSession testSession )
    {
        HomePage home = loginAndOpenHomePage( testSession );
        ContentBrowsePanel cmPage = home.openContentStudioApplication();
        return cmPage;
    }

    private static boolean waitInvisibilityOfElement( By by, long timeout, TestSession testSession )
    {
        //WebDriverWait wait = new WebDriverWait(driver, 15);
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//input[@id='text4']")));
        Wait<WebDriver> wait =
            new FluentWait<WebDriver>( testSession.getDriver() ).withTimeout( timeout, TimeUnit.MILLISECONDS ).pollingEvery( 400,
                                                                                                                             TimeUnit.MILLISECONDS ).ignoring(
                NoSuchElementException.class );
        try
        {
            wait.until( ExpectedConditions.invisibilityOfElementLocated( by ) );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public static UserBrowsePanel openUsersApp( TestSession testSession )
    {
        HomePage home = loginAndOpenHomePage( testSession );
        //closeXpTourDialogIfPresent( testSession );
//        if ( !waitInvisibilityOfElement( By.xpath( "//div[contains(@id,'BodyMask')]" ), 3, testSession ) )
//        {
//            TestUtils.saveScreenshot( testSession, NameHelper.uniqueName( "err_bodymask" ) );
//            throw new TestFrameworkException( "Body Mask still displayed on the Home Page" );
//        }
        UserBrowsePanel userBrowsePanel = home.openUsersApplication();
        userBrowsePanel.waitUntilPageLoaded( Application.EXPLICIT_LONG );
        return userBrowsePanel;
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
            throw new TestFrameworkException( "Unable to switch to the iframe " + iframeXpath );
        }
        testSession.getDriver().switchTo().frame( frames.get( 0 ) );
    }

    public static void switchToNextTab( TestSession session )
    {

        ArrayList<String> windowHandles = new ArrayList<String>( session.getDriver().getWindowHandles() );
        if ( windowHandles.size() == 0 )
        {
            TestUtils.saveScreenshot( session, NameHelper.uniqueName( "err_switch_tab" ) );
            throw new TestFrameworkException( "tab was not found!" );
        }

        session.getDriver().switchTo().window( windowHandles.get( windowHandles.size() - 1 ) );

    }

    public static String switchToBrowserTab( TestSession session, String urlPart )
    {
        WebDriver driver = session.getDriver();
        Set<String> windowHandles = driver.getWindowHandles();
        if ( !windowHandles.isEmpty() )
        {
            for ( String windowHandle : windowHandles )
            {
                try
                {
                    if ( driver.switchTo().window( windowHandle ).getCurrentUrl().contains( urlPart ) )
                    {
                        return windowHandle;
                    }
                }
                catch ( NoSuchWindowException e )
                {
                    throw new TestFrameworkException( "NoSuchWindowException- wrong handle" );
                }
            }
        }
        TestUtils.saveScreenshot( session, NameHelper.uniqueName( "err_switch_tab" ) );
        throw new TestFrameworkException( "tab was not found!" + urlPart );
    }

    public static String switchToBrowserTabByTitle( TestSession session, String titlePart )
    {
        WebDriver driver = session.getDriver();
        Set<String> windowHandles = driver.getWindowHandles();
        if ( !windowHandles.isEmpty() )
        {
            for ( String windowHandle : windowHandles )
            {
                try
                {
                    if ( driver.switchTo().window( windowHandle ).getTitle().contains( titlePart ) )
                    {
                        return windowHandle;
                    }
                }
                catch ( NoSuchWindowException e )
                {
                    throw new TestFrameworkException( "NoSuchWindowException- wrong handle" );
                }
            }
        }
        throw new TestFrameworkException( "tab was not found!" + titlePart );
    }

    public static boolean isWindowPresent( TestSession session, String titlePart )
    {
        WebDriver driver = session.getDriver();
        Set<String> windowHandles = driver.getWindowHandles();
        if ( !windowHandles.isEmpty() )
        {
            for ( String windowHandle : windowHandles )
            {
                try
                {
                    if ( driver.switchTo().window( windowHandle ).getTitle().contains( titlePart ) )
                    {
                        return true;
                    }
                }
                catch ( NoSuchWindowException e )
                {
                    throw new TestFrameworkException( "NoSuchWindowException- wrong handle" );
                }
            }
        }
        return false;
    }

    public static int countWindowTabsByTitle( TestSession session, String titlePart )
    {
        WebDriver driver = session.getDriver();
        int count = 0;
        Set<String> windowHandles = driver.getWindowHandles();
        if ( !windowHandles.isEmpty() )
        {
            for ( String windowHandle : windowHandles )
            {
                try
                {
                    if ( driver.switchTo().window( windowHandle ).getTitle().contains( titlePart ) )
                    {
                        count++;
                    }
                }
                catch ( NoSuchWindowException e )
                {
                    throw new TestFrameworkException( "NoSuchWindowException- wrong handle" );
                }
            }
        }
        return count;
    }

    public static HomePage loginAndOpenHomePage( TestSession testSession )
    {
        User user = testSession.getCurrentUser();
        LoginPage loginPage = new LoginPage( testSession );
        if ( !loginPage.isDisplayed() )
        {
            return new HomePage( testSession );
        }
        //save window-handle for HomePage
        String homeTabHandle = testSession.getDriver().getWindowHandle();
        testSession.put( HomePage.HOME_PAGE_TAB_HANDLE, homeTabHandle );
        if ( user != null )
        {
            return loginPage.doLogin( user.getDisplayName(), user.getPassword() );
        }
        else
        {
            return loginPage.doLogin( "su", "password" );
        }
    }
}
