package com.enonic.autotests.pages.form;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ImageSelectorFormViewPanel
    extends FormViewPanel
{
    public static final String IMAGES_PROPERTY = "images";

    protected final String CONTAINER_DIV = FORM_VIEW + "//div[contains(@id,'ImageSelector')]";

    protected final String OPTION_FILTER_INPUT = CONTAINER_DIV + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    protected final String SELECTED_IMAGES_VIEW = CONTAINER_DIV + "//div[contains(@id,'ImageSelectorSelectedOptionView')]";

    private final String SELECTED_IMAGES_NAME = SELECTED_IMAGES_VIEW + "//div[@class='label']";

    private String COMBOBOX_OPTIONS_ITEM = "//div[@class='slick-viewport']//div[contains(@id,'ImageSelectorViewer')]//h6[@title='%s']";

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
        for ( final String imageName : data.getStrings( IMAGES_PROPERTY ) )
        {
            clearAndType( optionFilterInput, imageName );
            sleep( 700 );
            selectOption( imageName );
            sleep( 300 );
        }
        return this;
    }

    public boolean isRemoveButtonDisplayed()
    {
        return findElements( By.xpath( REMOVE_BUTTON ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isEditButtonDisplayed()
    {
        return findElements( By.xpath( EDIT_BUTTON ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }


    public ImageSelectorFormViewPanel clickOnRemoveButton()
    {
        if ( findElements( By.xpath( REMOVE_BUTTON ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Remove button was not found!" );
        }
        findElements( By.xpath( REMOVE_BUTTON ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    public ImageSelectorFormViewPanel clickOnImage( String name )
    {
        List<WebElement> views = findElements( By.xpath( SELECTED_IMAGES_VIEW ) );

        List<WebElement> elements = findElements( By.xpath( SELECTED_IMAGES_NAME ) );
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

    public boolean isUploaderButtonEnabled()
    {
        return uploaderButton.isEnabled();
    }

    public List<String> getSelectedImages()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( SELECTED_IMAGES_VIEW ), 2 );
        if ( !result )
        {
            return Collections.emptyList();
        }
        return findElements( By.xpath( SELECTED_IMAGES_NAME ) ).stream().map( e -> getImageLabel( e ) ).collect( Collectors.toList() );
    }

    private String getImageLabel( WebElement div )
    {
        String script = "return arguments[0].innerText";
        String text = (String) ( (JavascriptExecutor) getDriver() ).executeScript( script, div );
        return text;
    }

    protected void selectOption( String option )
    {
        waitUntilVisibleNoException( By.xpath( String.format( COMBOBOX_OPTIONS_ITEM, option ) ), 2 );
        List<WebElement> elements = findElements( By.xpath( String.format( COMBOBOX_OPTIONS_ITEM, option ) ) );
        List<WebElement> displayedElements = elements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
        if ( displayedElements.size() == 0 )
        {
            throw new TestFrameworkException( "option was not found! " + option );
        }
        displayedElements.get( 0 ).click();
    }

}
