package com.enonic.autotests.pages.form.optionset;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 2/10/2017.
 */
public class SingleSelectionOptionSetView
    extends Application
{
    private final String CONTAINER =
        "//div[contains(@id,'FormView')]//div[contains(@id,'FormOptionSetOccurrenceView') and contains(@class,'single-selection')]";

    private final String DROPDOWN_HANDLER = CONTAINER + "//div[contains(@id,'Dropdown')]" + DROP_DOWN_HANDLE_BUTTON;

    private final String OPTION_SET_OCCUR_MENU_BUTTON =
        "//div[contains(@class,'single-selection-header selected')]" + "//button[contains(@id,'MoreButton')]";

    private final String OPTION_SET_NAME_INPUT =
        CONTAINER + "//div[contains(@id,'InputView') and descendant::div[text()='Name']]" + TEXT_INPUT;

    @FindBy(xpath = DROPDOWN_HANDLER)
    protected WebElement dropDownHandle;


    @FindBy(xpath = OPTION_SET_NAME_INPUT)
    protected WebElement setNameInput;


    public SingleSelectionOptionSetView( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER );
    }

    public SingleSelectionOptionSetView selectOption( String option )
    {
        dropDownHandle.click();
        sleep( 300 );
        String optionLocator = CONTAINER + String.format( NAMES_VIEW_BY_DISPLAY_NAME, option );
        waitUntilVisible( By.xpath( optionLocator ) );
        getDisplayedElement( By.xpath( optionLocator ) ).click();
        return this;
    }

    public SingleSelectionOptionSetView expandMenuClickOnMenuItem( int index, String menuItem )
    {
        List<WebElement> menuButtons = findElements( By.xpath( OPTION_SET_OCCUR_MENU_BUTTON ) );
        menuButtons.get( index ).click();
        sleep( 400 );
        selectMenuItem( menuItem );
        return this;
    }

    public SingleSelectionOptionSetView selectMenuItem( String menuItem )
    {
        String locatorMenuItem = "//div[contains(@id,'FormOptionSetOccurrenceView')]" +
            String.format( "//li[contains(@id,'MenuItem') and text()='%s']", menuItem );
        boolean isVisible = this.waitUntilVisibleNoException( By.xpath( locatorMenuItem ), EXPLICIT_QUICK );
        if ( !isVisible )
        {
            throw new TestFrameworkException( "Option Set - Menu item was not found: " + menuItem );
        }
        getDisplayedElement( By.xpath( locatorMenuItem ) ).click();
        sleep( 200 );
        return this;
    }

    public SingleSelectionOptionSetView typeSetName( String name )
    {
        clearAndType( setNameInput, name );
        return this;
    }
}
