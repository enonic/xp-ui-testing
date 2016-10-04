package com.enonic.autotests.pages.contentmanager.wizardpanel.image;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 28.09.2016.
 */
public class ImageEditor
    extends Application
{
    private final String CONTAINER_DIV = "//div[contains(@id,'ImageEditor')]";

    private final String FOCUS_CIRCLE = CONTAINER_DIV + "//*[name()='svg']/*[name()='g' and contains(@class,'focus-group')]";

    private final String UP_DRAG_HANDLE = CONTAINER_DIV + "//*[name()='svg' and contains(@id,'dragHandle')]//*[name()='use']";

    private final String IMAGE_FRAME = CONTAINER_DIV + "//div[@class='image-frame']";

    public ImageEditor( TestSession session )
    {
        super( session );
    }

    public boolean isPresent()
    {
        return isElementDisplayed( CONTAINER_DIV );
    }

    public ImageEditorToolbar getToolbar()
    {
        return new ImageEditorToolbar( getSession() );
    }

    public boolean isFocusCircleDisplayed()
    {
        return isElementDisplayed( FOCUS_CIRCLE );
    }

    public void doDragAndChangeSizeOfImage( int yOffset )
    {

        if ( !isElementDisplayed( UP_DRAG_HANDLE ) )
        {
            throw new TestFrameworkException( "drag handler was not found" );
        }
        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( findElements( By.xpath( UP_DRAG_HANDLE ) ).get( 0 ) ).moveByOffset( 0, yOffset ).release().perform();
        sleep( 1000 );

    }

    public int getImageHeight()
    {
        Object viewPortHeight =
            ( (JavascriptExecutor) getDriver() ).executeScript( "return document.getElementsByClassName('image-frame')[0].style.height" );
        return Integer.valueOf( viewPortHeight.toString().substring( 0, viewPortHeight.toString().indexOf( "." ) ) );
    }

}
