package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.ContentSettings;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SettingsWizardStepForm
    extends WizardStepForm
{
    private final String FORM_CONTAINER = "//div[contains(@id,'SettingsWizardStepForm')]";

    private final String LOCALE_COMBOBOX = FORM_CONTAINER + "//div[contains(@id,'LocaleComboBox')]";

    private final String PRINCIPAL_COMBOBOX = FORM_CONTAINER + "//div[contains(@id,'PrincipalComboBox')]";

    private final String LANGUAGE_FILTER_INPUT = LOCALE_COMBOBOX + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private final String PRINCIPAL_FILTER_INPUT = PRINCIPAL_COMBOBOX + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private String NAME_ITEM = "//h6[@class='main-name' and text()='%s']";

    private String REMOVE_LANG_BUTTON = LOCALE_COMBOBOX +
        "//div[contains(@class,'selected-options locale-selected-options-view') and descendant::h6[text()='%s']]//a[@class='icon-close']";

    private String REMOVE_OWNER_BUTTON = PRINCIPAL_COMBOBOX +
        "//div[contains(@class,'selected-options principal-selected-options-view') and descendant::h6[text()='%s']]//a[@class='icon-close']";

    private String LOCALE_TEXT =
        LOCALE_COMBOBOX + "//div[contains(@class,'selected-options locale-selected-options-view')]//h6[@class='main-name']";

    private String OWNER_TEXT =
        PRINCIPAL_COMBOBOX + "//div[contains(@class,'selected-options principal-selected-options-view')]//h6[@class='main-name']";


    @FindBy(xpath = LANGUAGE_FILTER_INPUT)
    WebElement languageFilterInput;

    @FindBy(xpath = PRINCIPAL_FILTER_INPUT)
    WebElement ownerFilterInput;


    public SettingsWizardStepForm( final TestSession session )
    {
        super( session );
    }

    public SettingsWizardStepForm typeSettings( ContentSettings settings )
    {
        selectLanguage( settings.getLanguage() );
        if ( settings.getOwner() != null )
        {
            selectOwner( settings.getOwner() );
        }
        return this;
    }

    public SettingsWizardStepForm removeLanguage( String language )
    {
        if ( findElements( By.xpath( String.format( REMOVE_LANG_BUTTON, language ) ) ).stream().filter( WebElement::isDisplayed ).count() ==
            0 )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + language );
            throw new TestFrameworkException( "language was not found in settings-form:  " + language );
        }
        findElements( By.xpath( String.format( REMOVE_LANG_BUTTON, language ) ) ).stream().filter(
            WebElement::isDisplayed ).findFirst().get().click();
        return this;
    }

    public SettingsWizardStepForm removeOwner( String owner )
    {
        if ( findElements( By.xpath( String.format( REMOVE_OWNER_BUTTON, owner ) ) ).stream().filter( WebElement::isDisplayed ).count() ==
            0 )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + owner );
            throw new TestFrameworkException( "Owner was not found in settings-form:  " + owner );
        }

        findElements( By.xpath( String.format( REMOVE_OWNER_BUTTON, owner ) ) ).stream().filter(
            WebElement::isDisplayed ).findFirst().get().click();
        sleep( 500 );
        return this;
    }

    public void selectLanguage( String language )
    {
        String languageSearch = language.substring( 0, language.indexOf( "(" ) - 1 );
        clearAndType( languageFilterInput, languageSearch );
        String optionItemXpath = LOCALE_COMBOBOX + "//div[contains(@class,'slick-cell')]" + String.format( NAME_ITEM, language );
        if ( findElements( By.xpath( optionItemXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "locale was not found!  " + language );
        }
        findElements( By.xpath( optionItemXpath ) ).get( 0 ).click();
    }

    public void selectOwner( String owner )
    {
        clearAndType( ownerFilterInput, owner );
        sleep( 700 );
        if ( findElements( By.xpath( PRINCIPAL_COMBOBOX + String.format( NAME_ITEM, owner ) ) ).stream().filter(
            WebElement::isDisplayed ).count() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), "err_select_" + owner );
            throw new TestFrameworkException( "owner was not found!  " + owner );
        }
        findElements( By.xpath( PRINCIPAL_COMBOBOX + String.format( NAME_ITEM, owner ) ) ).stream().filter(
            WebElement::isDisplayed ).findFirst().get().click();
        sleep( 500 );
    }

    public String getLanguage()
    {
        if ( findElements( By.xpath( LOCALE_TEXT ) ).stream().map( WebElement::getText ).count() > 0 )
        {
            return findElements( By.xpath( LOCALE_TEXT ) ).stream().map( WebElement::getText ).findFirst().get();
        }
        return null;
    }

    public String getOwner()
    {
        if ( findElements( By.xpath( OWNER_TEXT ) ).stream().map( WebElement::getText ).count() > 0 )
        {
            return findElements( By.xpath( OWNER_TEXT ) ).stream().map( WebElement::getText ).findFirst().get();
        }
        return null;
    }

    public boolean waitUntilDisplayed()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( FORM_CONTAINER ), Application.EXPLICIT_NORMAL );
        return result;
    }

    public boolean isLanguageInputFilterPresent()
    {
        return findElements( By.xpath( LANGUAGE_FILTER_INPUT ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

}
