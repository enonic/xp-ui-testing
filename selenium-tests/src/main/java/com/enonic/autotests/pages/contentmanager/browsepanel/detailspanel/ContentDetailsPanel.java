package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentDetailsPanel
    extends Application
{
    private final String SPLIT_PANEL_WITH_DETAILS_PANEL = "//div[contains(@class,'split-panel-with-details')]";

    public static final String DETAILS_PANEL = "//div[contains(@id,'ContentBrowsePanel')]//div[contains(@id,'DetailsPanel')]";

    private final String WIDGET_SELECTOR_DROPDOWN = DETAILS_PANEL + "//div[contains(@id,'WidgetSelectorDropdown')]";

    private final String VERSION_HISTORY_OPTION = WIDGET_SELECTOR_DROPDOWN + "//div[text()='Version history']";

    private final String DEPENDENCIES_OPTION = WIDGET_SELECTOR_DROPDOWN + "//div[text()='Dependencies']";

    private final String WIDGET_SELECTOR_DROPDOWN_HANDLER = WIDGET_SELECTOR_DROPDOWN + "//button[contains(@id,'DropdownHandle')]";

    private final String INFO_WIDGET_TOGGLE_BUTTON = DETAILS_PANEL + "//div[contains(@id,'InfoWidgetToggleButton')]";


    @FindBy(xpath = WIDGET_SELECTOR_DROPDOWN_HANDLER)
    private WebElement widgetSelectorDropDownHandler;


    private UserAccessWidgetItemView userAccessWidgetItemView;

    private AttachmentsWidgetItemView attachmentsWidgetItemView;

    private PropertiesWidgetItemView propertiesWidgetItemView;

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

    public DependenciesWidgetItemView openDependenciesWidget()
    {
        DependenciesWidgetItemView dependenciesWidgetItemView = new DependenciesWidgetItemView( getSession() );
        if ( dependenciesWidgetItemView.isDisplayed() )
        {
            return dependenciesWidgetItemView;
        }
        else
        {
            widgetSelectorDropDownHandler.click();
            if ( !isElementDisplayed( DEPENDENCIES_OPTION ) )
            {
                TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_dependencies_option" ) );
                throw new TestFrameworkException( "dependencies option was not found!" );
            }
            getDisplayedElement( By.xpath( DEPENDENCIES_OPTION ) ).click();
            sleep( 700 );
            return dependenciesWidgetItemView;
        }

    }

    public AllContentVersionsView openVersionHistory()
    {
        if ( !isElementDisplayed( VERSION_HISTORY_OPTION ) )
        {
            widgetSelectorDropDownHandler.click();
        }
        if ( !isElementDisplayed( VERSION_HISTORY_OPTION ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_history_opt" ) );
            throw new TestFrameworkException( "Version history option was not found!" );
        }
        getDisplayedElement( By.xpath( VERSION_HISTORY_OPTION ) ).click();
        sleep( 700 );
        return new AllContentVersionsView( getSession() );
    }

    public ContentInfoWidget openInfoWidget()
    {
        if ( !isElementDisplayed( INFO_WIDGET_TOGGLE_BUTTON ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_info_opt" ) );
            throw new TestFrameworkException( "Info widget was not opened!" );
        }
        getDisplayedElement( By.xpath( INFO_WIDGET_TOGGLE_BUTTON ) ).click();
        sleep( 700 );
        return new ContentInfoWidget( getSession() );
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
        WebElement splitPanel = getDisplayedElement( By.xpath( SPLIT_PANEL_WITH_DETAILS_PANEL ) );
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        return !(Boolean) executor.executeScript(
            "return window.api.dom.ElementRegistry.getElementById(arguments[0]).isSecondPanelHidden()", splitPanel.getAttribute( "id" ) );
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