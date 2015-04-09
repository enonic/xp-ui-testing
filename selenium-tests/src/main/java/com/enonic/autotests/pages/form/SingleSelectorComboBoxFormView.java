package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SingleSelectorComboBoxFormView
    extends ComboBoxFormViewPanel
{
    public SingleSelectorComboBoxFormView( final TestSession session )
    {
        super( session );
    }

    public ComboBoxFormViewPanel clickOnRemoveOptionButton()
    {
        return clickOnLastRemoveButton();

    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String option = data.getString( "option" );
        if ( option == null )
        {
            return this;
        }
        List<WebElement> elements =
            findElements( By.xpath( COMBO_BOX_OPTIONS_INPUT_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect(
                Collectors.toList() );
        if ( elements.size() == 0 )
        {
            throw new TestFrameworkException( "options filter input was not found" );
        }
        WebElement optionsInput = elements.get( 0 );

        clearAndType( optionsInput, option );
        sleep( 700 );
        selectOption( option );
        sleep( 300 );
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

    public String getSelectedOption()
    {
        List<WebElement> elements = findElements( By.xpath( FORM_VIEW + "//div[@class='selected-option']//div[@class='option-value']" ) );
        if ( elements.size() == 0 )
        {
            return "";
        }
        return elements.get( 0 ).getText();
    }

    public boolean isOptionFilterInputDisplayed()
    {
        List<WebElement> elements =
            findElements( By.xpath( COMBO_BOX_OPTIONS_INPUT_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect(
                Collectors.toList() );
        return elements.size() > 0;

    }
}

