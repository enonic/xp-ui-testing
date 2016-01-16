package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.LoginPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.modules.ApplicationBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.vo.usermanager.User;

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
        HomePage home = loginAndOpenHomePage( testSession );
        ContentBrowsePanel cmPage = home.openContentManagerApplication();
        return cmPage;
    }

    public static UserBrowsePanel openUsersApp( TestSession testSession )
    {
        HomePage home = loginAndOpenHomePage( testSession );
        UserBrowsePanel userBrowsePanel = home.openUserManagerApplication();
        return userBrowsePanel;
    }

    public static ApplicationBrowsePanel openApplications( TestSession testSession )
    {
        HomePage home = loginAndOpenHomePage( testSession );
        ApplicationBrowsePanel userBrowsePanel = home.openApplications();
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

    public static void switchToLiveEditFrame( TestSession testSession )
    {
        WebDriver driver = testSession.getDriver();
        driver.switchTo().window( driver.getWindowHandle() );
        //switch from content manager frame to 'main' frame
        driver.switchTo().frame( 0 );
        List<WebElement> liveEditFrames = driver.findElements( By.xpath( Application.LIVE_EDIT_FRAME ) );
        if ( liveEditFrames.size() == 0 )
        {
            throw new TestFrameworkException( "Unable to switch to the iframe " );
        }
        //switch to 'live edit' frame
        driver.switchTo().frame( liveEditFrames.get( 0 ) );
    }

    public static void switchToContentManagerFrame( TestSession testSession )
    {
        WebDriver driver = testSession.getDriver();
        driver.switchTo().window( driver.getWindowHandle() );
        List<WebElement> cm = driver.findElements( By.xpath( Application.CONTENT_MANAGER_FRAME_XPATH ) );
        driver.switchTo().frame( cm.get( 0 ) );
    }

    public static HomePage loginAndOpenHomePage( TestSession testSession )
    {
        User user = testSession.getCurrentUser();
        LoginPage loginPage = new LoginPage( testSession );
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
