package com.enonic.autotests.pages.form.liveedit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.LoaderComboBox;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 8/7/2017.
 */
public class FragmentComponentView
    extends UIComponent
{
    private final String FRAGMENT_COMPONENT_VIEW = "//div[contains(@id,'FragmentComponentView')]";

    private final String FRAGMENT_DROPDOWN_HANDLER = FRAGMENT_COMPONENT_VIEW + "//button[contains(@id,'DropdownHandle')]";

    @FindBy(xpath = FRAGMENT_DROPDOWN_HANDLER)
    private WebElement dropDownHandler;

    public FragmentComponentView( final TestSession session )
    {
        super( session );
    }

    /**
     * Types a name of the fragment and select the option
     *
     * @param fragmentDisplayName
     * @return
     */
    public LiveFormPanel selectFragment( String fragmentDisplayName )
    {
        WebElement optionFilterInput = findElement( By.xpath( FRAGMENT_COMPONENT_VIEW + COMBOBOX_OPTION_FILTER_INPUT ) );
        clearAndType( optionFilterInput, fragmentDisplayName );
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( fragmentDisplayName );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return new LiveFormPanel( getSession() );
    }

    public FragmentComponentView clickOnExpanderInDropDownList( String folderName )
    {
        boolean isExpanderPresent = isExpanderPresent( folderName );
        if ( !isExpanderPresent )
        {
            saveScreenshot( "err_expander_icon" );
            throw new TestFrameworkException( "expander was not found in the dropdown" );
        }
        String expanderIcon = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        getDisplayedElement( By.xpath( expanderIcon ) ).click();
        sleep( 500 );
        return this;
    }

    public boolean isExpanderPresent( String folderName )
    {
        String expanderElement = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        return isDynamicElementPresent( By.xpath( expanderElement ), 2 );
    }

    public FragmentComponentView clickOnDropDownHandler()
    {
        dropDownHandler.click();
        sleep( 700 );
        return this;
    }

    public FragmentComponentView clickOnOption( String fragmentName )
    {
        String optionXpath = String.format( NAMES_ICON_VIEW, fragmentName );
        if ( !isElementDisplayed( optionXpath ) )
        {
            saveScreenshot( "fragment_not_found" );
            throw new TestFrameworkException( "Image with name:  " + fragmentName + "  was not found!" );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
        return this;
    }
}
