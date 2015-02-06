package com.enonic.autotests.pages.form.liveedit;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.services.NavigatorHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ImageComponentView
    extends UIComponent
{
    private final String EMPTY_IMAGE_COMPONENT_CONTAINER =
        "//div[contains(@id,'api.liveedit.image.ImageComponentView') and contains(@class,'empty')]";

    private final String IMAGE_COMPONENT_CONTAINER = "//div[contains(@id,'api.liveedit.image.ImageComponentView')]";

    private final String UPLOAD_BUTTON = "//button[@class='button upload-button']";

    public ImageComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectImageItemFromList( String imageName )
    {
        clickOnDropDown();
        clickOnOptionsItem( imageName );
        return new LiveFormPanel( getSession() );
    }

    public void clickOnDropDown()
    {
        String dropDownButtonXpath = EMPTY_IMAGE_COMPONENT_CONTAINER + "//div[@class='dropdown-handle']";
        if ( findElements( By.xpath( dropDownButtonXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "element not exists or wrong xpath" );
        }
        findElements( By.xpath( dropDownButtonXpath ) ).get( 0 ).click();
        sleep( 1000 );
    }

    public LiveFormPanel doUploadImage( String imagePath )
        throws AWTException
    {
        //click upload button
        findElements( By.xpath( UPLOAD_BUTTON ) ).get( 0 ).click();
        sleep( 500 );
        clickOnUploadButtonAndShowDropZone();

        saveInSystemClipboard( imagePath );
        sleep( 1000 );
        Robot robot = new Robot();

        robot.keyPress( KeyEvent.VK_CONTROL );
        robot.keyPress( KeyEvent.VK_V );
        robot.keyRelease( KeyEvent.VK_V );
        robot.keyRelease( KeyEvent.VK_CONTROL );
        sleep( 3000 );
        robot.keyPress( KeyEvent.VK_ENTER );
        robot.keyRelease( KeyEvent.VK_ENTER );
        sleep( 2000 );
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        return new LiveFormPanel( getSession() );
    }

    private void clickOnUploadButtonAndShowDropZone()
    {
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        findElements( By.xpath( "//div[contains(@id,'ImageUploadDialog') and @style]//a[@class='dropzone']" ) ).get( 0 ).click();
    }

    private void clickOnOptionsItem( String imageName )
    {
        findElements( By.xpath( EMPTY_IMAGE_COMPONENT_CONTAINER + String.format(
            "//div[contains(@id,'api.app.NamesAndIconView')]//h6[@class='main-name' and text()='%s']", imageName ) ) ).get( 0 ).click();
    }

    private void saveInSystemClipboard( String filePath )
    {
        URL dirURL = ImageComponentView.class.getClassLoader().getResource( filePath );
        if ( dirURL == null )
        {
            throw new TestFrameworkException( "tests resource for upload tests was not found:" + filePath );
        }
        File file = null;
        try
        {
            getLogger().info( "path to resource  is###: " + dirURL.toURI() );
            file = new File( dirURL.toURI() );

        }
        catch ( URISyntaxException e )
        {
            getLogger().error( "wrong uri for file " + filePath );
        }

        StringSelection ss = new StringSelection( file.getAbsolutePath() );
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents( ss, null );
    }

}
