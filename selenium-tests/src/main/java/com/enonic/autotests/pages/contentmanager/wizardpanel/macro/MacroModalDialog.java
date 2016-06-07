package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class MacroModalDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'MacroModalDialog')]";

    public static String DIALOG_HEADER_TEXT = "Insert Macro";

    private final String DIALOG_HEADER = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]/h2";

    private final String URL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Url']]//input[@type='text']";

    private final String FILTER_INPUT = DIALOG_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT;

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Cancel']]";

    private final String INSERT_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Insert']]";

    private final String MACRO_SELECTED_VIEW = DIALOG_CONTAINER + "//div[contains(@id,'MacroSelectedOptionsView')]";

    private final String MACRO_SELECTED_DISPLAY_NAME = MACRO_SELECTED_VIEW + H6_DISPLAY_NAME;

    @FindBy(xpath = FILTER_INPUT)
    private WebElement optionFilterInput;

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = INSERT_BUTTON)
    private WebElement insertButton;


    public MacroModalDialog( TestSession session )
    {
        super( session );
    }

    public String getHeader()
    {
        return getDisplayedString( DIALOG_HEADER );
    }

    public boolean isCancelButtonDisplayed()
    {
        return isElementDisplayed( CANCEL_BUTTON );
    }

    public boolean isInsertButtonDisplayed()
    {
        return isElementDisplayed( INSERT_BUTTON );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );

    }

    public MacroModalDialog typeSearchText()
    {
        cancelButton.click();
        return this;
    }

    public String getSelectedMacroDisplayName()
    {
        return getDisplayedString( MACRO_SELECTED_DISPLAY_NAME );
    }

    public void clickOnCancel()
    {
        getDisplayedElement( By.xpath( URL_INPUT ) );
    }

    public boolean isOptionFilterDisplayed()
    {
        return isElementDisplayed( COMBOBOX_OPTION_FILTER_INPUT );
    }

    public MacroModalDialog selectOption( String name )
    {
        WebElement optionsInput = getDisplayedElement( By.xpath( DIALOG_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) );
        clearAndType( optionsInput, name );
        sleep( 500 );

        getDisplayedElement( By.xpath( String.format( COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME, name ) ) ).click();
        return this;
    }

    public void pressInsertButton()
    {
        getDisplayedElement( By.xpath( INSERT_BUTTON ) ).click();
        sleep( 400 );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_insert_link_dialog" );
            throw new TestFrameworkException( "InsertLink dialog was not opened!" );
        }
    }
}

