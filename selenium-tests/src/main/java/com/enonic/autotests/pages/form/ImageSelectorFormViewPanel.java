package com.enonic.autotests.pages.form;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.RichComboBoxInput;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ImageSelectorFormViewPanel
    extends FormViewPanel
{
    public static final String IMAGES_PROPERTY = "images";

    protected final String CONTAINER_DIV = FORM_VIEW + "//div[contains(@id,'ImageSelector')]";

    protected final String OPTION_FILTER_INPUT = CONTAINER_DIV + COMBOBOX_OPTION_FILTER_INPUT;

    protected final String SELECTED_IMAGE_VIEW = CONTAINER_DIV + "//div[contains(@id,'ImageSelectorSelectedOptionView')]";

    protected String SELECTED_IMAGE_VIEW_BY_NAME =
        CONTAINER_DIV + "//div[contains(@id,'ImageSelectorSelectedOptionView') and descendant::div[@class='label' and text()='%s']]";

    protected final String SELECTED_IMAGES_CHECKBOXES =
        SELECTED_IMAGE_VIEW + "//div[@class='checkbox form-input']//input[@type='checkbox']";

    protected String CHECKBOX_OF_SELECTED_BY_NAME_IMAGE =
        SELECTED_IMAGE_VIEW_BY_NAME + "//div[@class='checkbox form-input']//input[@type='checkbox']";

    protected String DIV_CHECKBOX_OF_SELECTED_BY_NAME_IMAGE = SELECTED_IMAGE_VIEW_BY_NAME + "//div[contains(@class,'checkbox form-input')]";

    private final String SELECTED_IMAGES_NAMES = SELECTED_IMAGE_VIEW + "//div[@class='label']";

    private final String UPLOADER_BUTTON = CONTAINER_DIV + "//a[@class='dropzone']";

    private final String EDIT_BUTTON = "//div[contains(@id,'SelectionToolbar')]//button[child::span[contains(.,'Edit')]]";

    private final String REMOVE_BUTTON = "//div[contains(@id,'SelectionToolbar')]//button[child::span[contains(.,'Remove')]]";

    private final String REMOVE_BUTTON_LABEL = REMOVE_BUTTON + "/span";

    @FindBy(xpath = OPTION_FILTER_INPUT)
    protected WebElement optionFilterInput;

    @FindBy(xpath = UPLOADER_BUTTON)
    private WebElement uploaderButton;

    public ImageSelectorFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public boolean isOptionFilterIsDisplayed()
    {
        return optionFilterInput.isDisplayed();
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        for ( final String imageName : data.getStrings( IMAGES_PROPERTY ) )
        {
            clearAndType( optionFilterInput, imageName );
            sleep( 700 );
            richComboBoxInput.selectOption( imageName );
            sleep( 300 );
        }
        return this;
    }

    public boolean isRemoveButtonDisplayed()
    {
        return isElementDisplayed( REMOVE_BUTTON );
    }

    public boolean isEditButtonDisplayed()
    {
        return isElementDisplayed( EDIT_BUTTON );
    }

    public ImageSelectorFormViewPanel clickOnRemoveButton()
    {
        if ( !isRemoveButtonDisplayed() )
        {
            throw new TestFrameworkException( "Remove button was not found!" );
        }
        getDisplayedElement( By.xpath( REMOVE_BUTTON ) ).click();
        sleep( 500 );
        return this;
    }

    @Override
    public void clickOnAddButton()
    {
        throw new TestFrameworkException( "Add button should not be present on this Form!" );
    }

    private void setCheckedForCheckbox( String imageName, boolean value )
    {
        String checkboxXpath = String.format( CHECKBOX_OF_SELECTED_BY_NAME_IMAGE, imageName );
        WebElement checkbox = getDisplayedElement( By.xpath( checkboxXpath ) );
        TestUtils.setCheckboxChecked( getSession(), checkbox.getAttribute( "id" ), value );
    }

    public ImageSelectorFormViewPanel clickOnImage( String name )
    {
        List<WebElement> views = findElements( By.xpath( SELECTED_IMAGE_VIEW ) );

        List<WebElement> elements = findElements( By.xpath( SELECTED_IMAGES_NAMES ) );
        for ( int i = 0; i < elements.size(); i++ )
        {
            if ( getImageLabel( elements.get( i ) ).equals( name ) )
            {
                views.get( i ).click();
                break;
            }
        }
        sleep( 500 );
        return this;
    }

    public ImageSelectorFormViewPanel clickOnCheckboxAndSelectImage( String imageName )
    {
        String checkBox = String.format( DIV_CHECKBOX_OF_SELECTED_BY_NAME_IMAGE, imageName );
        if ( !isElementDisplayed( checkBox ) )
        {
            saveScreenshot( "err_checkbox_" + imageName );
            throw new TestFrameworkException( "checkbox for " + imageName );
        }
        WebElement ch = getDisplayedElement( By.xpath( checkBox ) );
        Actions builder = new Actions( getDriver() );
        builder.click( ch ).build().perform();
        sleep( 500 );
        return this;
    }

    public int getNumberFromRemoveButton()
    {
        if ( findElements( By.xpath( REMOVE_BUTTON_LABEL ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Remove label was not found!" );
        }
        String label = findElements( By.xpath( REMOVE_BUTTON_LABEL ) ).get( 0 ).getText();
        return Integer.valueOf( label.substring( label.indexOf( "(" ) + 1, label.indexOf( ")" ) ) );
    }

    public boolean isUploaderButtonEnabled()
    {
        return uploaderButton.isEnabled();
    }

    public List<String> getSelectedImages()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( SELECTED_IMAGE_VIEW ), 2 );
        if ( !result )
        {
            return Collections.emptyList();
        }
        return findElements( By.xpath( SELECTED_IMAGES_NAMES ) ).stream().map( e -> getImageLabel( e ) ).collect( Collectors.toList() );
    }

    private String getImageLabel( WebElement div )
    {
        String script = "return arguments[0].innerHTML";
        String text = (String) ( (JavascriptExecutor) getDriver() ).executeScript( script, div );
        return text;
    }

    public boolean isOptionSelected()
    {
        return findElements( By.xpath( SELECTED_IMAGE_VIEW ) ).size() > 0;
    }
}
