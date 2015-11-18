package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContextWindowPageInspectionPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'PageInspectionPanel')]";

    private final String OPTION_FILTER_INPUT = CONTAINER + "//input[contains(@id,'DropdownOptionFilterInput')]";

    private final String PAGE_CONTROLLER_SELECTOR = CONTAINER + "//div[contains(@id,'PageControllerSelector')]";

    private final String RENDERER_SELECTOR = CONTAINER + "//div[contains(@id,'PageTemplateSelector')]";

    private final String PAGE_CONTROLLER_DROPDOWN_HANDLER = PAGE_CONTROLLER_SELECTOR + "//div[contains(@id,'DropdownHandle')]";

    private final String RENDERER_DROPDOWN_HANDLER = RENDERER_SELECTOR + "//div[contains(@id,'DropdownHandle')]";

    public ContextWindowPageInspectionPanel( final TestSession session )
    {
        super( session );
    }

    public boolean isOptionFilterDisplayed()
    {
        return isElementDisplayed( OPTION_FILTER_INPUT );
    }

    public ContextWindowPageInspectionPanel selectRenderer( String templateName )
    {
        //click on handler
        getDisplayedElement( By.xpath( RENDERER_DROPDOWN_HANDLER ) ).click();
        sleep( 300 );
        String optionItemXpath =
            RENDERER_SELECTOR + "//div[contains(@class,'slick-cell')]" + String.format( NAMES_VIEW_BY_DISPLAY_NAME, templateName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_renderer" );
            throw new TestFrameworkException( "option was not found!  " + templateName );
        }
        //select a option
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        return this;
    }

    public ContextWindowPageInspectionPanel selectPageController( String controllerName )
    {
        getDisplayedElement( By.xpath( PAGE_CONTROLLER_DROPDOWN_HANDLER ) ).click();
        sleep( 300 );
        String optionItemXpath = PAGE_CONTROLLER_SELECTOR + "//div[contains(@class,'slick-cell')]" +
            String.format( NAMES_VIEW_BY_DISPLAY_NAME, controllerName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_controller" );
            throw new TestFrameworkException( "option was not found!  " + controllerName );
        }
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        return this;
    }

    public String getSelectedPageController()
    {
        return getDisplayedElement(
            By.xpath( "//div[contains(@id,'contextwindow.inspect.page.PageControllerSelector')]//h6[@class='main-name']" ) ).getText();
    }

    public boolean isPageControllerSelectorDisplayed()
    {
        return waitUntilVisibleNoException( By.xpath( PAGE_CONTROLLER_SELECTOR ), Application.EXPLICIT_NORMAL );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( CONTAINER );
    }
}
