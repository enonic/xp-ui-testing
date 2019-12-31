package com.enonic.autotests.utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

public class TestUtils
{
    public static Logger logger = Logger.getLogger( TestUtils.class );

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";

    /**
     * The Default constructor.
     */
    private TestUtils()
    {

    }

    public static void setCheckboxChecked( TestSession session, String checkboxId, boolean value )
    {
        JavascriptExecutor executor = (JavascriptExecutor) session.getDriver();
        String script = String.format( "document.getElementById('%s').checked=arguments[0]", checkboxId );
        executor.executeScript( script, value );
    }

    public static String saveScreenshot( final TestSession testSession, String screenshotName )
    {
        WebDriver driver = testSession.getDriver();
        String fileName = screenshotName + ".png";
        File folder = new File( "build/screenshots" );
        if ( !folder.exists() )
        {
            if ( !folder.mkdir() )
            {
                System.out.println( "Folder for snapshots was not created " );
            }
            else
            {
                System.out.println( "Folder for snapshots was created " + folder.getAbsolutePath() );
            }
        }
        File screenshot = ( (TakesScreenshot) driver ).getScreenshotAs( OutputType.FILE );
        String fullFileName = folder.getAbsolutePath() + File.separator + fileName;
        try
        {
            FileUtils.copyFile( screenshot, new File( fullFileName ) );
        }
        catch ( IOException e )
        {
            System.out.println( "IOException was occurred" );
        }
        return fileName;
    }

    /**
     * @param testSession
     */
    public static String saveScreenshot( final TestSession testSession )
    {
        String fileName = timeNow();
        return saveScreenshot( testSession, fileName );
    }

    public static String timeNow()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT_NOW );
        return sdf.format( cal.getTime() );
    }

    public static void clickByLocator( final By locator, WebDriver driver )
    {
        WebElement myDynamicElement = ( new WebDriverWait( driver, 10 ) ).until( ExpectedConditions.presenceOfElementLocated( locator ) );
        myDynamicElement.click();
    }

    public static int getNumberFromFilterLabel( String label )
    {
        int start = label.indexOf( "(" );
        int end = label.indexOf( ")" );
        if ( start == -1 || end == -1 )
        {
            throw new TestFrameworkException( "wrong label!" );
        }
        return Integer.valueOf( label.substring( start + 1, end ) );
    }

    public static String waitNotification( final By locator, final WebDriver driver, long timeout )
    {
        WebDriverWait wait = new WebDriverWait( driver, timeout );
        WebElement element = wait.until( ExpectedConditions.visibilityOfElementLocated( locator ) );
        return element.getText();
    }

    public static String createTempFile( String s )
    {
        try
        {
            File f = File.createTempFile( "uploadTest", "tempfile" );
            f.deleteOnExit();
            writeStringToFile( s, f );
            return f.getAbsolutePath();
        }
        catch ( Exception e )
        {
            throw new TestFrameworkException( "Error during creation TMP-file" );
        }
    }

    public static void writeStringToFile( String s, File file )
        throws IOException
    {
        FileOutputStream in = null;
        try
        {
            in = new FileOutputStream( file );
            FileChannel fchan = in.getChannel();
            BufferedWriter bf = new BufferedWriter( Channels.newWriter( fchan, "UTF-8" ) );
            bf.write( s );
            bf.close();
        }
        finally
        {
            if ( in != null )
            {
                in.close();
            }
        }
    }

    public static String getPageSource( TestSession session, String title )
    {
        String current = session.getDriver().getWindowHandle();
        Set<String> allWindows = session.getDriver().getWindowHandles();
        String source = null;
        if ( !allWindows.isEmpty() )
        {
            for ( String windowId : allWindows )
            {
                try
                {
                    logger.info( "siteName:" + title );
                    if ( session.getDriver().switchTo().window( windowId ).getTitle().contains( title ) )
                    {
                        source = session.getDriver().getPageSource();
                        logger.info( "source  :  " + source );
                        saveScreenshot( session, NameHelper.uniqueName( "page-source" ) );
                        session.getDriver().close();
                    }
                }
                catch ( NoSuchWindowException e )
                {
                    throw new TestFrameworkException( "NoSuchWindowException- wrong ID" + e.getLocalizedMessage() );
                }
            }
        }
        session.getDriver().switchTo().window( current );
        return source;
    }

    public static void dragAndDrop( WebDriver driver, WebElement source, WebElement target )
    {
        Actions builder = new Actions( driver );
        builder.clickAndHold( source ).build().perform();
        builder.moveToElement( target ).build().perform();
        builder.release( target );
        builder.build().perform();
    }

    public static boolean isContains( Collection<String> collection, String target )
    {
        if ( collection.isEmpty() )
        {
            return false;
        }
        return collection.stream().anyMatch( s -> s.contains( target ) );

    }
}
