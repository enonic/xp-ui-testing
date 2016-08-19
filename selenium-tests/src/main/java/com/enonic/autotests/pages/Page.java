package com.enonic.autotests.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Predicate;

import com.enonic.autotests.TestSession;
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
        input.clear();
        sleep( 100 );
        input.sendKeys( text );
        sleep( 300 );
        logger.info( "text in input: " + text );
    }

    /**
     * @param by
     * @return
     */
    protected boolean waitAndFind( final By by )
    {
        return waitAndFind( by, Application.EXPLICIT_NORMAL );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    protected boolean waitAndFind( final By by, long timeout )
    {
        return WaitHelper.waitAndFind( by, getDriver(), timeout );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    protected boolean waitsElementNotVisible( By by, long timeout )
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), by, timeout );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    protected boolean waitUntilVisibleNoException( By by, long timeout )
    {
        return WaitHelper.waitUntilVisibleNoException( getDriver(), by, timeout );
    }

    protected boolean waitUntilClickableNoException( By by, long timeout )
    {
        return WaitHelper.waitUntilClickableNoException( getDriver(), by, timeout );
    }

    /**
     * @param by
     * @return true if element is visible , otherwise return false.
     */

    protected void waitUntilVisible( final By by )
    {
        WaitHelper.waitUntilVisible( getDriver(), by );
    }

    /**
     * @param title
     * @param timeout
     * @return
     */
    protected boolean waitUntilTitleLoaded( final String title, long timeout )
    {
        return WaitHelper.waitUntilTitleLoad( getDriver(), title, timeout );
    }

    /**
     * @param by
     * @param timeout
     * @return
     */
    protected boolean waitUntilElementEnabledNoException( final By by, long timeout )
    {
        return WaitHelper.waitUntilElementEnabledNoException( getDriver(), by, timeout );
    }

    protected void waitUntilElementEnabled( final By by, long timeout )
    {
        WaitHelper.waitUntilElementEnabled( getSession(), by, timeout );
    }


    protected Boolean waitElementExist( final String xpath, long timeout )
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
    protected boolean waitAndCheckAttrValue( final WebElement element, final String attributeName, final String attributeValue,
                                             long timeout )
    {
        return WaitHelper.waitAndCheckAttrValue( getDriver(), element, attributeName, attributeValue, timeout );
    }

    /**
     * @param element
     * @param attributeName
     * @param timeout
     * @return
     */
    protected boolean isAttributePresent( final WebElement element, final String attributeName, long timeout )
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
    protected String getAttribute( final WebElement element, final String attributeName, long timeout )
    {
        return WaitHelper.waitAndGetAttribute( getDriver(), element, attributeName, timeout );
    }

    /**
     * @param by
     * @return
     */
    protected WebElement waitAndFindElement( By by )
    {
        FluentWait<By> fluentWait = new FluentWait<By>( by );
        fluentWait.pollingEvery( 500, TimeUnit.MILLISECONDS );
        fluentWait.withTimeout( Application.EXPLICIT_LONG, TimeUnit.SECONDS );
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

    public WebElement findElement( By by )
    {
        return getDriver().findElement( by );
    }

    public WebElement getDisplayedElement( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).findFirst().get();
    }

    public List<WebElement> getDisplayedElements( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
    }

    protected long getNumberOfElements( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).count();
    }

    protected List<String> getDisplayedStrings( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
    }

    protected void waitForClickableAndClick( By by )
    {
        FluentWait<By> fluentWait = new FluentWait<By>( by );
        fluentWait.withTimeout( 3, TimeUnit.SECONDS ).pollingEvery( 500, TimeUnit.MICROSECONDS ).ignoring( NoSuchElementException.class );

        try
        {
            getDriver().findElement( by ).click();
        }
        catch ( WebDriverException e )
        {
            try
            {
                fluentWait.until( new Predicate<By>()
                {
                    public boolean apply( By by )
                    {
                        return getDriver().findElement( by ).isEnabled();
                    }
                } );
                getDriver().findElement( by ).click();
            }
            catch ( WebDriverException f )
            {
                logger.info( "[getElementByXpath] FluentWait findElement threw exception:\n\n" + f + "\n\n" );

                throw new WebDriverException( "Unable to find element " + by.toString() );
            }
        }
    }


    public List<WebElement> findElements( By by )
    {
        return session.getDriver().findElements( by );
    }

    public WebDriver getDriver()
    {
        return session.getDriver();
    }

    protected boolean isDynamicElementPresent( By locator, int tries )
    {
        getLogger().info( "Get element by locator: " + locator.toString() );
        long startTime = System.currentTimeMillis();
        getDriver().manage().timeouts().implicitlyWait( Application.EXPLICIT_QUICK, TimeUnit.SECONDS );
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
        getDriver().manage().timeouts().implicitlyWait( Application.EXPLICIT_QUICK, TimeUnit.SECONDS );
        return isFound;
    }

    /**
     * @param locator
     * @param tries
     * @return
     */
    protected WebElement getDynamicElement( By locator, int tries )
    {
        getLogger().info( "Get element by locator: " + locator.toString() );
        long startTime = System.currentTimeMillis();
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
        return we;
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
