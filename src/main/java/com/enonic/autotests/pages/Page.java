package com.enonic.autotests.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class Page
{

    private TestSession session;

    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * The constructor
     *
     * @param session
     */
    public Page( TestSession session )
    {
        this.session = session;
        PageFactory.initElements( session.getDriver(), this );
    }

    /**
     * The constructor
     *
     * @param driver
     */
    public Page( WebDriver driver )
    {
        this.session = new TestSession();
        session.setDriver( driver );
        PageFactory.initElements( driver, this );
    }

    public void logError( String message )
    {
        logger.error( message );
        TestUtils.saveScreenshot( getSession() );
    }

    /**
     * Types text in input field.
     *
     * @param input input type=text
     * @param text  text for input.
     */
    public void clearAndType( WebElement input, String text )
    {
        logger.info( "text will be typed: " + text );
        input.clear();
        sleep( 100 );
        input.sendKeys( text );
        logger.info( "text in input: " + text );

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

    /**
     * @param by
     * @param timeout
     * @return
     */
    public boolean waitsElementNotVisible( By by, long timeout )
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), by, timeout );
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
     * @param by
     * @return true if element is visible , otherwise return false.
     */

    public void waitUntilVisible( final By by )
    {
        WaitHelper.waitUntilVisible( getDriver(), by );
    }

    /**
     * @param title
     * @param timeout
     * @return
     */
    public boolean waitUntilTitleLoad( final String title, long timeout )
    {
        return WaitHelper.waitUntilTitleLoad( getDriver(), title, timeout );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    public boolean waitUntilElementEnabledNoException( final By by, long timeout )
    {
        return WaitHelper.waitUntilElementEnabledNoException( getDriver(), by, timeout );
    }

    public Boolean waitElementExist( final String xpath, long timeout )
    {
        return WaitHelper.waitElementExist( getDriver(), xpath, timeout );
    }

    /**
     * @param element
     * @param attributeName
     * @param attributeValue
     * @param timeout
     * @return
     */
    public boolean waitAndCheckAttrValue( final WebElement element, final String attributeName, final String attributeValue, long timeout )
    {
        return WaitHelper.waitAndCheckAttrValue( getDriver(), element, attributeName, attributeValue, timeout );
    }

    /**
     * @param element
     * @param attributeName
     * @param timeout
     * @return
     */
    public boolean isAttributePresent( final WebElement element, final String attributeName, long timeout )
    {
        String result = WaitHelper.waitAndGetAttribute( getDriver(), element, attributeName, timeout );
        return result != null ? true : false;
    }

    /**
     * @param element
     * @param attributeName
     * @param timeout
     * @return
     */
    public String getAttribute( final WebElement element, final String attributeName, long timeout )
    {
        return WaitHelper.waitAndGetAttribute( getDriver(), element, attributeName, timeout );
    }

    /**
     * @param by
     * @return
     */
    public WebElement findElement( By by )
    {
        FluentWait<By> fluentWait = new FluentWait<By>( by );
        fluentWait.pollingEvery( 500, TimeUnit.MILLISECONDS );
        fluentWait.withTimeout( 3l, TimeUnit.SECONDS );
        fluentWait.until( new Predicate<By>()
        {
            public boolean apply( By by )
            {
                try
                {
                    return getDriver().findElement( by ).isDisplayed();
                }
                catch ( NoSuchElementException ex )
                {
                    return false;
                }
            }
        } );
        return getDriver().findElement( by );
    }

    public List<WebElement> findElements( By by )
    {
        return session.getDriver().findElements( by );
    }

    public WebDriver getDriver()
    {
        return session.getDriver();
    }

    public boolean isDynamicElementPresent( By locator, int tries )
    {
        getLogger().info( "Get element by locator: " + locator.toString() );
        long startTime = System.currentTimeMillis();
        getDriver().manage().timeouts().implicitlyWait( Application.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS );
        boolean isFound = false;
        for ( int i = 0; i <= tries; i++ )
        {
            try
            {
                getDriver().findElement( locator );
                isFound = true;
                break;
            }
            catch ( StaleElementReferenceException ser )
            {
                getLogger().info( "ERROR: Stale element. " + locator.toString() );

            }
            catch ( NoSuchElementException nse )
            {
                getLogger().info( "ERROR: No such element. " + locator.toString() );

            }
            catch ( Exception e )
            {
                getLogger().info( e.getMessage() );
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        getLogger().info( "Finished click after waiting for " + totalTime + " milliseconds." );
        getDriver().manage().timeouts().implicitlyWait( Application.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS );
        return isFound;
    }

    /**
     * @param locator
     * @param tries
     * @return
     */
    public WebElement getDynamicElement( By locator, int tries )
    {
        getLogger().info( "Get element by locator: " + locator.toString() );
        long startTime = System.currentTimeMillis();
        getDriver().manage().timeouts().implicitlyWait( 5, TimeUnit.SECONDS );
        WebElement we = null;
        for ( int i = 0; i <= tries; i++ )
        {

            getLogger().info( "Locating remaining time:  ..." + " seconds." );
            try
            {
                we = getDriver().findElement( locator );
                getLogger().info( "getDynamicElement: dynamic web element was found!" );
                return we;
            }
            catch ( StaleElementReferenceException ser )
            {
                getLogger().info( "ERROR: Stale element. " + locator.toString() );

            }
            catch ( NoSuchElementException nse )
            {
                getLogger().info( "ERROR: No such element. " + locator.toString() );

            }
            catch ( Exception e )
            {
                getLogger().info( e.getMessage() );
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        getLogger().info( "Finished click after waiting for " + totalTime + " milliseconds." );
        getDriver().manage().timeouts().implicitlyWait( Application.IMPLICITLY_WAIT, TimeUnit.SECONDS );
        getLogger().info( "getDynamicElement is  null: " );
        return we;
    }


    public WebElement findDynamicElement( final WebDriver driver, final String xpath, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( driver, timeout );
        return wait.until( new ExpectedCondition<WebElement>()
        {
            @Override
            public WebElement apply( WebDriver webDriver )
            {
                try
                {
                    return driver.findElement( By.xpath( xpath ) );

                }
                catch ( StaleElementReferenceException e )
                {
                    return null;
                }
            }
        } );
    }


    public WebElement scrollTableAndFind( String elementXpath, String scrollXpath )
    {
        WebElement element = null;
        List<WebElement> divScroll = getDriver().findElements( By.xpath( scrollXpath ) );
        if ( divScroll.size() == 0 )
        {
            throw new TestFrameworkException( "Div was not found xpath: " + scrollXpath );
        }
        long gridHeight =
            (Long) ( (JavascriptExecutor) getDriver() ).executeScript( "return arguments[0].scrollHeight", divScroll.get( 0 ) );

        for ( int scrollTop = 0; scrollTop <= gridHeight; )
        {
            scrollTop += 40;
            ( (JavascriptExecutor) getDriver() ).executeScript( "arguments[0].scrollTop=arguments[1]", divScroll.get( 0 ), scrollTop );
            element = getDriver().findElement( By.xpath( elementXpath ) );
            if ( element.isDisplayed() )
            {
                return element;
            }
        }
        return null;
    }

    public TestSession getSession()
    {
        return session;
    }


    public void setSession( TestSession session )
    {
        this.session = session;
    }

    public Logger getLogger()
    {
        return logger;
    }


}
