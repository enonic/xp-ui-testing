package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ComboBoxFormViewPanel
    extends FormViewPanel

{
    protected final String COMBO_BOX = "//div[contains(@id,'ComboBox')]";

    protected final String COMBO_BOX_OPTIONS_INPUT_XPATH = FORM_VIEW + COMBO_BOX + COMBOBOX_OPTION_FILTER_INPUT;

    private final String REMOVE_SELECTED_OPTION_BUTTON =
        FORM_VIEW + "//div[contains(@id,'BaseSelectedOptionView')]//a[@class='remove']";

    private String COMBOBOX_OPTION =
        "//div[contains(@class,'slick-viewport')]//div[contains(@id,'ComboBoxDisplayValueViewer') and text()='%s']";

    @FindBy(xpath = COMBO_BOX_OPTIONS_INPUT_XPATH)
    protected WebElement optionFilterInput;

    public ComboBoxFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        for ( final String option : data.getStrings( "options" ) )
        {
            clearAndType( optionFilterInput, option );
            sleep( 700 );
            selectOption( option );
            sleep( 300 );
        }
        return this;
    }

    public ComboBoxFormViewPanel typeNameOfOptionAndSelectOption( String option )
    {
        clearAndType( optionFilterInput, option );
        sleep( 700 );
        selectOption( option );
        sleep( 300 );
        return this;
    }

    @Override
    public void clickOnAddButton()
    {
        throw new TestFrameworkException( "Add button should not be present on this Form!" );
    }

    public ComboBoxFormViewPanel clickOnLastRemoveButton()
    {
        List<WebElement> list = getDisplayedElements( By.xpath( REMOVE_SELECTED_OPTION_BUTTON ) );
        if ( list.size() == 0 )
        {
            saveScreenshot( "err_remove_option_in_combobox" );
            throw new TestFrameworkException( "Remove button was not found" );
        }
        list.get( list.size() - 1 ).click();
        sleep( 500 );
        return this;
    }

    protected void selectOption( String option )
    {
        String optionXpath = String.format( COMBOBOX_OPTION, option );
        if ( !isElementDisplayed( optionXpath ) )
        {
            throw new TestFrameworkException( "option was not found! " + option );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
    }

    public boolean isOptionFilterInputEnabled()
    {
        return optionFilterInput.isEnabled();
    }

    public List<String> getSelectedOptionValues()
    {
        return getDisplayedStrings( By.xpath( FORM_VIEW + "//div[@class='selected-option']//div[@class='option-value']" ) );
    }
}
