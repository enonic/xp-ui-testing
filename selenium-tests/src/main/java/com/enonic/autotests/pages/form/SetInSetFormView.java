package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 5/15/2017.
 */
public class SetInSetFormView
    extends FormViewPanel
{
    public static final String LAST_NAME_KEY = "last_name";

    public static final String FIRST_NAME_KEY = "first_name";

    private final String FIRST_NAME_INPUT_VIEW = "//div[contains(@id,'InputView') and descendant::div[text()='First Name']]";

    private final String FIRST_NAME_INPUT = FIRST_NAME_INPUT_VIEW + TEXT_INPUT;

    private final String LAST_NAME_INPUT_VIEW = "//div[contains(@id,'InputView') and descendant::div[text()='Last Name']]";

    private final String LAST_NAME_NAME_INPUT = LAST_NAME_INPUT_VIEW + TEXT_INPUT;

    private final String FIRST_NAME_VALIDATION_MESSAGE = FIRST_NAME_INPUT_VIEW + VALIDATION_RECORDING_VIEWER;

    private final String LAST_NAME_VALIDATION_MESSAGE = LAST_NAME_INPUT_VIEW + VALIDATION_RECORDING_VIEWER;

    private final String ADD_CONTACT_INFO_BUTTON = FORM_VIEW + "//button[contains(@id,'Button') and child::span[text()='Add']]";

    private final String ADD_PHONE_NUMBERS_BUTTON = FORM_VIEW + "//button[child::span[text()='Add']]";

    @FindBy(xpath = FIRST_NAME_INPUT)
    protected WebElement firstNameInput;

    @FindBy(xpath = LAST_NAME_NAME_INPUT)
    protected WebElement lastNameInput;

    /**
     * The constructor.
     *
     * @param session
     */
    public SetInSetFormView( final TestSession session )
    {
        super( session );
    }

    public SetInSetFormView typeNameAndLastName( final PropertyTree data )
    {
        String firstName = data.getString( FIRST_NAME_KEY );
        String lastName = data.getString( LAST_NAME_KEY );
        return typeNameAndLastName( firstName, lastName );
    }

    public SetInSetFormView typeNameAndLastName( final String firstName, final String lastName )
    {
        clearAndType( firstNameInput, firstName );
        clearAndType( lastNameInput, lastName );
        return this;
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        typeNameAndLastName( data );
        return this;
    }

    public SetInSetFormView clickOnAddContactInfoButton()
    {
        if ( !isElementDisplayed( ADD_CONTACT_INFO_BUTTON ) )
        {
            saveScreenshot( "err_add_contact_button" );
            throw new TestFrameworkException( "'Add contact info'button was not found!" );
        }
        getDisplayedElement( By.xpath( ADD_CONTACT_INFO_BUTTON ) ).click();
        sleep( 400 );
        return this;
    }

    public SetInSetFormView clickOnAddPhoneNumberButton()
    {
        if ( !isElementDisplayed( ADD_PHONE_NUMBERS_BUTTON ) )
        {
            saveScreenshot( "err_add_phone_button" );
            throw new TestFrameworkException( "'Add phone number' button was not found!" );
        }
        getDisplayedElement( By.xpath( ADD_PHONE_NUMBERS_BUTTON ) ).click();
        sleep( 400 );
        return this;
    }

    public SetInSetFormView selectImage( String imageName )
    {
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        clearAndType( getDisplayedElement( By.xpath( COMBOBOX_OPTION_FILTER_INPUT ) ), imageName );
        sleep( 800 );
        loaderComboBox.selectOption( imageName );
        sleep( 300 );
        return this;
    }

    public String getValidationMessageForFirstNameInput()
    {
        return getDisplayedString( FIRST_NAME_VALIDATION_MESSAGE );
    }

    public String getValidationMessageForLastNameInput()
    {
        return getDisplayedString( LAST_NAME_VALIDATION_MESSAGE );
    }

}
