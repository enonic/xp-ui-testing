package com.enonic.autotests.pages.contentmanager.wizardpanel.context_window;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PageInspectionPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'PageInspectionPanel')]";

    private final String PAGE_CONTROLLER_SELECTOR = CONTAINER + "//div[contains(@id,'PageTemplateAndControllerSelector')]";

    private final String PAGE_CONTROLLER_OPTION_FILTER_INPUT = PAGE_CONTROLLER_SELECTOR + DROPDOWN_OPTION_FILTER_INPUT;

    private final String RENDERER_SELECTOR = CONTAINER + "//div[contains(@id,'PageTemplateAndControllerSelector')]";

    private final String RENDERER_DROPDOWN_HANDLER = RENDERER_SELECTOR + "//button[contains(@id,'DropdownHandle')]";

    private final String PAGE_CONTROLLER_DROPDOWN_HANDLER = PAGE_CONTROLLER_SELECTOR + "//button[contains(@id,'DropdownHandle')]";

    private final String SAVE_AS_TEMPLATE_BUTTON = String.format( ACTION_BUTTON, "Save as Template" );

    @FindBy(xpath = PAGE_CONTROLLER_OPTION_FILTER_INPUT)
    protected WebElement pageControllerOptionFilterInput;

    public PageInspectionPanel( final TestSession session )
    {
        super( session );
    }

    /**
     * @return true if the selector for 'page-template' (renderer) is displayed
     */
    public boolean isPageTemplateSelectorDisplayed()
    {
        return isElementDisplayed( RENDERER_SELECTOR );
    }

    /**
     * Clicks on the drop down handler and selects the required renderer(page-template)
     */
    public PageInspectionPanel selectRenderer( String templateName )
    {
        if ( !isElementDisplayed( RENDERER_DROPDOWN_HANDLER ) )
        {
            saveScreenshot( "err_dropdown_renderer" );
            throw new TestFrameworkException( "dropdown handler was not found!  " + templateName );
        }
        getDisplayedElement( By.xpath( RENDERER_DROPDOWN_HANDLER ) ).click();
        sleep( 400 );
        String optionItemXpath = RENDERER_SELECTOR + SLICK_CELL + String.format( NAMES_VIEW_BY_DISPLAY_NAME, templateName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            saveScreenshot( "err_renderer" );
            throw new TestFrameworkException( "option was not found!  " + templateName );
        }
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        sleep( 500 );
        return this;
    }

    public PageInspectionPanel selectRendererByDisplayName( String templateDisplayName )
    {
        if ( !isElementDisplayed( RENDERER_DROPDOWN_HANDLER ) )
        {
            saveScreenshot( "err_dropdown_renderer" );
            throw new TestFrameworkException( "dropdown handler was not found!  " + templateDisplayName );
        }
        getDisplayedElement( By.xpath( RENDERER_DROPDOWN_HANDLER ) ).click();
        sleep( 500 );
        String optionItemXpath = RENDERER_SELECTOR + SLICK_CELL + String.format( NAMES_VIEW_BY_DISPLAY_NAME, templateDisplayName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            saveScreenshot( "err_renderer" );
            throw new TestFrameworkException( "option was not found!  " + templateDisplayName );
        }
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        sleep( 500 );
        return this;
    }

    /**
     * Types the display name of controller and selects the required controller
     */
    public PageInspectionPanel selectPageController( String controllerName )
    {
        clearAndType( pageControllerOptionFilterInput, controllerName );
        sleep( 300 );
        String optionItemXpath = PAGE_CONTROLLER_SELECTOR + "//div[contains(@class,'slick-cell')]" +
            String.format( NAMES_VIEW_BY_DISPLAY_NAME, controllerName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            saveScreenshot( "err_selecting_controller" );
            throw new TestFrameworkException( "option was not found!  " + controllerName );
        }
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        return this;
    }

    /**
     * clicks on the drop down handler and selects new controller
     */
    public PageInspectionPanel changePageController( String controllerName )
    {
        if ( !isElementDisplayed( PAGE_CONTROLLER_DROPDOWN_HANDLER ) )
        {
            saveScreenshot( "err_dropdown_page_controller" );
            throw new TestFrameworkException( "dropdown handler was not found!  " + controllerName );
        }
        getDisplayedElement( By.xpath( PAGE_CONTROLLER_DROPDOWN_HANDLER ) ).click();
        sleep( 300 );
        String optionItemXpath = PAGE_CONTROLLER_SELECTOR + SLICK_CELL + String.format( NAMES_VIEW_BY_DISPLAY_NAME, controllerName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            saveScreenshot( "err_inspection_controller" );
            throw new TestFrameworkException( "option was not found!  " + controllerName );
        }
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        sleep( 300 );
        return this;
    }

    public String getSelectedPageController()
    {
        boolean isOptionDisplayed =
            waitUntilVisibleNoException( By.xpath( PAGE_CONTROLLER_SELECTOR + H6_DISPLAY_NAME ), Application.EXPLICIT_NORMAL );
        if ( !isOptionDisplayed )
        {
            saveScreenshot( "err_context_wind_page_controller" );
            throw new TestFrameworkException( "page controller not displayed on the context window!" );
        }
        return getDisplayedElement( By.xpath( PAGE_CONTROLLER_SELECTOR + H6_DISPLAY_NAME ) ).getText();
    }

    public boolean isPageControllerSelectorDisplayed()
    {
        return waitUntilVisibleNoException( By.xpath( PAGE_CONTROLLER_SELECTOR ), Application.EXPLICIT_NORMAL );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( CONTAINER );
    }

    public void waitUntilPanelClosed( long timeout )
    {
        waitInvisibilityOfElement( By.xpath( CONTAINER ), timeout );
    }

    public boolean isSaveAsTemplateButtonDisplayed()
    {
        return isElementDisplayed( SAVE_AS_TEMPLATE_BUTTON );
    }

    public PageInspectionPanel clickOnSaveAsTemplateButton()
    {
        getDisplayedElement( By.xpath( SAVE_AS_TEMPLATE_BUTTON ) ).click();
        sleep( 1000 );
        return this;
    }

    public void waitForLoaded()
    {

        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_open_inspection_panel" );
            throw new TestFrameworkException( "PageInspectionPanel was not loaded!" );
        }

    }
}
