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

public class SSComboBoxFormViewPanel
    extends ComboBoxFormViewPanel
{

    protected final String ADD_BUTTON_XPATH = FORM_VIEW +
        "//button[contains(@id,'api.ui.button.Button') and child::span[text()='Add'] and not(contains(@style,'display: none'))]";

    @FindBy(xpath = ADD_BUTTON_XPATH)
    WebElement addButton;

    public SSComboBoxFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        for ( final String option : data.getStrings( "options" ) )
        {
            List<WebElement> elements =
                findElements( By.xpath( COMBO_BOX_OPTIONS_INPUT_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect(
                    Collectors.toList() );

            clearAndType( elements.get( 0 ), option );
            sleep( 700 );
            selectOption( option );
            findElements( By.xpath( ADD_BUTTON_XPATH ) ).get( 0 ).click();
            sleep( 400 );
        }

        return this;
    }

    protected void selectOption( String option )
    {
        List<WebElement> elements = findElements( By.xpath(
            String.format( "//div[@class='slick-viewport']//div[contains(@id,'DefaultOptionDisplayValueViewer') and text()='%s']",
                           option ) ) );
        List<WebElement> displayedElements = elements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
        if ( displayedElements.size() == 0 )
        {
            throw new TestFrameworkException( "option was not found! " + option );
        }
        displayedElements.get( 0 ).click();
        sleep( 300 );
    }

    public List<String> getSelectedOptions()
    {
        List<WebElement> elements = findElements( By.xpath( FORM_VIEW + "//div[@class='selected-option']//div[@class='option-value']" ) );
        return elements.stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public boolean isOptionFilterInputEnabled()
    {
        List<WebElement> elements =
            findElements( By.xpath( COMBO_BOX_OPTIONS_INPUT_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect(
                Collectors.toList() );
        if ( elements.size() == 0 )
        {
            throw new TestFrameworkException( "single selector: option filter was not found!" );
        }
        return elements.get( 0 ).isEnabled();
    }

    public boolean isAddButtonPresent()
    {
        return findElements( By.xpath( ADD_BUTTON_XPATH ) ).size() > 0;
    }
}

