package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.modules.ModuleBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.utils.TestUtils;
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
        if ( testSession.isLoggedIn() )
        {
            if ( ContentBrowsePanel.isOpened( testSession ) )
            {
                return new ContentBrowsePanel( testSession );
            }
            //TODO navigate to Content Manager Application
            return new ContentBrowsePanel( testSession );
        }
        // if user not logged in:
        else
        {

            HomePage home = loginAndOpenHomePage( testSession );
            ContentBrowsePanel cmPage = home.openContentManagerApplication();
            return cmPage;
        }

    }

    public static UserBrowsePanel openUserManager( TestSession testSession )
    {
        if ( testSession.isLoggedIn() )
        {
            if ( UserBrowsePanel.isOpened( testSession ) )
            {
                return new UserBrowsePanel( testSession );
            }
            //TODO navigate to User Manager Application
            return new UserBrowsePanel( testSession );
        }
        // if user not logged in:
        else
        {

            HomePage home = loginAndOpenHomePage( testSession );
            UserBrowsePanel userBrowsePanel = home.openUserManagerApplication();
            return userBrowsePanel;
        }

    }

    public static ModuleBrowsePanel openApplications( TestSession testSession )
    {
        if ( testSession.isLoggedIn() )
        {
            if ( UserBrowsePanel.isOpened( testSession ) )
            {
                return new ModuleBrowsePanel( testSession );
            }
            //TODO navigate to Modules Manager Application
            return new ModuleBrowsePanel( testSession );
        }
        // if user not logged in:
        else
        {

            HomePage home = loginAndOpenHomePage( testSession );
            TestUtils.saveScreenshot( testSession, "homepage" );
            ModuleBrowsePanel userBrowsePanel = home.openApplications();
            return userBrowsePanel;
        }

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
            home.open( user.getDisplayName(), user.getPassword() );
        }
        else
        {
            home.open( "su", "password" );
        }
        return home;
    }

}
