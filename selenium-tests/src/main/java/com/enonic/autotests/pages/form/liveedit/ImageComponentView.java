package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

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
        //NavigatorHelper.switchToLiveEditFrame( getSession() );
        getContentWizardPanel().switchToLiveEditFrame();
        selectOptionsItem( imageName );
        return new LiveFormPanel( getSession() );
    }

    private void selectOptionsItem( String imageName )
    {
        if ( !isElementDisplayed( EMPTY_IMAGE_COMPONENT_CONTAINER + "//input[contains(@id,'ComboBoxOptionFilterInput')]" ) )
        {
            throw new TestFrameworkException( "ImageComponentView: options filter input was not found!" );
        }
        findElements( By.xpath( EMPTY_IMAGE_COMPONENT_CONTAINER + "//input[contains(@id,'ComboBoxOptionFilterInput')]" ) ).get(
            0 ).sendKeys( imageName );
        sleep( 300 );
        String optionXpath = String.format( NAMES_ICON_VIEW, imageName );
        if ( !isElementDisplayed( optionXpath ) )
        {
            throw new TestFrameworkException( "Image with name:  " + imageName + "  was not found!" );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
    }
}
