package com.enonic.autotests.pages.form.liveedit;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView;
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentInfoWidget;
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView;
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageEmulatorPanel;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Contains 'Widget Selector Dropdown' and Context Window (Insert , Page tab items)
 */
public class WizardContextPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'ContentWizardPanel')]//div[contains(@id,'ContextView')]";

    private final String WIDGET_SELECTOR_DROPDOWN = CONTAINER + "//div[contains(@id,'WidgetSelectorDropdown')]";

    private final String VERSION_HISTORY_OPTION_ITEM =
        WIDGET_SELECTOR_DROPDOWN + String.format( NAMES_VIEW_BY_DISPLAY_NAME, "Version history" );

    private final String DETAILS_OPTION_ITEM = WIDGET_SELECTOR_DROPDOWN + "//div[text()='Details']";

    private final String WIDGET_SELECTOR_OPTIONS = WIDGET_SELECTOR_DROPDOWN + H6_DISPLAY_NAME;

    private final String DEPENDENCIES_OPTION_ITEM = WIDGET_SELECTOR_DROPDOWN + String.format( NAMES_VIEW_BY_DISPLAY_NAME, "Dependencies" );

    private final String EMULATOR_OPTION_ITEM = WIDGET_SELECTOR_DROPDOWN + String.format( NAMES_VIEW_BY_DISPLAY_NAME, "Emulator" );

    private final String WIDGET_SELECTOR_DROPDOWN_HANDLER = WIDGET_SELECTOR_DROPDOWN + DROP_DOWN_HANDLE_BUTTON;

    @FindBy(xpath = WIDGET_SELECTOR_DROPDOWN_HANDLER)
    private WebElement widgetSelectorDropDownHandler;

    public WizardContextPanel( final TestSession session )
    {
        super( session );
    }

    public void waitForLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( CONTAINER ), EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "err_floating_context_panel" ) );
            throw new TestFrameworkException( "LIVE EDIT:  FloatingContextPanel  was not loaded!" );
        }
    }

    public DependenciesWidgetItemView openDependenciesWidget()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        DependenciesWidgetItemView dependenciesWidgetItemView = new DependenciesWidgetItemView( getSession() );
        //click on drop down handler and show options
        widgetSelectorDropDownHandler.click();
        sleep( 500 );
        if ( !isElementDisplayed( DEPENDENCIES_OPTION_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_dependencies_option" ) );
            throw new TestFrameworkException( "Content Wizard - dependencies option was not found!" );
        }
        getDisplayedElement( By.xpath( DEPENDENCIES_OPTION_ITEM ) ).click();
        sleep( 700 );
        return dependenciesWidgetItemView;
    }

    public PageEmulatorPanel openEmulatorWidget()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        PageEmulatorPanel pageEmulatorPanel = new PageEmulatorPanel( getSession() );
        //click on drop down handler and show options
        widgetSelectorDropDownHandler.click();
        sleep( 500 );
        if ( !isElementDisplayed( EMULATOR_OPTION_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_dependencies_option" ) );
            throw new TestFrameworkException( "Content Wizard - dependencies option was not found!" );
        }
        getDisplayedElement( By.xpath( EMULATOR_OPTION_ITEM ) ).click();
        sleep( 700 );
        return pageEmulatorPanel;
    }

    public AllContentVersionsView openVersionHistory()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        widgetSelectorDropDownHandler.click();
        sleep( 500 );
        if ( !isElementDisplayed( VERSION_HISTORY_OPTION_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_history_opt" ) );
            throw new TestFrameworkException( "Version history option was not found!" );
        }
        getDisplayedElement( By.xpath( VERSION_HISTORY_OPTION_ITEM ) ).click();
        sleep( 500 );
        AllContentVersionsView versions = new AllContentVersionsView( getSession() );
        versions.waitUntilLoaded();
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return versions;
    }

    public ContentInfoWidget openDetailsWidget()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        ContentInfoWidget detailsWidget = new ContentInfoWidget( getSession() );
        widgetSelectorDropDownHandler.click();
        if ( !isElementDisplayed( DETAILS_OPTION_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_details_option" ) );
            throw new TestFrameworkException( "Content Wizard - Details option was not found!" );
        }
        getDisplayedElement( By.xpath( DETAILS_OPTION_ITEM ) ).click();
        sleep( 700 );
        return detailsWidget;
    }

    public boolean isWidgetSelectorVisible()
    {
        return isElementDisplayed( By.xpath( WIDGET_SELECTOR_DROPDOWN ) );
    }

}
