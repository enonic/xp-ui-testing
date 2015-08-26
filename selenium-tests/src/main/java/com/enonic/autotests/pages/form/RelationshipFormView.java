package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class RelationshipFormView
    extends FormViewPanel
{
    public static final String RELATIONSHIPS_PROPERTY = "relationships";

    protected final String CONTAINER_DIV = FORM_VIEW + "//div[contains(@id,'relationship.ContentSelector')]";

    protected final String OPTION_FILTER_INPUT = CONTAINER_DIV + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private String COMBOBOX_OPTIONS_ITEM = "//div[@class='slick-viewport']//div[contains(@id,'ContentSummaryViewer')]//h6[text()='%s']";

    protected final String STEP_XPATH = "//li[contains(@id,'api.ui.tab.TabBarItem')]//span[text()='Relationship']";

    @FindBy(xpath = OPTION_FILTER_INPUT)
    protected WebElement optionFilterInput;

    public RelationshipFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        List<String> alreadySelected = getNamesOfSelectedFiles();
        for ( final String imageName : data.getStrings( RELATIONSHIPS_PROPERTY ) )
        {
            if ( !alreadySelected.contains( imageName ) )
            {
                clearAndType( optionFilterInput, imageName );
                sleep( 700 );
                selectOption( imageName );
                sleep( 300 );
            }

        }
        return this;
    }

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( STEP_XPATH ), Application.EXPLICIT_NORMAL );
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
        elements.get( 0 ).click();
    }

    public long getNumberOfSelectedFiles()
    {
        return findElements( By.xpath( CONTAINER_DIV + "//div[contains(@id,'ContentSelectedOptionView')]" ) ).stream().filter(
            WebElement::isDisplayed ).count();

    }

    public List<String> getNamesOfSelectedFiles()
    {
        List<String> list = findElements(
            By.xpath( CONTAINER_DIV + "//div[contains(@id,'ContentSelectedOptionView')]//h6[@class='main-name']" ) ).stream().filter(
            WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
        return list;
    }

    public boolean isOptionFilterDisplayed()
    {
        return findElements( By.xpath( OPTION_FILTER_INPUT ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public void removeSelectedFile( String fileName )
    {
        String removeButtonXpath = String.format(
            "//div[contains(@id,'ContentSelectedOptionView') and descendant::h6[@class='main-name' and text()='%s']]//a[@class='remove']",
            fileName );
        boolean isDisplayed = waitUntilVisibleNoException( By.xpath( CONTAINER_DIV + removeButtonXpath ), Application.EXPLICIT_NORMAL );
        if ( !isDisplayed )
        {
            throw new TestFrameworkException( "Button remove for " + fileName + " was not found!" );
        }
        findElements( By.xpath( CONTAINER_DIV + removeButtonXpath ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().click();
    }
}
