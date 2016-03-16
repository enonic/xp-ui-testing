package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Strings;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DatePickerPopup;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class DateFormViewPanel
    extends FormViewPanel
{
    public static String DATE_PROPERTY = "date";

    private final String DATE_INPUT_XPATH =
        FORM_VIEW + "//div[contains(@id,'api.form.InputView')]//div[contains(@id,'inputtype.time.Date')]//input[contains(@id,'TextInput')]";

    @FindBy(xpath = DATE_INPUT_XPATH)
    private WebElement dateInput;


    public DateFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public DatePickerPopup showPicker()
    {
        Actions builder = new Actions( getDriver() );
        builder.click( dateInput ).build().perform();
        DatePickerPopup popup = new DatePickerPopup( getSession() );
        popup.waitUntilDialogLoaded();
        return popup;
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String date = data.getString( DATE_PROPERTY );
        if ( !Strings.isNullOrEmpty( date ) )
        {
            clearAndType( dateInput, date );
        }
        sleep( 300 );
        return this;
    }

    public boolean isInvalidDate()
    {
        WebElement input = getDisplayedElement( By.xpath( DATE_INPUT_XPATH ) );
        return waitAndCheckAttrValue( input, "class", "invalid", 1 );
    }

    public boolean isDateInputDisplayed()
    {
        return dateInput.isDisplayed();
    }

    public String getDateValue()
    {
        return dateInput.getAttribute( "value" );
    }
}
