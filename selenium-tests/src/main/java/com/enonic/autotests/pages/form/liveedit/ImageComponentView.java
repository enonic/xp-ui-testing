package com.enonic.autotests.pages.form.liveedit;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ImageComponentView
    extends UIComponent
{
    private final String EMPTY_IMAGE_COMPONENT_CONTAINER = "//div[contains(@id,'ImageComponentView') and contains(@class,'empty')]";

    private final String IMAGE_COMPONENT_VIEW = "//div[contains(@id,'ImageComponentView')]";

    private final String DROPDOWN_HANDLER = IMAGE_COMPONENT_VIEW + "//button[contains(@id,'DropdownHandle')]";

    private final String UPLOAD_BUTTON = IMAGE_COMPONENT_VIEW + "//div[contains(@class,'upload-button')]";

    private final String ERROR_IN_COMPONENT_MESSAGE = IMAGE_COMPONENT_VIEW + "//div[contains(@class,'error-container')]";

    @FindBy(xpath = DROPDOWN_HANDLER)
    private WebElement dropDownHandler;

    @FindBy(xpath = UPLOAD_BUTTON)
    private WebElement uploadButton;

    public ImageComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectImageItemFromList( String imageName )
    {
        selectOptionsItem( imageName );
        return new LiveFormPanel( getSession() );
    }

    private void selectOptionsItem( String imageName )
    {
        if ( !isElementDisplayed( EMPTY_IMAGE_COMPONENT_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) )
        {
            throw new TestFrameworkException( "ImageComponentView: options filter input was not found!" );
        }
        getDisplayedElement( By.xpath( EMPTY_IMAGE_COMPONENT_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) ).sendKeys( imageName );
        sleep( 500 );
        clickOnOption( imageName );
    }

    public ImageComponentView clickOnTheDropDownHandler()
    {
        dropDownHandler.click();
        sleep( 400 );
        return this;
    }

    public boolean isDropDownHandlerDisplayed()
    {
        return isElementDisplayed( DROPDOWN_HANDLER );
    }

    public boolean isUploadButtonDisplayed()
    {
        return isElementDisplayed( UPLOAD_BUTTON );
    }

    public ImageComponentView clickOnOption( String imageName )
    {
        String optionXpath = String.format( NAMES_ICON_VIEW, imageName );
        if ( !isElementDisplayed( optionXpath ) )
        {
            saveScreenshot( "img_comp_file_not_found" );
            throw new TestFrameworkException( "Image with name:  " + imageName + "  was not found!" );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
        return this;
    }

    public List<String> getDisplayedOptions()
    {
        String optionsDisplayName = IMAGE_COMPONENT_VIEW + H6_DISPLAY_NAME;
        return getDisplayedStrings( By.xpath( optionsDisplayName ) );
    }

    public boolean isErrorMessageDisplayed()
    {
        return isElementDisplayed( ERROR_IN_COMPONENT_MESSAGE );
    }
}
