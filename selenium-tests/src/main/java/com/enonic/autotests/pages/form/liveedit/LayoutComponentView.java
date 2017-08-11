package com.enonic.autotests.pages.form.liveedit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LayoutComponentView
    extends UIComponent
{
    private final String LAYOUT_COMPONENT_CONTAINER = "//div[contains(@id,'LayoutComponentView')]";

    public static String REGION_XPATH = "//div[contains(@id,'RegionView') and descendant::p[contains(.,'%s')]]";

    private final String DROPDOWN_HANDLER = LAYOUT_COMPONENT_CONTAINER + DROP_DOWN_HANDLE_BUTTON;

    @FindBy(xpath = LAYOUT_COMPONENT_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT)
    private WebElement optionFilterInput;

    @FindBy(xpath = DROPDOWN_HANDLER)
    private WebElement dropDownHandler;

    public LayoutComponentView( final TestSession session )
    {
        super( session );
    }

    /**
     * Types a name of layout and click on the option
     *
     * @param layoutName
     * @return
     */
    public LiveFormPanel selectLayout( String layoutName )
    {
        optionFilterInput.sendKeys( layoutName );
        sleep( 900 );
        if ( !isLayoutExists( layoutName ) )
        {
            saveScreenshot( "err_" + layoutName );
            throw new TestFrameworkException( "The layout with name: " + layoutName + "  was not found!" );
        }
        clickOnOptionsItem( layoutName );
        return new LiveFormPanel( getSession() );
    }

    public LayoutComponentView clickOnDropDownHandler()
    {
        dropDownHandler.click();
        sleep( 400 );
        return this;
    }

    private boolean isLayoutExists( String layoutName )
    {
        return isElementDisplayed( LAYOUT_COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, layoutName ) );
    }

    private void clickOnOptionsItem( String layoutName )
    {
        getDisplayedElement( By.xpath( LAYOUT_COMPONENT_CONTAINER + String.format( NAMES_ICON_VIEW, layoutName ) ) ).click();
    }

    public LayoutComponentView clickOnExpanderInDropDownList( String folderName )
    {
        boolean isExpanderPresent = isExpanderPresent( folderName );
        if ( !isExpanderPresent )
        {
            saveScreenshot( "err_expander_icon" );
            throw new TestFrameworkException( "expander was not found in the dropdown" );
        }
        String expanderIcon = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        getDisplayedElement( By.xpath( expanderIcon ) ).click();
        sleep( 600 );
        return this;
    }

    public boolean isExpanderPresent( String folderName )
    {
        String expanderElement = String.format( DROP_DOWN_ITEM_EXPANDER, folderName );
        return isDynamicElementPresent( By.xpath( expanderElement ), 2 );
    }
}