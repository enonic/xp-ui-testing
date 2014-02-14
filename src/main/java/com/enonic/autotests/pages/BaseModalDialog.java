package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.utils.WaitHelper;

public abstract class BaseModalDialog
{
    private TestSession session;

    /**
     * @param session
     */
    public BaseModalDialog( TestSession session )
    {
        this.session = session;
        PageFactory.initElements( session.getDriver(), this );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    public boolean waitUntilVisibleNoException( By by, long timeout )
    {
        return WaitHelper.waitUntilVisibleNoException( getDriver(), by, timeout );
    }

    /**
     * @param driver
     * @param by
     * @param timeout
     * @return
     */
    public boolean waitElementNotVisible( final WebDriver driver, By by, long timeout )
    {
        return WaitHelper.waitsElementNotVisible( driver, by, timeout );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    public boolean waitElementNotVisible( By by, long timeout )
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), by, timeout );
    }

    public Boolean waitElementExist( final String xpath, long timeout )
    {
        return WaitHelper.waitElementExist( getDriver(), xpath, timeout );
    }

    public TestSession getSession()
    {
        return session;
    }

    public void setSession( TestSession session )
    {
        this.session = session;
    }

    public List<WebElement> findElements( By by )
    {
        return session.getDriver().findElements( by );
    }

    public WebElement findElement( By by )
    {
        return session.getDriver().findElement( by );
    }

    public WebDriver getDriver()
    {
        return session.getDriver();
    }

    /**
     * @param by
     * @return
     */
    public boolean waitAndFind( final By by )
    {
        return waitAndFind( by, Application.IMPLICITLY_WAIT );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    public boolean waitAndFind( final By by, long timeout )
    {
        return WaitHelper.waitAndFind( by, getDriver(), timeout );
    }

}
