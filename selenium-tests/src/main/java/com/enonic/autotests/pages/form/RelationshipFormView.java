package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.RichComboBoxInput;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class RelationshipFormView
    extends FormViewPanel
{
    public static final String RELATIONSHIPS_PROPERTY = "relationships";

    protected final String CONTAINER_DIV = FORM_VIEW + "//div[contains(@id,'ContentSelector')]";

    protected final String OPTION_FILTER_INPUT = CONTAINER_DIV + COMBOBOX_OPTION_FILTER_INPUT;

    protected final String STEP_XPATH = "//li[contains(@id,'TabBarItem') and child::a[contains(.,'Relationship')]]";

    private String REMOVE_TARGET_BUTTON =
        "//div[contains(@id,'ContentSelectedOptionView') and descendant::h6[contains(@class,'main-name') and text()='%s']]//a[@class='remove']";

    @FindBy(xpath = OPTION_FILTER_INPUT)
    protected WebElement optionFilterInput;

    public RelationshipFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        List<String> alreadySelected = getDisplayNamesOfSelectedFiles();
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        for ( final String imageDisplayName : data.getStrings( RELATIONSHIPS_PROPERTY ) )
        {
            if ( !alreadySelected.contains( imageDisplayName ) )
            {
                clearAndType( optionFilterInput, imageDisplayName );
                sleep( 700 );
                richComboBoxInput.selectOption( imageDisplayName );
                sleep( 300 );
            }
        }
        return this;
    }

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( STEP_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public long getNumberOfSelectedFiles()
    {
        return getNumberOfElements( By.xpath( CONTAINER_DIV + CONTENT_SELECTED_OPTION_VIEW ) );
    }

    public List<String> getNamesOfSelectedFiles()
    {
        return getDisplayedStrings( By.xpath( CONTAINER_DIV + CONTENT_SELECTED_OPTION_VIEW + P_NAME ) );
    }

    public List<String> getDisplayNamesOfSelectedFiles()
    {
        return getDisplayedStrings( By.xpath( CONTAINER_DIV + CONTENT_SELECTED_OPTION_VIEW + H6_MAIN_NAME ) );
    }

    public boolean isOptionFilterDisplayed()
    {
        return isElementDisplayed( OPTION_FILTER_INPUT );
    }

    public void removeSelectedFile( String fileName )
    {
        String removeButtonXpath = String.format( REMOVE_TARGET_BUTTON, fileName );
        boolean isDisplayed = waitUntilVisibleNoException( By.xpath( CONTAINER_DIV + removeButtonXpath ), Application.EXPLICIT_NORMAL );
        if ( !isDisplayed )
        {
            saveScreenshot( "err_remove_button_relationship" );
            throw new TestFrameworkException( "Button remove for " + fileName + " was not found!" );
        }
        getDisplayedElement( By.xpath( CONTAINER_DIV + removeButtonXpath ) ).click();
    }
}
