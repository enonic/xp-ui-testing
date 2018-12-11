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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.utils.NameHelper;
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
    protected void clearAndType( WebElement input, String text )
    {
        input.clear();
        sleep( 100 );
        input.sendKeys( text );
        sleep( 200 );
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

    protected boolean waitInvisibilityOfElement( By by, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( getDriver(), timeout );
        try
        {
            wait.until( invisibilityOfElementLocated( by ) );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    private ExpectedCondition<Boolean> invisibilityOfElementLocated( final By locator )
    {
        return new ExpectedCondition<Boolean>()
        {
            public Boolean apply( WebDriver driver )
            {
                try
                {
                    return Boolean.valueOf( !isElementDisplayed( locator ) );
                }
                catch ( NoSuchElementException var3 )
                {
                    return Boolean.valueOf( true );
                }
                catch ( StaleElementReferenceException var4 )
                {
                    return Boolean.valueOf( true );
                }
            }

            public String toString()
            {
                return "element to no longer be visible: " + locator;
            }
        };
    }

    protected boolean isElementDisplayed( String xpath )
    {
        return findElements( By.xpath( xpath ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    protected boolean isElementDisplayed( final By locator )
    {
        return findElements( locator ).stream().filter( WebElement::isDisplayed ).count() > 0;
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


    protected Boolean waitForElementExist( final String xpath, long timeout )
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

    protected boolean waitIsElementEnabled( final WebElement element, long timeout )
    {
        return WaitHelper.waitAttrHasNoValue( getDriver(), element, "class", "disabled", timeout );
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
        Wait<WebDriver> wait = new FluentWait<WebDriver>( getDriver() ).withTimeout( 30, TimeUnit.MILLISECONDS ).pollingEvery( 400,
                                                                                                                               TimeUnit.MILLISECONDS ).ignoring(
            NoSuchElementException.class );

        WebElement element = wait.until( new Function<WebDriver, WebElement>()
        {
            public WebElement apply( WebDriver driver )
            {
                return driver.findElement( by );
            }
        } );
        return getDriver().findElement( by );
    }

    protected WebElement findElement( By by )
    {
        return getDriver().findElement( by );
    }

    protected WebElement getDisplayedElement( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).findFirst().get();
    }

    protected List<WebElement> getDisplayedElements( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
    }

    protected long getNumberOfElements( By by )
    {
        return findElements( by ).stream().filter( WebElement::isDisplayed ).count();
    }

    protected List<String> getDisplayedStrings( By by )
    {
        try
        {
            return findElements( by ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
        }
        catch ( StaleElementReferenceException e )
        {
            sleep( 1000 );
            return findElements( by ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
        }
    }


    protected List<WebElement> findElements( By by )
    {
        return session.getDriver().findElements( by );
    }

    protected WebDriver getDriver()
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

    protected TestSession getSession()
    {
        return session;
    }

    protected void setSession( TestSession session )
    {
        this.session = session;
    }

    protected Logger getLogger()
    {
        return logger;
    }
}
