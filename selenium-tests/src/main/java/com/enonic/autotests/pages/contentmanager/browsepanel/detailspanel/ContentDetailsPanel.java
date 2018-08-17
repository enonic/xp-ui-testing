package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentDetailsPanel
    extends Application
{
    private final String SPLIT_PANEL_WITH_DETAILS_PANEL = "//div[contains(@id,'DockedDetailsPanel')]";

    public static final String DETAILS_PANEL = "//div[contains(@id,'ContentBrowsePanel')]//div[contains(@id,'DetailsPanel')]";

    private final String WIDGET_SELECTOR_DROPDOWN = DETAILS_PANEL + "//div[contains(@id,'WidgetSelectorDropdown')]";

    private final String VERSION_HISTORY_OPTION_ITEM = WIDGET_SELECTOR_DROPDOWN + "//div[text()='Version history']";

    private final String DETAILS_OPTION_ITEM = WIDGET_SELECTOR_DROPDOWN + "//div[text()='Details']";

    private final String WIDGET_SELECTOR_OPTIONS =
        WIDGET_SELECTOR_DROPDOWN + "//div[contains(@class,'slick-cell')]//div[contains(@id,'DefaultOptionDisplayValueViewer')]";

    private final String DEPENDENCIES_OPTION_ITEM = WIDGET_SELECTOR_DROPDOWN + "//div[text()='Dependencies']";

    private final String WIDGET_SELECTOR_DROPDOWN_HANDLER = WIDGET_SELECTOR_DROPDOWN + DROP_DOWN_HANDLE_BUTTON;

    private UserAccessWidgetItemView userAccessWidgetItemView;

    private AttachmentsWidgetItemView attachmentsWidgetItemView;

    private PageTemplateWidgetItemView pageTemplateWidgetItemView;

    private PropertiesWidgetItemView propertiesWidgetItemView;


    @FindBy(xpath = WIDGET_SELECTOR_DROPDOWN_HANDLER)
    private WebElement widgetSelectorDropDownHandler;

    public ContentDetailsPanel( final TestSession session )
    {
        super( session );
    }

    public UserAccessWidgetItemView getUserAccessWidgetItemView()
    {
        if ( userAccessWidgetItemView == null )
        {
            return new UserAccessWidgetItemView( getSession() );
        }
        return userAccessWidgetItemView;
    }

    public PropertiesWidgetItemView getPropertiesWidgetItemView()
    {
        if ( propertiesWidgetItemView == null )
        {
            return new PropertiesWidgetItemView( getSession() );
        }
        return propertiesWidgetItemView;
    }

    public AttachmentsWidgetItemView getAttachmentsWidgetItemView()
    {
        if ( attachmentsWidgetItemView == null )
        {
            return new AttachmentsWidgetItemView( getSession() );
        }
        return attachmentsWidgetItemView;
    }

    public PageTemplateWidgetItemView getPageTemplateWidgetItemView()
    {
        if ( pageTemplateWidgetItemView == null )
        {
            return new PageTemplateWidgetItemView( getSession() );
        }
        return pageTemplateWidgetItemView;
    }

    public DependenciesWidgetItemView openDependenciesWidget()
    {
        DependenciesWidgetItemView dependenciesWidgetItemView = new DependenciesWidgetItemView( getSession() );
        if ( dependenciesWidgetItemView.isDisplayed() )
        {
            return dependenciesWidgetItemView;
        }
        else
        {
            //click on drop down handler and show options
            widgetSelectorDropDownHandler.click();
            if ( !isElementDisplayed( DEPENDENCIES_OPTION_ITEM ) )
            {
                saveScreenshot( NameHelper.uniqueName( "err_dependencies_option" ) );
                throw new TestFrameworkException( "dependencies option was not found!" );
            }
            getDisplayedElement( By.xpath( DEPENDENCIES_OPTION_ITEM ) ).click();
            sleep( 700 );
            return dependenciesWidgetItemView;
        }
    }

    public AllContentVersionsView openVersionHistory()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( VERSION_HISTORY_OPTION_ITEM ) )
        {
            //click on drop down handler and show options
            widgetSelectorDropDownHandler.click();
        }
        if ( !isElementDisplayed( VERSION_HISTORY_OPTION_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_history_opt" ) );
            throw new TestFrameworkException( "Version history option was not found!" );
        }
        getDisplayedElement( By.xpath( VERSION_HISTORY_OPTION_ITEM ) ).click();
        sleep( 700 );
        AllContentVersionsView versions = new AllContentVersionsView( getSession() );
        versions.waitUntilLoaded();
        return versions;
    }

    public List<String> getMenuOptions()
    {
        //click on drop down handler and show options
        widgetSelectorDropDownHandler.click();
        sleep( 300 );
        return getDisplayedStrings( By.xpath( WIDGET_SELECTOR_OPTIONS ) );
    }

    public ContentInfoWidget openDetailsWidget()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        ContentInfoWidget detailsWidget = new ContentInfoWidget( getSession() );
        if ( detailsWidget.isDisplayed() )
        {
            return detailsWidget;
        }
        if ( !isElementDisplayed( DETAILS_OPTION_ITEM ) )
        {
            //click on drop down handler and show options
            widgetSelectorDropDownHandler.click();
        }
        if ( !isElementDisplayed( DETAILS_OPTION_ITEM ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_details_opt" ) );
            throw new TestFrameworkException( "Details option was not found!" );
        }
        getDisplayedElement( By.xpath( DETAILS_OPTION_ITEM ) ).click();
        sleep( 700 );
        return detailsWidget;
    }

    public boolean isOpened( String contentDisplayName )
    {
        return isElementDisplayed( DETAILS_PANEL + String.format( NAMES_VIEW_BY_DISPLAY_NAME, contentDisplayName ) );
    }

    /**
     * @return true if details panel, that located on 'split panel' is displayed
     */
    public boolean isDisplayed()
    {
//        WebElement splitPanel = getDisplayedElement( By.xpath( SPLIT_PANEL_WITH_DETAILS_PANEL ) );
//        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
//        return !(Boolean) executor.executeScript(
//            "return window.api.dom.ElementRegistry.getElementById(arguments[0]).isSecondPanelHidden()", splitPanel.getAttribute( "id" ) );
        return isElementDisplayed( SPLIT_PANEL_WITH_DETAILS_PANEL );
    }

    public String getContentDisplayName()
    {
        if ( !isElementDisplayed( DETAILS_PANEL + H6_DISPLAY_NAME ) )
        {
            saveScreenshot( "err_det_panel_displayname" );
            throw new TestFrameworkException( "display name was not found on the details panel!" );
        }
        return getDisplayedString( DETAILS_PANEL + H6_DISPLAY_NAME );
    }
}