package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

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
    public static String VALIDATION_MESSAGE_1_1 = "This field is required";

    protected final String COMBO_BOX = "//div[contains(@id,'ComboBox')]";

    protected final String COMBO_BOX_OPTIONS_INPUT_XPATH = FORM_VIEW + COMBO_BOX + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

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

    public ComboBoxFormViewPanel clickOnLastRemoveButton()
    {
        List<WebElement> allElements = findElements( By.xpath( FORM_VIEW + "//div[@class='selected-option']//a[@class='remove']" ) );

        List<WebElement> list = allElements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
        if ( list.size() == 0 )
        {
            throw new TestFrameworkException( "Remove button was not found" );
        }
        list.get( list.size() - 1 ).click();
        sleep( 500 );
        return this;

    }

    protected void selectOption( String option )
    {
        List<WebElement> elements = findElements( By.xpath(
            String.format( "//div[@class='slick-viewport']//div[contains(@id,'ComboBoxDisplayValueViewer') and text()='%s']", option ) ) );
        List<WebElement> displayedElements = elements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
        if ( displayedElements.size() == 0 )
        {
            throw new TestFrameworkException( "option was not found! " + option );
        }
        displayedElements.get( 0 ).click();
    }

    public boolean isOptionFilterInputEnabled()
    {
        return optionFilterInput.isEnabled();
    }

    public List<String> getSelectedOptionValues()
    {
        List<WebElement> elements = findElements( By.xpath( FORM_VIEW + "//div[@class='selected-option']//div[@class='option-value']" ) );
        return elements.stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public String getValidationMessage()
    {
        if ( isValidationMessagePresent() )
        {
            return findElements( By.xpath( VALIDATION_VIEWER + "//li" ) ).get( 0 ).getText();
        }
        else
        {
            throw new TestFrameworkException( "validation message was not found!" );
        }
    }
}
