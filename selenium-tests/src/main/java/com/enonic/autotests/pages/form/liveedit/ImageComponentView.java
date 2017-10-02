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

    private final String IMAGE_CONTENT_SELECTOR = IMAGE_COMPONENT_VIEW + "//div[@name='imageContentSelector']";

    private final String DROPDOWN_HANDLER = IMAGE_COMPONENT_VIEW + DROP_DOWN_HANDLE_BUTTON;

    private final String UPLOAD_BUTTON = IMAGE_COMPONENT_VIEW + "//div[contains(@class,'upload-button')]";

    private final String ERROR_IN_COMPONENT_MESSAGE = IMAGE_COMPONENT_VIEW + "//div[contains(@class,'error-container')]";

    private final String DROP_DOWN_MODE_TOGGLER = IMAGE_CONTENT_SELECTOR + "//button[contains(@id,'ModeTogglerButton')]";


    @FindBy(xpath = DROPDOWN_HANDLER)
    private WebElement dropDownHandler;

    @FindBy(xpath = DROP_DOWN_MODE_TOGGLER)
    private WebElement dropDownModeToogler;

    @FindBy(xpath = UPLOAD_BUTTON)
    private WebElement uploadButton;

    public ImageComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectImageFromOptions( String imageName )
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

    public ImageComponentView clickOnDropDownHandler()
    {
        dropDownHandler.click();
        sleep( 400 );
        return this;
    }

    public ImageComponentView clickOnDropDownModeToggler()
    {
        dropDownModeToogler.click();
        sleep( 500 );
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

    public ImageComponentView clickOnExpanderInDropDownList( String folderName )
    {
        boolean isExpanderPresent = isExpanderPresent( folderName );
        if ( !isExpanderPresent )
        {
            saveScreenshot( "err_expander_icon" );
            throw new TestFrameworkException( "expander was not found in the dropdown" );
        }
        String expanderIcon = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        findElements( By.xpath( expanderIcon ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    private boolean isExpanderPresent( String folderName )
    {
        String expanderElement = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        boolean isPresent = isDynamicElementPresent( By.xpath( expanderElement ), 2 );
        if ( !isPresent )
        {
            getLogger().info( "expander for folder:" + folderName + " was not found! " );
            return false;
        }
        return true;
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
