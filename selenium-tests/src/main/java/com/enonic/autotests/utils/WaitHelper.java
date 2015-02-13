package com.enonic.autotests.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

public class WaitHelper
{

    public static Logger logger = Logger.getLogger( WaitHelper.class );

    /**
     * @param by
     * @param driver
     * @return
     */
    public static boolean waitAndFind( final By by, final WebDriver driver )
    {
        return waitAndFind( by, driver, Application.IMPLICITLY_WAIT );
    }

    public static boolean waitAndFind( final By by, final WebDriver driver, long timeout )
    {
        driver.manage().timeouts().implicitlyWait( timeout, TimeUnit.SECONDS );
        List<WebElement> elements = driver.findElements( by );
        driver.manage().timeouts().implicitlyWait( Application.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS );
        return ( ( elements.size() > 0 ) );
    }

    /**
     * @param by
     * @param timeout
     * @return true if element is invisible , otherwise return false.
     */
    public static boolean waitsElementNotVisible( final WebDriver driver, By by, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( driver, timeout );
        try
        {
            wait.until( ExpectedConditions.invisibilityOfElementLocated( by ) );
            System.out.println( "Element not presented on the page or not visible: " + by.toString() );
            return true;
        }
        catch ( Exception e )
        {
            System.out.println( "Element is visible:" + by.toString() );
            return false;

        }

    }

    /**
     * @param driver
     * @param by
     * @param timeout
     * @return true if element is visible , otherwise return false.
     */
    public static boolean waitUntilVisibleNoException( WebDriver driver, By by, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( driver, timeout );
        try
        {
            wait.until( ExpectedConditions.visibilityOfElementLocated( by ) );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public static void waitUntilVisible( final WebDriver driver, final By by )
    {
        new WebDriverWait( driver, Application.IMPLICITLY_WAIT ).until( ExpectedConditions.visibilityOfElementLocated( by ) );
    }

    public static void waitUntilTitleVisible( final WebDriver driver, final String title )
    {
        ( new WebDriverWait( driver, Application.IMPLICITLY_WAIT ) ).until( new ExpectedCondition<Boolean>()
        {
            public Boolean apply( WebDriver d )
            {
                return d.getTitle().trim().contains( title );
            }
        } );
    }

    /**
     * An expectation for checking an element is visible and enabled such that
     * you can click it.
     *
     * @param testSession
     * @param by
     * @throws TestFrameworkException
     */
    public static void waitUntilElementEnabled( final TestSession testSession, final By by )
        throws TestFrameworkException
    {
        try
        {
            new WebDriverWait( testSession.getDriver(), Application.IMPLICITLY_WAIT ).until(
                ExpectedConditions.elementToBeClickable( by ) );
        }
        catch ( TimeoutException ex )
        {
            logger.error( "TimeoutException, element is disabled" + by.toString() );
            throw new TestFrameworkException( "Element is disabled but should be enabled! " + ex.getMessage() );
        }
    }

    public static boolean waitUntilElementEnabledNoException( final WebDriver driver, final By by, long timeout )
    {
        try
        {
            new WebDriverWait( driver, timeout ).until( ExpectedConditions.elementToBeClickable( by ) );
            return true;
        }
        catch ( TimeoutException ex )
        {
            logger.info( "TimeoutException, element is disabled" + by.toString() );
            return false;
        }
    }

    public static boolean waitUntilTitleLoad( final WebDriver driver, final String title, long timeout )
    {
        try
        {
            new WebDriverWait( driver, timeout ).until( new ExpectedCondition<Boolean>()
            {
                public Boolean apply( WebDriver d )
                {
                    return d.getTitle().trim().contains( title );
                }
            } );
            return true;
        }
        catch ( TimeoutException ex )
        {
            logger.info( "TimeoutException, title not loaded. expected title:" + title + " timeout expired in sec: " + timeout );
            return false;
        }
    }

    public static Boolean waitElementExist( final WebDriver driver, final String xpath, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( driver, timeout );
        return wait.until( new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply( WebDriver webDriver )
            {
                try
                {
                    driver.findElement( By.xpath( xpath ) );

                }
                catch ( StaleElementReferenceException e )
                {

                    return false;
                }
                return true;
            }
        } );
    }

    public static boolean waitAndCheckAttrValue( WebDriver webDriver, final WebElement element, final String attributeName,
                                                 final String attributeValue, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( webDriver, timeout );
        try
        {
            return wait.until( new ExpectedCondition<Boolean>()
            {
                @Override
                public Boolean apply( WebDriver webDriver )
                {
                    try
                    {
                        return element.getAttribute( attributeName ).contains( attributeValue );

                    }
                    catch ( Exception e )
                    {

                        return false;
                    }
                }
            } );
        }
        catch ( org.openqa.selenium.TimeoutException e )
        {
            return false;
        }

    }

    public static String waitAndGetAttribute( WebDriver webDriver, final WebElement element, final String atrName, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( webDriver, timeout );
        try
        {
            return wait.until( new ExpectedCondition<String>()
            {
                @Override
                public String apply( WebDriver webDriver )
                {
                    try
                    {
                        return element.getAttribute( atrName );

                    }
                    catch ( Exception e )
                    {

                        return null;
                    }
                }
            } );
        }
        catch ( org.openqa.selenium.TimeoutException e )
        {
            return null;
        }

    }
}
