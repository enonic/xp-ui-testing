package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SingleSelectorDropDownFormView
    extends FormViewPanel
{
    private String SINGLE_SELECTOR = "//div[contains(@id,'api.form.inputtype.singleselector.SingleSelector')]";

    protected final String DROP_DOWN = "//div[contains(@id,'api.ui.selector.dropdown.Dropdown')]";

    protected final String DROP_DOWN_OPTIONS_INPUT_XPATH = FORM_VIEW + DROP_DOWN + OPTION_FILTER_INPUT;

    public SingleSelectorDropDownFormView( final TestSession session )
    {
        super( session );
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
            findElements( By.xpath( DROP_DOWN_OPTIONS_INPUT_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect(
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

    public boolean isOptionFilterInputDisplayed()
    {
        List<WebElement> elements =
            findElements( By.xpath( DROP_DOWN_OPTIONS_INPUT_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect(
                Collectors.toList() );
        return elements.size() > 0;

    }

    public SingleSelectorDropDownFormView clickOnChangeOptionButton()
    {
        String elementXpath = SINGLE_SELECTOR + DROP_DOWN + "//button[contains(@id,'api.ui.selector.dropdown.SelectedOptionView')]";
        findElements( By.xpath( elementXpath ) ).get( 0 ).click();
        return this;
    }

    public String getSelectedOption( String checkboxId )
    {
        String elementXpath = SINGLE_SELECTOR + DROP_DOWN +
            "//button[contains(@id,'api.ui.selector.dropdown.SelectedOptionView')]//div[contains(@id,'DefaultOptionDisplayValueViewer')]";
        return findElements( By.xpath( elementXpath ) ).get( 0 ).getText();


    }


}
