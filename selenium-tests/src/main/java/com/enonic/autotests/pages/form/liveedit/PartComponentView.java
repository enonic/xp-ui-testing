package com.enonic.autotests.pages.form.liveedit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class PartComponentView
    extends UIComponent
{
    private final String COMPONENT_CONTAINER = "//div[contains(@id,'PartComponentView')]";

    private final String DROPDOWN_HANDLER = COMPONENT_CONTAINER + DROP_DOWN_HANDLE_BUTTON;

    public static String PART_XPATH = "//div[contains(@id,'PartDescriptorViewer') and descendant::p[contains(.,'%s')]]";

    @FindBy(xpath = DROPDOWN_HANDLER)
    private WebElement dropDownHandler;

    public PartComponentView( final TestSession session )
    {
        super( session );
    }

    public LiveFormPanel selectItem( String partName )
    {
        if ( !isElementDisplayed( COMPONENT_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_opt_filter" ) );
            throw new TestFrameworkException( "option filter input was not displayed" );
        }
        getDisplayedElement( By.xpath( COMPONENT_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) ).sendKeys( partName );
        sleep( 400 );
        if ( !isPartExists( partName ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_" + partName ) );
            throw new TestFrameworkException( "The part with name: " + partName + "  was not found!" );
        }
        clickOnOptionsItem( partName );
        sleep( 1000 );
        return new LiveFormPanel( getSession() );
    }

    private boolean isPartExists( String partName )
    {
        return isElementDisplayed( COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, partName ) );
    }

    private void clickOnOptionsItem( String partName )
    {
        getDisplayedElement( By.xpath( COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, partName ) ) ).click();
    }

    public PartComponentView clickOnExpanderInDropDownList( String folderName )
    {
        boolean isExpanderPresent = isExpanderPresent( folderName );
        if ( !isExpanderPresent )
        {
            saveScreenshot( "err_expander_icon" );
            throw new TestFrameworkException( "expander was not found in the dropdown" );
        }
        String expanderIcon = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        getDisplayedElement( By.xpath( expanderIcon ) ).click();
        sleep( 400 );
        return this;
    }

    public boolean isExpanderPresent( String folderName )
    {
        String expanderElement = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        return isDynamicElementPresent( By.xpath( expanderElement ), 2 );
    }

    public PartComponentView clickOnDropDownHandler()
    {
        dropDownHandler.click();
        sleep( 400 );
        return this;
    }
}
